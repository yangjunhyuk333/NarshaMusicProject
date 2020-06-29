package com.junhyuk.narshamusicproject.database.app_data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.junhyuk.narshamusicproject.database.dao.CMD_DAO;
import com.junhyuk.narshamusicproject.database.data.CMD_Data;

@Database(entities = {CMD_Data.class}, version = 1)
public abstract class CMD_Database extends RoomDatabase {

    private static CMD_Database INSTANCE;

    public abstract CMD_DAO cmd_dao();

    public static CMD_Database getCMD_Database(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, CMD_Database.class, "CMD-javava-db").build();
        }
        return INSTANCE;
    }

    public static void destroyINSTANCE(){
        INSTANCE = null;
    }

}
