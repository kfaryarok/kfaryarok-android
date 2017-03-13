package io.github.kfaryarok.kfaryarokapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.github.kfaryarok.kfaryarokapp.R;

/**
 * Utility class for getting values of various preferences.
 *
 * @author tbsc on 10/03/2017
 */
public class PreferenceUtil {

    private static SharedPreferences prefs;

    /**
     * Returns the current class that's stored in the shared preferences.
     * @param ctx Context for getting preferences
     * @return Class of user
     */
    public static String getClassPreference(Context ctx) {
        if (prefs == null) prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getString(ctx.getString(R.string.pref_class_string), "");
    }

}
