package com.example.caparking;



import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.caparking.util.SessionManager;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private NotificationManagerCompat notificationManager;
    public static int NOTIFICATION_NUMBER=1;
    SessionManager manager;


    @Override
    public void onReceive(Context context, Intent intent) {

        manager = new SessionManager(context.getApplicationContext());
        String parkName = manager.getName();
        String parkQty = manager.getPrice();
        int parkNumber = manager.getNumber();

        MediaPlayer player = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        player.start();


        notificationManager = NotificationManagerCompat.from(context);
        sendOnChannel(context,"Your parking time is up.Please move your vehicle out of"+parkName+parkNumber,intent);

    }

    public void sendOnChannel(Context context, String message, Intent intent){
        Intent resultIntent = new Intent(context,AlarmFragment.class);
        resultIntent.putExtra("parkName",manager.getName());
        resultIntent.putExtra("parkTime",manager.getTime());
        resultIntent.putExtra("parkQty",manager.getPrice());
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(context, String.valueOf(R.string.channel_id))
                .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
                .setContentTitle("Parking Reminder")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .build();
        notificationManager.notify(NOTIFICATION_NUMBER++,notification);

    }
}
