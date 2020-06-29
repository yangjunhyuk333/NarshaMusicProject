package com.junhyuk.narshamusicproject.Adapter;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.junhyuk.narshamusicproject.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    int i = 10;

    Application application;

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
        readFile();
        viewHolder.musicText.setText("음악 이름");
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

    public void getApplication(Application application){
        this.application = application;
    }

    private void readFile(){
        Context context = application;
        Uri externalUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.MIME_TYPE
        };

        Cursor cursor = context.getContentResolver().query(externalUri, projection, null, null, null);

        if(cursor == null || !cursor.moveToFirst()){
            Log.e("TAG", "cursor null or cursor is empty");
            return;
        }

        do {
            String contentUrl = externalUri.toString() + "/" + cursor.getString(0);

            try {
                InputStream is = context.getContentResolver().openInputStream(Uri.parse(contentUrl));
                int data = 0;
                StringBuilder sb = new StringBuilder();

                while ((data = is.read()) != -1){
                    sb.append((char) data);
                }

                is.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }while (cursor.moveToNext());
    }
}
