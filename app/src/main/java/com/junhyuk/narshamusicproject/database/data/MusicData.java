package com.junhyuk.narshamusicproject.database.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MusicData
{
    @PrimaryKey(autoGenerate = true)
    public int musicId;

    private String musicTitle;
    private int playBackTime;
    private String singer;
    private String link;

}
