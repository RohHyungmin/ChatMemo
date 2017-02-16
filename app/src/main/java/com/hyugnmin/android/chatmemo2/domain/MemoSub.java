package com.hyugnmin.android.chatmemo2.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by besto on 2017-02-15.
 */

@DatabaseTable(tableName = "memoSub")
public class MemoSub {
    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    Date date;

    @DatabaseField
    String memoSub;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setMemoSub(String content) {
        this.memoSub = content;
    }

    public String  getMemoSub() {
     return memoSub;
    }

    public MemoSub() {

    }

    public  MemoSub (Date date) {
        this.date = date;
    }
}
