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
import org.jraf.klibreddit.model.listings.Post
import org.jraf.klibreddit.model.listings.PostWithComments

internal data class PostWithCommentsImpl(
    override val post: Post,
    override val comments: List<Comment>,
    override val moreCommentIds: List<String>
) : PostWithComments {
    override fun updateComment(commentToSearchAndReplace: Comment): PostWithComments {
        val newComments = mutableListOf<Comment>()
        var found = false
        for (origComment in comments) {
            if (!found) {
                if (origComment.id == commentToSearchAndReplace.id) {
                    found = true
                    newComments += commentToSearchAndReplace
                } else {
                    val updatedComment = updateComment(origComment, commentToSearchAndReplace)
                    found = updatedComment !== origComment
                    newComments += updatedComment
                }
            } else {
                newComments += origComment
            }
        }
        return if (found) {
            copy(comments = newComments)
        } else {
            this
        }
    }

    companion object {
        private fun updateComment(parentComment: Comment, replyToSearchAndReplace: Comment): Comment {
            val newReplies = mutableListOf<Comment>()
            var found = false
            for (origReply in parentComment.replies) {
                if (!found) {
                    if (origReply.id == replyToSearchAndReplace.id) {
                        found = true
                        newReplies += replyToSearchAndReplace
                    } else {
                        // Recurse
                        val updatedReply = updateComment(origReply, replyToSearchAndReplace)
                        found = updatedReply !== origReply
                        newReplies += updatedReply
                    }
                } else {
                    newReplies += origReply
                }
            }
            return if (found) {
                (parentComment as CommentImpl).copy(replies = newReplies)
            } else {
                parentComment
            }
        }
    }
}