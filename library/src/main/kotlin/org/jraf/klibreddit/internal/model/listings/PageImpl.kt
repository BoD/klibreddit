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

package org.jraf.klibreddit.internal.model.listings

import org.jraf.klibreddit.model.listings.After
import org.jraf.klibreddit.model.listings.Before
import org.jraf.klibreddit.model.listings.Page
import org.jraf.klibreddit.model.listings.Pagination

internal data class PageImpl<out T>(
    private val before: String?,
    private val after: String?,
    private val itemCount: Int,
    override val list: List<T>
) : Page<T> {
    override val nextPageIndex = after?.let { After(it) }
    override val previousPageIndex = before?.let { Before(it) }
    override val nextPagination = nextPageIndex?.let { Pagination(nextPageIndex, itemCount) }
    override val previousPagination = previousPageIndex?.let { Pagination(previousPageIndex, itemCount) }
}
