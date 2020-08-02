package com.junhyuk.narshamusicproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.junhyuk.narshamusicproject.constant.Value;
import com.junhyuk.narshamusicproject.database.Array_data.data;
import com.junhyuk.narshamusicproject.notification.NotificationPlayer;
import com.junhyuk.narshamusicproject.util.Util;

import java.io.IOException;

public class MusicService extends Service {

    public static MediaPlayer mediaPlayer = new MediaPlayer();

    NotificationManager notifiM;

    Notification notifi;
    RemoteViews remoteView;
    private final int notifyId = 846;
    Notification notification;
    NotificationManager mNotificationManager;


    int position;


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent clsIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, clsIntent, 0);

        //노티피케이션 이벤트 핸들러
        if (intent != null) {
            String action = intent.getAction();
            if (Value.TOGGLE_PLAY.equals(action)) {
               Log.e("mService","TOGGLE_PLAY");
               return START_NOT_STICKY ;
            } else if (Value.REWIND.equals(action)) {
                Log.e("mService","REWIND");
                return START_NOT_STICKY ;
            } else if (Value.FORWARD.equals(action)) {
                Log.e("mService","FORWARD");
                playNextSong();
                return START_NOT_STICKY ;
            } else if (Value.CLOSE.equals(action)) {
                Log.e("mService","CLOSE");
                return START_NOT_STICKY ;
            }
        }

        NotificationCompat.Builder clsBuilder;
        if( Build.VERSION.SDK_INT >= 26 )
        {
            String CHANNEL_ID = "channel_id";
            NotificationChannel clsChannel = new NotificationChannel( CHANNEL_ID, "서비스 앱", NotificationManager.IMPORTANCE_DEFAULT );
            clsChannel.setLightColor(Color.BLUE);
            clsChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel( clsChannel );
            mNotificationManager  = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

            clsBuilder = new NotificationCompat.Builder(this, CHANNEL_ID );
        }
        else
        {
            clsBuilder = new NotificationCompat.Builder(this );
        }

        remoteView = createRemoteView(R.layout.notification_foreground);
        remoteView.setOnClickPendingIntent(R.id.next, pendingIntent);

        notification = clsBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContent(remoteView)
                .setContentIntent(pendingIntent) //intent
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notifyId, clsBuilder.build());
        startForeground(notifyId, notification);


        new Thread(() -> {
            position = intent.getExtras().getInt("position");
            if (mediaPlayer != null)
                mediaPlayer.stop();
            try {
                mediaPlayer.release();
                mediaPlayer = null;
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(data.musicList.get(position)));
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());
                mHandler.obtainMessage(Value.NOTI_UPDATE).sendToTarget();


                Log.d("TAG", "ge");
            } catch (IOException e) {
                Log.d("TAG", "he");
                e.printStackTrace();
            }
            mediaPlayer.setOnCompletionListener(mp -> {
                Log.d("TAG", "노래끝");
                playNextSong();
            });
        }).start();
        return START_NOT_STICKY;
    }


    public void playNextSong() {
        position++;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (position == (data.musicCount-1))
            position = 0;
        Log.d("Playing", "Play : " + position);
        try {
            mediaPlayer.release();
            mediaPlayer = null;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(data.musicList.get(position)));
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(mp ->
                    mediaPlayer.start());
            mHandler.obtainMessage(Value.NOTI_UPDATE).sendToTarget();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private RemoteViews createRemoteView(int layoutId) {
        RemoteViews remoteView = new RemoteViews(getPackageName(), layoutId);
        Intent actionTogglePlay = new Intent(Value.TOGGLE_PLAY);
        Intent actionForward = new Intent(Value.FORWARD);
        Intent actionRewind = new Intent(Value.REWIND);
//        Intent actionClose = new Intent(Value.CLOSE); //필요시 구현해라
        PendingIntent togglePlay = PendingIntent.getService(this, 0, actionTogglePlay, 0);
        PendingIntent forward = PendingIntent.getService(this, 0, actionForward, 0);
        PendingIntent rewind = PendingIntent.getService(this, 0, actionRewind, 0);
//        PendingIntent close = PendingIntent.getService(this, 0, actionClose, 0);

        remoteView.setOnClickPendingIntent(R.id.start_stop_button, togglePlay);
        remoteView.setOnClickPendingIntent(R.id.next_button, forward);
        remoteView.setOnClickPendingIntent(R.id.perv_button, rewind);
        return remoteView;
    }
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch ((msg.what)){
                case Value.NOTI_UPDATE:
                    String title = data.musicTitle.get(position);
                    Log.e("mService","title : "+title);
                    remoteView.setTextViewText(R.id.song_title,data.musicTitle.get(position));
                    if(notification !=null){

                        mNotificationManager.notify(notifyId, notification);
                    }
                    break;
            }
        }
    };


}






