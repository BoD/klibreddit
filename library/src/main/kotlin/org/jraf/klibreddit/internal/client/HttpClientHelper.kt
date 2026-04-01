/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2022-present Benoit 'BoD' Lubek (BoD@JRAF.org)
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

import io.ktor.client.HttpClient
import io.ktor.client.engine.ProxyBuilder
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders.Authorization
import io.ktor.http.URLBuilder
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.CancellationException
import kotlinx.serialization.json.Json
import org.jraf.klibreddit.client.RedditClientException
import org.jraf.klibreddit.client.RedditClientRequestException
import org.jraf.klibreddit.client.configuration.ClientConfiguration
import org.jraf.klibreddit.client.configuration.HttpLoggingLevel

internal object HttpClientHelper {
    private const val PARAM_RAW_JSON_NAME = "raw_json"
    private const val PARAM_RAW_JSON_VALUE = "1"

    fun provideHttpClient(
        clientConfiguration: ClientConfiguration,
        authTokenProvider: AuthTokenProvider
    ): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        // XXX Comment this to have API changes make the parsing fail
                        ignoreUnknownKeys = true

                        // This is needed to accept JSON Numbers to be deserialized as Strings
                        isLenient = true

                        // This may improve performance
                        // PLUS is a workaround for these issues:
                        // - https://youtrack.jetbrains.com/issue/KTOR-2740
                        // - https://github.com/Kotlin/kotlinx.serialization/issues/1450
                        useAlternativeNames = false
                    }
                )
            }
            // Token refresh (should be unnecessary since we refresh preemptively, but just in case)
            install(Auth) {
                bearer {
                    refreshTokens {
                        BearerTokens(authTokenProvider.refreshAuthToken(), "")
                    }
                }
            }
            defaultRequest {
                // Auth
                authTokenProvider.authToken?.let {
                    header(Authorization, "Bearer $it")
                }

                // Ask for raw json
                url.parameters.append(PARAM_RAW_JSON_NAME, PARAM_RAW_JSON_VALUE)
            }
            install(UserAgent) {
                agent = clientConfiguration.userAgent.toString()
            }
            engine {
                // Setup a proxy if requested
                clientConfiguration.httpConfiguration.httpProxy?.let { httpProxy ->
                    proxy = ProxyBuilder.http(URLBuilder().apply {
                        host = httpProxy.host
                        port = httpProxy.port
                    }.build())
                }
            }
            // Setup logging if requested
            if (clientConfiguration.httpConfiguration.loggingLevel != HttpLoggingLevel.NONE) {
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = when (clientConfiguration.httpConfiguration.loggingLevel) {
                        HttpLoggingLevel.NONE -> LogLevel.NONE
                        HttpLoggingLevel.INFO -> LogLevel.INFO
                        HttpLoggingLevel.HEADERS -> LogLevel.HEADERS
                        HttpLoggingLevel.BODY -> LogLevel.BODY
                        HttpLoggingLevel.ALL -> LogLevel.ALL
                    }
                }
            }
            HttpResponseValidator {
                handleResponseExceptionWithRequest { cause: Throwable, _ ->
                    if (cause is ClientRequestException) throw RedditClientRequestException(
                        cause = cause,
                        body = cause.response.bodyAsText(),
                    )

                    if (cause !is CancellationException) throw RedditClientException(cause)
                }
            }
        }
    }

    interface AuthTokenProvider {
        val authToken: String?
        suspend fun refreshAuthToken(): String
    }
}
