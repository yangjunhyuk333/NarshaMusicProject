package com.junhyuk.narshamusicproject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.junhyuk.narshamusicproject.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    int i = 3;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.musicImage.setImageResource(R.drawable.call_api);
        viewHolder.musicText.setText("음악이름입니다!하하하");
    }

    @Override
    public int getItemCount() {
        return i;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView musicImage;
        public TextView musicText;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            musicImage = itemView.findViewById(R.id.music_image);
            musicText = itemView.findViewById(R.id.music_title);
        }
    }
}
