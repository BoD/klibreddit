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

package org.jraf.klibreddit.internal.api.model.listings

import org.jraf.klibreddit.internal.api.model.ApiConverter
import org.jraf.klibreddit.internal.model.listings.CommentImpl
import org.jraf.klibreddit.internal.util.PREFIX_COMMENT
import org.jraf.klibreddit.model.listings.Comment

internal object ApiJsonDataCommentOrMoreConverter :
    ApiConverter<ApiJsonEnvelope<ApiCommentOrMore>, List<Comment>> {
    override fun convert(apiModel: ApiJsonEnvelope<ApiCommentOrMore>): List<Comment> {
        val comments = apiModel.json.data.things
            .filter { it.apiComment != null }
            .map { ApiCommentConverter.convert(it.apiComment!!) }
        val commentsById = comments.associateBy { it.id }.toMutableMap()
        val depth0Comments = mutableListOf<Comment>()
        for (comment in comments) {
            if (comment.parentId.startsWith(PREFIX_COMMENT)) {
                val parentCommentId = comment.parentId.substringAfter(PREFIX_COMMENT)
                val parentComment = commentsById[parentCommentId]
                if (parentComment != null) {
                    val replies = parentComment.replies.toMutableList()
                    replies += comment
                    commentsById[parentCommentId] = parentComment.copy(replies = replies)
                } else {
                    depth0Comments += comment
                }
            } else {
                depth0Comments += comment
            }
        }

        return depth0Comments.map { updateReferences(it as CommentImpl, commentsById) }
    }

    private fun updateReferences(comment: CommentImpl, commentsById: MutableMap<String, CommentImpl>): CommentImpl {
        var c = commentsById[comment.id]!!
        val replies = mutableListOf<CommentImpl>()
        for (reply in c.replies) {
            // Recursion
            replies += updateReferences(reply as CommentImpl, commentsById)
        }
        c = c.copy(replies = replies)
        commentsById[c.id] = c
        return c
    }
}