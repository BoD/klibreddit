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

import java.util.Date

interface Me {
    val id: String
    val name: String
    val created: Date
    val isEmployee: Boolean
    val hasVisitedNewProfile: Boolean
    val prefNoProfanity: Boolean
    val isSuspended: Boolean
    val prefGeopopular: String
    val isSponsor: Boolean
    val verified: Boolean
    val over18: Boolean
    val isGold: Boolean
    val isMod: Boolean
    val hasVerifiedEmail: Boolean
    val inRedesignBeta: Boolean
    val iconImg: String
    val hasModMail: Boolean
    val oauthClientId: String
    val hideFromRobots: Boolean
    val linkKarma: Int
    val inboxCount: Int
    val prefTopKarmaSubreddits: Boolean?
    val hasMail: Boolean
    val prefShowSnoovatar: Boolean
    val commentKarma: Int
}