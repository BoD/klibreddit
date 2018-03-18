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

package org.jraf.klibreddit.internal.api

import io.reactivex.Single
import org.jraf.klibreddit.internal.api.model.ApiAccessTokenResult
import org.jraf.klibreddit.internal.api.model.account.ApiMe
import org.jraf.klibreddit.internal.api.model.listings.ApiList
import org.jraf.klibreddit.internal.api.model.listings.ApiMeta
import org.jraf.klibreddit.internal.api.model.listings.ApiPost
import org.jraf.klibreddit.internal.api.model.listings.ApiPostOrApiComment
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface RedditService {
    companion object {
        private const val PATH_API_V1 = "api/v1"
        const val URL_BASE_API_V1 = "https://api.reddit.com/$PATH_API_V1"
        const val URL_BASE_OAUTH = "https://oauth.reddit.com/"
    }

    @POST("$URL_BASE_API_V1/access_token")
    @FormUrlEncoded
    fun accessToken(
        @Field("grant_type") grantType: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String,
        @Header("Authorization") authorizationHeader: String
    ): Single<ApiAccessTokenResult>

    @POST("$URL_BASE_API_V1/access_token")
    @FormUrlEncoded
    fun refreshToken(
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String,
        @Header("Authorization") authorizationHeader: String
    ): Single<ApiAccessTokenResult>


    @GET("$PATH_API_V1/me")
    fun me(): Single<ApiMe>

    @GET("best")
    fun best(
        @Query("before") before: String?,
        @Query("after") after: String?,
        @Query("limit") limit: Int
    ): Single<ApiMeta<ApiList<ApiMeta<ApiPost>>>>

    @GET("{subreddit}/{order}")
    fun subredditPostsOrdered(
        @Path("subreddit", encoded = true) subreddit: String?,
        @Path("order") order: String,
        @Query("t") t: String?,
        @Query("before") before: String?,
        @Query("after") after: String?,
        @Query("limit") limit: Int
    ): Single<ApiMeta<ApiList<ApiMeta<ApiPost>>>>

    @GET("comments/{postId}")
    fun comments(
        @Path("postId") order: String
    ): Single<List<ApiList<ApiPostOrApiComment>>>
}