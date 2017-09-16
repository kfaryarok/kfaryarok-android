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

package io.github.kfaryarok.kfaryarokapp.alerts;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import io.github.kfaryarok.kfaryarokapp.MainActivity;
import io.github.kfaryarok.kfaryarokapp.R;
import io.github.kfaryarok.kfaryarokapp.prefs.TimePreference;
import io.github.kfaryarok.kfaryarokapp.updates.Update;
import io.github.kfaryarok.kfaryarokapp.updates.UpdateHelper;
import io.github.kfaryarok.kfaryarokapp.util.PreferenceUtil;

/**
 * This class is used to configure update alerts based on preferences and other things;
 * you do not need to give it any values.
 *
 * BUGS:
 * - TODO Alert going of at random times
 * - DONE Disabling alert doesn't turn if off
 * - TODO Figure out a way to cache data to reduce traffic
 *
 * @author tbsc on 11/03/2017
 */
public class AlertHelper {

    public static final int NOTIFICATION_ALERT = 1;

    private static AlarmManager mAlarmManager;
    private static PendingIntent mPendingAlertReceiver;

    /**
     * Enables an alarm using preferences and default values.
     * @param ctx Context, used to create a pending intent for receiving alarm and setting the alarm
     */
    public static void enableAlert(Context ctx) {
        initiateFields(ctx);

        String alertTime = PreferenceUtil.getAlertTimePreference(ctx);
        int alertHour = TimePreference.parseHour(alertTime);
        int alertMinute = TimePreference.parseMinute(alertTime);

        // create calendar instance for calculating alert time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        // set alert time fields
        calendar.set(Calendar.HOUR_OF_DAY, alertHour);
        calendar.set(Calendar.MINUTE, alertMinute);
        calendar.set(Calendar.SECOND, 0);

        // if alert hour is before current time, push it forward by a day
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        if (calendar.before(now)) {
            calendar.add(Calendar.DATE, 1);
        }

        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, mPendingAlertReceiver);
    }

    /**
     * Cancels the current alert. If the alert wasn't set with {@link #createIntent(Context)}, then
     * it won't be able to cancel the alert.
     */
    public static void disableAlert(Context ctx) {
        initiateFields(ctx);

        mAlarmManager.cancel(mPendingAlertReceiver);
    }

    /**
     * Initiates the alarm manager field and the pending intent field, in case they're null.
     * @param ctx Used to get the alarm manager instance and to create the pending intent
     */
    private static void initiateFields(Context ctx) {
        if (mAlarmManager == null) {
            // cache alarm manager if null
            mAlarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        }
        if (mPendingAlertReceiver == null) {
            // init pendingintent if null
            mPendingAlertReceiver = createIntent(ctx);
        }
    }

    /**
     * Creates an instance of the pending intent that is used to fire the receiver after
     * the alarm was called.
     * @param ctx For creating the intent
     * @return A pending intent that calls {@link AlertReceiver}
     */
    private static PendingIntent createIntent(Context ctx) {
        return PendingIntent.getBroadcast(ctx, 0, new Intent(ctx, AlertReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Creates a notification containing updates.
     * This method fetches updates, parses them, filters them (based on prefs) and formats them
     * into a notification, with updates line-separated.
     * @param context Used for getting preferences and strings
     * @param show Should it be shown to the user
     * @return The notification created
     */
    public static Notification showNotification(Context context, boolean show) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(context.getString(R.string.alert_updates_title))
                .setSmallIcon(R.mipmap.ic_launcher);

        Update[] updates = new Update[0];
        try {
            updates = UpdateHelper.getUpdates(context);
        } catch (IOException | JSONException e) {
            // TODO: Replace sync system here too
            e.printStackTrace();
        }
        if (updates.length == 0)
            // no updates, so no notification to show then exit
            return null;

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        for (Update update : updates) {
            if (!PreferenceUtil.getGlobalAlertsPreference(context))
                // if set to not show global updates
                if (update.getAffected().isGlobal())
                    // and it's a global update, skip
                    continue;
            // add update as line to the notification
            inboxStyle.addLine(UpdateHelper.formatUpdate(update, context));
        }

        // give style to builder
        inboxStyle.setBigContentTitle("עדכונים:");
        builder.setStyle(inboxStyle);

        // create explicit intent to main activity
        Intent mainActivity = new Intent(context, MainActivity.class);
        // create a stack for telling android what to launch
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // tell it to go to main activity
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(mainActivity);

        // get the pending intent
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        // give it to the notification builder
        builder.setContentIntent(pendingIntent);

        // cancel notif when clicked
        builder.setAutoCancel(true);

        // bob the build-it
        Notification notif = builder.build();

        if (show) {
            // show notification
            NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notifManager.notify(AlertHelper.NOTIFICATION_ALERT, notif);
        }

        return notif;
    }

}
