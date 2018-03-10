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
import org.jraf.klibreddit.client.RedditClient
import org.jraf.klibreddit.model.client.ClientConfiguration
import org.jraf.klibreddit.model.client.UserAgent
import org.jraf.klibreddit.model.oauth.OAuthConfiguration

const val PLATFORM = "cli"
const val APP_ID = "klibreddit-sample"
const val VERSION = "1.0.0"
const val AUTHOR_REDDIT_NAME = "bodlulu"

fun main(av: Array<String>) {
    val client = RedditClient.newRedditClient(
        ClientConfiguration(
            UserAgent(PLATFORM, APP_ID, VERSION, AUTHOR_REDDIT_NAME),
            OAuthConfiguration(
                System.getenv("OAUTH_CLIENT_ID"),
                System.getenv("OAUTH_REDIRECT_URI")
            )
        )
    )

//    println(client.getAuthorizeUrl(*OAuthScope.values()))
//    client.onAuthorizeRedirect("http://jraf.org/klibreddit?state=51ebeff1-0594-4fc6-ae33-85c8c11e71f1&code=SqC7DlgeE75v49-YqukUjHKTbJQ")
//        .flatMap { client.me() }


    client.setRefreshToken(System.getenv("OAUTH_REFRESH_TOKEN"))
//    client.me()
//        .subscribeBy { println("id: ${it.id} name: ${it.name} created: ${it.created}") }
    client.best()
        .subscribeBy { println(it) }
}