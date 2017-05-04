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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.kfaryarok.kfaryarokapp.util.PreferenceUtil;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(AndroidJUnit4.class)
public class AboutActivityInstrumentedTest {

    @SuppressLint("ApplySharedPref")
    @Test
    public void devModeEasterEggOffWorking() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        ViewInteraction interact = onView(withId(R.id.btn_about_advancedmode_easteregg));
        PreferenceUtil.getSharedPreferences(appContext).edit().putBoolean(appContext.getString(R.string.pref_advanced_mode_bool), true).commit();
        InstrumentationRegistry.getInstrumentation().startActivitySync(new Intent(appContext, AboutActivity.class));

        interact.perform(click());

        Assert.assertThat(PreferenceUtil.getDeveloperModePreference(appContext), is(false));
    }

    @SuppressLint("ApplySharedPref")
    @Test
    public void devModeEasterEggOnWorking() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        ViewInteraction interact = onView(withId(R.id.btn_about_advancedmode_easteregg));
        PreferenceUtil.getSharedPreferences(appContext).edit().putBoolean(appContext.getString(R.string.pref_advanced_mode_bool), false).commit();
        InstrumentationRegistry.getInstrumentation().startActivitySync(new Intent(appContext, AboutActivity.class));

        for (int i = 0; i < 8; i++)
            interact.perform(click());

        Assert.assertThat(PreferenceUtil.getDeveloperModePreference(appContext), is(true));
    }

}
