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


/* internal */ data class ApiPost(
    val subreddit_id: String,
    val send_replies: Boolean,
    val thumbnail: String,
    val thumbnail_width: Int?,
    val thumbnail_height: Int?,
    val subreddit: String,
    val selftext_html: String?,
    val selftext: String?,
    val is_reddit_media_domain: Boolean,
    val saved: Boolean,
    val id: String,
    val archived: Boolean,
    val clicked: Boolean,
    val no_follow: Boolean,
    val title: String,
    val num_crossposts: Int,
    val can_mod_post: Boolean,
    val is_crosspostable: Boolean,
    val pinned: Boolean,
    val score: Int,
    val over_18: Boolean,
    val domain: String,
    val hidden: Boolean,
    val preview: ApiPreview,
    val edited: ApiOptionalDate,
    val contest_mode: Boolean,
    val gilded: Int,
    val downs: Int,
    val brand_safe: Boolean,
//     val secure_media_embed: ApiSecureMediaEmbed?,
//     val media_embed: ApiMediaEmbed?,
    val post_hint: String?,
    val stickied: Boolean,
    val can_gild: Boolean,
    val parent_whitelist_status: String?,
    val name: String,
    val spoiler: Boolean,
    val permalink: String,
    val subreddit_type: String,
    val locked: Boolean,
    val hide_score: Boolean,
    val created: Double,
    val url: String,
    val whitelist_status: String?,
    val quarantine: Boolean,
    val author: String,
    val created_utc: Double,
    val subreddit_name_prefixed: String,
    val ups: Int,
    val num_comments: Int,
    val is_self: Boolean,
    val visited: Boolean,
    val is_video: Boolean
)
