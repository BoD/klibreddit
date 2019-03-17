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

package org.jraf.klibreddit.model.oauth

/**
 * Available scopes granting accesses to the different APIs.
 *
 * List obtained via [https://www.reddit.com/api/v1/scopes].
 */
enum class OAuthScope {
    CREDDITS,
    MODCONTRIBUTORS,
    MODMAIL,
    MODCONFIG,
    SUBSCRIBE,
    STRUCTUREDSTYLES,
    VOTE,
    WIKIEDIT,
    MYSUBREDDITS,
    SUBMIT,
    MODLOG,
    MODPOSTS,
    MODFLAIR,
    SAVE,
    MODOTHERS,
    READ,
    PRIVATEMESSAGES,
    REPORT,
    IDENTITY,
    LIVEMANAGE,
    ACCOUNT,
    MODTRAFFIC,
    WIKIREAD,
    EDIT,
    MODWIKI,
    MODSELF,
    HISTORY,
    FLAIR,
}