package io.github.kfaryarok.kfaryarokapp.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import io.github.kfaryarok.kfaryarokapp.R;
import io.github.kfaryarok.kfaryarokapp.prefs.TimePreference;
import io.github.kfaryarok.kfaryarokapp.prefs.TimePreferenceDialogFragmentCompat;
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

        CheckBoxPreference cbAlerts = (CheckBoxPreference) findPreference(getString(R.string.pref_alerts_enabled_bool));
        final TimePreference timePreference = (TimePreference) findPreference(getString(R.string.pref_alerts_time_string));
        final CheckBoxPreference cbGlobalAlerts = (CheckBoxPreference) findPreference(getString(R.string.pref_globalalerts_enabled_bool));

        cbAlerts.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean newBool = (boolean) newValue;
                timePreference.setEnabled(newBool);
                cbGlobalAlerts.setEnabled(newBool);
                return true;
            }
        });

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

        // set advanced settings prefscreen category's visibility based on prefs
        PreferenceCategory prefCategoryAdvanced = (PreferenceCategory) findPreference(getString(R.string.settings_advanced_category));
        prefCategoryAdvanced.setVisible(pref.getBoolean(getString(R.string.pref_advanced_mode_bool), false));
        if (!pref.getBoolean(getString(R.string.pref_advanced_mode_bool), false)) {
            prefCategoryAdvanced.removeAll();
        }
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment dialogFragment = null;
        if (preference instanceof TimePreference) {
            dialogFragment = new TimePreferenceDialogFragmentCompat();
            Bundle bundle = new Bundle(1);
            bundle.putString("key", preference.getKey());
            dialogFragment.setArguments(bundle);
        }

        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getFragmentManager(), "android.support.v7.preference.PreferenceFragment.DIALOG");
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

}
