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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("io.github.kfaryarok.kfaryarokapp", appContext.getPackageName());
    }

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
