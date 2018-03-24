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
    val subreddit_id: String,
    val approved_at_utc: Double?,
    val ups: Int,
//val mod_reason_by: null,
//val banned_by: null,
//val removal_reason: null,
    val link_id: String,
//val likes: null,
    val no_follow: Boolean,
//val user_reports: [],
    val saved: Boolean,
    val id: String,
    val banned_at_utc: Double?,
//    val mod_reason_title: null,
    val gilded: Int,
    val archived: Boolean,
//    val report_reasons: null,
    val author: String,
    val can_mod_post: Boolean,
    val send_replies: Boolean,
    val parent_id: String,
    val score: Int,
//val approved_by: null,
    val downs: Int,
    val body: String,
    val edited: ApiOptionalInt,
//val author_flair_css_class: null,
    val collapsed: Boolean,
    val is_submitter: Boolean,
//val collapsed_reason: null,
    val body_html: String,
    val subreddit_type: String,
    val can_gild: Boolean,
    val subreddit: String,
    val name: String,
    val score_hidden: Boolean,
    val permalink: String,
//val num_reports: null,
    val stickied: Boolean,
    val created: Double,
//val author_flair_text: null,
    val created_utc: Double,
    val subreddit_name_prefixed: String,
    val controversiality: Int,
    val depth: Int,
//val mod_reports: [],
//val mod_note: null,
//val distinguished: null
    val replies: ApiOptionalMeta<ApiList<ApiCommentOrMore>>
)