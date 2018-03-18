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

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson

internal class ApiCommentOrApiMoreAdapter {
    @FromJson
    fun fromJson(
        reader: JsonReader,
        apiCommentAdapter: JsonAdapter<ApiComment>,
        apiMoreAdapter: JsonAdapter<ApiMore>
    ): ApiCommentOrApiMore {
        val jsonMeta =
            reader.readJsonValue() as? Map<*, *> ?: throw JsonDataException("Expected a field of type Json object")
        return when (jsonMeta["kind"]) {
            "t1" -> ApiCommentOrApiMore(apiCommentAdapter.fromJson(jsonMeta["data"] as String), null)
            "more" -> ApiCommentOrApiMore(null, apiMoreAdapter.fromJson(jsonMeta["data"] as String))
            else -> throw JsonDataException("Expected kind either 't1' or 'more'")
        }
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: ApiCommentOrApiMore) {
        throw UnsupportedOperationException()
    }
}
