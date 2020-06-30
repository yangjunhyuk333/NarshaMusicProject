package com.junhyuk.narshamusicproject.musicPlayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.junhyuk.narshamusicproject.R;

public class MusicPlayer extends AppCompatActivity {
    private int songs[]; // 음원 목록
    private int playing = 0; // 현재 연주중인 음원 지시자

    MediaPlayer mediaPlayer;

    Button preButton;
    Button nextButton;
    Button startButton;
    Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);

        songs = new int[3];
        songs[0] = R.raw.buluming_iu;
        songs[1] = R.raw.izone;
        songs[2] = R.raw.red_face;

        preButton = findViewById(R.id.pre);
        startButton = findViewById(R.id.start);
        stopButton = findViewById(R.id.stop);
        nextButton = findViewById(R.id.next);



        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null)
                    mediaPlayer.stop();
                mediaPlayer = MediaPlayer.create(MusicPlayer.this, songs[playing]);
                mediaPlayer.start();
            }
        });

        preButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playing--;
                if(mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                if(playing <= -1)
                    playing = 2;
                Log.d("Playing", "Play : " + playing);
                mediaPlayer = MediaPlayer.create(MusicPlayer.this, songs[playing]);
                mediaPlayer.start();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playing++;
                if(mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                if(playing == 3)
                    playing = 0;
                Log.d("Playing", "Play : " + playing);
                mediaPlayer = MediaPlayer.create(MusicPlayer.this, songs[playing]);
                mediaPlayer.start();

            }
        });



        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer == null){
                    return;
                }
                mediaPlayer.stop();
            }
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
