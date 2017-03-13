package io.github.kfaryarok.kfaryarokapp.alerts;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import java.util.Calendar;

import io.github.kfaryarok.kfaryarokapp.R;
import io.github.kfaryarok.kfaryarokapp.util.PreferenceUtil;

/**
 * TODO setup alart when needed
 * @author tbsc on 11/03/2017
 */
public class AlertManager {

    private static AlarmManager mAlarmManager;

    public static void setUpdateAlert(Context ctx, PendingIntent alarmIntent) {
        if (mAlarmManager == null) {
            mAlarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        }

        String alertTime = PreferenceUtil.getSharedPreferences(ctx).getString(ctx.getString(R.string.pref_alerts_time_string), ctx.getString(R.string.tv_firstlaunch_alerthour_def));
        String[] time = alertTime.split(":");
        int alertHour = Integer.parseInt(time[0]);
        int alertMinute = Integer.parseInt(time[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR, alertHour);
        calendar.set(Calendar.MINUTE, alertMinute);

        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }

}
