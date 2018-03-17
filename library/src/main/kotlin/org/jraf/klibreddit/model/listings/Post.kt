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

package org.jraf.klibreddit.model.listings

import java.util.Date

data class Post(
    val id: String,
    val url: String,
    val permalink: String,
    val name: String,

    val title: String,
    val author: String,
    val created: Date,
    val subreddit: String,
    val subredditId: String,
    val subredditNamePrefixed: String,
    val subredditType: String,
    val ups: Int,
    val downs: Int,
    val score: Int,
    val numComments: Int,
    val numCrossposts: Int,

    val selftext: String?,
    val selftextHtml: String?,

    val thumbnail: String,
    val thumbnailWidth: Int?,
    val thumbnailHeight: Int?,

    val preview: Preview?,

    val archived: Boolean,
    val brandSafe: Boolean,
    val canGild: Boolean,
    val canModPost: Boolean,
    val clicked: Boolean,
    val contestMode: Boolean,
    val domain: String,
    val edited: Date?,
    val gilded: Int,
    val hidden: Boolean,
    val hideScore: Boolean,
    val isCrosspostable: Boolean,
    val isRedditMediaDomain: Boolean,
    val isSelf: Boolean,
    val isVideo: Boolean,
    val locked: Boolean,
    val noFollow: Boolean,
    val over18: Boolean,
    val pinned: Boolean,
    val postHint: String?,
    val quarantine: Boolean,
    val saved: Boolean,
    val sendReplies: Boolean,
    val spoiler: Boolean,
    val stickied: Boolean,
    val visited: Boolean,
    val whitelistStatus: String?,
    val parentWhitelistStatus: String?

    //     val media_embed: ApiMediaEmbed?,
    //     val secure_media_embed: ApiSecureMediaEmbed?,


) {
    override fun toString(): String {
        return "\nid: $id\npermalink: $permalink\ntitle: $title\n\n"
    }
}
