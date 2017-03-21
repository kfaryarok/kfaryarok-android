package io.github.kfaryarok.kfaryarokapp.settings;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TimePicker;
import android.widget.Toast;

import io.github.kfaryarok.kfaryarokapp.MainActivity;
import io.github.kfaryarok.kfaryarokapp.R;
import io.github.kfaryarok.kfaryarokapp.alerts.AlertHelper;
import io.github.kfaryarok.kfaryarokapp.alerts.BootReceiver;
import io.github.kfaryarok.kfaryarokapp.prefs.TimePreference;
import io.github.kfaryarok.kfaryarokapp.util.ClassUtil;
import io.github.kfaryarok.kfaryarokapp.util.PreferenceUtil;

/**
 * Class for configuring preferences.
 * Small note, in the SharedPreferences class is stored in HEBREW!
 *
 * @author tbsc on 06/03/2017
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    private SharedPreferences prefs;
    private CheckBoxPreference mCbAlerts;
    private TimePreference mTpAlertTime;
    private CheckBoxPreference mCbGlobalAlerts;
    private EditTextPreference mEtpClass;
    private EditTextPreference mEtpUpdateServer;
    private CheckBoxPreference mCbReset;

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

        prefs = getPreferenceManager().getSharedPreferences();

        mFirstLaunchActivity = getActivity().getIntent().getBooleanExtra(Intent.EXTRA_TEXT, false);

        mCbAlerts = (CheckBoxPreference) findPreference(getString(R.string.pref_alerts_enabled_bool));
        mTpAlertTime = (TimePreference) findPreference(getString(R.string.pref_alerts_time_string));
        mCbGlobalAlerts = (CheckBoxPreference) findPreference(getString(R.string.pref_globalalerts_enabled_bool));
        mEtpClass = (EditTextPreference) findPreference(getString(R.string.pref_class_string));
        mEtpUpdateServer = (EditTextPreference) findPreference(getString(R.string.pref_updateserver_string));
        mCbReset = (CheckBoxPreference) findPreference(getString(R.string.pref_reset_bool));

        boolean alertsEnabled = PreferenceUtil.getAlertEnabledPreference(getContext());
        mTpAlertTime.setEnabled(alertsEnabled);
        mCbGlobalAlerts.setEnabled(alertsEnabled);

        mCbAlerts.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ComponentName receiver = new ComponentName(getContext(), BootReceiver.class);
                PackageManager pm = getContext().getPackageManager();
                boolean newBool = (boolean) newValue;

                if (newBool) {
                    // alerts are enabled, enable alert and boot receiver
                    AlertHelper.enableAlert(getContext());
                    pm.setComponentEnabledSetting(receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                } else {
                    // alerts are disabled, disable alert and boot receiver
                    AlertHelper.disableAlert();
                    pm.setComponentEnabledSetting(receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                }

                mTpAlertTime.setEnabled(newBool);
                mCbGlobalAlerts.setEnabled(newBool);
                return true;
            }
        });

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
        mEtpClass.setSummary(prefs.getString(mEtpClass.getKey(), ""));

        // set advanced settings prefscreen category's visibility based on prefs
        PreferenceCategory prefCategoryAdvanced = (PreferenceCategory) findPreference(getString(R.string.settings_advanced_category));
        prefCategoryAdvanced.setVisible(prefs.getBoolean(getString(R.string.pref_advanced_mode_bool), false));
        if (!prefs.getBoolean(getString(R.string.pref_advanced_mode_bool), false)) {
            prefCategoryAdvanced.removeAll();
        }

        mCbReset.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @SuppressLint("ApplySharedPref")
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // clear prefs
                // because app quits immediately, we need to clear prefs immediately
                prefs.edit().clear().commit();
                // relaunch app
                System.exit(0);
                return true;
            }

        });
    }

    @Override
    public void onDisplayPreferenceDialog(final Preference preference) {
        Dialog dialog = null;
        if (preference instanceof TimePreference) {
            dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    SharedPreferences prefs = PreferenceUtil.getSharedPreferences(getContext());
                    String time = TimePreference.timeToString(hourOfDay, minute);
                    prefs.edit()
                            .putString(getString(R.string.pref_alerts_time_string), time)
                            .apply();
                    preference.setSummary(time);
                    // let it know alert time was changed
                    AlertHelper.enableAlert(getContext());
                }
            }, PreferenceUtil.parseAlertHour(getContext()), PreferenceUtil.parseAlertMinute(getContext()), true);
        }

        if (dialog != null) {
            dialog.show();
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
                // tell main activity that first launched just finished so recreate main activity
                MainActivity.mResumeFromFirstLaunch = true;
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
