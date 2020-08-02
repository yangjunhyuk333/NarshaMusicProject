package com.junhyuk.narshamusicproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.junhyuk.narshamusicproject.database.app_data.MusicDataBase;
import com.junhyuk.narshamusicproject.database.data.MusicData;
import com.junhyuk.narshamusicproject.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AddSong extends AppCompatActivity {

    String time;

    ArrayList<Uri> musicList = new ArrayList<>();
    ArrayList<String> musicTitle = new ArrayList<>();
    ArrayList<String> musicDuration = new ArrayList<>();
    ArrayList<String> musicArtist = new ArrayList<>();
    ArrayList<String> musicAlbum = new ArrayList<>();

    MusicDataBase musicDataBase;
    MediaPlayer mediaPlayer;

    private int maxCount;
    List<String> list = Arrays.asList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);

        musicDataBase = MusicDataBase.getMusicDatabase(getApplicationContext());
        //uriArrayList = new ArrayList<>();
        Util.getMusicData(this, musicList, musicTitle, musicDuration, musicArtist, musicAlbum);

        maxCount = Util.getCount(this);
        mediaPlayer = new MediaPlayer();
        try {
            musicDataBase.music_dao().getTitle().observe(AddSong.this, strings -> {
                Log.d("DataBase", "data: " + strings.get(0));
                list = strings;

            });
        }catch ( Exception e){}
        // mediaPlayer.setDataSource(this, Uri.parse(Util.getMediaStoreReadFileUri(getApplicationContext())));
        new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i=0; i<maxCount; i++)
                {

                    if(list.contains(musicTitle.get(i))) {
                        Log.e("addSong","skip song : "+musicTitle.get(i));
                        continue;
                    }
                    Log.e("addSong","musicDuration.get(i) : "+musicTitle.get(i));
                    getPlayTime(musicDuration.get(i));
                    musicDuration.remove(i);
                    musicDuration.add(i,time);
                    MusicData musicData = new MusicData(musicTitle.get(i), musicDuration.get(i), musicArtist.get(i), musicList.get(i).toString());

                    musicDataBase.music_dao().insert(musicData);
                }
                setResult(RESULT_OK);
                finish();
                //startActivity(new Intent(AddSong.this, MainActivity.class));

            }
        }).start();
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

}