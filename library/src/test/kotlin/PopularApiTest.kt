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
import org.jraf.klibreddit.model.listings.Subreddits
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PopularApiTest : ApiTest() {
    @Test
    fun testPopularApi1() {
        mockBestPage1Response("popular_page1-response1.json")
        mockBestPage2Response("popular_page2-response1.json")
        val page1 = client.listings.top(
            subreddit = Subreddits.POPULAR,
            pagination = Pagination(FirstPage, 4)
        ).blockingGet()
        assertEquals(4, page1.list.size)
        assertEquals(After("t3_5jrlw1"), page1.nextPageIndex)
        assertNull(page1.previousPageIndex)

        validatePost(
            page1.list[0],
            "funny", 1480959674L,
            "iH8myPP",
            "Guardians of the Front Page",
            283488,
            5069,
            "http://i.imgur.com/OOFRJvr.gifv"
        )
        validatePost(
            page1.list[1],
            "pics",
            1478651245,
            "Itsjorgehernandez",
            "Thanks, Obama.",
            230825,
            6169,
            "https://i.reddituploads.com/58986555f545487c9d449bd5d9326528?fit=max&h=1536&w=1536&s=c15543d234ef9bbb27cb168b01afb87d"
        )

        val page2 = client.listings.top(
            subreddit = Subreddits.POPULAR,
            pagination = Pagination(page1.nextPageIndex!!, 4)
        ).blockingGet()
        assertEquals(4, page2.list.size)
        assertEquals(After("t3_4vq7rj"), page2.nextPageIndex)

        validatePost(
            page2.list[2],
            "pics",
            1485198917L,
            "GallowBoob",
            "Don't forget about this",
            157911,
            5379,
            "http://i.imgur.com/7aGdxhe.jpg"
        )

        validatePost(
            page2.list[3],
            "funny",
            1470104173L,
            "Nephelus",
            "Due to all the health hazards surrounding the Rio Olympics, I figured they could use a new logo. [OC]",
            141607,
            2190,
            "http://i.imgur.com/FApqk3D.jpg"
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
            get(urlMatching("/r/popular/top.t=all.limit=.*$")).willReturn(
                aResponse().withStatus(200).withBodyFile(
                    responseFile
                )
            )
        )
    }

    private fun mockBestPage2Response(responseFile: String) {
        stubFor(
            get(urlMatching("/r/popular/top.t=all.after.*$"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBodyFile(responseFile)
                )
        )
    }

}
