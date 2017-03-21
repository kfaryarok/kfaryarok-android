package io.github.kfaryarok.kfaryarokapp.alerts;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import java.util.Calendar;

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
        if (mAlarmManager == null) {
            // cache alarm manager if null
            mAlarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        }
        if (mPendingAlertReceiver == null) {
            // init pendingintent if null
            mPendingAlertReceiver = PendingIntent.getBroadcast(ctx, 0, new Intent(ctx, AlertReceiver.class), 0);
        }

        String alertTime = PreferenceUtil.getAlertTimePreference(ctx);
        int alertHour = TimePreference.parseHour(alertTime);
        int alertMinute = TimePreference.parseMinute(alertTime);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, alertHour);
        calendar.set(Calendar.MINUTE, alertMinute);
        calendar.set(Calendar.SECOND, 0);

        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, mPendingAlertReceiver);
    }

    /**
     * Takes the current set alert and disable it
     */
    public static void disableAlert() {
        if (mPendingAlertReceiver != null) {
            // if the pending intent isn't null, then the alarm manager can't be null too
            if (mAlarmManager != null) {
                // but there's no downside to being more safe
                mAlarmManager.cancel(mPendingAlertReceiver);
            }
        }
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

        Update[] updates = UpdateHelper.getUpdates(context);
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
