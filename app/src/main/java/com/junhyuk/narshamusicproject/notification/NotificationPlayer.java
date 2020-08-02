package com.junhyuk.narshamusicproject.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.RemoteViews;

import com.junhyuk.narshamusicproject.MainActivity;
import com.junhyuk.narshamusicproject.R;

import androidx.core.app.NotificationCompat;

public class NotificationPlayer {
    private final static int NOTIFICATION_PLAYER_ID = 0x846;
    private Service mService;
    private NotificationManager mNotificationManager;
    private NotificationManagerBuilder mNotificationManagerBuilder;
    private boolean isForeground;

    public NotificationPlayer(Service service) {
        mService = service;
        mNotificationManager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void updateNotificationPlayer() {
        cancel();
        mNotificationManagerBuilder = new NotificationManagerBuilder();
        mNotificationManagerBuilder.execute();
    }

    public void removeNotificationPlayer() {
        cancel();
        mService.stopForeground(true);
        isForeground = false;
    }

    private void cancel() {
        if (mNotificationManagerBuilder != null) {
            mNotificationManagerBuilder.cancel(true);
            mNotificationManagerBuilder = null;
        }
    }

    private class NotificationManagerBuilder extends AsyncTask<Void, Void, Notification> {
        private RemoteViews mRemoteViews;
        private NotificationCompat.Builder mNotificationBuilder;
        private PendingIntent mMainPendingIntent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Intent mainActivity = new Intent(mService, MainActivity.class);
            mMainPendingIntent = PendingIntent.getActivity(mService, 0, mainActivity, 0);
            mRemoteViews = createRemoteView(R.layout.notification_foreground);
            if( Build.VERSION.SDK_INT >= 26 )
            {
                String CHANNEL_ID = "MusicJavava";
                NotificationChannel clsChannel = new NotificationChannel( CHANNEL_ID, "서비스 앱", NotificationManager.IMPORTANCE_DEFAULT );
                ((NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel( clsChannel );

                mNotificationBuilder = new NotificationCompat.Builder(mService, CHANNEL_ID );
            }
            else
            {
                mNotificationBuilder = new NotificationCompat.Builder(mService );
            }
            //mNotificationBuilder = new NotificationCompat.Builder(mService);
            mNotificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true)
                    .setContentIntent(mMainPendingIntent)
                    .setContent(mRemoteViews);

            Notification notification = mNotificationBuilder.build();
            notification.priority = Notification.PRIORITY_MAX;
            notification.contentIntent = mMainPendingIntent;
            if (!isForeground) {
                isForeground = true;
                // 서비스를 Foreground 상태로 만든다
                mService.startForeground(NOTIFICATION_PLAYER_ID, notification);
            }
        }

        @Override
        protected Notification doInBackground(Void... voids) {
            mNotificationBuilder.setContent(mRemoteViews);
            mNotificationBuilder.setContentIntent(mMainPendingIntent);
            mNotificationBuilder.setPriority(Notification.PRIORITY_MAX);
            Notification notification = mNotificationBuilder.build();
            updateRemoteView(mRemoteViews, notification);
            return notification;
        }

        private RemoteViews createRemoteView(int layoutId) {
            RemoteViews remoteView = new RemoteViews(mService.getPackageName(), layoutId);

            return remoteView;
        }
        private void updateRemoteView(RemoteViews remoteViews, Notification notification) {


//            String title = mService.getAudioItem().mTitle;
//            remoteViews.setTextViewText(R.id.txt_title, title);
//            Uri albumArtUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), mService.getAudioItem().mAlbumId);
//            Picasso.with(mService).load(albumArtUri).error(R.drawable.empty_albumart).into(remoteViews, R.id.img_albumart, NOTIFICATION_PLAYER_ID, notification);

        }
    }
}
