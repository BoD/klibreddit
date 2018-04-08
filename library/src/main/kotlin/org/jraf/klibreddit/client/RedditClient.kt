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

package org.jraf.klibreddit.client

import io.reactivex.Single
import org.jraf.klibreddit.internal.client.RedditClientImpl
import org.jraf.klibreddit.model.account.Me
import org.jraf.klibreddit.model.client.ClientConfiguration
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
        fun onAuthorizeRedirect(authorizeRedirectUri: String): Single<String>
        fun setRefreshToken(refreshToken: String)
    }

    interface Account {
        fun me(): Single<Me>
    }

    interface Listings {
        /**
         * Best posts of the subreddits you've subscribed to.
         */
        fun best(pagination: Pagination = Pagination(FirstPage)): Single<Page<Post>>

        /**
         * Hot posts of all the subreddits you've subscribed to, or a specific subreddit.
         */
        fun hot(
            subreddit: String? = null,
            pagination: Pagination = Pagination(FirstPage)
        ): Single<Page<Post>>

        /**
         * New posts of all the subreddits you've subscribed to, or a specific subreddit.
         */
        fun new(
            subreddit: String? = null,
            pagination: Pagination = Pagination(FirstPage)
        ): Single<Page<Post>>

        /**
         * Rising posts of all the subreddits you've subscribed to, or a specific subreddit.
         */
        fun rising(
            subreddit: String? = null,
            pagination: Pagination = Pagination(FirstPage)
        ): Single<Page<Post>>

        /**
         * Controversial posts of all the subreddits you've subscribed to, or a specific subreddit.
         */
        fun controversial(
            subreddit: String? = null,
            period: Period = Period.ALL,
            pagination: Pagination = Pagination(FirstPage)
        ): Single<Page<Post>>

        /**
         * Top posts of all the subreddits you've subscribed to, or a specific subreddit.
         */
        fun top(
            subreddit: String? = null,
            period: Period = Period.ALL,
            pagination: Pagination = Pagination(FirstPage)
        ): Single<Page<Post>>

        /**
         * Post with its comments.
         */
        fun comments(
            postId: String,
            order: CommentListOrder = CommentListOrder.CONFIDENCE,
            itemCount: Int? = null,
            maxDepth: Int? = null
        ): Single<PostWithComments>

        /**
         * Retrieve more reply comments for the given comment.
         */
        fun moreReplies(comment: Comment): Single<Comment>
    }

    val oAuth: OAuth
    val account: Account
    val listings: Listings
}