/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2018-present Benoit 'BoD' Lubek (BoD@JRAF.org)
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

package org.jraf.klibreddit.internal.model.account

import org.jraf.klibreddit.model.account.Me
import java.util.Date

internal data class MeImpl(
    override val id: String,
    override val name: String,
    override val created: Date,
    override val isEmployee: Boolean,
    override val hasVisitedNewProfile: Boolean,
    override val prefNoProfanity: Boolean,
    override val isSuspended: Boolean,
    override val prefGeopopular: String,
    override val isSponsor: Boolean,
    override val verified: Boolean,
    override val over18: Boolean,
    override val isGold: Boolean,
    override val isMod: Boolean,
    override val hasVerifiedEmail: Boolean,
    override val inRedesignBeta: Boolean,
    override val iconImg: String,
    override val hasModMail: Boolean,
    override val oauthClientId: String,
    override val hideFromRobots: Boolean,
    override val linkKarma: Int,
    override val inboxCount: Int,
    override val prefTopKarmaSubreddits: Boolean?,
    override val hasMail: Boolean,
    override val prefShowSnoovatar: Boolean,
    override val commentKarma: Int
) : Me