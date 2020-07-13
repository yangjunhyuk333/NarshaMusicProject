package com.junhyuk.narshamusicproject.Intro.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.junhyuk.narshamusicproject.Intro.fragment.IntroFragment;
import com.junhyuk.narshamusicproject.MainActivity;
import com.junhyuk.narshamusicproject.R;
import com.junhyuk.narshamusicproject.database.Array_data.data;
import com.junhyuk.narshamusicproject.database.app_data.MusicDataBase;
import com.junhyuk.narshamusicproject.database.app_data.UsrDataBase;
import com.junhyuk.narshamusicproject.database.data.MusicData;

import java.util.Arrays;
import java.util.List;

import android.os.Handler;

public class IntroActivity extends AppCompatActivity {

    MusicDataBase musicDataBase;

    List<String> list = Arrays.asList();

    UsrDataBase usrDataBase;

    Handler handler = new Handler();
    Runnable runnable = () -> {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    };

    public static boolean checkIntent = false;

    List<String> getNameList = Arrays.asList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        musicDataBase = MusicDataBase.getMusicDatabase(getApplicationContext());


        usrDataBase = UsrDataBase.getUsrDataBase(this);

        usrDataBase.usr_dao().getName().observe(IntroActivity.this, strings -> {
            if (!strings.isEmpty()) {
                Log.d("TEST1", "getNameData: " + strings);
                getNameList = strings;
                data.userName.addAll(getNameList);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                fragmentTransaction.replace(R.id.container, new IntroFragment());
                fragmentTransaction.commit();
            }
        });


        musicDataBase.music_dao().getTitle().observe(IntroActivity.this, strings -> {
            list = strings;
            data.musicTitle.addAll(list);
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }

    @Override
    protected void onRestart() {
        //인트로 엑티비티는 한번 호출되고 다시 호출될일이 없어서 onRestart 메소드를 통해서 바로 종료
        super.onRestart();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}