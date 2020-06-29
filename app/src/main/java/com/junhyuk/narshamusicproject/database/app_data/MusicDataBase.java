package com.junhyuk.narshamusicproject.database.app_data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.junhyuk.narshamusicproject.database.dao.MUSIC_DAO;
import com.junhyuk.narshamusicproject.database.data.MusicData;

@Database(entities = {MusicData.class}, version = 1)
public abstract class MusicDataBase extends RoomDatabase {

    private static MusicDataBase INSTANCE;

    public abstract MUSIC_DAO music_dao();

    public static MusicDataBase getMusicDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, MusicDataBase.class, "music-javava-db").build();
        }
        return INSTANCE;
    }

    public static void destroyINSTANCE(){
        INSTANCE = null;
    }

}
