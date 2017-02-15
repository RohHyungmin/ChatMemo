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
    //리스트 각 행에서 사용되는 레이아웃 xml의 id

    Context context; //클릭처리, 애니매이션 등을 위해 시스템 자원 사용이 필요

    public CardAdapter2(List<MemoSub> datas2, Context context){
        this.datas2 = datas2;
        this.context = context;
    }


    //뷰를 생성해서 홀더에 저장하는 역할
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.read_item, parent, false);

        CustomViewHolder cvh = new CustomViewHolder(view);

        return cvh;
    }
    //listView 에서의 getView 함수를 대체
    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        final Memo memo = datas.get(position);
//        holder.textViewCard2.setText(memo.getMemo()+"");
        holder.textViewCard2.setText("메모메모메ㅗ메모ㅔ모ㅔ모ㅔ모메");
        holder.position = position;


        //카드 뷰 애니메이션
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
        holder.cardView2.setAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return datas2.size();
    }

    //Recycler 뷰에서 사용하는 뷰홀더 / 이 뷰 홀더를 사용하는 Adapter는 generic으로 선언된 부모객체를 상속받아 구현해야 한다.
    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCard2;
        CardView cardView2;
        int position;

        public CustomViewHolder(View itemView) {
            super(itemView);
            //생성자가 호출되는 순간, new 하는 순간 내부의 위젯을 생성해서 변수에 담아둔다.
            textViewCard2 = (TextView) itemView.findViewById(R.id.textViewCard2);
            cardView2 = (CardView) itemView.findViewById(R.id.cardView2);
            cardView2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });


        }
    }
}