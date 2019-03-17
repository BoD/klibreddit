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

package org.jraf.klibreddit.internal.model.listings

import org.jraf.klibreddit.model.listings.Post
import java.util.Date

internal data class PostImpl(
    override val id: String,
    override val url: String,
    override val permalink: String,
    override val name: String,

    override val title: String,
    override val author: String,
    override val created: Date,
    override val subreddit: String,
    override val subredditId: String,
    override val subredditNamePrefixed: String,
    override val subredditType: String,

    override val ups: Int,
    override val downs: Int,
    override val score: Int,
    override val hideScore: Boolean,

    override val numComments: Int,
    override val numCrossposts: Int,

    override val selftext: String?,
    override val selftextHtml: String?,

    override val thumbnail: String,
    override val thumbnailWidth: Int?,
    override val thumbnailHeight: Int?,

    override val preview: PreviewImpl?,

    override val archived: Boolean,
    override val brandSafe: Boolean,
    override val canGild: Boolean,
    override val canModPost: Boolean,
    override val clicked: Boolean,
    override val contestMode: Boolean,
    override val domain: String,
    override val edited: Date?,
    override val gilded: Int,
    override val hidden: Boolean,
    override val isCrosspostable: Boolean,
    override val isRedditMediaDomain: Boolean,
    override val isSelf: Boolean,
    override val isVideo: Boolean,
    override val locked: Boolean,
    override val noFollow: Boolean,
    override val over18: Boolean,
    override val pinned: Boolean,
    override val postHint: String?,
    override val quarantine: Boolean,
    override val saved: Boolean,
    override val sendReplies: Boolean,
    override val spoiler: Boolean,
    override val stickied: Boolean,
    override val visited: Boolean,
    override val whitelistStatus: String?,
    override val parentWhitelistStatus: String?
) : Post {
    override fun toString(): String {
        return "\nid: $id\npermalink: $permalink\ntitle: $title\n\n"
    }
}
