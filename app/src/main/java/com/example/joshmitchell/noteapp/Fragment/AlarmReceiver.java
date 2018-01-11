package com.example.joshmitchell.noteapp.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.joshmitchell.noteapp.Activity.NoteListActivity;
import com.example.joshmitchell.noteapp.Activity.ReminderActivity;
import com.example.joshmitchell.noteapp.R;

/**
 * Created by Josh Mitchell on 10/01/2018.
 */

public class AlarmReceiver extends BroadcastReceiver{

    public static final String EXTRA_NOTE_TITLE = "title";
    public static final String EXTRA_NOTE_CONTENT = "content";
    public static final String EXTRA_NOTE_ID = "noteid";

    private String title, content;
    private long Id;

    @Override
    public void onReceive(Context context, Intent intent){
        String CHANNEL_ID = "my_channel_01";
        title = intent.getStringExtra(EXTRA_NOTE_TITLE);
        content = intent.getStringExtra(EXTRA_NOTE_CONTENT);
        Id = intent.getLongExtra(EXTRA_NOTE_ID, -1);

        NotificationChannel channel;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT > 25){
            channel = new NotificationChannel(CHANNEL_ID, "Urgent", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLockscreenVisibility(1);
            mNotificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_add_white_24px)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setChannelId(CHANNEL_ID);

        Intent resultIntent = new Intent(context, NoteListActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NoteListActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify((int)Id, mBuilder.build());
    }
}
