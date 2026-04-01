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

import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.jraf.klibreddit.internal.json.JsonConverter
import org.jraf.klibreddit.internal.model.listings.PostImpl
import org.jraf.klibreddit.internal.util.toDate

internal object JsonPostConverter : JsonConverter<JsonPost, PostImpl> {
    override fun convert(jsonModel: JsonPost) = PostImpl(
        jsonModel.id,
        jsonModel.url,
        jsonModel.permalink,
        jsonModel.name,
        jsonModel.title,
        jsonModel.author,
        jsonModel.created_utc.toDate(),
        jsonModel.subreddit,
        jsonModel.subreddit_id,
        jsonModel.subreddit_name_prefixed,
        jsonModel.subreddit_type,
        jsonModel.ups,
        jsonModel.downs,
        jsonModel.score,
        jsonModel.hide_score,
        jsonModel.num_comments,
        jsonModel.num_crossposts,
        if (jsonModel.selftext == "") null else jsonModel.selftext,
        jsonModel.selftext_html,
        jsonModel.thumbnail,
        jsonModel.thumbnail_width,
        jsonModel.thumbnail_height,
        JsonPreviewConverter.convert(jsonModel.preview),
        jsonModel.archived,
        jsonModel.brand_safe == true,
        jsonModel.can_gild,
        jsonModel.can_mod_post,
        jsonModel.clicked,
        jsonModel.contest_mode,
        jsonModel.domain,
        JsonOptionalDateConverter.convert(jsonModel.edited),
        jsonModel.gilded,
        jsonModel.hidden,
        jsonModel.is_crosspostable,
        jsonModel.is_reddit_media_domain,
        jsonModel.is_self,
        jsonModel.is_video,
        jsonModel.locked,
        jsonModel.no_follow,
        jsonModel.over_18,
        jsonModel.pinned,
        jsonModel.post_hint,
        jsonModel.quarantine,
        jsonModel.saved,
        jsonModel.send_replies,
        jsonModel.spoiler,
        jsonModel.stickied,
        jsonModel.visited,
        jsonModel.whitelist_status,
        jsonModel.parent_whitelist_status
    )
}
