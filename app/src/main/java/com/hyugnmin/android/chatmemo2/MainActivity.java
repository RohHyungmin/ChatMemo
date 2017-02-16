package com.hyugnmin.android.chatmemo2;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
import java.util.Date;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements ListInterface, DetailInterface {

    TabLayout tab;
    ViewPager viewPager;
    final int TAB_COUNT = 2;
    FragmentWrite writeFrag;
    int page_position = 0;
    boolean backpress = false;
    Stack<Integer> pageStack = new Stack<>();


    FragmentManager manager;
    DBHelper dbHelper;
    DBHelper2 dbHelper2;
    List<Memo> datas = new ArrayList<>();
    List<MemoSub> datas2 = new ArrayList<>();
    Dao<Memo, Integer> memoDao;
    Dao<MemoSub, Integer> memoSubDao;
    FragmentRead readFrag;

    public static final int REQ_DELETE = 1;
    public static final int REQ_UPDATE = 2;
    private final int REQ_PERMISSION = 100; // 권한요청코드

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
    public void loadData2() throws SQLException {
        dbHelper2 = new DBHelper2(this);
        memoSubDao = dbHelper2.getMemoSubDao();
        datas2 = memoSubDao.queryForAll();
        dbHelper2.close();
    }

    @Override
    public void refreshData() throws SQLException {
        Log.i("메인리프레쉬~~~~~~", "메인리프레쉬`~~~~~~~~~~~~~~~~~~~");
        loadData2();
        readFrag.setData(datas2);
        readFrag.refreshCardAdapter2();
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
    public void saveToRead() throws SQLException{
        loadData();
        dbHelper2 = new DBHelper2(this);
        memoSubDao = dbHelper2.getMemoSubDao();
        String memoToMemoSub = "";
        for(Memo item : datas) {
            memoToMemoSub =  memoToMemoSub + item.getMemo() + " " + "\r\n";
        }
        MemoSub memoSub = new MemoSub();
        memoSub.setMemoSub(memoToMemoSub);
        memoSub.setDate(new Date(System.currentTimeMillis()));
        memoSubDao.create(memoSub);
        dbHelper2.close();

        refreshData();
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


    //1. 권한 체크
    @TargetApi(Build.VERSION_CODES.M) //target 지정 annotation
    private void checkPermission() {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            if( PermissionControl.checkPermission(this, REQ_PERMISSION) ){
//                init();
            }
        }else{
//            init();
        }
    }

    //2. 권한 체크 후 call back < 사용자가 확인 후 시스템이 호출하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQ_PERMISSION){
            if( PermissionControl.onCheckResult(grantResults)){
//                init();
            }else{
                Toast.makeText(this, "권한을 허용하지 않으시면 프로그램을 실행할 수 없습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }

}
