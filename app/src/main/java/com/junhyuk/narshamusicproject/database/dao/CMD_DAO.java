package com.junhyuk.narshamusicproject.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.junhyuk.narshamusicproject.database.data.CMD_Data;

import java.util.List;

@Dao
public interface CMD_DAO {

    @Query("SELECT * FROM 'CMD_Data'")
    LiveData<List<CMD_Data>> getAll();

    @Query("SELECT cmd_name FROM 'cmd_data'")
    LiveData<List<String>> getCmd_name();

}
