package com.junhyuk.narshamusicproject.Intro.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.junhyuk.narshamusicproject.Intro.activity.IntroActivity;
import com.junhyuk.narshamusicproject.MainActivity;
import com.junhyuk.narshamusicproject.R;

public class IntroFragment extends Fragment {

    public static IntroFragment newInstance(){
        return new IntroFragment();
    }

    Button nextButton;

    boolean setIntent = IntroActivity.checkIntent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro, container, false);

        nextButton = view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(v -> {
            if(setIntent){
                startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
            }else{
                ((IntroActivity)getActivity()).replaceFragment(UserDaterFragment.newInstance());
            }
        });

        return view;
    }
}
