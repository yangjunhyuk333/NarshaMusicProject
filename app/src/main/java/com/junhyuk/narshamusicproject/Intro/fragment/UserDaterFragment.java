package com.junhyuk.narshamusicproject.Intro.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import android.os.Handler;
import android.widget.Toast;

import com.junhyuk.narshamusicproject.Intro.activity.IntroActivity;
import com.junhyuk.narshamusicproject.MainActivity;
import com.junhyuk.narshamusicproject.R;
import com.junhyuk.narshamusicproject.database.Array_data.data;
import com.junhyuk.narshamusicproject.database.app_data.UsrDataBase;
import com.junhyuk.narshamusicproject.database.data.UsrData;

import java.util.Arrays;
import java.util.List;

public class UserDaterFragment extends Fragment {

    public static UserDaterFragment newInstance(){
        return new UserDaterFragment();
    }

    EditText editText;
    Button nextButton;
    Intent intent;

    UsrDataBase usrDataBase;

    Handler delayHandler = new Handler();

    List<String> userNameList = Arrays.asList();

    String editName;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_data, container, false);

        nextButton = view.findViewById(R.id.next_button2);

        editText = view.findViewById(R.id.user_data);

        intent = new Intent(getContext(), MainActivity.class);

        usrDataBase = UsrDataBase.getUsrDataBase(getActivity());

        final FragmentActivity context = getActivity();

        nextButton.setOnClickListener(v -> {

            editName = editText.getText().toString();

            new Thread(() -> {
                Log.d("TEST1",  "editData: " + editText.getText().toString());
                usrDataBase.usr_dao().insert(new UsrData(editName));

                delayHandler.postDelayed(() -> {
                    Log.d("UsetDater", "usrDataBase.usr_dao() : "+ usrDataBase.usr_dao());
                    Log.d("UsetDater", "usrDataBase.usr_dao()getName : "+ usrDataBase.usr_dao().getName());
                    Log.d("UsetDater", "getActivity : "+ context);
                    usrDataBase.usr_dao().getName().observe(context, strings -> {
                        Log.d("UsetDater", "usrDataBase.usr_dao() .observe(getActivity(): "+ strings);
                    });

                    usrDataBase.usr_dao().getName().observe(context, strings -> {
                        userNameList = strings;
                        data.userName.addAll(userNameList);
                        mHandler.obtainMessage(1002).sendToTarget();
                    });
                }, 1000);
            }).start();

        });

        return view;
    }
    public android.os.Handler mHandler = new android.os.Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1002:
                    startActivity(intent);
                    break;
            }
        }
    };
}
