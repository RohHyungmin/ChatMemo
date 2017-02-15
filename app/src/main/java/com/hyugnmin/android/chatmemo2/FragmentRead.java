package com.hyugnmin.android.chatmemo2;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hyugnmin.android.chatmemo2.domain.Memo;
import com.hyugnmin.android.chatmemo2.domain.MemoSub;
import com.hyugnmin.android.chatmemo2.interfaces.ListInterface;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRead extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private List<MemoSub> datas2;


    Context context = null;
    ListInterface listInterface = null;

    View view = null;
    RecyclerView recyclerView;
    CardAdapter2 cardAdapter2;



    public FragmentRead() {
        // Required empty public constructor
    }

    public static FragmentRead newInstance(int columnCount) {
        FragmentRead fragment = new FragmentRead();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_fragment_read, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list2);

        List<MemoSub> datas2 = new ArrayList<>();


        cardAdapter2 = new CardAdapter2(datas2, context);
        recyclerView.setAdapter(cardAdapter2);


        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.listInterface = (ListInterface) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setData(List<MemoSub> datas2){
        this.datas2 = datas2;
    }

    public void refreshCardAdapter() {
        cardAdapter2 = new CardAdapter2 (datas2, context);
        recyclerView.setAdapter(cardAdapter2);
        cardAdapter2.notifyDataSetChanged();
    }


}
