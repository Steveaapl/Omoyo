package com.example.muditi.omoyo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by muditi on 06-12-2015.
 */
public class firstpagespinneradapter extends ArrayAdapter<String> {
Context context;
    int id;
    ArrayList<String> data;
    String wht;
    public firstpagespinneradapter(String what,Context context,int id,ArrayList<String> object){
        super(context,id,object);
        this.context=context;
        this.data=object;
        this.id=id;
        this.wht=what;
    }
    public View getvvv(int position ,View convertView,ViewGroup parent){
        LayoutInflater inflate=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflate.inflate(id,parent,false);
        LinearLayout linear=ButterKnife.findById(view, R.id.parentoflayout);
        linear.setBackgroundColor(context.getResources().getColor(R.color.white));
        if(position!=0){
            ImageView imageView= ButterKnife.findById(view,R.id.iconof);
            imageView.setVisibility(View.GONE);
            RelativeLayout relativeLayout=ButterKnife.findById(view,R.id.relativelayoutoffirstpage);
            relativeLayout.setVisibility(View.GONE);
            TextView textView=ButterKnife.findById(view,R.id.spinnertext);
            textView.setText(data.get(position));
        }
        else{
            TextView textView=ButterKnife.findById(view,R.id.spinnertext);
            //textView.setText("Select "+this.wht);
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
            textView.setText(data.get(position));
        if(position==0){
            textView.setTextSize((float)(0.1));
textView.setVisibility(View.INVISIBLE);
        }
        else{
            textView.setTextSize(25);
        }
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflate=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflate.inflate(id,parent,false);
        LinearLayout linear=ButterKnife.findById(view, R.id.parentoflayout);
        linear.setBackgroundResource(R.drawable.firstpagespinnerround);
        if(position!=0 )
        {
            TextView textView=ButterKnife.findById(view,R.id.spinnertext);
            textView.setText(data.get(position));
          //  Omoyo.toast(position+"if:" + Omoyo.spinnerfirstpagecheck, context);
        }
        else{

                    TextView textView = ButterKnife.findById(view, R.id.spinnertext);
                    textView.setText("Select Your " + this.wht);

          //  Omoyo.toast(position+"else:"+Omoyo.spinnerfirstpagecheck,context);

        }
        return view;
    }
}
