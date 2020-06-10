package com.junhyuk.narshamusicproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageButton voiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        voiceButton = findViewById(R.id.voice_button);

        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //다이얼로그 실행
                Toast.makeText(getApplicationContext(), "다이얼로그 실행", Toast.LENGTH_LONG).show();
                CustomDialog customDialog = new CustomDialog(MainActivity.this);
                customDialog.show();
            }
        });


    }

}