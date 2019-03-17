/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2018-present Carmen Alvarez
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
import org.jraf.klibreddit.model.listings.After
import org.jraf.klibreddit.model.listings.FirstPage
import org.jraf.klibreddit.model.listings.Pagination
import org.jraf.klibreddit.model.listings.Post
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BestApiTest : ApiTest() {
    @Test
    fun testBestApi1() {
        mockBestPage1Response("best_page1-response1.json")
        mockBestPage2Response("best_page2-response1.json")
        val page1 = client.listings.best(Pagination(FirstPage, 2)).blockingGet()
        assertEquals(2, page1.list.size)
        assertEquals(After("t3_85b32g"), page1.nextPageIndex)
        assertNull(page1.previousPageIndex)

        validatePost(
            page1.list[0],
            "EmpireDidNothingWrong",
            1521383873L,
            "officialeskimo",
            "All hail",
            11309,
            56,
            "https://i.redd.it/woqucdcu9jm01.jpg"
        )

        validatePost(
            page1.list[1],
            "Eyebleach",
            1521378145L,
            "RespectMyAuthoriteh",
            "He looks so guilty",
            19499,
            116,
            "https://i.imgur.com/IfBGx3E.gifv"
        )
        val page2 = client.listings.best(Pagination(page1.nextPageIndex!!, 2)).blockingGet()
        assertEquals(2, page2.list.size)
        assertEquals(After("t3_85clp6"), page2.nextPageIndex)

        validatePost(
            page2.list[0],
            "PrequelMemes",
            1521381507L,
            "JunkNuggets",

            "When you have a job interview and the hiring manager’s first words are, “Hello there.”",
            8080,
            115,
            "https://i.redd.it/0lvt2x7t2jm01.jpg"
        )

        validatePost(
            page2.list[1],
            "happycowgifs",
            1521393568L,
            "NoBat0",
            "Cow kisses",
            441,
            9,
            "https://i.imgur.com/zAf5usd.gifv"
        )
        assertNull(page1.previousPageIndex)
    }

    private fun validatePost(
        post: Post, expectedSubreddit: String, expectedCreatedTime: Long,
        expectedAuthor: String, expectedTitle: String, expectedUps: Int, expectedNumComments: Int,
        expectedUrl: String
    ) {
        assertEquals(expectedSubreddit, post.subreddit)
        assertEquals(expectedCreatedTime, post.created.time / 1000)
        assertEquals(expectedAuthor, post.author)
        assertEquals(expectedTitle, post.title)
        assertEquals(expectedUps, post.ups)
        assertEquals(expectedNumComments, post.numComments)
        assertEquals(expectedUrl, post.url)
    }

    private fun mockBestPage1Response(responseFile: String) {
        stubFor(
            get(urlMatching("/best.limit=2&raw_json=1")).willReturn(
                aResponse().withStatus(200).withBodyFile(
                    responseFile
                )
            )
        )
    }

    private fun mockBestPage2Response(responseFile: String) {
        stubFor(
            get(urlMatching("/best.after.*$"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBodyFile(responseFile)
                )
        )
    }

}
