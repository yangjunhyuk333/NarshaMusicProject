package com.junhyuk.narshamusicproject.database.app_data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.junhyuk.narshamusicproject.database.dao.USR_DAO;
import com.junhyuk.narshamusicproject.database.data.UsrData;

@Database(entities = {UsrData.class}, version = 1)
public abstract class UsrDataBase extends RoomDatabase {

    private static UsrDataBase INSTANCE;

    public abstract USR_DAO usr_dao();

    public static UsrDataBase getUsrDataBase(Context context){
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, UsrDataBase.class, "Usr-data-db").build();
        }
        return INSTANCE;
    }

    public static void destroyINSTANCE(){
        INSTANCE = null;
    }
}
