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

package org.jraf.klibreddit.client

import org.jraf.klibreddit.internal.client.RedditClientImpl
import org.jraf.klibreddit.model.account.Me
import org.jraf.klibreddit.client.configuration.ClientConfiguration
import org.jraf.klibreddit.model.listings.Comment
import org.jraf.klibreddit.model.listings.CommentListOrder
import org.jraf.klibreddit.model.listings.FirstPage
import org.jraf.klibreddit.model.listings.Page
import org.jraf.klibreddit.model.listings.Pagination
import org.jraf.klibreddit.model.listings.Period
import org.jraf.klibreddit.model.listings.Post
import org.jraf.klibreddit.model.listings.PostWithComments
import org.jraf.klibreddit.model.oauth.OAuthScope

interface RedditClient {
    companion object {
        fun newRedditClient(configuration: ClientConfiguration): RedditClient = RedditClientImpl(configuration)
    }

    interface OAuth {
        fun getAuthorizeUrl(vararg scopes: OAuthScope): String
        suspend fun onAuthorizeRedirect(authorizeRedirectUri: String): String
        fun setRefreshToken(refreshToken: String)
    }

    interface Account {
        suspend fun me(): Me
    }

    interface Listings {
        /**
         * Retrieve the best posts of the subreddits you've subscribed to.
         */
        suspend fun best(pagination: Pagination = Pagination(FirstPage)): Page<Post>

        /**
         * Retrieve the hot posts of all the subreddits you've subscribed to, or a specific subreddit.
         */
        suspend fun hot(
            subreddit: String? = null,
            pagination: Pagination = Pagination(FirstPage)
        ): Page<Post>

        /**
         * Retrieve the new posts of all the subreddits you've subscribed to, or a specific subreddit.
         */
        suspend fun new(
            subreddit: String? = null,
            pagination: Pagination = Pagination(FirstPage)
        ): Page<Post>

        /**
         * Retrieve the rising posts of all the subreddits you've subscribed to, or a specific subreddit.
         */
        suspend fun rising(
            subreddit: String? = null,
            pagination: Pagination = Pagination(FirstPage)
        ): Page<Post>

        /**
         * Retrieve the controversial posts of all the subreddits you've subscribed to, or a specific subreddit.
         */
        suspend fun controversial(
            subreddit: String? = null,
            period: Period = Period.ALL,
            pagination: Pagination = Pagination(FirstPage)
        ): Page<Post>

        /**
         * Retrieve the top posts of all the subreddits you've subscribed to, or a specific subreddit.
         */
        suspend fun top(
            subreddit: String? = null,
            period: Period = Period.ALL,
            pagination: Pagination = Pagination(FirstPage)
        ): Page<Post>

        /**
         * Retrieve the post [postId] with its comments.
         */
        suspend fun comments(
            postId: String,
            order: CommentListOrder = CommentListOrder.CONFIDENCE,
            itemCount: Int? = null,
            maxDepth: Int? = null
        ): PostWithComments

        /**
         * Retrieve more comments for the given post with comments.
         */
        suspend fun moreComments(postWithComments: PostWithComments): PostWithComments

        /**
         * Retrieve more reply comments for the given comment.
         */
        suspend fun moreComments(comment: Comment): Comment
    }

    interface LinksAndComments {
        /**
         * Post a reply to a comment.
         */
        suspend fun comment(comment: Comment, text: String)
    }


    val oAuth: OAuth
    val account: Account
    val listings: Listings
    val linksAndComments: LinksAndComments
}
