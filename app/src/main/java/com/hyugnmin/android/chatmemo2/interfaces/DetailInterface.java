package com.hyugnmin.android.chatmemo2.interfaces;

/**
 * Created by besto on 2017-02-14.
 */



import com.hyugnmin.android.chatmemo2.domain.Memo;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by pc on 2/14/2017.
 */

public interface DetailInterface {
    public void backToList();
    public void saveToList(Memo memo) throws SQLException;
    public void resetData() throws SQLException;
    public void saveToRead(List<Memo> datas) throws  SQLException;
}