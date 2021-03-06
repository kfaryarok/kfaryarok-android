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

package io.github.kfaryarok.kfaryarokapp.settings;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import io.github.kfaryarok.kfaryarokapp.MainActivity;
import io.github.kfaryarok.kfaryarokapp.R;
import io.github.kfaryarok.kfaryarokapp.alerts.AlertHelper;
import io.github.kfaryarok.kfaryarokapp.alerts.BootReceiver;
import io.github.kfaryarok.kfaryarokapp.prefs.ClassPreference;
import io.github.kfaryarok.kfaryarokapp.prefs.ClassPreferenceDialogFragmentCompat;
import io.github.kfaryarok.kfaryarokapp.prefs.TimePreference;
import io.github.kfaryarok.kfaryarokapp.updates.UpdateFetcher;
import io.github.kfaryarok.kfaryarokapp.updates.UpdateHelper;
import io.github.kfaryarok.kfaryarokapp.updates.UpdateTask;
import io.github.kfaryarok.kfaryarokapp.updates.api.Update;
import io.github.kfaryarok.kfaryarokapp.util.PreferenceUtil;

/**
 * Class for configuring preferences.
 * Small note, in the SharedPreferences class is stored in HEBREW!
 *
 * @author tbsc on 06/03/2017
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    private CheckBoxPreference alertsCb;
    private TimePreference alertTimeTp;
    private CheckBoxPreference globalAlertsCb;
    private ClassPreference classCd;
    private EditTextPreference updateServerEtp;
    private Preference forceFetchBp;
    private Preference resetAppBp;

    private Toast toast;

    /**
     * This activity is used both for the first launch of the app and for normal settings.
     * This variable lets me change various things based of the use of this activity.
     */
    private boolean firstLaunchActivity = false;

    @Override
    @SuppressLint("ApplySharedPref")
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_kfaryarok);
        setHasOptionsMenu(true);

        firstLaunchActivity = getActivity().getIntent().getBooleanExtra(Intent.EXTRA_TEXT, false);

        alertsCb = (CheckBoxPreference) findPreference(getString(R.string.pref_alerts_enabled_bool));
        alertTimeTp = (TimePreference) findPreference(getString(R.string.pref_alerts_time_string));
        globalAlertsCb = (CheckBoxPreference) findPreference(getString(R.string.pref_globalalerts_enabled_bool));
        classCd = (ClassPreference) findPreference(getString(R.string.pref_class_string));
        // mEtpClass = (EditTextPreference) findPreference(getString(R.string.pref_class_string));
        updateServerEtp = (EditTextPreference) findPreference(getString(R.string.pref_updateserver_string));
        forceFetchBp = findPreference(getString(R.string.pref_forcefetch));
        resetAppBp = findPreference(getString(R.string.pref_reset_bool));

        boolean alertsEnabled = PreferenceUtil.getAlertEnabledPreference(getContext());
        alertTimeTp.setEnabled(alertsEnabled);
        globalAlertsCb.setEnabled(alertsEnabled);

        alertsCb.setOnPreferenceChangeListener((preference, newValue) -> {
            ComponentName receiver = new ComponentName(getContext(), BootReceiver.class);
            PackageManager pm = getContext().getPackageManager();
            boolean newBool = (boolean) newValue;

            // sadly, when coding this, I didn't document why I used the package manager
            // so I have no idea why it's there
            if (newBool) {
                // alerts are enabled, enable alert and boot receiver
                AlertHelper.enableAlert(getContext());
                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
            } else {
                // alerts are disabled, disable alert and boot receiver
                AlertHelper.disableAlert(getContext());
                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }

            alertTimeTp.setEnabled(newBool);
            globalAlertsCb.setEnabled(newBool);
            return true;
        });

        classCd.setSummary(PreferenceUtil.getClassPreference(getContext()));

        // set advanced settings prefscreen category's visibility based on prefs
        PreferenceCategory prefCategoryAdvanced = (PreferenceCategory) findPreference(getString(R.string.settings_advanced_category));

        boolean devModeActive = PreferenceUtil.getDeveloperModePreference(getContext());
        prefCategoryAdvanced.setVisible(devModeActive);
        if (!devModeActive) {
            prefCategoryAdvanced.removeAll();
        }

        resetAppBp.setOnPreferenceClickListener(preference -> {
            // clear prefs
            // because app quits immediately, we need to clear prefs immediately
            PreferenceUtil.getSharedPreferences(getContext()).edit().clear().commit();
            // relaunch app
            System.exit(0);
            return true;
        });

        forceFetchBp.setOnPreferenceClickListener(preference -> {
            UpdateHelper.deleteCache(getContext());
            Update[] updates = new Update[0];
            try {
                updates = new UpdateTask(s -> {
                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(getContext(), s, Toast.LENGTH_LONG);
                    toast.show();
                }).execute(getContext()).get();
            } catch (InterruptedException | ExecutionException e) {
                Log.e("SettingsFragment", "UpdateTask interrupted: " + e.getMessage());
            }
            if (updates != null) {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(getContext(), getString(R.string.toast_devmode_forcefetch), Toast.LENGTH_LONG);
                toast.show();
            }
            return true;
        });

        updateServerEtp.setOnPreferenceChangeListener((preference, newValue) -> {
            String server = (String) newValue;

            if ("".equals(server)) {
                server = UpdateFetcher.DEFAULT_UPDATE_URL;
                PreferenceUtil.getSharedPreferences(getContext()).edit()
                        .putString(getString(R.string.pref_updateserver_string), getString(R.string.pref_updateserver_string_def))
                        .commit();
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(getContext(), getString(R.string.toast_devmode_defaultserver_revert), Toast.LENGTH_LONG);
                toast.show();
            } else {
                // check if it's a valid url
                try {
                    // use URL to see if the url is valid
                    new URL(server);
                } catch (MalformedURLException e) {
                    // invalid
                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(getContext(), getString(R.string.toast_devmode_invalid_server), Toast.LENGTH_LONG);
                    toast.show();
                    return false;
                }
            }

            updateServerEtp.setSummary(server);

            // if changing server, than delete cache so new data from new server is fetched
            UpdateHelper.deleteCache(getContext());

            return true;
        });

        updateServerEtp.setSummary(PreferenceUtil.getUpdateServerPreference(getContext()));
    }

    @Override
    public void onDisplayPreferenceDialog(final Preference preference) {
        Dialog dialog = null;
        DialogFragment dialogFragment = null;
        if (preference instanceof TimePreference) {
            dialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
                SharedPreferences prefs = PreferenceUtil.getSharedPreferences(getContext());
                String time = TimePreference.timeToString(hourOfDay, minute);
                prefs.edit()
                        .putString(getString(R.string.pref_alerts_time_string), time)
                        .apply();
                preference.setSummary(time);
                // let it know alert time was changed
                AlertHelper.enableAlert(getContext());
            }, PreferenceUtil.parseAlertHour(getContext()), PreferenceUtil.parseAlertMinute(getContext()), true);
        } else if (preference instanceof ClassPreference) {
            dialogFragment = new ClassPreferenceDialogFragmentCompat();
            Bundle bundle = new Bundle(1);
            bundle.putString("key", preference.getKey());
            dialogFragment.setArguments(bundle);
        }

        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getFragmentManager(), "android.support.v7.preference.PreferenceFragment.DIALOG");
        } else if (dialog != null) {
            dialog.show();
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (firstLaunchActivity) {
            // first launch, so put the first launch menu
            inflater.inflate(R.menu.first_launch, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_firstlaunch_accept) {
            // if there's a class stored in prefs already
            if (!"".equals(PreferenceUtil.getClassPreference(getContext()))) {
                // allow continuing
                getActivity().finish();
                // mark in preferences that first launch just finished
                getPreferenceManager().getSharedPreferences().edit().putBoolean(getString(R.string.pref_launched_before_bool), true).apply();
                // tell main activity that first launched just finished so recreate main activity
                MainActivity.resumeFromFirstLaunch = true;
            } else {
                // else notify user
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(getContext(), getString(R.string.toast_firstlaunch_no_class), Toast.LENGTH_LONG);
                toast.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
