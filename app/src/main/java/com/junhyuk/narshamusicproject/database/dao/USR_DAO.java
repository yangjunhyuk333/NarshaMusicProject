package com.junhyuk.narshamusicproject.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.junhyuk.narshamusicproject.database.data.UsrData;

import java.util.List;

@Dao
public interface USR_DAO {

    @Query("SELECT * FROM UsrData")
    LiveData<List<UsrData>> getAll();

    @Query("SELECT usrName FROM 'UsrData'")
    LiveData<List<String>> getName();

    @Insert
    void insert(UsrData usrData);

}
