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
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MeApiTest : ApiTest() {
    @Test
    fun testMeApi1() {
        mockMeResponse("me-response1.json")
        val me = client.account.me().blockingGet()
        assertEquals("KLibRedditTester", me.name)
        assertTrue(me.hasVerifiedEmail)
        assertFalse(me.hasMail)
        assertFalse(me.over18)
        assertFalse(me.isGold)
        assertFalse(me.isSuspended)
        assertFalse(me.isEmployee)
        assertTrue(me.prefNoProfanity)
        assertEquals(90, me.commentKarma)
        assertEquals(150, me.linkKarma)
    }

    private fun mockMeResponse(responseFile: String) {
        stubFor(
                get(urlMatching("/api/v1/me.*$"))
                    .willReturn(
                            aResponse()
                                .withStatus(200)
                                .withBodyFile(responseFile)
                    )
        )
    }
}
