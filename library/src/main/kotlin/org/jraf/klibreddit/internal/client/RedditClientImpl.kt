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

import com.squareup.moshi.Moshi
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.Credentials
import org.jraf.klibreddit.client.RedditClient
import org.jraf.klibreddit.internal.api.OkHttpHelper
import org.jraf.klibreddit.internal.api.RedditService
import org.jraf.klibreddit.internal.api.RedditService.Companion.URL_BASE_API_V1
import org.jraf.klibreddit.internal.api.RedditService.Companion.URL_BASE_OAUTH
import org.jraf.klibreddit.internal.api.model.DateOrNullAdapter
import org.jraf.klibreddit.internal.api.model.account.ApiMeConverter
import org.jraf.klibreddit.internal.api.model.listings.ApiCommentOrApiMoreAdapter
import org.jraf.klibreddit.internal.api.model.listings.ApiList
import org.jraf.klibreddit.internal.api.model.listings.ApiPostListConverter
import org.jraf.klibreddit.internal.api.model.listings.ApiPostOrApiComment
import org.jraf.klibreddit.internal.api.model.listings.ApiPostOrApiCommentAdapter
import org.jraf.klibreddit.internal.util.StringUtil.toUrlEncoded
import org.jraf.klibreddit.internal.util.UriUtil.queryParams
import org.jraf.klibreddit.model.account.Me
import org.jraf.klibreddit.model.client.ClientConfiguration
import org.jraf.klibreddit.model.listings.Page
import org.jraf.klibreddit.model.listings.Pagination
import org.jraf.klibreddit.model.listings.Period
import org.jraf.klibreddit.model.listings.Post
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
) : RedditClient,
    RedditClient.OAuth,
    RedditClient.Account,
    RedditClient.Listings,
    OkHttpHelper.AuthTokenProvider {

    companion object {
        private const val URL_AUTHORIZE =
            "${URL_BASE_API_V1}/authorize.compact?client_id=%1\$s&response_type=code&state=%2\$s&redirect_uri=%3\$s&duration=permanent&scope=%4\$s"

        private const val AUTHORIZE_REDIRECT_PARAM_CODE = "code"
        private const val GRANT_TYPE_AUTHORIZE = "authorization_code"
        private const val GRANT_REFRESH_TOKEN = "refresh_token"
    }

    override val oAuth = this
    override val account = this
    override val listings = this

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
//                        .add(KotlinJsonAdapterFactory())
//                        .add(ApplicationJsonAdapterFactory.INSTANCE)
                        .add(DateOrNullAdapter())
                        .add(ApiCommentOrApiMoreAdapter())
                        .add(ApiPostOrApiCommentAdapter())
                        .build()
                )
            )
            .client(OkHttpHelper.provideOkHttpClient(clientConfiguration, this))
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

    private fun <T> call(call: Single<T>): Single<T> = ensureAuthToken().andThen(call)


    // Account

    override fun me(): Single<Me> {
        return call(service.me())
            .map { ApiMeConverter.convert(it) }
    }


    // Listings

    private fun String?.subreddit() = if (this == null) "" else if (startsWith("/r/")) this else "/r/$this"

    override fun best(pagination: Pagination): Single<Page<Post>> {
        return call(
            service.best(
                pagination.pageIndex.before,
                pagination.pageIndex.after,
                pagination.itemCount
            )
        )
            .map { ApiPostListConverter.convert(it) }
    }

    private fun subredditPostsOrdered(
        subreddit: String?,
        pagination: Pagination,
        listingOrder: ListingOrder,
        period: Period? = null
    ): Single<Page<Post>> {
        return call(
            service.subredditPostsOrdered(
                subreddit.subreddit(),
                listingOrder.name.toLowerCase(Locale.US),
                period?.name?.toLowerCase(Locale.US),
                pagination.pageIndex.before,
                pagination.pageIndex.after,
                pagination.itemCount
            )
        )
            .map { ApiPostListConverter.convert(it) }
    }

    override fun hot(subreddit: String?, pagination: Pagination): Single<Page<Post>> {
        return subredditPostsOrdered(subreddit, pagination, ListingOrder.HOT)
    }

    override fun new(subreddit: String?, pagination: Pagination): Single<Page<Post>> {
        return subredditPostsOrdered(subreddit, pagination, ListingOrder.NEW)
    }

    override fun rising(subreddit: String?, pagination: Pagination): Single<Page<Post>> {
        return subredditPostsOrdered(subreddit, pagination, ListingOrder.RISING)
    }

    override fun controversial(subreddit: String?, period: Period, pagination: Pagination): Single<Page<Post>> {
        return subredditPostsOrdered(subreddit, pagination, ListingOrder.CONTROVERSIAL, period)
    }

    override fun top(subreddit: String?, period: Period, pagination: Pagination): Single<Page<Post>> {
        return subredditPostsOrdered(subreddit, pagination, ListingOrder.TOP, period)
    }

    override fun comments(postId: String): Single<List<ApiList<ApiPostOrApiComment>>> {
        return service.comments(postId)
    }

}

