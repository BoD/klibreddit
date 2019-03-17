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

package org.jraf.klibreddit.model.listings

data class Pagination(val pageIndex: PageIndex, val itemCount: Int = DEFAULT_ITEM_COUNT) {
    companion object {
        const val DEFAULT_ITEM_COUNT = 50
    }
}

sealed class PageIndex {
    internal abstract val before: String?
    internal abstract val after: String?
}

object FirstPage : PageIndex() {
    override val before: String? = null
    override val after: String? = null
}

data class Before(private val elementId: String) : PageIndex() {
    override val before = elementId
    override val after: String? = null
}

data class After(private val elementId: String) : PageIndex() {
    override val before: String? = null
    override val after = elementId
}
