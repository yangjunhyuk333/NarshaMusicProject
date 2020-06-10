package com.junhyuk.narshamusicproject.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.junhyuk.narshamusicproject.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    ImageView musicImage;
    TextView musicText;

    public ViewHolder(@NonNull final View itemView) {
        super(itemView);

        musicImage = itemView.findViewById(R.id.music_image);
        musicText = itemView.findViewById(R.id.music_title);
    }
}
