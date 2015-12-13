package com.example.muditi.omoyo;

import android.content.Context;
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
            jsonarray = new JSONArray(Omoyo.shared.getString("ads","f"));
            for(int i=0;i<jsonarray.length();i++){
                jsonobject=jsonarray.getJSONObject(i);
                jsonarray =jsonobject.getJSONArray("url");
                for(int k=0;k<jsonarray.length();k++){
                    jsonobject=jsonarray.getJSONObject(k);
                    String url=jsonobject.getString("url");
                    Glide.with(getApplicationContext()).load(url).asBitmap().into(new SimpleTarget<Bitmap>(){
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation){
                            LayoutInflater inflate = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                            View view=inflate.inflate(R.layout.shoppageviewadder, null);
                            LinearLayout linearLayout2 =    ButterKnife.findById(view, R.id.linearlayoutasadder);
                            RelativeLayout relativeLayout=  ButterKnife.findById(view,R.id.relativelayoutgridlayout);
                            relativeLayout.setBackgroundDrawable(new BitmapDrawable(
                                    getResources(),resource));
                            LinearLayout adderlayout2 = (LinearLayout) findViewById(R.id.linearlayoutadder);
                            adderlayout2.addView(linearLayout2);
                        }
                    });
                }
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
