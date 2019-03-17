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
import org.jraf.klibreddit.internal.util.toDate
import java.util.Collections

internal object ApiCommentConverter : ApiConverter<ApiComment, CommentImpl> {
    override fun convert(apiModel: ApiComment): CommentImpl {
        val replies = mutableListOf<CommentImpl>()
        var moreReplyIds = emptyList<String>()
        if (apiModel.replies.data != null) {
            for ((apiComment, apiMore) in apiModel.replies.data.children) {
                if (apiComment != null) {
                    // Recurse
                    replies += convert(apiComment)
                } else if (apiMore != null) {
                    moreReplyIds = apiMore.children
                }
            }
        }
        return CommentImpl(
            apiModel.id,
            apiModel.link_id,
            apiModel.permalink,
            apiModel.name,
            apiModel.parent_id,
            apiModel.subreddit,
            apiModel.subreddit_id,
            apiModel.subreddit_name_prefixed,
            apiModel.subreddit_type,
            apiModel.author,
            apiModel.created_utc.toDate(),
            apiModel.body,
            apiModel.body_html,
            apiModel.ups,
            apiModel.downs,
            apiModel.score,
            apiModel.score_hidden,
            apiModel.approved_at_utc?.toDate(),
            apiModel.banned_at_utc?.toDate(),
            apiModel.saved,
            apiModel.archived,
            apiModel.gilded,
            apiModel.can_gild,
            apiModel.no_follow,
            apiModel.can_mod_post,
            apiModel.send_replies,
            apiModel.edited.int,
            apiModel.collapsed,
            apiModel.is_submitter,
            apiModel.stickied,
            apiModel.controversiality,
            apiModel.depth,
            Collections.unmodifiableList(replies),
            Collections.unmodifiableList(moreReplyIds)
        )
    }
}