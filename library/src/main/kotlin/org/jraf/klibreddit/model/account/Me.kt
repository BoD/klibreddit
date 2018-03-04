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

package org.jraf.klibreddit.model.account

import org.jraf.klibreddit.internal.api.model.ApiMe
import java.util.Date
import java.util.concurrent.TimeUnit

data class Me(
    val isEmployee: Boolean,
    val hasVisitedNewProfile: Boolean,
    val prefNoProfanity: Boolean,
    val isSuspended: Boolean,
    val prefGeopopular: String,
    val isSponsor: Boolean,
    val id: String,
    val verified: Boolean,
    val over18: Boolean,
    val isGold: Boolean,
    val isMod: Boolean,
    val hasVerifiedEmail: Boolean,
    val inRedesignBeta: Boolean,
    val iconImg: String,
    val hasModMail: Boolean,
    val oauthClientId: String,
    val hideFromRobots: Boolean,
    val linkKarma: Int,
    val inboxCount: Int,
    val prefTopKarmaSubreddits: Boolean,
    val hasMail: Boolean,
    val prefShowSnoovatar: Boolean,
    val name: String,
    val created: Date,
    val commentKarma: Int
) {
    companion object {
        internal fun fromApi(apiMe: ApiMe) = Me(
            apiMe.is_employee,
            apiMe.has_visited_new_profile,
            apiMe.pref_no_profanity,
            apiMe.is_suspended,
            apiMe.pref_geopopular,
            apiMe.is_sponsor,
            apiMe.id,
            apiMe.verified,
            apiMe.over_18,
            apiMe.is_gold,
            apiMe.is_mod,
            apiMe.has_verified_email,
            apiMe.in_redesign_beta,
            apiMe.icon_img,
            apiMe.has_mod_mail,
            apiMe.oauth_client_id,
            apiMe.hide_from_robots,
            apiMe.link_karma,
            apiMe.inbox_count,
            apiMe.pref_top_karma_subreddits,
            apiMe.has_mail,
            apiMe.pref_show_snoovatar,
            apiMe.name,
            Date(TimeUnit.SECONDS.toMillis(apiMe.created_utc.toLong())),
            apiMe.comment_karma
        )
    }
}
