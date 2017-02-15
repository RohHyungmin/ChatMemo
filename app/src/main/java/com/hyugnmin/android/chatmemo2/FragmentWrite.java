package com.hyugnmin.android.chatmemo2;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hyugnmin.android.chatmemo2.data.DBHelper;
import com.hyugnmin.android.chatmemo2.domain.Memo;
import com.hyugnmin.android.chatmemo2.domain.MemoSub;
import com.hyugnmin.android.chatmemo2.interfaces.DetailInterface;
import com.hyugnmin.android.chatmemo2.interfaces.ListInterface;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentWrite extends Fragment implements View.OnClickListener {

    Button btnInput, btnDone, btnCamera;
    EditText editTextInput;
    View view = null;
    RecyclerView recyclerViewMain;
    RecyclerViewMainAdapter recyclerViewMainAdapter;

    Context context = null;
    DetailInterface detailInterface = null;
    List<Memo> datas;
    CardAdapter cardAdapter;



    public FragmentWrite() {
    }

    public static FragmentWrite newInstance() {
        FragmentWrite fragment = new FragmentWrite();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_fragment_write, container, false);

            btnInput = (Button) view.findViewById(R.id.btnInput);
            btnCamera = (Button) view.findViewById(R.id.btnCamera);
            btnDone = (Button) view.findViewById(R.id.btnDone);
            editTextInput = (EditText) view.findViewById(R.id.editTextInput);
            recyclerViewMain = (RecyclerView) view.findViewById(R.id.recyclerViewMain);

            List<Memo> datas = new ArrayList<>();

            CardAdapter cardAdapter = new CardAdapter(datas, getContext());
            recyclerViewMain.setAdapter(cardAdapter);
            recyclerViewMain.setLayoutManager(new LinearLayoutManager(getContext()));



            btnDone.setOnClickListener(this);
            btnInput.setOnClickListener(this);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.detailInterface = (DetailInterface) context;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.btnInput:
                try {
                    Memo memo = new Memo();
                    String string = editTextInput.getText().toString();
                    memo.setMemo(string);
                    memo.setDate(new Date(System.currentTimeMillis()));

                    detailInterface.saveToList(memo);
                }catch (SQLException e){
                    e.printStackTrace();
                }
                editTextInput.setText("");
                break;

            case R.id.btnDone:
                try {
                    detailInterface.resetData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;

            case R.id.btnCamera :
                break;

        }
    }

    public void setData(List<Memo> datas){
        this.datas = datas;
    }

    public void refreshCardAdapter() {
        cardAdapter = new CardAdapter(datas, context);
        recyclerViewMain.setAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();
    }


}
