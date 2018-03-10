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

package org.jraf.klibreddit.internal.api.model.listings

import org.jraf.klibreddit.internal.api.model.ApiConverter
import org.jraf.klibreddit.internal.util.DateUtil.toDate
import org.jraf.klibreddit.model.listings.Post

internal object ApiPostConverter : ApiConverter<ApiPost, Post> {
    override fun convert(apiModel: ApiPost) = Post(
        apiModel.id,
        apiModel.url,
        apiModel.permalink,
        apiModel.name,
        apiModel.title,
        apiModel.author,
        apiModel.created_utc.toDate(),
        apiModel.subreddit,
        apiModel.subreddit_id,
        apiModel.subreddit_name_prefixed,
        apiModel.subreddit_type,
        apiModel.ups,
        apiModel.downs,
        apiModel.score,
        apiModel.num_comments,
        apiModel.num_crossposts,
        apiModel.selftext,
        apiModel.selftext_html,
        apiModel.thumbnail,
        apiModel.thumbnail_width,
        apiModel.thumbnail_height,
        ApiPreviewConverter.convert(apiModel.preview),
        apiModel.archived,
        apiModel.brand_safe,
        apiModel.can_gild,
        apiModel.can_mod_post,
        apiModel.clicked,
        apiModel.contest_mode,
        apiModel.domain,
        apiModel.edited.date,
        apiModel.gilded,
        apiModel.hidden,
        apiModel.hide_score,
        apiModel.is_crosspostable,
        apiModel.is_reddit_media_domain,
        apiModel.is_self,
        apiModel.is_video,
        apiModel.locked,
        apiModel.no_follow,
        apiModel.over_18,
        apiModel.pinned,
        apiModel.post_hint,
        apiModel.quarantine,
        apiModel.saved,
        apiModel.send_replies,
        apiModel.spoiler,
        apiModel.stickied,
        apiModel.visited,
        apiModel.whitelist_status,
        apiModel.parent_whitelist_status
    )
}