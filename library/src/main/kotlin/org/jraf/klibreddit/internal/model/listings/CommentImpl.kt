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

package org.jraf.klibreddit.internal.model.listings

import org.jraf.klibreddit.model.listings.Comment
import java.util.Date

internal data class CommentImpl(
    override val id: String,
    override val linkId: String,
    override val permalink: String,
    override val name: String,
    override val parentId: String,

    override val subreddit: String,
    override val subredditId: String,
    override val subredditNamePrefixed: String,
    override val subredditType: String,

    override val author: String,
    override val created: Date,
    override val body: String,
    override val bodyHtml: String,

    override val ups: Int,
    override val downs: Int,
    override val score: Int,
    override val scoreHidden: Boolean,

    override val approvedAt: Date?,
    override val bannedAt: Date?,

    override val saved: Boolean,
    override val archived: Boolean,

    override val gilded: Int,
    override val canGild: Boolean,

    override val noFollow: Boolean,
    override val canModPost: Boolean,
    override val sendReplies: Boolean,
    override val edited: Int?,
    override val collapsed: Boolean,
    override val isSubmitter: Boolean,
    override val stickied: Boolean,
    override val controversiality: Int,

    override val depth: Int,

    override val replies: List<Comment>,
    override val moreReplyIds: List<String>
) : Comment {
    override fun toString(): String {
        return "\nid: $id\npermalink: $permalink\nbody: $body\n\n"
    }
}
