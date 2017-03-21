package io.github.kfaryarok.kfaryarokapp.alerts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Receiver class for knowing when an alert was triggered.
 *
 * @author tbsc on 13/03/2017
 */
public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // alert called, show notification
        AlertHelper.showNotification(context, true);
    }

}
