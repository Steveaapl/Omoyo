package com.example.muditi.omoyo;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;



public class shoppage extends ActionBarActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsingtoolbar)
    CollapsingToolbarLayout collapsingtoolbar;
    @Bind(R.id.recycleview)
    RecyclerView recyclerView;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.float_for_way)
    FloatingActionButton float_for_way;
    @Bind(R.id.float_for_faverotes)
    FloatingActionButton float_for_faverotes;
    SearchView searchView;
    private boolean flager = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppage);
        ButterKnife.bind(this);


        try{
            JSONArray jsonArray = new JSONArray(Omoyo.shared.getString("favorets",""));
            for(int i=0 ; i<jsonArray.length() ;i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("type_of").equals("2")){
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    if(jsonObject1.getString("shop_id").equals(Omoyo.currentShopId)){
                        float_for_faverotes.setImageDrawable(getResources().getDrawable(R.mipmap.ic_favorite_black_48dp));
                        flager= false;
                    }
                }
            }
        }
        catch(JSONException jx){
           Log.d("TAGERROR",jx.getMessage());
        }


        showTheJam();

        if(!Omoyo.shared.getBoolean("user_status",false)) {
            Dialog_For_Shop_Page dialog_for_shop_page = new Dialog_For_Shop_Page();
            Bundle bundle = new Bundle();
            bundle.putInt("type_of", 7);
            dialog_for_shop_page.setArguments(bundle);
            dialog_for_shop_page.show(getSupportFragmentManager(),"h");
        }


        if(Omoyo.shared.getBoolean("user_status",false)) {
           showTheJam();
        }

       if(flager)
        float_for_faverotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   float_for_faverotes.setImageDrawable(getResources().getDrawable(R.mipmap.ic_favorite_black_48dp));
                try {
                    Omoyo.addtofavorets(2, new JSONObject(Omoyo.shared.getString("shop", "shop")));
                }
                catch(JSONException ex){

                }
                Log.d("swx", Omoyo.shared.getString("shop", "shop"));
            }
        });

        float_for_way.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showTheJam();
            }
        });

        toolbar.showOverflowMenu();
        try {
            collapsingtoolbar.setTitle(new JSONObject(Omoyo.shared.getString("shop", "shop")).getString("shop_name"));
        }
        catch(JSONException e)
        {

        }
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(getResources().getString(R.string.status));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        collapsingtoolbar.setCollapsedTitleTextColor(Color.WHITE);
        collapsingtoolbar.setExpandedTitleColor(Color.WHITE);
        collapsingtoolbar.setExpandedTitleTextAppearance(R.style.collapsebartitleexpanding);
        collapsingtoolbar.setCollapsedTitleTextAppearance(R.style.collapsebartitlecollapsing);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new Shop_Page_Adapter_Class(getApplicationContext(), getFragmentManager() , getSupportFragmentManager()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        Glide.with(getApplicationContext()).load("http://"+getResources().getString(R.string.ip)+"/bitmap/shop/shop.jpg").asBitmap()
                .into(new SimpleTarget<Bitmap>(Omoyo.screendisplay.getWidth(), 250) {
                    @Override
                    public void onResourceReady(Bitmap bitmap,
                                                GlideAnimation<? super Bitmap> arg1) {

                        appbar.setBackgroundDrawable(new BitmapDrawable(
                                getResources(), bitmap));

                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                int mutedColor = palette.getMutedColor(getResources().getColor(R.color.appcolor));
                                collapsingtoolbar.setContentScrimColor(palette.getDarkMutedColor(getResources().getColor(R.color.appcolor)));
                                collapsingtoolbar.setStatusBarScrimColor(palette.getDarkMutedColor(getResources().getColor(R.color.appcolor)));
                            }
                        });

                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        //  Omoyo.toast("Started",getApplicationContext());
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        // Omoyo.toast(e.getMessage(),getApplicationContext());
                    }
                });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
              //  Omoyo.toast(""+newState,getApplicationContext());
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
             //   Omoyo.toast("X:"+dx+"Y:"+dy,getApplicationContext());
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_shoppage, menu);
        MenuItem item = menu.findItem(R.id.searchItem);
        // Get the SearchView and set the searchable configuration
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

        if(id == R.id.searchItem){


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

private void showTheJam(){
    Intent intent = getIntent();
    dialog_class dialog =  new dialog_class();
    Bundle bundle = new Bundle();
    if(intent.getIntExtra("type_of",1007) == 0){
        bundle.putInt("type_of",5);
        bundle.putString("_id", intent.getStringExtra("_id"));
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "Hello");
    }
    if(intent.getIntExtra("type_of",1007) == 1){
        bundle.putInt("type_of",6);
        bundle.putString("_id", intent.getStringExtra("_id"));
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "Hello");
    }
    if(intent.getIntExtra("type_of",1007) == 2){
        bundle.putInt("type_of",7);
    }

}

    private void downloadCoordinateOfShop(){
        try{
            String defaultlocation=String.format("[{\"location_id\" : \"%s\"}]","1008");
            JSONObject jsonObject = new JSONObject(Omoyo.shared.getString("location", defaultlocation));
            final  String location_id=jsonObject.getString("location_id");
            OkHttpClient okhttp=new OkHttpClient();
            String json=String.format("{\"location_id\" : \"%s\"}",location_id);
            final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
            RequestBody requestbody=RequestBody.create(JSON, json);
            Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/coordinateOfShop/").post(requestbody).build();
            Call call=okhttp.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if(response.isSuccessful()) {
                        String data = response.body().string();
                        Omoyo.edit.putString("coordinateOfShop", data);
                        Omoyo.edit.commit();
                    }
                }
            });
        }
        catch(JSONException ex) {

        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        if(!Omoyo.shared.getBoolean("user_status",false)) {
            Dialog_For_Shop_Page dialog_for_shop_page = new Dialog_For_Shop_Page();
            Bundle bundle = new Bundle();
            bundle.putInt("type_of", 7);
            dialog_for_shop_page.setArguments(bundle);
            dialog_for_shop_page.show(getSupportFragmentManager(),"h");
        }
    }
}
