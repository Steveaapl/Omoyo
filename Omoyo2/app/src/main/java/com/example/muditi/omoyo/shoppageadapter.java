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

        CustomViewHolder viewHolder = new CustomViewHolder(view,i);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

int position;
        public CustomViewHolder(View view,int position) {
            super(view);
            this.position=position;
            LinearLayout linearlayout1=ButterKnife.findById(view,R.id.linearlayoutshoppage1);
            LinearLayout linearlayout2=ButterKnife.findById(view,R.id.linearlayoutshoppage2);
            TextView textview1=ButterKnife.findById(view,R.id.textshoppage1);
            TextView textview2=ButterKnife.findById(view,R.id.textshoppage2);
            textview1.setMaxWidth(Omoyo.screendisplay.getWidth() * 2 / 5);
            textview2.setMaxWidth(Omoyo.screendisplay.getWidth() * 2 / 5);
            ImageView imageview1=ButterKnife.findById(view,R.id.iconshoppage1);
            ImageView imageview2=ButterKnife.findById(view,R.id.iconshoppage2);
            imageview1.setMaxWidth(Omoyo.screendisplay.getWidth() / 10);
            imageview2.setMaxWidth(Omoyo.screendisplay.getWidth() / 10);
            linearlayout1.setMinimumWidth(Omoyo.screendisplay.getWidth() / 2);
            linearlayout2.setMinimumWidth(Omoyo.screendisplay.getWidth()/2);
            switch(position){
                case 3:
                    linearlayout2.setVisibility(View.GONE);
                    break;
                case 4:
                    linearlayout2.setVisibility(View.GONE);
                    break;
                case 5:
                    linearlayout2.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
