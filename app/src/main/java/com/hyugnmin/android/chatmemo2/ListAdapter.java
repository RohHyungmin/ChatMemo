package com.hyugnmin.android.chatmemo2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyugnmin.android.chatmemo2.domain.Memo;

import java.util.List;

/**
 * Created by besto on 2017-02-15.
 */

public class ListAdapter  extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    Context context;
    List<Memo> datas;

    public ListAdapter(Context context, List<Memo> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Memo memo = datas.get(position);
        holder.textView.setText(memo.getMemo());
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.textView);
        }
    }
}
