/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2018 Carmen Alvarez
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

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit.WireMockClassRule
import okhttp3.logging.HttpLoggingInterceptor
import org.jraf.klibreddit.client.RedditClient
import org.jraf.klibreddit.model.client.ClientConfiguration
import org.jraf.klibreddit.model.client.UserAgent
import org.jraf.klibreddit.model.oauth.OAuthConfiguration
import org.junit.AfterClass
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule


open class ApiTest {

    protected lateinit var client: RedditClient

    @Rule
    @JvmField
    val instanceRule = wireMockRule

    companion object {
        @ClassRule
        @JvmField
        var wireMockRule = WireMockClassRule(
                WireMockConfiguration.wireMockConfig().dynamicPort().notifier(
                        ConsoleNotifier(true)
                )
        )

        @AfterClass
        fun stopWireMock() {
            wireMockRule.stop()
        }
    }

    @Before
    fun setup() {
        mockAccessToken()
        client = RedditClient.newRedditClient(
                ClientConfiguration(
                        UserAgent("some platform", "some app id", "some version", "some author"),
                        OAuthConfiguration("some_client_id", "some_redirect_uri"),
                        loggingLevel = HttpLoggingInterceptor.Level.BODY,
                        mockServerHost = "localhost",
                        mockServerPort = wireMockRule.port(),
                        mockServerScheme = "http"
                )
        )
        client.oAuth.setRefreshToken("some refresh token")
    }

    private fun mockAccessToken() {
        stubFor(
                post(urlMatching("/api/v1/access_token.*$"))
                    .willReturn(
                            aResponse()
                                .withStatus(200)
                                .withBodyFile("access_token-response1.json")
                    )
        )
    }
}
