package com.google.muditi.deligoo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.muditi.deligoo.Google_analytics_class;
import com.example.muditi.deligoo.Omoyo;
import com.example.muditi.deligoo.R;
import com.example.muditi.deligoo.Shop_Page_Adapter_Class;
import com.example.muditi.deligoo.SmsVarification;
import com.example.muditi.deligoo.dialog_class;
import com.example.muditi.deligoo.login_dialog;
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


public class shoppage extends ActionBarActivity implements login_dialog.login_interface{
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
    @Bind(R.id.text_view_for_timing_of_shop)
    TextView text_view_for_timing_of_shop;
    SearchView searchView;
    private boolean flager = true , loginflag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppage);
        ButterKnife.bind(this);
        Omoyo.shared = getSharedPreferences("omoyo", Context.MODE_PRIVATE);
        Omoyo.edit = Omoyo.shared.edit();
        Display display=getWindowManager().getDefaultDisplay();
        Omoyo.screendisplay=display;
        if(!Omoyo.shared.contains("gpsposition")){
            downloadCoordinateOfShop();
        }
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


        //showTheJam();

        if(!Omoyo.shared.getBoolean("user_status",false)) {
            loginflag = false;
            login_dialog login = new login_dialog();
            login.setCancelable(false);
            login.show(getSupportFragmentManager(),"");
        }


        if(Omoyo.shared.getBoolean("user_status",false)) {
           showTheJam();
            Log.d("DIALOG-C:X", "X");
        }

       if(flager)
        float_for_faverotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   float_for_faverotes.setImageDrawable(getResources().getDrawable(R.mipmap.ic_favorite_black_48dp));

                try {
                    Omoyo.addtofavorets(2, new JSONObject(Omoyo.shared.getString("shop", "shop")));
                    saveUserDataToServerOfShop();
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
            text_view_for_timing_of_shop.setText(new JSONObject(Omoyo.shared.getString("shop", "shop")).getString("shop_timing"));
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
        recyclerView.setAdapter(new Shop_Page_Adapter_Class(getApplicationContext(), getFragmentManager(), getSupportFragmentManager()));
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_36dp);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Omoyo.errorReportByMint(getApplicationContext());
        try {
            Glide.with(getApplicationContext()).load(new JSONObject(Omoyo.shared.getString("shop", "shop")).getString("shop_bitmap_url")).asBitmap()
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
        }
        catch(JSONException ex){

        }
        catch(NullPointerException nullex){
            Log.d("NULLEX",nullex.getLocalizedMessage());
        }
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
       // bundle.putInt("type_of",7);
    }

}



    public void saveUserDataToServerOfShop(){
        OkHttpClient okhttp=new OkHttpClient();
        String json=String.format("{\"user_id\" : \"%s\",\"shop_id\" : \"%s\",\"time\" : \"%s\"}",Omoyo.shared.getString("user_id","1007"),Omoyo.currentShopId,Omoyo.Date());
        final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/userShopFaverote/").post(requestbody).build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String data = response.body().string();

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!Omoyo.shared.getBoolean("user_status",false) && loginflag) {
            login_dialog login = new login_dialog();
            login.setCancelable(false);
            login.show(getSupportFragmentManager(),"");
        }
        else{
            loginflag = true ;
        }
        Log.d("DIALOG-C:XXX", "X");
        Google_analytics_class.getInstance().trackScreenView("Shop Page View");

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
                    final String data = response.body().string();
                    Omoyo.edit.putString("gpsposition",data);
                    Omoyo.edit.commit();
                }
            });
        }
        catch(JSONException ex) {

        }
    }

    @Override
    public void oncancle() {
        onBackPressed();
    }

    @Override
    public void onlogin() {
        startActivity(new Intent(getApplicationContext(),SmsVarification.class));
        overridePendingTransition(R.anim.activity_transition_forword_in, R.anim.activity_transition_forword_out);
    }
}

