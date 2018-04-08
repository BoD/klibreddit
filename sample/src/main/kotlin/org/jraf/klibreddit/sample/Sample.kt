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

package org.jraf.klibreddit.sample

import io.reactivex.rxkotlin.subscribeBy
import org.apache.commons.text.WordUtils
import org.jraf.klibreddit.client.RedditClient
import org.jraf.klibreddit.model.client.ClientConfiguration
import org.jraf.klibreddit.model.client.HttpConfiguration
import org.jraf.klibreddit.model.client.HttpLoggingLevel
import org.jraf.klibreddit.model.client.HttpProxy
import org.jraf.klibreddit.model.client.UserAgent
import org.jraf.klibreddit.model.listings.Comment
import org.jraf.klibreddit.model.listings.FirstPage
import org.jraf.klibreddit.model.listings.Pagination
import org.jraf.klibreddit.model.listings.Subreddits
import org.jraf.klibreddit.model.oauth.OAuthConfiguration

const val PLATFORM = "cli"
const val APP_ID = "klibreddit-sample"
const val VERSION = "1.0.0"
const val AUTHOR_REDDIT_NAME = "bodlulu"

fun main(av: Array<String>) {
    // Logging
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace")

    val client = RedditClient.newRedditClient(
        ClientConfiguration(
            UserAgent(PLATFORM, APP_ID, VERSION, AUTHOR_REDDIT_NAME),
            OAuthConfiguration(
                System.getenv("OAUTH_CLIENT_ID"),
                System.getenv("OAUTH_REDIRECT_URI")
            ),
            HttpConfiguration(
                loggingLevel = HttpLoggingLevel.BASIC,
                httpProxy = HttpProxy("localhost", 8888)
            )
        )
    )

//    println(client.oAuth.getAuthorizeUrl(*OAuthScope.values()))
//    client.oAuth.onAuthorizeRedirect("http://jraf.org/klibreddit?state=92179eb5-e76d-4992-888b-650213ee4113&code=m6nd1vz4hush7EFz2_O45H9fEM4")
//        .doOnSuccess { println("Your refresh token is $it") }
//        .flatMap { client.account.me() }
//        .subscribeBy { println("id: ${it.id} name: ${it.name} created: ${it.created}") }

    client.oAuth.setRefreshToken(System.getenv("OAUTH_REFRESH_TOKEN"))

//    client.account.me()
//        .subscribeBy { println("id: ${it.id} name: ${it.name} created: ${it.created}") }

//    client.listings.best(Pagination(FirstPage, 2))
//        .doOnSuccess { println(it) }
//        .flatMap { client.listings.best(Pagination(it.nextPageIndex!!, 2)) }
//        .subscribeBy { println(it) }


    // First page of /r/popular
    client.listings.top(
        subreddit = Subreddits.POPULAR,
        pagination = Pagination(FirstPage, 4)
    )
        .doOnSuccess { println(it) }
        .flatMap {
            // Second page of /r/popular
            client.listings.top(
                subreddit = Subreddits.POPULAR,
                pagination = it.nextPagination!!
            )
        }
        .doOnSuccess { println(it) }
        .flatMap {
            // Comments of first post of second page of /r/popular
            client.listings.comments(it.list.first().id)
        }
        // Print the comments
        .doOnSuccess { it.comments.forEach { printCommentWithReplies(it) } }
        .flatMap {
            // Get more comments
            client.listings.moreComments(it)
        }
        // Print the comments (there should be more)
        .subscribeBy {
            println(repeatString("*", 72))
            it.comments.forEach { printCommentWithReplies(it) }
        }

//    client.listings.comments("890iek", maxDepth = 1)
//        .doOnSuccess { it.comments.forEach { printCommentWithReplies(it) } }
//        .flatMap {
//            val firstComment = it.comments.first()
//            if (firstComment.moreReplyIds.isNotEmpty()) {
//                client.listings.moreComments(firstComment)
//            } else {
//                Single.just(firstComment)
//            }
//        }
//        .subscribeBy { printCommentWithReplies(it) }
}

fun printCommentWithReplies(comment: Comment, depth: Int = 0) {
    val indent = repeatString("    ", depth)
    val separator = repeatString("-", 72)
    println("$indent$separator")
    println("${indent}Author: ${comment.author}")
    println("${indent}Date: ${comment.created}")
    println(indent)
    println(indent + comment.body.wrapAndIndent(72, indent))
    println("$indent$separator")
    println()
    for (reply in comment.replies) {
        // Recursion
        printCommentWithReplies(reply, depth + 1)
    }
}

private fun repeatString(s: String, times: Int): String {
    var res = ""
    for (i in 0 until times) res += s
    return res
}

private fun String.wrapAndIndent(wrapSize: Int, indent: String): String {
    val wrapped = WordUtils.wrap(this, wrapSize)
    return wrapped.trimEnd().replace("\n", "\n$indent")
}