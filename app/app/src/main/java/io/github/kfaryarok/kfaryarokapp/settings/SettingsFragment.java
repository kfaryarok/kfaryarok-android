package io.github.kfaryarok.kfaryarokapp.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import io.github.kfaryarok.kfaryarokapp.R;
import io.github.kfaryarok.kfaryarokapp.prefs.TimePreference;
import io.github.kfaryarok.kfaryarokapp.prefs.TimePreferenceDialogFragmentCompat;
import io.github.kfaryarok.kfaryarokapp.util.ClassUtil;
import io.github.kfaryarok.kfaryarokapp.util.PreferenceUtil;

/**
 * Class for configuring preferences.
 *
 * Small note, in the SharedPreferences class is stored in HEBREW!
 *
 * @author tbsc on 06/03/2017
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    private CheckBoxPreference mCbAlerts;
    private TimePreference mTpAlertTime;
    private CheckBoxPreference mCbGlobalAlerts;
    private EditTextPreference mEtpClass;

    private Toast mToast;

    /**
     * This activity is used both for the first launch of the app and for normal settings.
     * This variable lets me change various things based of the use of this activity.
     */
    private boolean mFirstLaunchActivity = false;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_kfaryarok);
        setHasOptionsMenu(true);

        SharedPreferences pref = getPreferenceManager().getSharedPreferences();

        mFirstLaunchActivity = getActivity().getIntent().getBooleanExtra(Intent.EXTRA_TEXT, false);

        mCbAlerts = (CheckBoxPreference) findPreference(getString(R.string.pref_alerts_enabled_bool));
        mTpAlertTime = (TimePreference) findPreference(getString(R.string.pref_alerts_time_string));
        mCbGlobalAlerts = (CheckBoxPreference) findPreference(getString(R.string.pref_globalalerts_enabled_bool));

        mCbAlerts.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean newBool = (boolean) newValue;
                mTpAlertTime.setEnabled(newBool);
                mCbGlobalAlerts.setEnabled(newBool);
                return true;
            }
        });

        mEtpClass = (EditTextPreference) findPreference(getString(R.string.pref_class_string));
        mEtpClass.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // if entered class name isn't valid, prevent saving it
                if (!ClassUtil.checkValidHebrewClassName((String) newValue)) {
                    Toast.makeText(getContext(), getString(R.string.toast_invalid_class), Toast.LENGTH_LONG).show();
                    return false;
                }

                // update summary
                mEtpClass.setSummary((String) newValue);
                return true;
            }

        });

        // set summary to contain current value
        mEtpClass.setSummary(pref.getString(mEtpClass.getKey(), ""));

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mFirstLaunchActivity) {
            // first launch, so put the first launch menu
            inflater.inflate(R.menu.first_launch, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_firstlaunch_accept) {
            // if there's a class stored in prefs already
            if (!PreferenceUtil.getClassPreference(getContext()).equalsIgnoreCase("")) {
                // allow continuing
                getActivity().finish();
                // mark in preferences that first launch just finished
                getPreferenceManager().getSharedPreferences().edit().putBoolean(getString(R.string.pref_launched_before_bool), true).apply();
            } else {
                // else notify user
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(getContext(), getString(R.string.toast_firstlaunch_no_class), Toast.LENGTH_LONG);
                mToast.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
