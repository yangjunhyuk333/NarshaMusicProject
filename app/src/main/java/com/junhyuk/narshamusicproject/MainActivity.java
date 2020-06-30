package com.junhyuk.narshamusicproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.junhyuk.narshamusicproject.Adapter.RecyclerViewAdapter;
import com.junhyuk.narshamusicproject.database.Array_data.data;
import com.junhyuk.narshamusicproject.database.app_data.MusicDataBase;
import com.junhyuk.narshamusicproject.database.data.MusicData;
import com.junhyuk.narshamusicproject.dialog.CustomDialog;
import com.junhyuk.narshamusicproject.musicPlayer.MusicPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String[] permission_list = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    String userName;

    Intent intent;

    ImageButton voiceButton;

    RecyclerView recyclerView;

    RecyclerViewAdapter recyclerViewAdapter;

    TextView musicPlus;

    TextView textView;

    MusicDataBase musicDataBase;

    EditText editText;

    Button enterButton;

    Context context;

    RecyclerViewAdapter.ViewHolder viewHolder;

    DBThread dbThread;

    List<String> list = Arrays.asList();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        musicDataBase = MusicDataBase.getMusicDatabase(getApplicationContext());

        context = getApplicationContext();

        voiceButton = findViewById(R.id.voice_button);

        editText = findViewById(R.id.editText);

        enterButton = findViewById(R.id.enter_button);

        recyclerView = findViewById(R.id.recycler_view);

        musicPlus = findViewById(R.id.music_plus);

        textView = findViewById(R.id.textView);

        dbThread = new DBThread();

        intent = getIntent();

        userName = intent.getExtras().getString("userName");

        Log.d("Test", "data: " + userName);

        enterButton.setOnClickListener(v -> {
            Log.d("DataBase", "buttonClick");
            dbThread.start();
            musicDataBase.music_dao().getTitle().observe(MainActivity.this, strings -> {
                Log.d("DataBase", "data: " + strings.get(0));
                list = strings;
                data.musicTitle.addAll(list);
            });
        });

        textView.setText(userName + "님 반갑습니다.");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecyclerViewAdapter());

        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //다이얼로그 실행
                Toast.makeText(getApplicationContext(), "다이얼로그 실행", Toast.LENGTH_LONG).show();
                CustomDialog customDialog = new CustomDialog(MainActivity.this);
                customDialog.show();
            }
        });

        musicPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MusicPlayer.class);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;
        for (String permission : permission_list) {
            int chk = checkCallingOrSelfPermission(permission);
            if (chk == PackageManager.PERMISSION_DENIED) {
                requestPermissions(permission_list, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getApplicationContext(), "앱권한설정하세요", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    public class DBThread extends Thread{
        @Override
        public void run() {
            musicDataBase.music_dao().insert(new MusicData(editText.getText().toString()));
        }
    }

}