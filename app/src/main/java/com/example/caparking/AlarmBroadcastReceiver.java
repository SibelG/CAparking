package com.example.caparking;



import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.caparking.util.SessionManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;



public class AlarmBroadcastReceiver  extends BroadcastReceiver  {

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


            //Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

            Intent intent1 = new Intent(context,AlarmFragment.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            /*TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addParentStack(AlarmFragment.class);
            taskStackBuilder.addNextIntent(intent1);*/

            PendingIntent intent2 = PendingIntent.getActivity(context,1,intent1,PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            NotificationChannel channel = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                channel = new NotificationChannel("my_channel_01","hello", NotificationManager.IMPORTANCE_HIGH);
            }

            Notification notification = builder.setContentTitle("Car Parking")
                    .setContentText(message).setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentIntent(intent2)
                    .setChannelId("my_channel_01")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setAutoCancel(true)
                    .setContentIntent(intent2)
                    .build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(1, notification);

        }

}
