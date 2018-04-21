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

interface Comment {
    val id: String
    val linkId: String
    val permalink: String
    val name: String
    val parentId: String
    val subreddit: String
    val subredditId: String
    val subredditNamePrefixed: String
    val subredditType: String
    val author: String
    val created: Date
    val body: String
    val bodyHtml: String
    val ups: Int
    val downs: Int
    val score: Int
    val scoreHidden: Boolean
    val approvedAt: Date?
    val bannedAt: Date?
    val saved: Boolean
    val archived: Boolean
    val gilded: Int
    val canGild: Boolean
    val noFollow: Boolean
    val canModPost: Boolean
    val sendReplies: Boolean
    val edited: Int?
    val collapsed: Boolean
    val isSubmitter: Boolean
    val stickied: Boolean
    val controversiality: Int
    val depth: Int
    val replies: List<Comment>
    val moreReplyIds: List<String>

    val isDeleted: Boolean
}