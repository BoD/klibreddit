/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2022-present Benoit 'BoD' Lubek (BoD@JRAF.org)
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

package org.jraf.klibreddit.internal.json.listings

import org.jraf.klibreddit.internal.json.JsonConverter
import org.jraf.klibreddit.internal.model.listings.CommentImpl
import org.jraf.klibreddit.internal.util.toDate
import java.util.Collections

internal object JsonCommentConverter :
    JsonConverter<JsonComment, CommentImpl> {
    override fun convert(jsonModel: JsonComment): CommentImpl {
        val replies = mutableListOf<CommentImpl>()
        var moreReplyIds = emptyList<String>()
        if (jsonModel.replies.data != null) {
            for (jsonCommentOrMore in jsonModel.replies.data.children) {
                if (jsonCommentOrMore is JsonCommentOrMore.Comment) {
                    // Recurse
                    replies += convert(jsonCommentOrMore.data)
                } else if (jsonCommentOrMore is JsonCommentOrMore.More) {
                    moreReplyIds = jsonCommentOrMore.data.children
                }
            }
        }
        return CommentImpl(
            jsonModel.id,
            jsonModel.link_id,
            jsonModel.permalink,
            jsonModel.name,
            jsonModel.parent_id,
            jsonModel.subreddit,
            jsonModel.subreddit_id,
            jsonModel.subreddit_name_prefixed,
            jsonModel.subreddit_type,
            jsonModel.author,
            jsonModel.created_utc.toDate(),
            jsonModel.body,
            jsonModel.body_html,
            jsonModel.ups,
            jsonModel.downs,
            jsonModel.score,
            jsonModel.score_hidden,
            jsonModel.approved_at_utc?.toDate(),
            jsonModel.banned_at_utc?.toDate(),
            jsonModel.saved,
            jsonModel.archived,
            jsonModel.gilded,
            jsonModel.can_gild,
            jsonModel.no_follow,
            jsonModel.can_mod_post,
            jsonModel.send_replies,
            JsonOptionalDateConverter.convert(jsonModel.edited),
            jsonModel.collapsed,
            jsonModel.is_submitter,
            jsonModel.stickied,
            jsonModel.controversiality,
            jsonModel.depth,
            Collections.unmodifiableList(replies),
            Collections.unmodifiableList(moreReplyIds)
        )
    }
}
