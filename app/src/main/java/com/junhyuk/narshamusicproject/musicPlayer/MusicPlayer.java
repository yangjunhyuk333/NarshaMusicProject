package com.junhyuk.narshamusicproject.musicPlayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.junhyuk.narshamusicproject.R;
import com.junhyuk.narshamusicproject.util.Util;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayer extends AppCompatActivity {
    private int playing = 0; // 현재 연주중인 음원 지시자

    MediaPlayer mediaPlayer;

    Button preButton;
    Button nextButton;
    Button startButton;
    Button stopButton;

    ArrayList<Uri> uriArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);
        if (uriArrayList == null) {
            //uriArrayList = new ArrayList<>();
            uriArrayList = Util.getMediaStoreReadFileUri(this);
        }


        mediaPlayer = new MediaPlayer();
        // mediaPlayer.setDataSource(this, Uri.parse(Util.getMediaStoreReadFileUri(getApplicationContext())));
        Log.d("TAG", ""+uriArrayList.get(0));


        preButton = findViewById(R.id.pre);
        startButton = findViewById(R.id.start);
        stopButton = findViewById(R.id.stop);
        nextButton = findViewById(R.id.next);


        startButton.setOnClickListener(v -> {
            if (mediaPlayer != null)
                mediaPlayer.stop();
            try {
                mediaPlayer.setDataSource(getApplicationContext(), uriArrayList.get(playing));
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });

                Log.d("TAG", "ge");
            } catch (IOException e) {
                Log.d("TAG", "he");
                e.printStackTrace();
            }
        });

        preButton.setOnClickListener(v -> {
            playing--;
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            if (playing <= -1)
                playing = 2;
            Log.d("Playing", "Play : " + playing);
            try {
                mediaPlayer.setDataSource(getApplicationContext(), uriArrayList.get(playing));
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        nextButton.setOnClickListener(v -> {
            playing++;
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            if (playing == 3)
                playing = 0;
            Log.d("Playing", "Play : " + playing);
            try {
                mediaPlayer.setDataSource(getApplicationContext(), uriArrayList.get(playing));
//                mediaPlayer.prepare();
//                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mp) {
//
//                    }
//                });
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });


        stopButton.setOnClickListener(v -> {
            if (mediaPlayer == null) {
                return;
            }
            mediaPlayer.stop();
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // MediaPlayer 해지
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
