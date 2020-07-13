package com.junhyuk.narshamusicproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.junhyuk.narshamusicproject.Adapter.RecyclerViewAdapter;
import com.junhyuk.narshamusicproject.constant.Value;
import com.junhyuk.narshamusicproject.database.Array_data.data;
import com.junhyuk.narshamusicproject.database.app_data.MusicDataBase;
import com.junhyuk.narshamusicproject.database.app_data.UsrDataBase;
import com.junhyuk.narshamusicproject.database.data.MusicData;
import com.junhyuk.narshamusicproject.dialog.CustomDialog;
import com.junhyuk.narshamusicproject.musicPlayer.MusicPlayer;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    String[] permission_list = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    UsrDataBase usrDataBase;

    String userName;

    Intent intent;

    ImageButton voiceButton;

    RecyclerView recyclerView;

    TextView musicPlus;

    TextView textView;

    TextView musicpPlayer;

    MusicDataBase musicDataBase;

    EditText editText;

    Button enterButton;

    Context context;

    List<String> list = Arrays.asList();

    List<String> userNameList = Arrays.asList();

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

        musicpPlayer = findViewById(R.id.music_player);

        intent = getIntent();

        usrDataBase = UsrDataBase.getUsrDataBase(this);

        enterButton.setOnClickListener(v -> {
            Log.d("DataBase", "buttonClick");
            musicDataBase.music_dao().getTitle().observe(MainActivity.this, strings -> {
                Log.d("DataBase", "data: " + strings.get(0));
                list = strings;
            });
        });

        Log.d("TEST1", "data: " + data.userName);

        if(data.userName != null){
            textView.setText(data.userName.get(0) + "님 반갑습니다.");
        }else{
            finish();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecyclerViewAdapter());

        voiceButton.setOnClickListener(v -> {
            //다이얼로그 실행
            Toast.makeText(getApplicationContext(), "다이얼로그 실행", Toast.LENGTH_LONG).show();
            CustomDialog customDialog = new CustomDialog(MainActivity.this);
            customDialog.show();
        });

        musicpPlayer.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),MusicPlayer.class);
            startActivity(intent);
        });

        //음악 파일 추가 MediaStore 등록
        musicPlus.setOnClickListener(view -> {
            int code = Value.FIND_AUDIO_REQUEST;

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT).
                    addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/*");

            startActivityForResult(intent, code);
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Value.FIND_AUDIO_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri uri  = data.getData();
            try {
                Log.d(TAG,"uri : "+ URLDecoder.decode(uri.toString(),"UTF-8"));
                Log.d(TAG,"uri P : "+uri.getPath());
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if(cursor !=null &&cursor.getCount() > 0){
                    cursor.moveToFirst();
                    String name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    Log.d(TAG,"uri F : "+name);
                   //Util.mediaStoreSaveFile(getApplicationContext(), uri, name);
                    //Util.mediaStoreReadFile(getApplicationContext());
                }
            } catch (UnsupportedEncodingException e) {

                Log.d(TAG,"err : "+ e.getMessage());
            }
        }
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
}