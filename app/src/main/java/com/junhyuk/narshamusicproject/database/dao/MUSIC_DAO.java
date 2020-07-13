package com.junhyuk.narshamusicproject.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.junhyuk.narshamusicproject.database.data.MusicData;

import java.util.List;

@Dao
public interface MUSIC_DAO {

    @Query("SELECT * FROM MusicData")
    LiveData<List<MusicData>> getAll();

    @Query("SELECT musicId FROM 'MusicData'")
    LiveData<List<Integer>> getMusicId();

    @Query("SELECT musicTitle FROM 'MusicData'")
    LiveData<List<String>> getTitle();

    @Query("SELECT playBackTime FROM 'MusicData'")
    LiveData<List<String>> getPlayTime();

    @Query("SELECT singer FROM 'MusicData'")
    LiveData<List<String>> getSinger();

    @Query("SELECT link FROM 'MusicData'")
    LiveData<List<String>> getLink();

    @Insert
    void insert(MusicData musicData);

    @Delete
    void delete(MusicData musicData);



}
