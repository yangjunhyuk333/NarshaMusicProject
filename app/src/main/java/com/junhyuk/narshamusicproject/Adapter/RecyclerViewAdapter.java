package com.junhyuk.narshamusicproject.Adapter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.junhyuk.narshamusicproject.Intro.activity.IntroActivity;
import com.junhyuk.narshamusicproject.MainActivity;
import com.junhyuk.narshamusicproject.MusicService;
import com.junhyuk.narshamusicproject.R;
import com.junhyuk.narshamusicproject.database.Array_data.data;
import com.junhyuk.narshamusicproject.database.app_data.MusicDataBase;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public static Context context;

    Application application;

    MusicDataBase musicDataBase;

    List<String> list = Arrays.asList();

    public static void setContext(Context context) {
        RecyclerViewAdapter.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.musicImage.setImageResource(R.drawable.music_thumnail_example);
        if (data.musicTitle != null && data.musicTitle.size() > 0) {
            viewHolder.musicText.setText(data.musicTitle.get(position));
        } else {
            viewHolder.musicText.setText("노래없음");
        }


        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MusicService.class);
            intent.putExtra("position", position);
            context.startService(intent);

        });

    }

    @Override
    public int getItemCount() {
        //받아온 음악수에 따라 리사이클럽 뷰 수 지정
        if (data.musicTitle != null && data.musicTitle.size() > 0) {
            return data.musicTitle.size();
        } else {
            return 1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView musicImage;
        public TextView musicText;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            musicImage = itemView.findViewById(R.id.music_image);
            musicText = itemView.findViewById(R.id.music_title);
        }
    }
}
