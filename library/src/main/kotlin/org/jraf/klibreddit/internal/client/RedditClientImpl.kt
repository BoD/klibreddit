/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2018 Benoit 'BoD' Lubek (BoD@JRAF.org)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jraf.klibreddit.internal.client

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.Credentials
import org.jraf.klibreddit.client.RedditClient
import org.jraf.klibreddit.internal.api.OkHttpHelper
import org.jraf.klibreddit.internal.api.RedditService
import org.jraf.klibreddit.internal.api.RedditService.Companion.URL_BASE_API
import org.jraf.klibreddit.internal.api.RedditService.Companion.URL_BASE_OAUTH
import org.jraf.klibreddit.internal.util.StringUtil.toUrlEncoded
import org.jraf.klibreddit.internal.util.UriUtil.queryParams
import org.jraf.klibreddit.model.account.Me
import org.jraf.klibreddit.model.client.ClientConfiguration
import org.jraf.klibreddit.model.oauth.OAuthScope
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.URI
import java.util.Date
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit

internal class RedditClientImpl(
    private val clientConfiguration: ClientConfiguration
) : RedditClient, OkHttpHelper.AuthTokenProvider {

    companion object {
        private const val URL_AUTHORIZE =
            "${URL_BASE_API}authorize.compact?client_id=%1\$s&response_type=code&state=%2\$s&redirect_uri=%3\$s&duration=permanent&scope=%4\$s"

        private const val AUTHORIZE_REDIRECT_PARAM_CODE = "code"
        private const val GRANT_TYPE_AUTHORIZE = "authorization_code"
        private const val GRANT_REFRESH_TOKEN = "refresh_token"
    }

    private var oAuthTokens: OAuthTokens? = null

    override val authToken: String?
        get() = oAuthTokens?.accessToken

    private val service: RedditService by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE_OAUTH)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .client(OkHttpHelper.provideOkHttpClient(clientConfiguration.userAgent, this))
            .build()
            .create(RedditService::class.java)
    }

    override fun getAuthorizeUrl(vararg scopes: OAuthScope) = URL_AUTHORIZE.format(
        Locale.US,
        clientConfiguration.oAuthConfiguration.clientId.toUrlEncoded(),
        UUID.randomUUID().toString().toUrlEncoded(),
        clientConfiguration.oAuthConfiguration.redirectUri.toUrlEncoded(),
        scopes.joinToString(",") { it.name.toLowerCase(Locale.US) }.toUrlEncoded()
    )

    override fun onAuthorizeRedirect(authorizeRedirectUri: String): Single<String> {
        val code = URI(authorizeRedirectUri).queryParams[AUTHORIZE_REDIRECT_PARAM_CODE]
                ?: throw IllegalArgumentException("Invalid format for authorizeRedirectUri")

        return service.accessToken(
            grantType = GRANT_TYPE_AUTHORIZE,
            code = code,
            redirectUri = clientConfiguration.oAuthConfiguration.redirectUri,
            authorizationHeader = Credentials.basic(clientConfiguration.oAuthConfiguration.clientId, "")
        )
            .doOnSuccess {
                oAuthTokens = OAuthTokens(
                    it.access_token,
                    Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(it.expires_in.toLong())),
                    it.refresh_token!!
                )
            }
            .map { it.refresh_token }
    }

    override fun refreshAuthToken(): Single<String> {
        return service.refreshToken(
            grantType = GRANT_REFRESH_TOKEN,
            refreshToken = oAuthTokens!!.refreshToken,
            authorizationHeader = Credentials.basic(clientConfiguration.oAuthConfiguration.clientId, "")
        )
            .doOnSuccess {
                oAuthTokens = OAuthTokens(
                    it.access_token,
                    Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(it.expires_in.toLong())),
                    oAuthTokens!!.refreshToken
                )
            }
            .map { it.access_token }
    }

    override fun setRefreshToken(refreshToken: String) {
        oAuthTokens = OAuthTokens(refreshToken = refreshToken)
    }

    private fun ensureAuthToken(): Completable {
        return if (oAuthTokens!!.needsRefresh) {
            refreshAuthToken().toCompletable()
        } else {
            Completable.complete()
        }
    }

    override fun me(): Single<Me> {
        return ensureAuthToken()
            .andThen(service.me())
            .map { it.toPublicModel() }
    }
}