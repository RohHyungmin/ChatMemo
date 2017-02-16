package com.hyugnmin.android.chatmemo2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hyugnmin.android.chatmemo2.domain.Memo;
import com.hyugnmin.android.chatmemo2.domain.MemoSub;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by besto on 2017-02-15.
 */

public class DBHelper2 extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME = "memoSub.db";
    public static final int DB_VERSION = 1;

    public DBHelper2(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, MemoSub.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, MemoSub.class, false);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DBHelper 를 싱글턴으로 사용하기 때문에 dao 객체도 열어놓고 사용가능하다
    private Dao<MemoSub, Integer> memoSubDao = null;
    public Dao<MemoSub, Integer> getMemoSubDao() throws SQLException {
        if(memoSubDao == null){
            memoSubDao = getDao(MemoSub.class);
        }
        return memoSubDao;
    }
    public void releaseMemoDao() {
        if(memoSubDao != null){
            memoSubDao= null;
        }
    }


}