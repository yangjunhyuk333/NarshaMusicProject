package com.junhyuk.narshamusicproject.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public List<String> getsonglist(Context context) {

        List<String> songlist = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //Uri uri = MediaStore.Files.getContentUri("external");
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA

        };
        Cursor cursor = context.getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%utm%"}, null);

        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            do {

                String fullPath = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                // ...process entry...
                Log.e("curso", "name : " + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                Log.e("curso", "ALBUM : " + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                Log.e("curso", "ARTIST : " + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                Log.e("curso", "DURATION : " + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));

                Log.e("curso", "fullPath : " + fullPath);

                songlist.add(fullPath);
                Uri contentUri = Uri.withAppendedPath(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)).toString()
                );
                //contentResolver.delete(contentUri, null, null);
            } while (cursor.moveToNext());
        }


        return songlist;
    }

    public static void mediaStoreSaveFile(Context context, Uri uri, String fileName) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "audio/*");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.IS_PENDING, 1);
        }

        ContentResolver contentResolver = context.getContentResolver();
        Uri item = contentResolver.insert(uri, values);

    }

    public static int getCount(Context context) {
        Uri externalUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{

        };
        Cursor cursor = context.getContentResolver().query(externalUri, projection, null, null, null);
        Log.d("MainB", "cnt : "+cursor.getCount());
        return cursor.getCount();
    }

    public static void getMusicData(Context context, ArrayList<Uri> musicList, ArrayList<String> musicTitle, ArrayList<String> musicDuration, ArrayList<String> musicArtist, ArrayList<String> musicAlbum) {

        Uri externalUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };

        Cursor cursor = context.getContentResolver().query(externalUri, projection, null, null, null);

        Log.d("MainA", "cnt : "+cursor.getCount());
        if (cursor == null || !cursor.moveToFirst()) {
            return;
        }
        do {
            String fullPath = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
            Uri contenturi = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)));

            Log.d("MainA", "uri : "+fullPath);
            Log.d("MainA", "uri : "+contenturi);

            fullPath = "content://com.alphainventor.filemanager.fileprovider/root"+fullPath;

            musicList.add(contenturi);
            musicTitle.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
            musicDuration.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            musicArtist.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            musicAlbum.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
        } while (cursor.moveToNext());
    }
}
