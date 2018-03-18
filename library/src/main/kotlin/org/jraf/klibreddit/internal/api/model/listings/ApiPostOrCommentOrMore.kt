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

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

/* internal */ data class ApiPostOrCommentOrMore(
    val apiPost: ApiPost?,
    val apiComment: ApiComment?,
    val apiMore: ApiMore?
) {
    object Deserializer : JsonDeserializer<ApiPostOrCommentOrMore> {
        @Throws(JsonParseException::class)
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): ApiPostOrCommentOrMore {
            return when (json.asJsonObject["kind"].asString) {
                "t3" -> ApiPostOrCommentOrMore(
                    context.deserialize(json.asJsonObject["data"], ApiPost::class.java),
                    null,
                    null
                )

                "t1" -> ApiPostOrCommentOrMore(
                    null,
                    context.deserialize(json.asJsonObject["data"], ApiComment::class.java),
                    null
                )

                "more" -> ApiPostOrCommentOrMore(
                    null,
                    null,
                    context.deserialize(json.asJsonObject["data"], ApiMore::class.java)
                )

                else -> throw JsonParseException("Expected kind either 't3' or 't1' or 'more'")
            }
        }
    }
}
