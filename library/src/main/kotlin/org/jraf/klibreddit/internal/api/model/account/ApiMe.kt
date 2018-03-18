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

package org.jraf.klibreddit.internal.api.model.account

/* internal */ data class ApiMe(
    val is_employee: Boolean,
    val has_visited_new_profile: Boolean,
    val pref_no_profanity: Boolean,
    val is_suspended: Boolean,
    val pref_geopopular: String,
    val is_sponsor: Boolean,
    val id: String,
    val verified: Boolean,
    val over_18: Boolean,
    val is_gold: Boolean,
    val is_mod: Boolean,
    val has_verified_email: Boolean,
    val in_redesign_beta: Boolean,
    val icon_img: String,
    val has_mod_mail: Boolean,
    val oauth_client_id: String,
    val hide_from_robots: Boolean,
    val link_karma: Int,
    val inbox_count: Int,
    val pref_top_karma_subreddits: Boolean?,
    val has_mail: Boolean,
    val pref_show_snoovatar: Boolean,
    val name: String,
    val created: Double,
    val gold_creddits: Int,
    val created_utc: Double,
    val in_beta: Boolean,
    val comment_karma: Int,
    val has_subscribed: Boolean
)