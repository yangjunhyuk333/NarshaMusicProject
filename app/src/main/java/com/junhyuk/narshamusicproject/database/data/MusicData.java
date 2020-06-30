package com.junhyuk.narshamusicproject.database.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Until;

@Entity
public class MusicData
{
    @PrimaryKey(autoGenerate = true)
    public int musicId;

    private String musicTitle;
    private int playBackTime;
    private String singer;
    private String link;

    public MusicData(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    public String getMusicTitle() {
        return musicTitle;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    public int getPlayBackTime() {
        return playBackTime;
    }

    public void setPlayBackTime(int playBackTime) {
        this.playBackTime = playBackTime;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
