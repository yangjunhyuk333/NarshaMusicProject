package com.junhyuk.narshamusicproject.Intro.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.junhyuk.narshamusicproject.MainActivity;
import com.junhyuk.narshamusicproject.R;

public class UserDaterFragment extends Fragment {

    public static UserDaterFragment newInstance(){
        return new UserDaterFragment();
    }

    EditText editText;
    Button nextButton;
    Intent intent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_data, container, false);

        nextButton = view.findViewById(R.id.next_button2);

        editText = view.findViewById(R.id.user_data);

        intent = new Intent(getContext(), MainActivity.class);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("userName", editText.getText().toString());
                Log.d("Test", "data: " + editText.getText().toString());
                startActivity(intent);
            }
        });

        return view;
    }
}
