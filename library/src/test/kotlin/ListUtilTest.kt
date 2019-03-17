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

import org.jraf.klibreddit.internal.util.split
import org.junit.Assert
import org.junit.Test

class ListUtilTest {

    @Test
    fun `Split, nominal`() {
        val list = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        Assert.assertEquals(
            listOf(
                listOf(0, 1, 2, 3),
                listOf(4, 5, 6, 7, 8, 9)
            ),
            list.split(4)
        )
    }

    @Test
    fun `Split, size too large`() {
        val list = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        Assert.assertEquals(
            listOf(
                list,
                listOf()
            ),
            list.split(10)
        )

        Assert.assertEquals(
            listOf(
                list,
                listOf()
            ),
            list.split(42)
        )
    }
}
