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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jraf.klibreddit.model.client.ClientConfiguration
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.net.Proxy


internal object OkHttpHelper {
    private var LOGGER = LoggerFactory.getLogger(OkHttpHelper::class.java)

    private const val HEADER_USER_AGENT = "User-Agent"
    private const val HEADER_AUTHORIZATION = "Authorization"
    private const val BEARER = "bearer"
    private const val PARAM_RAW_JSON_NAME = "raw_json"
    private const val PARAM_RAW_JSON_VALUE = "1"

    fun provideOkHttpClient(
        clientConfiguration: ClientConfiguration,
        authTokenProvider: AuthTokenProvider
    ): OkHttpClient {

        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                chain.proceed(
                    request.newBuilder().apply {
                        // User agent
                        header(HEADER_USER_AGENT, clientConfiguration.userAgent.toString())

                        // Auth token, if present
                        if (request.header(HEADER_AUTHORIZATION) == null) {
                            header(HEADER_AUTHORIZATION, "$BEARER ${authTokenProvider.authToken ?: "-"}")
                        }

                        val urlBuilder = request.url().newBuilder()

                        // Use mock server, if configured
                        clientConfiguration.httpConfiguration.mockServerBaserUri?.let {
                            urlBuilder
                                .scheme(it.scheme)
                                .host(it.host)
                                .port(it.port)
                        }

                        // Ask for raw json
                        url(urlBuilder.addQueryParameter(PARAM_RAW_JSON_NAME, PARAM_RAW_JSON_VALUE).build())
                    }
                        .build()
                )
            }

            // Logging
            .addInterceptor(HttpLoggingInterceptor {
                LOGGER.trace(it)
            }.setLevel(clientConfiguration.httpConfiguration.loggingLevel.okHttpLevel))

            // Authenticator
            .authenticator { _, response ->
                val request = response.request()
                val newAuthToken = authTokenProvider.refreshAuthToken()
                    .blockingGet()
                request.newBuilder()
                    .header(HEADER_AUTHORIZATION, "$BEARER $newAuthToken")
                    .build()
            }

        // Proxy, if configured
        clientConfiguration.httpConfiguration.httpProxy?.let {
            clientBuilder.proxy(
                Proxy(
                    Proxy.Type.HTTP,
                    InetSocketAddress(it.host, it.port)
                )
            )
        }

        return clientBuilder.build()
    }

    interface AuthTokenProvider {
        val authToken: String?
        fun refreshAuthToken(): Single<String>
    }
}