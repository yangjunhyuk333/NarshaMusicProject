package com.junhyuk.narshamusicproject;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.junhyuk.narshamusicproject.database.Array_data.data;
import com.junhyuk.narshamusicproject.util.Util;

import java.io.IOException;

public class MusicService extends Service {

    MediaPlayer mediaPlayer;

    int position;

    int maxCount = Util.getCount(this);
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        position = intent.getExtras().getInt("position");
        if (mediaPlayer != null)
            mediaPlayer.stop();
        try {
            mediaPlayer.release();
            mediaPlayer = null;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getApplicationContext(), data.musicList.get(position));
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
        return START_NOT_STICKY;
    }

    public void playNextSong() {
        position++;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (position == maxCount)
            position = 0;
        Log.d("Playing", "Play : " + position);
        try {
            mediaPlayer.release();
            mediaPlayer = null;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getApplicationContext(), data.musicList.get(position));
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(mp ->
                    mediaPlayer.start());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
