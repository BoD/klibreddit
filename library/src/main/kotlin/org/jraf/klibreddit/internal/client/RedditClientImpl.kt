/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2018-present Benoit 'BoD' Lubek (BoD@JRAF.org)
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

import com.google.gson.GsonBuilder
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.Credentials
import org.jraf.klibreddit.client.RedditClient
import org.jraf.klibreddit.internal.api.OkHttpHelper
import org.jraf.klibreddit.internal.api.RedditService
import org.jraf.klibreddit.internal.api.RedditService.Companion.URL_BASE_API_V1
import org.jraf.klibreddit.internal.api.RedditService.Companion.URL_BASE_OAUTH
import org.jraf.klibreddit.internal.api.model.account.ApiMeConverter
import org.jraf.klibreddit.internal.api.model.listings.ApiCommentOrMore
import org.jraf.klibreddit.internal.api.model.listings.ApiJsonDataCommentOrMoreConverter
import org.jraf.klibreddit.internal.api.model.listings.ApiOptionalDate
import org.jraf.klibreddit.internal.api.model.listings.ApiOptionalInt
import org.jraf.klibreddit.internal.api.model.listings.ApiOptionalMeta
import org.jraf.klibreddit.internal.api.model.listings.ApiPostListConverter
import org.jraf.klibreddit.internal.api.model.listings.ApiPostOrCommentOrMore
import org.jraf.klibreddit.internal.api.model.listings.ApiPostOrCommentOrMoreListConverter
import org.jraf.klibreddit.internal.model.listings.CommentImpl
import org.jraf.klibreddit.internal.model.listings.PostWithCommentsImpl
import org.jraf.klibreddit.internal.util.nameLowerCase
import org.jraf.klibreddit.internal.util.queryParams
import org.jraf.klibreddit.internal.util.split
import org.jraf.klibreddit.internal.util.toUrlEncoded
import org.jraf.klibreddit.model.account.Me
import org.jraf.klibreddit.model.client.ClientConfiguration
import org.jraf.klibreddit.model.listings.Comment
import org.jraf.klibreddit.model.listings.CommentListOrder
import org.jraf.klibreddit.model.listings.Page
import org.jraf.klibreddit.model.listings.Pagination
import org.jraf.klibreddit.model.listings.Period
import org.jraf.klibreddit.model.listings.Post
import org.jraf.klibreddit.model.listings.PostWithComments
import org.jraf.klibreddit.model.oauth.OAuthScope
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
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
    RedditClient.LinksAndComments,
    OkHttpHelper.AuthTokenProvider {

    companion object {
        private const val URL_AUTHORIZE =
            "$URL_BASE_API_V1/authorize.compact?client_id=%1\$s&response_type=code&state=%2\$s&redirect_uri=%3\$s&duration=permanent&scope=%4\$s"

        private const val AUTHORIZE_REDIRECT_PARAM_CODE = "code"
        private const val GRANT_TYPE_AUTHORIZE = "authorization_code"
        private const val GRANT_REFRESH_TOKEN = "refresh_token"

        private const val MORE_CHILDREN_MAX_IDS = 100
    }

    override val oAuth = this
    override val account = this
    override val listings = this
    override val linksAndComments = this

    private var oAuthTokens: OAuthTokens? = null

    override val authToken: String?
        get() = oAuthTokens?.accessToken

    private val service: RedditService by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE_OAUTH)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(ApiOptionalDate::class.java, ApiOptionalDate.Deserializer)
                        .registerTypeAdapter(ApiCommentOrMore::class.java, ApiCommentOrMore.Deserializer)
                        .registerTypeAdapter(ApiPostOrCommentOrMore::class.java, ApiPostOrCommentOrMore.Deserializer)
                        .registerTypeAdapter(ApiOptionalMeta::class.java, ApiOptionalMeta.Deserializer)
                        .registerTypeAdapter(ApiOptionalInt::class.java, ApiOptionalInt.Deserializer)
                        .create()
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
            refreshAuthToken().ignoreElement()
        } else {
            Completable.complete()
        }
    }

    private fun <T> call(call: Single<T>): Single<T> = ensureAuthToken().andThen(call)
    private fun call(call: Completable): Completable = ensureAuthToken().andThen(call)

    private fun String?.subreddit() = when {
        this == null -> ""
        startsWith("/r/") -> this
        startsWith("r/") -> "/$this"
        else -> "/r/$this"
    }


    // Account

    override fun me(): Single<Me> {
        return call(service.me())
            .map { ApiMeConverter.convert(it) }
    }


    // Listings

    override fun best(pagination: Pagination): Single<Page<Post>> {
        return call(
            service.best(
                pagination.pageIndex.before,
                pagination.pageIndex.after,
                pagination.itemCount
            )
        )
            .map { ApiPostListConverter.convert(it to pagination.itemCount) }
    }

    private fun subredditPostsOrdered(
        subreddit: String?,
        pagination: Pagination,
        postListOrder: PostListOrder,
        period: Period? = null
    ): Single<Page<Post>> {
        return call(
            service.subredditPostsOrdered(
                subreddit.subreddit(),
                postListOrder.nameLowerCase,
                period?.nameLowerCase,
                pagination.pageIndex.before,
                pagination.pageIndex.after,
                pagination.itemCount
            )
        )
            .map { ApiPostListConverter.convert(it to pagination.itemCount) }
    }

    override fun hot(subreddit: String?, pagination: Pagination): Single<Page<Post>> {
        return subredditPostsOrdered(subreddit, pagination, PostListOrder.HOT)
    }

    override fun new(subreddit: String?, pagination: Pagination): Single<Page<Post>> {
        return subredditPostsOrdered(subreddit, pagination, PostListOrder.NEW)
    }

    override fun rising(subreddit: String?, pagination: Pagination): Single<Page<Post>> {
        return subredditPostsOrdered(subreddit, pagination, PostListOrder.RISING)
    }

    override fun controversial(subreddit: String?, period: Period, pagination: Pagination): Single<Page<Post>> {
        return subredditPostsOrdered(subreddit, pagination, PostListOrder.CONTROVERSIAL, period)
    }

    override fun top(subreddit: String?, period: Period, pagination: Pagination): Single<Page<Post>> {
        return subredditPostsOrdered(subreddit, pagination, PostListOrder.TOP, period)
    }

    override fun comments(
        postId: String,
        order: CommentListOrder,
        itemCount: Int?,
        maxDepth: Int?
    ): Single<PostWithComments> {
        return call(
            service.comments(
                postId,
                order.nameLowerCase,
                itemCount,
                maxDepth
            )
        )
            .map { ApiPostOrCommentOrMoreListConverter.convert(it) }
    }

    override fun moreComments(postWithComments: PostWithComments): Single<PostWithComments> {
        if (postWithComments.moreCommentIds.isEmpty()) return Single.just(postWithComments)

        val moreCommentIds = postWithComments.moreCommentIds.split(MORE_CHILDREN_MAX_IDS)
        return call(
            service.morechildren(
                moreCommentIds[0].joinToString(","),
                postWithComments.post.name
            )
        )
            .map {
                val comments = postWithComments.comments.toMutableList()
                comments += ApiJsonDataCommentOrMoreConverter.convert(it)
                (postWithComments as PostWithCommentsImpl).copy(
                    post = postWithComments.post,
                    comments = comments,
                    moreCommentIds = moreCommentIds[1]
                )
            }
    }

    override fun moreComments(comment: Comment): Single<Comment> {
        if (comment.moreReplyIds.isEmpty()) return Single.just(comment)

        val moreReplyIds = comment.moreReplyIds.split(MORE_CHILDREN_MAX_IDS)
        return call(
            service.morechildren(
                moreReplyIds[0].joinToString(","),
                comment.linkId
            )
        )
            .map {
                val replies = comment.replies.toMutableList()
                replies += ApiJsonDataCommentOrMoreConverter.convert(it)
                (comment as CommentImpl).copy(
                    replies = replies,
                    moreReplyIds = moreReplyIds[1]
                )
            }
    }


    // LinksAndComments

    override fun comment(comment: Comment, text: String): Completable {
        return call(service.comment(comment.name, text))
    }

}
