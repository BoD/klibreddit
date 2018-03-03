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

package org.jraf.klibreddit.core

import org.jraf.klibreddit.core.model.client.ClientConfiguration
import org.jraf.klibreddit.core.model.oauth.OAuthScope
import org.jraf.klibreddit.util.StringUtil.toUrlEncoded
import java.util.Locale
import java.util.UUID

class RedditClient(
    private val clientConfiguration: ClientConfiguration
) {
    companion object {
        private const val URL_AUTHORIZE =
            "https://www.reddit.com/api/v1/authorize.compact?client_id=%1\$s&response_type=code&state=%2\$s&redirect_uri=%3\$s&duration=permanent&scope=%4\$s"
    }

    fun getAuthorizeUrl(vararg scopes: OAuthScope) = URL_AUTHORIZE.format(
        Locale.US,
        clientConfiguration.oAuthConfiguration.clientId.toUrlEncoded(),
        UUID.randomUUID().toString().toUrlEncoded(),
        clientConfiguration.oAuthConfiguration.redirectUri.toUrlEncoded(),
        scopes.joinToString(",") { it.name.toLowerCase(Locale.US) }.toUrlEncoded()
    )
}