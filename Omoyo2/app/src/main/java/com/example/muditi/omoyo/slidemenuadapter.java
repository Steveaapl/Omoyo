package com.example.muditi.omoyo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import butterknife.ButterKnife;

/**
 * Created by muditi on 02-12-2015.
 */
public class slidemenuadapter extends BaseAdapter {
    Context context;
    String[] slidemenulistitemname;
    public slidemenuadapter(Context context){
        this.context=context;
        slidemenulistitemname=context.getResources().getStringArray(R.array.slidemenulistitemname);
    }
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return slidemenulistitemname.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflate=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflate.inflate(R.layout.slidemenulistlayout,null);
        TextView itemname= ButterKnife.findById(view,R.id.itemname);
        itemname.setText(slidemenulistitemname[position]);
        return view;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
