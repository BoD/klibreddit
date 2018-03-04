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

import okhttp3.OkHttpClient
import org.jraf.klibreddit.model.client.UserAgent
import java.net.InetSocketAddress
import java.net.Proxy

object OkHttpHelper {
    fun provideOkHttpClient(userAgent: UserAgent, authTokenProvider: AuthTokenProvider): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                chain.proceed(
                    request.newBuilder().apply {
                        // Add user agent
                        header("User-Agent", userAgent.toString())

                        // Auth token, if present
                        authTokenProvider.authToken?.let { authToken ->
                            header("Authorization", "bearer $authToken")
                        }

                        // Ask for raw json
                        url(request.url().newBuilder().addQueryParameter("raw_json", "1").build())
                    }
                        .build()
                )
            }
            .proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("localhost", 8888)))
            .build()
    }

    interface AuthTokenProvider {
        val authToken: String?
    }
}