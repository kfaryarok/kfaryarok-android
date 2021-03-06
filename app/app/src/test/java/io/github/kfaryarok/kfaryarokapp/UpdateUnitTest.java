/*
 * This file is part of kfaryarok-android.
 *
 * kfaryarok-android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * kfaryarok-android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with kfaryarok-android.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.kfaryarok.kfaryarokapp;

import android.content.Context;

import org.junit.Test;

import io.github.kfaryarok.kfaryarokapp.updates.api.Update;
import io.github.kfaryarok.kfaryarokapp.updates.UpdateHelper;
import io.github.kfaryarok.kfaryarokapp.updates.api.UpdateImpl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class UpdateUnitTest {

    /**
     * Checks if {@link UpdateHelper#formatClassString(String[], String)} works as it should,
     * as in checking if it puts everything in the right order and with correct commas
     */
    @Test
    public void correctClassStringFormatting() {
        // 3 classes

        // user's class first
        assertThat(UpdateHelper.formatClassString(new String[] {
                "ח4", "י9", "ח3"
        }, "ח4"), is("ח4, י9, ח3"));
        // user's class first but others switched
        assertThat(UpdateHelper.formatClassString(new String[] {
                "ח4", "ח3", "י9"
        }, "ח4"), is("ח4, ח3, י9"));
        // user's class second
        assertThat(UpdateHelper.formatClassString(new String[] {
                "י9", "ח3", "ז4"
        }, "ח3"), is("ח3, י9, ז4"));
        // user's class second but others switched
        assertThat(UpdateHelper.formatClassString(new String[] {
                "ז4", "ח3", "י9"
        }, "ח3"), is("ח3, ז4, י9"));
        // user's class last
        assertThat(UpdateHelper.formatClassString(new String[] {
                "ז4", "ח3", "י9"
        }, "י9"), is("י9, ז4, ח3"));
        // user's class still last but other classes were switched
        assertThat(UpdateHelper.formatClassString(new String[] {
                "ח3", "ז4", "י9"
        }, "י9"), is("י9, ח3, ז4"));

        // 2 classes

        // user's class first
        assertThat(UpdateHelper.formatClassString(new String[] {
                "ח4", "י9"
        }, "ח4"), is("ח4, י9"));
        // user's class last
        assertThat(UpdateHelper.formatClassString(new String[] {
                "י9", "ח4"
        }, "ח4"), is("ח4, י9"));

        // 1 class
        assertThat(UpdateHelper.formatClassString(new String[] {
                "ח4"
        }, "ח4"), is("ח4"));
    }

    /**
     * Tests {@link UpdateHelper#formatUpdate(Update, Context)} using various valid examples
     */
    @Test
    public void correctUpdateStringFormatting() {
        // if update is null, return value should be null
        assertThat(UpdateHelper.formatUpdate(
                null,
                "עדכון כללי",
                "ח4"),
                is(nullValue()));

        // general test for formatting
        assertThat(UpdateHelper.formatUpdate(
                new UpdateImpl(new String[] {
                        "ח4", "י6"
                }, "טקסט חסר משמעות"),
                "עדכון כללי",
                "ח4"),
                is("ח4, י6: טקסט חסר משמעות"));

        // test for putting user's class first
        assertThat(UpdateHelper.formatUpdate(
                new UpdateImpl(new String[] {
                        "י6", "ח4"
                }, "טקסט חסר משמעות"),
                "עדכון כללי",
                "ח4"),
                is("ח4, י6: טקסט חסר משמעות"));

        // test for putting user's class first
        assertThat(UpdateHelper.formatUpdate(
                new UpdateImpl(new String[] {
                        "ח4"
                }, "טקסט חסר משמעות"),
                "עדכון כללי",
                "ח4"),
                is("ח4: טקסט חסר משמעות"));
    }

}