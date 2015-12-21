package com.example.muditi.omoyo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;


public class shoplist extends ActionBarActivity {
@Bind(R.id.toolbar)
 Toolbar toolbar;
    @Bind(R.id.linearlayoutadder)
    LinearLayout adderlayout;
    JSONArray jsonarray;
    JSONObject jsonobject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoplist);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getResources().getString(R.string.categoryofshop));
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setSubtitle(getResources().getString(R.string.subtitle));
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        try
        {
            jsonarray = new JSONArray(Omoyo.shared.getString("shoplist","f"));
            for(int i=0;i<jsonarray.length();i++) {
                    jsonobject=jsonarray.getJSONObject(i);
                    String url="http://192.168.0.113:15437/bitmap/shop/shop.jpg";
                    LayoutInflater inflate = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                    View view=inflate.inflate(R.layout.shoppageviewadder, null);
                    LinearLayout linearLayout2 =    ButterKnife.findById(view, R.id.linearlayoutasadder);
                    final  RelativeLayout relativeLayout=  ButterKnife.findById(view, R.id.relativelayoutgridlayout);
                    Glide.with(getApplicationContext()).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                            relativeLayout.setBackgroundDrawable(new BitmapDrawable(
                                    getResources(), resource));

                        }
                    });
                    TextView textshopname=ButterKnife.findById(view,R.id.categoryname);
                    textshopname.setText(jsonobject.getString("shop_name"));
                    TextView textshopitem=ButterKnife.findById(view, R.id.itemincategory);
                    StringBuilder stringBuilder=new StringBuilder();
                    JSONArray jsonArray1=jsonobject.getJSONArray("shop_item");
                    for(int k=0;k<jsonArray1.length();k++)
                       {
                         stringBuilder.append(jsonArray1.getJSONObject(k).getString("item")+"      ");
                       }
                     textshopitem.setText(stringBuilder.toString());
                    stringBuilder.delete(0, stringBuilder.length());
                    linearLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Omoyo.edit.putString("shop",jsonobject.toString());
                        Omoyo.edit.commit();
                        startActivity(new Intent(getApplicationContext(), shoppage.class));
                    }
                    });
                    LinearLayout adderlayout2 = (LinearLayout) findViewById(R.id.linearlayoutadder);
                    adderlayout2.addView(linearLayout2);
            }
        }
        catch(JSONException jsonexp){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shoplist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
