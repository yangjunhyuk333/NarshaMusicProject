package com.junhyuk.narshamusicproject.database.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CMD_Data {
    @PrimaryKey(autoGenerate = true)
    public int idx;

    private int cmd_code;
    private String cmd_name;
}
