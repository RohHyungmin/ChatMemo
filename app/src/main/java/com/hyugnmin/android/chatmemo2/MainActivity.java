package com.hyugnmin.android.chatmemo2;

import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hyugnmin.android.chatmemo2.data.DBHelper;
import com.hyugnmin.android.chatmemo2.data.DBHelper2;
import com.hyugnmin.android.chatmemo2.domain.Memo;
import com.hyugnmin.android.chatmemo2.domain.MemoSub;
import com.hyugnmin.android.chatmemo2.interfaces.DetailInterface;
import com.hyugnmin.android.chatmemo2.interfaces.ListInterface;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements ListInterface, DetailInterface {

    TabLayout tab;
    ViewPager viewPager;
    final int TAB_COUNT = 2;
    FragmentWrite writeFrag;
    private int page_position = 0;
    boolean backpress = false;
    Stack<Integer> pageStack = new Stack<>();


    FragmentManager manager;
    DBHelper dbHelper;
    DBHelper2 dbHelper2;
    List<Memo> datas = new ArrayList<>();
    List<MemoSub> datas2 = new ArrayList<>();
    Dao<Memo, Integer> memoDao;
    Dao<Memo, Integer> memoSubDao;
    FragmentRead readFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readFrag = new FragmentRead();
        writeFrag = new FragmentWrite();

        tab = (TabLayout) findViewById(R.id.tab);
        tab.addTab(tab.newTab().setText("Write"));
        tab.addTab(tab.newTab().setText("Read"));

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(!backpress) {
                    pageStack.push(page_position);
                }else {
                    page_position = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tab.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }


    class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;

            switch (position){
                case 0 : fragment = writeFrag; break;
                case 1 : fragment = readFrag; break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }

    @Override
    public void onBackPressed() {
        switch (page_position) {
            default:
                goBackStack();
                break;
        }
    }

    private void goBackStack() {
        if(pageStack.size() < 1) {
            super.onBackPressed();
        } else {
            backpress = true;
            viewPager.setCurrentItem(pageStack.pop());
        }
    }

    public void loadData() throws SQLException {
        dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        memoDao = dbHelper.getMemoDao();
        datas = memoDao.queryForAll();
    }


    @Override
    public void resetData() throws SQLException {
        SQLiteDatabase db =  dbHelper.getWritableDatabase();
        db.execSQL("delete from " + "memo");
        loadData();
        writeFrag.setData(datas);
        writeFrag.refreshCardAdapter();
    }

    @Override
    public void saveToRead(List<Memo> datas) throws SQLException{
        loadData();
        dbHelper2 = OpenHelperManager.getHelper(this, DBHelper2.class);
        memoSubDao = dbHelper2.getMemoSubDao();
        for(Memo item : datas) {
            memoSubDao.create(item);
        }



    }

    @Override
    public void backToList() {
        super.onBackPressed();
    }

    @Override
    public void saveToList(Memo memo) throws SQLException {
        dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        memoDao = dbHelper.getMemoDao();
        memoDao.create(memo);
        loadData();
        writeFrag.setData(datas);
        writeFrag.refreshCardAdapter();
    }

    @Override
    public void goDetail() {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.activity_main, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void goDetail(int position) {

    }
}
