package io.github.kfaryarok.kfaryarokapp.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import io.github.kfaryarok.kfaryarokapp.R;
import io.github.kfaryarok.kfaryarokapp.util.ClassUtil;

/**
 * Class for configuring preferences.
 *
 * Small note, in the SharedPreferences class is stored in HEBREW!
 *
 * @author tbsc on 06/03/2017
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_kfaryarok);
        SharedPreferences pref = getPreferenceManager().getSharedPreferences();

        final EditTextPreference etpClass = (EditTextPreference) findPreference(getString(R.string.pref_class_string));
        etpClass.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // if entered class name isn't valid, prevent saving it
                if (!ClassUtil.checkValidHebrewClassName((String) newValue)) {
                    Toast.makeText(getContext(), "כיתה לא תקינה.", Toast.LENGTH_LONG).show();
                    return false;
                }

                // update summary
                etpClass.setSummary((String) newValue);
                return true;
            }

        });

        // set summary to contain current value
        etpClass.setSummary(pref.getString(etpClass.getKey(), "N/A"));

        // set advanced settings prefscreen button's visibility based on prefs
        PreferenceScreen prefScreenAdvanced = (PreferenceScreen) findPreference(getString(R.string.settings_advanced_category));
        prefScreenAdvanced.setVisible(pref.getBoolean(getString(R.string.pref_advanced_mode_bool), false));
    }

}
