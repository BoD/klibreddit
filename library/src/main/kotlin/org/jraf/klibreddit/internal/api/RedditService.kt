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

package org.jraf.klibreddit.internal.api

import org.jraf.klibreddit.internal.api.model.ApiAccessTokenResult
import org.jraf.klibreddit.internal.api.model.account.ApiMe
import org.jraf.klibreddit.internal.api.model.listings.ApiCommentOrMore
import org.jraf.klibreddit.internal.api.model.listings.ApiJsonEnvelope
import org.jraf.klibreddit.internal.api.model.listings.ApiList
import org.jraf.klibreddit.internal.api.model.listings.ApiMeta
import org.jraf.klibreddit.internal.api.model.listings.ApiPost
import org.jraf.klibreddit.internal.api.model.listings.ApiPostOrCommentOrMore
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface RedditService {
    companion object {
        private const val PATH_API = "api"
        private const val PATH_API_V1 = "$PATH_API/v1"
        const val URL_BASE_API_V1 = "https://api.reddit.com/$PATH_API_V1"
        const val URL_BASE_OAUTH = "https://oauth.reddit.com/"
    }

    @POST("$URL_BASE_API_V1/access_token")
    @FormUrlEncoded
    suspend fun accessToken(
        @Field("grant_type") grantType: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String,
        @Header("Authorization") authorizationHeader: String
    ): ApiAccessTokenResult

    @POST("$URL_BASE_API_V1/access_token")
    @FormUrlEncoded
    suspend fun refreshToken(
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String,
        @Header("Authorization") authorizationHeader: String
    ): ApiAccessTokenResult


    @GET("$PATH_API_V1/me")
    suspend fun me(): ApiMe

    @GET("best")
    suspend fun best(
        @Query("before") before: String?,
        @Query("after") after: String?,
        @Query("limit") limit: Int
    ): ApiMeta<ApiList<ApiMeta<ApiPost>>>

    @GET("{subreddit}/{order}")
    suspend fun subredditPostsOrdered(
        @Path("subreddit", encoded = true) subreddit: String?,
        @Path("order") order: String,
        @Query("t") t: String?,
        @Query("before") before: String?,
        @Query("after") after: String?,
        @Query("limit") limit: Int
    ): ApiMeta<ApiList<ApiMeta<ApiPost>>>

    @GET("comments/{postId}")
    suspend fun comments(
        @Path("postId") postId: String,
        @Query("order") order: String?,
        @Query("limit") limit: Int?,
        @Query("truncate") truncate: Int?
    ): List<ApiMeta<ApiList<ApiPostOrCommentOrMore>>>

    @GET("$PATH_API/morechildren?api_type=json")
    suspend fun morechildren(
        @Query("children") children: String,
        @Query("link_id") link_id: String
    ): ApiJsonEnvelope<ApiCommentOrMore>

    @POST("$PATH_API/comment?api_type=json")
    @FormUrlEncoded
    suspend fun comment(
        @Field("thing_id") thing_id: String,
        @Field("text") text: String
    )
}
