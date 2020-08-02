package com.junhyuk.narshamusicproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.junhyuk.narshamusicproject.database.Array_data.data;
import com.junhyuk.narshamusicproject.util.Util;

import java.io.IOException;

public class MusicService extends Service {

    public static MediaPlayer mediaPlayer = new MediaPlayer();

    NotificationManager notifiM;

    Notification notifi;


    int position;


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent clsIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, clsIntent, 0);

        NotificationCompat.Builder clsBuilder;
        if( Build.VERSION.SDK_INT >= 26 )
        {
            String CHANNEL_ID = "channel_id";
            NotificationChannel clsChannel = new NotificationChannel( CHANNEL_ID, "서비스 앱", NotificationManager.IMPORTANCE_DEFAULT );
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel( clsChannel );

            clsBuilder = new NotificationCompat.Builder(this, CHANNEL_ID );
        }
        else
        {
            clsBuilder = new NotificationCompat.Builder(this );
        }

        // QQQ: notification 에 보여줄 타이틀, 내용을 수정한다.
        clsBuilder.setSmallIcon( R.drawable.voice_icon )
                .setContentTitle( "서비스 앱" ).setContentText( "서비스 앱" )
                .setContentIntent( pendingIntent );

        // foreground 서비스로 실행한다.
        startForeground( 1, clsBuilder.build() );

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}






