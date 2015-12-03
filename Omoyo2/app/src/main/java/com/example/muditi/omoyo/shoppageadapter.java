package com.example.muditi.omoyo;

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
public class shoppageadapter extends RecyclerView.Adapter {

    public shoppageadapter(){

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shopdatalayout, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {


        public CustomViewHolder(View view) {
            super(view);
            LinearLayout linearlayout1=ButterKnife.findById(view,R.id.linearlayoutshoppage1);
            LinearLayout linearlayout2=ButterKnife.findById(view,R.id.linearlayoutshoppage2);
            linearlayout1.setMinimumWidth(Omoyo.screendisplay.getWidth()/2);
            linearlayout2.setMinimumWidth(Omoyo.screendisplay.getWidth()/2);
        }
    }
}
