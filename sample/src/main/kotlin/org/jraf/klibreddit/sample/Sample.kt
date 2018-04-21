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

import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import org.apache.commons.text.WordUtils
import org.jraf.klibreddit.client.RedditClient
import org.jraf.klibreddit.model.client.ClientConfiguration
import org.jraf.klibreddit.model.client.HttpConfiguration
import org.jraf.klibreddit.model.client.HttpLoggingLevel
import org.jraf.klibreddit.model.client.HttpProxy
import org.jraf.klibreddit.model.client.UserAgent
import org.jraf.klibreddit.model.listings.Comment
import org.jraf.klibreddit.model.listings.PostWithComments
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

//    client.oAuth.onAuthorizeRedirect("http://jraf.org/klibreddit?state=7c8ff238-212c-4c39-9086-d72849a7e4b9&code=oBoQconTgEboprzSaLmobs89z_Y")
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


//    // First page of /r/popular
//    client.listings.top(
//        subreddit = Subreddits.POPULAR,
//        pagination = Pagination(FirstPage, 4)
//    )
//        .doOnSuccess { println(it) }
//        .flatMap {
//            // Second page of /r/popular
//            client.listings.top(
//                subreddit = Subreddits.POPULAR,
//                pagination = it.nextPagination!!
//            )
//        }
//        .doOnSuccess { println(it) }
//        .flatMap {
//            // Comments of first post of second page of /r/popular
//            client.listings.comments(it.list.first().id)
//        }
//        // Print the comments
//        .doOnSuccess { it.comments.forEach { printCommentWithReplies(it) } }
//        .flatMap {
//            // Get more comments
//            client.listings.moreComments(it)
//        }
//        // Print the comments (there should be more)
//        .subscribeBy {
//            println(repeatString("*", 72))
//            it.comments.forEach { printCommentWithReplies(it) }
//        }

//    client.listings.comments("8aqt03", itemCount = 10)
//        .doOnSuccess { printComments(it.comments) }
//        .flatMap {
//            client.listings.moreComments(it)
//        }
//        .doOnSuccess {
//            println(repeatString("*", 120))
//            println()
//            printComments(it.comments)
//        }
//        .subscribe()
    //        .flatMap {
//            val firstComment = it.comments.first()
//            if (firstComment.moreReplyIds.isNotEmpty()) {
//                client.listings.moreComments(firstComment)
//            } else {
//                Single.just(firstComment)
//            }
//        }

//    client.listings.comments("8aqt03")
    client.listings.comments("8c47fe")
        .flatMap(allCommentsRecursively(client))
        .flatMap(allRepliesRecursively(client))
        .subscribeBy {
            printComments(it.comments)
        }


//    client.listings.comments("8aqt03")
//        .doOnSuccess {
//            printComments(it.comments)
//        }
//        .map { it.comments.last() }
//        .doOnSuccess {
//            println(repeatString("*", 72))
//            if (it != null) printComments(listOf(it), false)
//        }
//        .flatMapCompletable { client.linksAndComments.comment(it, "This comment was posted on ${Date()}") }
//        .subscribe()
}

private fun allCommentsRecursively(client: RedditClient): (PostWithComments) -> Single<PostWithComments> {
    return { postWithComments ->
        if (postWithComments.moreCommentIds.isNotEmpty()) {
            client.listings.moreComments(postWithComments)
                // Recurse
                .flatMap(allCommentsRecursively(client))
        } else {
            Single.just(postWithComments)
        }
    }
}

private fun allRepliesRecursively(client: RedditClient): (PostWithComments) -> Single<PostWithComments> {
    return { postWithComments ->
        val comment = findFirstCommentThatHasMoreReplies(postWithComments.comments)
        if (comment == null) {
            Single.just(postWithComments)
        } else {
            client.listings.moreComments(comment)
                .map { postWithComments.updateComment(it) }
                // Recurse
                .flatMap(allRepliesRecursively(client))
        }
    }
}

private fun findFirstCommentThatHasMoreReplies(comments: List<Comment>): Comment? {
    for (comment in comments) {
        if (comment.moreReplyIds.isNotEmpty()) {
            return comment
        } else {
            // Recurse
            val found = findFirstCommentThatHasMoreReplies(comment.replies)
            if (found != null) return found
        }
    }
    return null
}


private fun printComments(
    comments: List<Comment>,
    withReplies: Boolean = true,
    depth: Int = 0
) {
    val indent = repeatString("    ", depth)
    val separator = repeatString("─", 72)
    for (comment in comments) {
        println("$indent╭$separator")
        println("${indent}│Author: ${comment.author}")
        println("${indent}│Date: ${comment.created}")
        if (!withReplies && comment.replies.isNotEmpty()) {
            println("${indent}│Replies: ${comment.replies.size}")
        }
        if (comment.moreReplyIds.isNotEmpty()) {
            println("${indent}│More replies: ${comment.moreReplyIds.size}")
        }
        println("$indent│")
        println(indent + comment.body.wrapAndIndent(72, indent))
        println("$indent╰$separator")
        println()
        // Recursion
        if (withReplies) printComments(comment.replies, withReplies, depth + 1)
    }
}

private fun repeatString(s: String, times: Int): String {
    var res = ""
    for (i in 0 until times) res += s
    return res
}

private fun String.wrapAndIndent(wrapSize: Int, indent: String): String {
    val wrapped = WordUtils.wrap(this, wrapSize)
    return "│" + wrapped.trimEnd().replace("\n", "\n$indent│")
}