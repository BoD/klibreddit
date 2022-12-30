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

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.Parameters
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

internal class RedditService(
    private val httpClient: HttpClient
) {
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
    ): ApiAccessTokenResult {
        return httpClient.submitForm("$URL_BASE_API_V1/access_token",
            formParameters = Parameters.build {
                append("grant_type", grantType)
                append("code", code)
                append("redirect_uri", redirectUri)
            }
        ) {
            headers.append("Authorization", authorizationHeader)
        }
            .body()
    }

    @POST("$URL_BASE_API_V1/access_token")
    @FormUrlEncoded
    suspend fun refreshToken(
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String,
        @Header("Authorization") authorizationHeader: String
    ): ApiAccessTokenResult {
        return httpClient.submitForm("$URL_BASE_API_V1/access_token",
            formParameters = Parameters.build {
                append("grant_type", grantType)
                append("refresh_token", refreshToken)
            }
        ) {
            headers.append("Authorization", authorizationHeader)
        }
            .body()
    }

    @GET("$PATH_API_V1/me")
    suspend fun me(): ApiMe {
        return httpClient.get("$URL_BASE_OAUTH/api/v1/me")
            .body()
    }

    @GET("best")
    suspend fun best(
        @Query("before") before: String?,
        @Query("after") after: String?,
        @Query("limit") limit: Int
    ): ApiMeta<ApiList<ApiMeta<ApiPost>>> {
        return httpClient.get("$URL_BASE_OAUTH/best") {
            parameter("before", before)
            parameter("after", after)
            parameter("limit", limit)
        }
            .body()
    }

    @GET("{subreddit}/{order}")
    suspend fun subredditPostsOrdered(
        @Path("subreddit", encoded = true) subreddit: String?,
        @Path("order") order: String,
        @Query("t") t: String?,
        @Query("before") before: String?,
        @Query("after") after: String?,
        @Query("limit") limit: Int
    ): ApiMeta<ApiList<ApiMeta<ApiPost>>> {
        return httpClient.get("$URL_BASE_OAUTH/$subreddit/$order") {
            parameter("t", t)
            parameter("before", before)
            parameter("after", after)
            parameter("limit", limit)
        }
            .body()
    }

    @GET("comments/{postId}")
    suspend fun comments(
        @Path("postId") postId: String,
        @Query("order") order: String?,
        @Query("limit") limit: Int?,
        @Query("truncate") truncate: Int?
    ): List<ApiMeta<ApiList<ApiPostOrCommentOrMore>>> {
        return httpClient.get("$URL_BASE_OAUTH/comments/$postId") {
            parameter("order", order)
            parameter("limit", limit)
            parameter("truncate", truncate)
        }
            .body()
    }

    @GET("$PATH_API/morechildren?api_type=json")
    suspend fun morechildren(
        @Query("children") children: String,
        @Query("link_id") link_id: String
    ): ApiJsonEnvelope<ApiCommentOrMore> {
        return httpClient.get("$URL_BASE_OAUTH/api/morechildren") {
            parameter("api_type", "json")
            parameter("children", children)
            parameter("link_id", link_id)
        }
            .body()
    }

    @POST("$PATH_API/comment?api_type=json")
    @FormUrlEncoded
    suspend fun comment(
        @Field("thing_id") thing_id: String,
        @Field("text") text: String
    ) {
        httpClient.submitForm(
            url = "$URL_BASE_OAUTH/api/comment?api_type=json",
            formParameters = Parameters.build {
                append("thing_id", thing_id)
                append("text", text)
            }
        )
    }
}
