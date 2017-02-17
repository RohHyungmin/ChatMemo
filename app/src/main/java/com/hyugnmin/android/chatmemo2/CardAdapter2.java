package com.hyugnmin.android.chatmemo2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.hyugnmin.android.chatmemo2.domain.Memo;
import com.hyugnmin.android.chatmemo2.domain.MemoSub;

import java.util.List;

/**
 * Created by besto on 2017-02-15.
 */

public class CardAdapter2 extends RecyclerView.Adapter<CardAdapter2.CustomViewHolder> {
    List<MemoSub> datas2;
    List<Memo> datas;


    Context context;

    public CardAdapter2(List<MemoSub> datas2, Context context){

        this.datas2 = datas2;
        this.context = context;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.read_item, parent, false);

        CustomViewHolder cvh = new CustomViewHolder(view);

        return cvh;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        final MemoSub memoSub = datas2.get(position);
        holder.textViewCard2.setText(memoSub.getMemoSub()+"");
        holder.textViewID.setText(memoSub.getId()+"");
        holder.textViewDATE.setText(memoSub.getDate()+"");

        holder.positionID = memoSub.getId();

        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
        holder.cardView2.setAnimation(animation);
    }

    @Override
    public int getItemCount() {

        return datas2.size();

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCard2, textViewID, textViewDATE;
        CardView cardView2;

        int positionID;

        public CustomViewHolder(View itemView) {
            super(itemView);
            Log.i("여기까지된다", "여기까지된다~~~~~~~~~~~~~");
            textViewCard2 = (TextView) itemView.findViewById(R.id.textViewCard2);
            textViewID = (TextView) itemView.findViewById(R.id.textViewID);
            textViewDATE = (TextView) itemView.findViewById(R.id.textViewDATE);

            cardView2 = (CardView) itemView.findViewById(R.id.cardView2);
            cardView2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("position", positionID);
                    context.startActivity(intent);
                }
            });

        }
    }
}