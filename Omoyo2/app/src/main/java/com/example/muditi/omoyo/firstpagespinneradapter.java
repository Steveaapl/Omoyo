package com.example.muditi.omoyo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import butterknife.ButterKnife;

/**
 * Created by muditi on 06-12-2015.
 */
public class firstpagespinneradapter extends ArrayAdapter<String> {
Context context;
    int id;
    String[] data;
    String wht;
    public firstpagespinneradapter(String what,Context context,int id,String[] object){
        super(context,id,object);
        this.context=context;
        this.data=object;
        this.id=id;
        this.wht=what;
    }
    public View getvvv(int position ,View convertView,ViewGroup parent){
        LayoutInflater inflate=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflate.inflate(id,parent,false);
        if(position!=0){
            ImageView imageView= ButterKnife.findById(view,R.id.iconof);
            imageView.setVisibility(View.GONE);
            RelativeLayout relativeLayout=ButterKnife.findById(view,R.id.relativelayoutoffirstpage);
            relativeLayout.setVisibility(View.GONE);
            TextView textView=ButterKnife.findById(view,R.id.spinnertext);
            textView.setText(data[position]);
        }
        else{
            TextView textView=ButterKnife.findById(view,R.id.spinnertext);
            textView.setText("Select "+this.wht);
        }
        return view;
    }



    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflate=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflate.inflate(id, parent, false);
            ImageView imageView= ButterKnife.findById(view,R.id.iconof);
            imageView.setVisibility(View.GONE);
            RelativeLayout relativeLayout=ButterKnife.findById(view,R.id.relativelayoutoffirstpage);
            relativeLayout.setVisibility(View.GONE);
            TextView textView=ButterKnife.findById(view,R.id.spinnertext);
            textView.setText(data[position]);
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflate=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflate.inflate(id,parent,false);
        if(position!=0){
            TextView textView=ButterKnife.findById(view,R.id.spinnertext);
            textView.setText(data[position]);
        }

        else{
            if(Omoyo.spinnerfirstpagecheck==0) {
                TextView textView = ButterKnife.findById(view, R.id.spinnertext);
                textView.setText("Select " + this.wht);
            }
            else{
                TextView textView = ButterKnife.findById(view, R.id.spinnertext);
                textView.setText(data[1]);
            }
        }
        return view;
    }
}
