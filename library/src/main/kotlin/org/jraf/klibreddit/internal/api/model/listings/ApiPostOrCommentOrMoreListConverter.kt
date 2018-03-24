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

import org.jraf.klibreddit.internal.api.model.ApiConverter
import org.jraf.klibreddit.model.listings.Comment
import org.jraf.klibreddit.model.listings.PostWithComments

internal object ApiPostOrCommentOrMoreListConverter :
    ApiConverter<List<ApiMeta<ApiList<ApiPostOrCommentOrMore>>>, PostWithComments> {
    override fun convert(apiModel: List<ApiMeta<ApiList<ApiPostOrCommentOrMore>>>): PostWithComments {
        val post = ApiPostConverter.convert(apiModel[0].data.children[0].apiPost!!)
        val apiComments = apiModel[1].data.children
        val comments = mutableListOf<Comment>()
        var moreCommentIds: List<String> = emptyList()
        for (apiComment in apiComments) {
            if (apiComment.apiComment != null) {
                comments += ApiCommentConverter.convert(apiComment.apiComment)
            } else if (apiComment.apiMore != null) {
                moreCommentIds = apiComment.apiMore.children
            }
        }
        return PostWithComments(post, comments, moreCommentIds)
    }
}