package io.github.kfaryarok.kfaryarokapp.prefs;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;

import java.util.Locale;

import io.github.kfaryarok.kfaryarokapp.R;

/**
 * Preference that, when clicked, shows a TimePicker dialog.
 *
 * @author tbsc on 10/03/2017
 */
public class TimePreference extends DialogPreference {

    public int mHour = 0;
    public int mMinute = 0;

    public static int parseHour(String value) {
        try {
            String[] time = value.split(":");
            return (Integer.parseInt(time[0]));
        } catch (Exception e) {
            return 0;
        }
    }

    public static int parseMinute(String value) {
        try {
            String[] time = value.split(":");
            return (Integer.parseInt(time[1]));
        } catch (Exception e) {
            return 0;
        }
    }

    public static String timeToString(int h, int m) {
        return String.format(Locale.US, "%02d", h) + ":" + String.format(Locale.US, "%02d", m);
    }

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String value;
        if (restoreValue) {
            if (defaultValue == null) {
                value = getPersistedString(getContext().getString(R.string.pref_alerts_time_string_def));
            } else {
                value = getPersistedString(defaultValue.toString());
            }
        } else {
            value = defaultValue.toString();
        }

        mHour = parseHour(value);
        mMinute = parseMinute(value);

        setSummary(value);
    }

    public void persistStringValue(String value) {
        persistString(value);
    }

}
