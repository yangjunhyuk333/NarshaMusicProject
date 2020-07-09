package com.junhyuk.narshamusicproject.database.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UsrData {

    @PrimaryKey(autoGenerate = true)
    public int Id;

    private String usrName;

    public UsrData(String usrName) {
        this.usrName = usrName;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }
}
