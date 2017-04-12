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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TimePicker;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

import io.github.kfaryarok.kfaryarokapp.MainActivity;
import io.github.kfaryarok.kfaryarokapp.R;
import io.github.kfaryarok.kfaryarokapp.alerts.AlertHelper;
import io.github.kfaryarok.kfaryarokapp.alerts.BootReceiver;
import io.github.kfaryarok.kfaryarokapp.prefs.ClassPreference;
import io.github.kfaryarok.kfaryarokapp.prefs.ClassPreferenceDialogFragmentCompat;
import io.github.kfaryarok.kfaryarokapp.prefs.TimePreference;
import io.github.kfaryarok.kfaryarokapp.util.PreferenceUtil;

/**
 * Class for configuring preferences.
 * Small note, in the SharedPreferences class is stored in HEBREW!
 *
 * @author tbsc on 06/03/2017
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    private CheckBoxPreference mCbAlerts;
    private TimePreference mTpAlertTime;
    private CheckBoxPreference mCbGlobalAlerts;
    private ClassPreference mCdClass;
    // private EditTextPreference mEtpClass;
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

        mFirstLaunchActivity = getActivity().getIntent().getBooleanExtra(Intent.EXTRA_TEXT, false);

        mCbAlerts = (CheckBoxPreference) findPreference(getString(R.string.pref_alerts_enabled_bool));
        mTpAlertTime = (TimePreference) findPreference(getString(R.string.pref_alerts_time_string));
        mCbGlobalAlerts = (CheckBoxPreference) findPreference(getString(R.string.pref_globalalerts_enabled_bool));
        mCdClass = (ClassPreference) findPreference(getString(R.string.pref_class_string));
        // mEtpClass = (EditTextPreference) findPreference(getString(R.string.pref_class_string));
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
                    AlertHelper.disableAlert(getContext());
                    pm.setComponentEnabledSetting(receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                }

                mTpAlertTime.setEnabled(newBool);
                mCbGlobalAlerts.setEnabled(newBool);
                return true;
            }
        });

        mCdClass.setSummary(PreferenceUtil.getClassPreference(getContext()));

//        mEtpClass.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                // if entered class name isn't valid, prevent saving it
//                if (!ClassUtil.checkValidHebrewClassName((String) newValue)) {
//                    if (mToast != null) {
//                        mToast.cancel();
//                    }
//                    mToast = Toast.makeText(getContext(), getString(R.string.toast_invalid_class), Toast.LENGTH_LONG);
//                    mToast.show();
//                    return false;
//                }
//
//                // update summary
//                mEtpClass.setSummary((String) newValue);
//                return true;
//            }
//
//        });
//
//        // set summary to contain current value
//        mEtpClass.setSummary(PreferenceUtil.getClassPreference(getContext()));

        // set advanced settings prefscreen category's visibility based on prefs
        PreferenceCategory prefCategoryAdvanced = (PreferenceCategory) findPreference(getString(R.string.settings_advanced_category));

        boolean devModeActive = PreferenceUtil.getDeveloperModePreference(getContext());
        prefCategoryAdvanced.setVisible(devModeActive);
        if (!devModeActive) {
            prefCategoryAdvanced.removeAll();
        }

        mCbReset.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @SuppressLint("ApplySharedPref")
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // clear prefs
                // because app quits immediately, we need to clear prefs immediately
                PreferenceUtil.getSharedPreferences(getContext()).edit().clear().commit();
                // relaunch app
                System.exit(0);
                return true;
            }

        });

        mEtpUpdateServer.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String server = (String) newValue;

                // check if it's a valid url
                try {
                    // allow empty url, so reverting to default server is possible
                    if (!"".equals(server))
                        new URL(server);
                } catch (MalformedURLException e) {
                    // invalid
                    if (mToast != null) {
                        mToast.cancel();
                    }
                    mToast = Toast.makeText(getContext(), getString(R.string.toast_devmode_invalid_server), Toast.LENGTH_LONG);
                    mToast.show();
                    return false;
                }

                mEtpUpdateServer.setSummary(server);
                return true;
            }
        });

        mEtpUpdateServer.setSummary(PreferenceUtil.getUpdateServerPreference(getContext()));
    }

    @Override
    public void onDisplayPreferenceDialog(final Preference preference) {
        Dialog dialog = null;
        DialogFragment dialogFragment = null;
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
        if (mFirstLaunchActivity) {
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
