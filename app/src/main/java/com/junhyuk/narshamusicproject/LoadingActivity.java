package com.junhyuk.narshamusicproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.junhyuk.narshamusicproject.database.Array_data.data;
import com.junhyuk.narshamusicproject.database.app_data.MusicDataBase;

import java.util.Arrays;
import java.util.List;

public class LoadingActivity extends AppCompatActivity {

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(LoadingActivity.this, MainActivity.class));
            finish();
        }
    };

    MusicDataBase musicDataBase;

    List<String> list = Arrays.asList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        musicDataBase = MusicDataBase.getMusicDatabase(getApplicationContext());

        musicDataBase.music_dao().getTitle().observe(LoadingActivity.this, strings -> {
            Log.d("DataBase", "data: " + strings.get(0));
            list = strings;
            data.musicTitle.addAll(list);
        });
    }

    @Override
    protected void onRestart() {
        //인트로 엑티비티는 한번 호출되고 다시 호출될일이 없어서 onRestart 메소드를 통해서 바로 종료
        super.onRestart();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}