package com.junhyuk.narshamusicproject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.junhyuk.narshamusicproject.R;
import com.junhyuk.narshamusicproject.viewHolder.ViewHolder;

class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
