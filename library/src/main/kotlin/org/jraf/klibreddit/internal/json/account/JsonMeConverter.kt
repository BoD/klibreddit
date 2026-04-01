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

package org.jraf.klibreddit.internal.json.account

import org.jraf.klibreddit.internal.model.account.MeImpl
import org.jraf.klibreddit.internal.util.toDate

internal object JsonMeConverter :
    org.jraf.klibreddit.internal.json.JsonConverter<JsonMe, MeImpl> {
    override fun convert(jsonModel: JsonMe) = MeImpl(
        jsonModel.id,
        jsonModel.name,
        jsonModel.created_utc.toDate(),
        jsonModel.is_employee,
        jsonModel.has_visited_new_profile,
        jsonModel.pref_no_profanity,
        jsonModel.is_suspended,
        jsonModel.pref_geopopular,
        jsonModel.is_sponsor,
        jsonModel.verified,
        jsonModel.over_18,
        jsonModel.is_gold,
        jsonModel.is_mod,
        jsonModel.has_verified_email,
        jsonModel.in_redesign_beta,
        jsonModel.icon_img,
        jsonModel.has_mod_mail,
        jsonModel.oauth_client_id,
        jsonModel.hide_from_robots,
        jsonModel.link_karma,
        jsonModel.inbox_count,
        jsonModel.pref_top_karma_subreddits,
        jsonModel.has_mail,
        jsonModel.pref_show_snoovatar,
        jsonModel.comment_karma
    )
}
