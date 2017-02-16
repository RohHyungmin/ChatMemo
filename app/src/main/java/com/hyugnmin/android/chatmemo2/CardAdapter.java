package com.hyugnmin.android.chatmemo2;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyugnmin.android.chatmemo2.domain.Gallery;
import com.hyugnmin.android.chatmemo2.domain.Memo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by besto on 2017-02-15.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CustomViewHolder> {
   List<Memo> datas;
    Context context;
    Uri fileUri = null;
    List<Gallery> datasCamera = new ArrayList<>();

    public CardAdapter(List<Memo> datas, Context context){
        this.datas = datas;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);


        CustomViewHolder cvh = new CustomViewHolder(view);

        return cvh;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
       final Memo memo = datas.get(position);
        holder.textView.setText(memo.getMemo()+"");
        holder.position = position;

//        final Gallery gallery = datasCamera.get(position);
//        fileUri = gallery.getUri();
        Log.i("사진장착", "사진장착~~~~~~~~~~~~~~~~!!!!");
        Glide.with(context).load(fileUri).into(holder.imageViewWrite);

        //카드 뷰 애니메이션
        Animation animation = Animation
        Utils.loadAnimation(context, android.R.anim.slide_out_right);
        holder.cardView.setAnimation(animation);
    }

    @Override
    public int getItemCount() {
      return datas.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        CardView cardView;
        ImageView imageViewWrite;
        int position;

        public CustomViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            imageViewWrite = (ImageView) itemView.findViewById(R.id.imageViewWrite);

        }
    }

    public void getFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }
}