package io.github.kfaryarok.kfaryarokapp.alerts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.github.kfaryarok.kfaryarokapp.util.PreferenceUtil;

/**
 * Responsible for receiving boot broadcasts and resetting the alert back.
 *
 * @author tbsc on 11/03/2017
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (PreferenceUtil.getAlertEnabledPreference(context)) {
            // reenable alert only if it's enabled in prefs
            AlertHelper.enableAlert(context);
        }
    }

}
