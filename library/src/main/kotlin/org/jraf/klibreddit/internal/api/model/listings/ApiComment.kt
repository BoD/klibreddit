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

/* internal */ data class ApiComment(
    val archived: Boolean,
    val url: String,
//    val secure_media_embed: Secure_media_embed,
//    val media_embed: Media_embed,
//    val approved_by: Any,
//    val distinguished: Any,
    val selftext: String?,
    val selftext_html: String?,
    val subreddit: String,
    val thumbnail: String,
    val thumbnail_width: Int?,
    val thumbnail_height: Int?,
    val approved_at_utc: Double?,
    val is_video: Boolean,
//    val removal_reason: Any,
//    val mod_note: Any,
//    val banned_by: Any,
//    val likes: Any,
    val domain: String,
    val subreddit_type: String,
//    val secure_media: Any,
    val link_flair_text: String,
    val locked: Boolean,
    val author: String,
    val created: Double,
//    val mod_reports: List<Any>,
//    val banned_at_utc: Any,
    val hide_score: Boolean,
    val permalink: String,
    val created_utc: Double,
    val is_crosspostable: Boolean,
    val upvote_ratio: Double,
    val title: String,
    val subreddit_name_prefixed: String,
    val stickied: Boolean,
    val spoiler: Boolean,
//    val media: Any,
    val num_comments: Int,
    val num_reports: Int?,
    val visited: Boolean,
    val preview: ApiPreview,
    val can_mod_post: Boolean,
    val gilded: Int,
    val contest_mode: Boolean,
    val no_follow: Boolean,
    val id: String,
    val can_gild: Boolean,
    val send_replies: Boolean,
    val parent_whitelist_status: String,
//    val report_reasons: Any,
    val num_crossposts: Int,
    val ups: Int,
    val pinned: Boolean,
    val over_18: Boolean,
    val name: String,
    val is_self: Boolean,
//    val view_count: Any,
    val score: Int,
//    val mod_reason_by: Any,
//    val suggested_sort: Any,
    val link_flair_css_class: String,
    val subreddit_subscribers: Int,
//    val user_reports: List<Any>,
    val is_reddit_media_domain: Boolean,
    val edited: Boolean,
    val quarantine: Boolean,
    val downs: Int,
    val hidden: Boolean,
    val clicked: Boolean,
    val brand_safe: Boolean,
    val post_hint: String,
//    val author_flair_text: Any,
//    val mod_reason_title: Any,
    val whitelist_status: String,
    val saved: Boolean,
//    val author_flair_css_class: Any,
    val subreddit_id: String,
    val score_hidden: Boolean,
    val collapsed: Boolean,
    val body_html: String,
    val link_id: String,
    val replies: String,
//    val collapsed_reason: Any,
    val controversiality: Int,
    val is_submitter: Boolean,
    val parent_id: String,
    val depth: Int,
    val body: String,
    val children: ApiMeta<ApiList<ApiCommentOrApiMore>>,
    val count: Int
)