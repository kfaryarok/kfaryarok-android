package io.github.kfaryarok.kfaryarokapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.github.kfaryarok.kfaryarokapp.R;
import io.github.kfaryarok.kfaryarokapp.prefs.TimePreference;

/**
 * Utility class for getting values of various preferences.
 *
 * @author tbsc on 10/03/2017
 */
public class PreferenceUtil {

    private static SharedPreferences prefs;

    public static SharedPreferences getSharedPreferences(Context ctx) {
        if (prefs == null) prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs;
    }

    /**
     * Returns the current class that's stored in the shared preferences.
     * @param ctx Context for getting preferences
     * @return Class of user
     */
    public static String getClassPreference(Context ctx) {
        if (prefs == null) prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getString(ctx.getString(R.string.pref_class_string), ctx.getString(R.string.pref_class_string_def));
    }

    public static boolean getAlertEnabledPreference(Context ctx) {
        if (prefs == null) prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getBoolean(ctx.getString(R.string.pref_alerts_enabled_bool), Boolean.parseBoolean(ctx.getString(R.string.pref_alerts_enabled_bool_def)));
    }

    public static String getAlertTimePreference(Context ctx) {
        if (prefs == null) prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getString(ctx.getString(R.string.pref_alerts_time_string), ctx.getString(R.string.pref_alerts_time_string_def));
    }

    public static boolean getGlobalAlertsPreference(Context ctx) {
        if (prefs == null) prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getBoolean(ctx.getString(R.string.pref_globalalerts_enabled_bool), Boolean.parseBoolean(ctx.getString(R.string.pref_globalalerts_enabled_bool_def)));
    }

    public static int parseAlertHour(Context ctx) {
        return TimePreference.parseHour(getAlertTimePreference(ctx));
    }

    public static int parseAlertMinute(Context ctx) {
        return TimePreference.parseMinute(getAlertTimePreference(ctx));
    }

    public static boolean getLaunchedBeforePreference(Context ctx) {
        if (prefs == null) prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getBoolean(ctx.getString(R.string.pref_launched_before_bool), Boolean.parseBoolean(ctx.getString(R.string.pref_launched_before_bool_def)));
    }

    public static boolean getDeveloperModePreference(Context ctx) {
        if (prefs == null) prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getBoolean(ctx.getString(R.string.pref_advanced_mode_bool), Boolean.parseBoolean(ctx.getString(R.string.pref_advanced_mode_bool_def)));
    }

}
