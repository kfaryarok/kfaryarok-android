package io.github.kfaryarok.kfaryarokapp.prefs;

import android.content.Context;
import android.os.Build;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.View;
import android.widget.TimePicker;

/**
 * Fragment for the TimePreference to show when clicked, and to have control of it.
 *
 * @author tbsc on 10/03/2017
 */
public class TimePreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat implements DialogPreference.TargetFragment {

    TimePicker timePicker = null;

    @Override
    protected View onCreateDialogView(Context context) {
        timePicker = new TimePicker(context);
        return timePicker;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        timePicker.setIs24HourView(true);

        TimePreference pref = (TimePreference) getPreference();

        // this is really dumb
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(pref.mHour);
            timePicker.setMinute(pref.mMinute);
        } else {
            timePicker.setCurrentHour(pref.mHour);
            timePicker.setCurrentMinute(pref.mMinute);
        }
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            TimePreference pref = (TimePreference) getPreference();
            pref.mHour = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? timePicker.getHour() : timePicker.getCurrentHour();
            pref.mMinute = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? timePicker.getMinute() : timePicker.getCurrentMinute();

            String value = TimePreference.timeToString(pref.mHour, pref.mMinute);
            if (pref.callChangeListener(value)) {
                pref.persistStringValue(value);
            }

            getPreference().setSummary(value);
        }
    }

    @Override
    public Preference findPreference(CharSequence charSequence) {
        return getPreference();
    }

}
