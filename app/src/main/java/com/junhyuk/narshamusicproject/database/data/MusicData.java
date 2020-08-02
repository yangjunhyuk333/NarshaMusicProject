package com.junhyuk.narshamusicproject.database.data;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Until;

@Entity
public class MusicData
{
    @PrimaryKey(autoGenerate = true)
    public int musicId;

    private String musicTitle;
    private String playBackTime;
    private String singer;
    private Uri link;

    public MusicData(String musicTitle, String playBackTime, String singer, Uri link) {
        this.musicTitle = musicTitle;
        this.playBackTime = playBackTime;
        this.singer = singer;
        this.link = link;
    }

    public String getMusicTitle() {
        return musicTitle;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    public String getPlayBackTime() {
        return playBackTime;
    }

    public void setPlayBackTime(String playBackTime) {
        this.playBackTime = playBackTime;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public Uri getLink() {
        return link;
    }

    public void setLink(Uri link) {
        this.link = link;
    }

}
