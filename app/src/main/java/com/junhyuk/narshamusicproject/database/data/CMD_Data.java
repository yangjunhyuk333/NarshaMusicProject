package com.junhyuk.narshamusicproject.database.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CMD_Data {
    @PrimaryKey(autoGenerate = true)
    public int idx;

    private int cmd_code;
    private String cmd_name;

    public int getCmd_code() {
        return cmd_code;
    }

    public void setCmd_code(int cmd_code) {
        this.cmd_code = cmd_code;
    }

    public String getCmd_name() {
        return cmd_name;
    }

    public void setCmd_name(String cmd_name) {
        this.cmd_name = cmd_name;
    }
}
