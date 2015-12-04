package com.example.muditi.omoyo;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;

/**
 * Created by muditi on 03-12-2015.
 */
public class shoppageadapter extends RecyclerView.Adapter<shoppageadapter.CustomViewHolder> {
Context context;
    String[] textdata;
    public shoppageadapter(Context context){
this.context=context;
        textdata=context.getResources().getStringArray(R.array.shoppagedatatext);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shopdatalayout, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view,i);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, int i) {
        viewHolder.linearlayout1.setMinimumWidth(Omoyo.screendisplay.getWidth() / 2);
        viewHolder.linearlayout2.setMinimumWidth(Omoyo.screendisplay.getWidth() / 2);
        switch(i){
            case 0:
                viewHolder.textview1.setText(textdata[0]);
                viewHolder.textview2.setText(textdata[1]);
                break;
            case 1:
                viewHolder.textview1.setText(textdata[2]);
                viewHolder.textview2.setText(textdata[3]);
                break;
            case 2:
                viewHolder.textview1.setText(textdata[4]);
                viewHolder.textview2.setText(textdata[5]);
                break;
            case 3:
               viewHolder.linearlayout2.setVisibility(View.GONE);
                viewHolder.linearlayout1.setMinimumWidth(Omoyo.screendisplay.getWidth());
                viewHolder.linearlayout1.setMinimumHeight(Omoyo.screendisplay.getHeight() / 5);
                viewHolder.textview1.setText(context.getResources().getString(R.string.shoppagedetalofsomeitem));
                viewHolder.textview1.setTextSize(12);
                break;
            case 4:
                viewHolder.linearlayout2.setVisibility(View.GONE);
                viewHolder.linearlayout1.setMinimumWidth(Omoyo.screendisplay.getWidth());
                viewHolder.linearlayout1.setMinimumHeight(Omoyo.screendisplay.getHeight() / 10);
                viewHolder.textview1.setText(context.getResources().getString(R.string.shoppagedetalofsomeitem));
                viewHolder.textview1.setTextSize(12);
                break;
            case 5:
                viewHolder.linearlayout2.setVisibility(View.GONE);
                viewHolder.linearlayout1.setMinimumWidth(Omoyo.screendisplay.getWidth());
                viewHolder.linearlayout1.setMinimumHeight(Omoyo.screendisplay.getHeight() / 5);
                viewHolder.textview1.setText(context.getResources().getString(R.string.shoppagedetalofsomeitem));
                viewHolder.textview1.setTextSize(12);
                break;
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
       protected LinearLayout linearlayout2;
        protected  LinearLayout linearlayout1;
        protected TextView textview1,textview2;
        protected ImageView imageview1,imageview2;
       int position;
        public CustomViewHolder(View view,int position) {
            super(view);
            this.position=position;
            this.linearlayout1=ButterKnife.findById(view,R.id.linearlayoutshoppage1);
            this.linearlayout2=ButterKnife.findById(view,R.id.linearlayoutshoppage2);
            this.textview1=ButterKnife.findById(view,R.id.textshoppage1);
            this.textview2=ButterKnife.findById(view,R.id.textshoppage2);
            this.imageview1=ButterKnife.findById(view,R.id.iconshoppage1);
            this.imageview2=ButterKnife.findById(view,R.id.iconshoppage2);


        }
    }
}
