package com.hyugnmin.android.chatmemo2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyugnmin.android.chatmemo2.domain.Memo;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewMainAdapter extends RecyclerView.Adapter<RecyclerViewMainAdapter.CustomViewHolder> {
    List<Memo> datas;
    //리스트 각 행에서 사용되는 레이아웃 xml의 id
    int itemLayout;

    public RecyclerViewMainAdapter(List<Memo> datas, int itemLayout){
        this.datas = datas;
        this.itemLayout = itemLayout;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        //context.getSystemService...와 같은 문장

        CustomViewHolder cvh = new CustomViewHolder(view);

        return cvh;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Memo memo = datas.get(position);
        holder.textView2.setText("");
        holder.position = position;

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView textView2;
        int position;

        public CustomViewHolder(View itemView) {
            super(itemView);
            textView2 = (TextView) itemView.findViewById(R.id.textView2);

        }
    }
}
