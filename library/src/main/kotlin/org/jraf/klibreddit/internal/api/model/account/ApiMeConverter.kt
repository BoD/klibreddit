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

import org.jraf.klibreddit.internal.api.model.ApiConverter
import org.jraf.klibreddit.internal.util.DateUtil.toDate
import org.jraf.klibreddit.model.account.Me

internal object ApiMeConverter : ApiConverter<ApiMe, Me> {
    override fun convert(apiModel: ApiMe) = Me(
        apiModel.id,
        apiModel.name,
        apiModel.created_utc.toDate(),
        apiModel.is_employee,
        apiModel.has_visited_new_profile,
        apiModel.pref_no_profanity,
        apiModel.is_suspended,
        apiModel.pref_geopopular,
        apiModel.is_sponsor,
        apiModel.verified,
        apiModel.over_18,
        apiModel.is_gold,
        apiModel.is_mod,
        apiModel.has_verified_email,
        apiModel.in_redesign_beta,
        apiModel.icon_img,
        apiModel.has_mod_mail,
        apiModel.oauth_client_id,
        apiModel.hide_from_robots,
        apiModel.link_karma,
        apiModel.inbox_count,
        apiModel.pref_top_karma_subreddits,
        apiModel.has_mail,
        apiModel.pref_show_snoovatar,
        apiModel.comment_karma
    )
}