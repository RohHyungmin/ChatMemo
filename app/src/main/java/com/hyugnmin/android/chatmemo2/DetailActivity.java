package com.hyugnmin.android.chatmemo2;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyugnmin.android.chatmemo2.data.DBHelper2;
import com.hyugnmin.android.chatmemo2.domain.Memo;
import com.hyugnmin.android.chatmemo2.domain.MemoSub;
import com.hyugnmin.android.chatmemo2.interfaces.DetailInterface;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends MainActivity implements View.OnClickListener{

    Button btnDelete, btnUpdate, btnUpdateDone;
    TextView textViewDetail;
    ImageView imageView;
    Dao<MemoSub, Integer> memoSubDao;
    List<MemoSub> datas2 = new ArrayList<>();
    List<Memo> datas = new ArrayList<>();
    DBHelper2 dbHelper2;
    int positionID;
    EditText updateText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        btnDelete = (Button)findViewById(R.id.btnDelete);
        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        btnUpdateDone= (Button) findViewById(R.id.btnUdateDone);
        textViewDetail = (TextView)findViewById(R.id.textViewDetail);
        imageView = (ImageView)findViewById(R.id.imageView);
        updateText = (EditText) findViewById(R.id.updateText);


        updateText.setVisibility(View.GONE);

        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnUpdateDone.setOnClickListener(this);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        positionID = bundle.getInt("position");

        try {
            getData(positionID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getData(int positionID) throws SQLException{
        dbHelper2 = new DBHelper2(this);
        memoSubDao = dbHelper2.getMemoSubDao();
        MemoSub memoSub = memoSubDao.queryForId(positionID);
        String text = memoSub.getMemoSub();
        textViewDetail.setText(text);
        dbHelper2.close();
    }

    public void deleteData (int positionID) throws SQLException {
        dbHelper2 = new DBHelper2(this);
        memoSubDao = dbHelper2.getMemoSubDao();
        memoSubDao.deleteById(positionID);
        dbHelper2.close();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch(v.getId()) {
            case R.id.btnDelete :

                try {
                    deleteData(positionID);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivityForResult(intent, REQ_DELETE);

                break;

            case R.id.btnUpdate :
                updateText.setVisibility(View.VISIBLE);
                textViewDetail.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.GONE);
                btnUpdateDone.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.GONE);
                break;

            case R.id.btnUdateDone :


                try {
                    updateData(positionID);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, REQ_UPDATE);
                break;
        }
    }


    public void updateData(int position) throws SQLException {

        dbHelper2 = new DBHelper2(this);
        memoSubDao = dbHelper2.getMemoSubDao();
        MemoSub memoSub = memoSubDao.queryForId(position);
        Log.i("업뎃", "업뎃!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11");
//        updateText.setText(memoSub.getMemoSub());

        String temp = updateText.getText().toString();
        memoSub.setMemoSub(temp);
        memoSubDao.update(memoSub);
        dbHelper2.close();
    }



}
