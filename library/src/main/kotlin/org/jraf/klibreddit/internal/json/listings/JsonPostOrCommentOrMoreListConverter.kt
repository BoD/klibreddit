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

package org.jraf.klibreddit.internal.json.listings

import org.jraf.klibreddit.internal.json.JsonConverter
import org.jraf.klibreddit.internal.model.listings.CommentImpl
import org.jraf.klibreddit.internal.model.listings.PostWithCommentsImpl

internal object JsonPostOrCommentOrMoreListConverter :
    JsonConverter<List<JsonMeta<JsonList<JsonPostOrCommentOrMore>>>, PostWithCommentsImpl> {
    override fun convert(jsonModel: List<JsonMeta<JsonList<JsonPostOrCommentOrMore>>>): PostWithCommentsImpl {
        val post = JsonPostConverter.convert((jsonModel[0].data.children[0] as JsonPostOrCommentOrMore.Post).data)
        val apiComments = jsonModel[1].data.children
        val comments = mutableListOf<CommentImpl>()
        var moreCommentIds: List<String> = emptyList()
        for (apiComment: JsonPostOrCommentOrMore in apiComments) {
            if (apiComment is JsonPostOrCommentOrMore.Comment) {
                comments += JsonCommentConverter.convert(apiComment.data)
            } else if (apiComment is JsonPostOrCommentOrMore.More) {
                moreCommentIds = apiComment.data.children
            }
        }
        return PostWithCommentsImpl(post, comments, moreCommentIds)
    }
}
