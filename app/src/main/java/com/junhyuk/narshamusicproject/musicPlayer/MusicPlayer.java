package com.junhyuk.narshamusicproject.musicPlayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.junhyuk.narshamusicproject.R;
import com.junhyuk.narshamusicproject.database.app_data.MusicDataBase;
import com.junhyuk.narshamusicproject.database.data.MusicData;
import com.junhyuk.narshamusicproject.util.Util;
import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayer extends AppCompatActivity {
    private int playing = 0;
    private int maxCount;
    String time;

    MusicDataBase musicDataBase;
    MediaPlayer mediaPlayer;

    Button preButton;
    Button nextButton;
    Button startButton;
    Button stopButton;

    public TextView textMusicTitle;
    public TextView textMusicDuration;
    public TextView textMusicArtist;

    public ImageView imageMusicAlbum;

    ArrayList<Uri> musicList = new ArrayList<>();
    ArrayList<String> musicTitle = new ArrayList<>();
    ArrayList<String> musicDuration = new ArrayList<>();
    ArrayList<String> musicArtist = new ArrayList<>();
    ArrayList<String> musicAlbum = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);

        musicDataBase = MusicDataBase.getMusicDatabase(getApplicationContext());
        //uriArrayList = new ArrayList<>();
        Util.getMusicData(this, musicList, musicTitle, musicDuration, musicArtist, musicAlbum);

        maxCount = Util.getCount(this);
        mediaPlayer = new MediaPlayer();

        // mediaPlayer.setDataSource(this, Uri.parse(Util.getMediaStoreReadFileUri(getApplicationContext())));
        for(int i=0; i<maxCount; i++)
        {
            getPlayTime(musicDuration.get(i));
            musicDuration.remove(i);
            musicDuration.add(i,time);
            MusicData musicData = new MusicData(musicTitle.get(i), musicDuration.get(i), musicArtist.get(i), musicList.get(i).toString());
            musicDataBase.music_dao().insert(musicData);
        }

        Log.d("SELECT", musicDataBase.music_dao().getAll().toString());

        preButton = findViewById(R.id.pre);
        startButton = findViewById(R.id.start);
        stopButton = findViewById(R.id.stop);
        nextButton = findViewById(R.id.next);
        textMusicArtist = findViewById(R.id.music_artiste);
        textMusicDuration = findViewById(R.id.musicDuration);
        textMusicTitle = findViewById(R.id.music_title);
        imageMusicAlbum = findViewById(R.id.music_image);


        startButton.setOnClickListener(v -> {
            if (mediaPlayer != null)
                mediaPlayer.stop();
            try {
                mediaPlayer.release();
                mediaPlayer = null;
                mediaPlayer = new MediaPlayer();
                setData();
                mediaPlayer.setDataSource(getApplicationContext(), musicList.get(playing));
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
        });

        preButton.setOnClickListener(v -> {
            playing--;
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            if (playing <= -1)
                playing = maxCount - 1;
            Log.d("Playing", "Play : " + playing);
            try {
                mediaPlayer.release();
                mediaPlayer = null;
                mediaPlayer = new MediaPlayer();
                setData();
                mediaPlayer.setDataSource(getApplicationContext(), musicList.get(playing));
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        nextButton.setOnClickListener(v -> {
            playNextSong();
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
        // MediaPlayer 해지
        super.onDestroy();
        Log.d("TAG", "distory");
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void setData() {
        textMusicTitle.setText((musicTitle.get(playing)).replace(".mp3", " "));
        textMusicDuration.setText(musicDuration.get(playing));
        textMusicArtist.setText(musicArtist.get(playing));
    }

    public void getPlayTime(String timeList) {
        int hour = (Integer.parseInt(timeList))/1000/3600;
        int minute = ((Integer.parseInt(timeList))/1000/60) % 60;
        int second = ((Integer.parseInt(timeList))/1000) % 60;
        //time = String.format (   (hour + ":" + minute + ":" + second);
        if(hour == 0)
        {
            time = String.format ("%02d:%02d",minute,second);
        }
        else if(hour == 0 && minute == 0)
        {
            time = String.format ("%02d",second);
        }
        else
            time = String.format ("%02d:%02d",minute,second);
    }

    public void playNextSong() {
        playing++;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (playing == maxCount)
            playing = 0;
        Log.d("Playing", "Play : " + playing);
        try {
            mediaPlayer.release();
            mediaPlayer = null;
            mediaPlayer = new MediaPlayer();
            setData();
            mediaPlayer.setDataSource(getApplicationContext(), musicList.get(playing));
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(mp ->
                    mediaPlayer.start());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
