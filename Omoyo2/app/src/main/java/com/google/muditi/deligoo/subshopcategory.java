package com.google.muditi.deligoo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.muditi.deligoo.*;
import com.example.muditi.deligoo.shoplist;
import com.example.muditi.deligoo.shoppage;
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


public class subshopcategory extends ActionBarActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.hozintalscroolview)
    HorizontalScrollView horizontalscrollview;
    @Bind(R.id.drawerlayout)
    DrawerLayout drawerlayout;
    @Bind(R.id.gridlayout)
    GridLayout gridlayout;
    @Bind(R.id.adslayout)
    LinearLayout adslayout;
    String location,category_id;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        category_id=getIntent().getStringExtra("category_id");
        Display display=getWindowManager().getDefaultDisplay();
        Omoyo.screendisplay=display;
        Omoyo.widthofscreen=display.getWidth();
        Omoyo.heightofscreen=display.getHeight();
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setSubtitleTextColor(Color.WHITE);
        location=Omoyo.shared.getString("area","Modinager")+","+Omoyo.shared.getString("city","Ghaziabad");
        toolbar.setSubtitle(location);
        toolbar.showOverflowMenu();
        View view=getLayoutInflater().inflate(R.layout.serachbox, null);
        final EditText searchboxedittext=ButterKnife.findById(view, R.id.searchboxedittext);
        searchboxedittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchboxedittext.setFocusableInTouchMode(true);
                searchboxedittext.setFocusable(true);
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Omoyo.shared.contains("ads")){
         //   Omoyo.toast("contains ads",getApplicationContext());
            categoryAdsLoader();
        }
        else{
            adsloader();
        }

        if(Omoyo.shared.contains("suncategory")){
            Omoyo.toast("contains ads",getApplicationContext());
            subCategoryLoader();
        }
        else{
           subcategoryloader(category_id);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        int home=item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(home==R.id.home){
            drawerlayout.openDrawer(Gravity.LEFT);
        }
        return super.onOptionsItemSelected(item);
    }

    public void adsloader(){
        try {
            String defaultlocation=String.format("[{\"location_id\" : \"%s\"}]","1008");
            JSONObject jsonObject = new JSONObject(Omoyo.shared.getString("location", defaultlocation));
            final  String location_id=jsonObject.getString("location_id");
            OkHttpClient okhttp=new OkHttpClient();
            String json=String.format("{\"location_id\" : \"%s\"}", location_id);
            final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
            RequestBody requestbody=RequestBody.create(JSON, json);
            Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/ads/").post(requestbody).build();
            Call call=okhttp.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Omoyo.toast("Error in the network", getApplicationContext());
                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Omoyo.shoploader(getApplicationContext());
                        final String data = response.body().string();
                        Omoyo.edit.putString("ads", data);
                        Omoyo.edit.commit();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                categoryAdsLoader();
                            }
                        });
                    }
                }
            });
        }
        catch(JSONException jsone){

        }
    }
    public void categoryloader(){
        OkHttpClient okhttp=new OkHttpClient();
        Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/category/").build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Omoyo.toast("Error in the network", getApplicationContext());
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String data = response.body().string();
                    Omoyo.edit.putString("category", data);
                    Omoyo.edit.commit();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonarray = new JSONArray(data);
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    categoryshopcountloader(jsonobject);
                                }

                            } catch (JSONException jsonexc) {
                                Log.d("Error", jsonexc.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }
    public void categoryshopcountloader(final JSONObject category) {
        try {
            OkHttpClient okhttp = new OkHttpClient();
            String json = String.format("{\"category_id\" : \"%s\"}", category.getString("category_id"));
            final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
            RequestBody requestbody = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url("http://" + getResources().getString(R.string.ip) + "/categoryshopcount/").post(requestbody).build();
            Call call = okhttp.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Omoyo.toast("Error in loader",getApplicationContext());
                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {

                    final String data = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Omoyo.toast(data,getApplicationContext());
                            try {
                                JSONObject jsonobjectcount =new JSONObject(data);
                                String numberOfShop = jsonobjectcount.getString("count");
                                LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                View view = inflate.inflate(R.layout.shoppageviewadder, null);
                                LinearLayout linearLayout2 = ButterKnife.findById(view, R.id.linearlayoutasadder);
                                final  RelativeLayout relativeLayout = ButterKnife.findById(view, R.id.relativelayoutgridlayout);
                                TextView textshopcount=ButterKnife.findById(view,R.id.shopcount);
                                textshopcount.setText(numberOfShop);
                                TextView textcategoryname=ButterKnife.findById(view,R.id.categoryname);
                                String value=category.getString("category_name").substring(0,1).toUpperCase()+category.getString("category_name").substring(1, category.getString("category_name").length());
                                textcategoryname.setText(value);
                                StringBuilder builder=new StringBuilder();
                                try {
                                    JSONArray jsonArray =category.getJSONArray("category_item");
                                    for (int k = 0; k < jsonArray.length(); k++) {
                                        String valueofitem=jsonArray.getJSONObject(k).getString("item").substring(0,1).toUpperCase()+jsonArray.getJSONObject(k).getString("item").substring(1,jsonArray.getJSONObject(k).getString("item").length());
                                        builder.append(valueofitem+"       ");
                                    }
                                    TextView textitem=ButterKnife.findById(view,R.id.itemincategory);
                                    textitem.setText(builder.toString());
                                    builder.delete(0, builder.length());
                                }
                                catch(JSONException e){

                                }

                                Glide.with(getApplicationContext()).load("http://192.168.0.113:15437/bitmap/category/category.jpg").asBitmap().into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        relativeLayout.setBackgroundDrawable(new BitmapDrawable(
                                                getResources(), resource));
                                    }
                                });
                                //categoryclick
                                linearLayout2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try{
                                            if(category.getString("sub_category")=="true") {
                                                subcategoryloader(category.getString("category_id"));
                                            }
                                            else{

                                            }
                                        }
                                        catch(JSONException e){

                                        }
                                    }
                                });


                                gridlayout.addView(linearLayout2);
                            } catch (JSONException e) {
                                Omoyo.toast("Error in json",getApplicationContext());
                            }
                        }
                    });
                }
            });
        }
        catch(JSONException e){
            Log.d("E:",e.getMessage());
        }
    }
    public  void shoploader(){
        OkHttpClient okhttp=new OkHttpClient();
        String json=String.format("{\"shop_id\" : \"%s\"}", Omoyo.currentShopId);
        final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/shop/").post(requestbody).build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String data = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                         try{
                             JSONArray jsonArray = new JSONArray(data);
                             Omoyo.edit.putString("shop",jsonArray.getJSONObject(0).toString());
                             Omoyo.edit.commit();
                             startActivity(new Intent(getApplicationContext(), shoppage.class));
                         }
                         catch(JSONException e){

                         }
                        }
                    });
                }
            }
        });
    }
    private void subcategoryloader(final  String category_id){
        OkHttpClient okhttp=new OkHttpClient();
        String json = String.format("{\"category_id\" : \"%s\"}", category_id);
        final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url("http://" + getResources().getString(R.string.ip) + "/subcategory/").post(requestbody).build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Omoyo.toast("Error in the network", getApplicationContext());
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String data = response.body().string();
                    Omoyo.edit.putString("subcategory", data);
                    Omoyo.edit.commit();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          subCategoryLoader();
                        }
                    });
                }
            }
        });

    }
public void categoryAdsLoader(){
    try {
        JSONArray jsonArray = new JSONArray(Omoyo.shared.getString("ads","ads"));
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            Log.d(""+jsonObject.getString("category_id").equals(category_id),""+jsonObject.getString("category_id").equals(category_id));
           if(jsonObject.getString("category_id").equals(category_id)){
               LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
               View view = inflate.inflate(R.layout.adslayout, null);
               LinearLayout linearLayout2 = ButterKnife.findById(view, R.id.adslinearlayout);
               final  RelativeLayout relativeLayout = ButterKnife.findById(view, R.id.relativelayouthorizantalscroll);
               TextView textshopcaption=ButterKnife.findById(view,R.id.textshopcaption);
               textshopcaption.setText(jsonObject.getString("ads_description"));
               TextView textitem=ButterKnife.findById(view,R.id.textitem);
               final  TextView textshopid=ButterKnife.findById(view,R.id.textshopid);
               textshopid.setText(jsonObject.getString("shop_id"));
               StringBuilder stringBuilder=new StringBuilder();
               JSONArray jsonArray1=jsonObject.getJSONArray("ads_item");
               for(int k=0;k<jsonArray1.length();k++)
               {
                   stringBuilder.append(jsonArray1.getJSONObject(k).getString("item")+"      ");
               }
               textitem.setText(stringBuilder.toString());
               stringBuilder.delete(0, stringBuilder.length());
               Glide.with(getApplicationContext()).load("http://192.168.0.113:15437/bitmap/ads/ads.jpg").asBitmap().into(new SimpleTarget<Bitmap>() {
                   @Override
                   public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                       relativeLayout.setBackgroundDrawable(new BitmapDrawable(
                               getResources(), resource));
                   }
               });
               linearLayout2.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Omoyo.currentShopId=textshopid.getText().toString();
                       shoploader();
                   }
               });
               adslayout.addView(linearLayout2);
           }
            else{
              // Omoyo.toast(jsonObject.getString("category_id")+":::"+category_id,getApplicationContext());
           }
        }
    }
    catch(JSONException e){

    }
}

    private void subCategoryLoader(){
        try {
            JSONArray jsonarray = new JSONArray(Omoyo.shared.getString("subcategory","subcategory"));
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                subCategoryShopCountLoader(jsonobject);
            }

        } catch (JSONException jsonexc) {
            Log.d("Error", jsonexc.getMessage());
        }
    }

             private void subCategoryShopCountLoader(final JSONObject subcategory){
                 try {
                     OkHttpClient okhttp = new OkHttpClient();
                     String json = String.format("{\"subcategory_id\" : \"%s\"}", subcategory.getString("sub_category_id"));
                   //  Omoyo.toast(json,getApplicationContext());
                     final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                     RequestBody requestbody = RequestBody.create(JSON, json);
                     Request request = new Request.Builder().url("http://" + getResources().getString(R.string.ip) + "/subcategoryshopcount/").post(requestbody).build();
                     Call call = okhttp.newCall(request);
                     call.enqueue(new Callback() {
                         @Override
                         public void onFailure(Request request, IOException e) {
                             runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     Omoyo.toast("Error in loader",getApplicationContext());
                                 }
                             });
                         }

                         @Override
                         public void onResponse(Response response) throws IOException {

                             final String data = response.body().string();
                             runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     // Omoyo.toast(data,getApplicationContext());
                                     try {
                                         JSONObject jsonobjectcount =new JSONObject(data);
                                         String numberOfShop = jsonobjectcount.getString("count");
                                         LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                         View view = inflate.inflate(R.layout.shoppageviewadder, null);
                                         LinearLayout linearLayout2 = ButterKnife.findById(view, R.id.linearlayoutasadder);
                                         final  RelativeLayout relativeLayout = ButterKnife.findById(view, R.id.relativelayoutgridlayout);
                                         TextView textshopcount=ButterKnife.findById(view,R.id.shopcount);
                                         textshopcount.setText(numberOfShop);
                                         TextView textcategoryname=ButterKnife.findById(view,R.id.categoryname);
                                         String value=subcategory.getString("sub_category_name").substring(0,1).toUpperCase()+subcategory.getString("sub_category_name").substring(1, subcategory.getString("sub_category_name").length());
                                         textcategoryname.setText(value);
                                         StringBuilder builder=new StringBuilder();
                                         try {
                                             JSONArray jsonArray =subcategory.getJSONArray("sub_category_item");
                                             for (int k = 0; k < jsonArray.length(); k++) {
                                                 String valueofitem=jsonArray.getJSONObject(k).getString("item").substring(0,1).toUpperCase()+jsonArray.getJSONObject(k).getString("item").substring(1,jsonArray.getJSONObject(k).getString("item").length());
                                                 builder.append(valueofitem+"       ");
                                             }
                                             TextView textitem=ButterKnife.findById(view,R.id.itemincategory);
                                             textitem.setText(builder.toString());
                                             builder.delete(0, builder.length());
                                         }
                                         catch(JSONException e){

                                         }

                                         Glide.with(getApplicationContext()).load("http://192.168.0.113:15437/bitmap/category/category.jpg").asBitmap().into(new SimpleTarget<Bitmap>() {
                                             @Override
                                             public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                                 relativeLayout.setBackgroundDrawable(new BitmapDrawable(
                                                         getResources(), resource));
                                             }
                                         });
                                         //categoryclick
                                         linearLayout2.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 try {
                                                     shopListLoader(subcategory.getString("sub_category_id"));
                                                 }
                                                 catch(JSONException e){

                                                 }
                                             }
                                         });
                                         gridlayout.addView(linearLayout2);
                                     } catch (JSONException e) {
                                         Omoyo.toast("Error in json",getApplicationContext());
                                     }
                                 }
                             });
                         }
                     });
                 }
                 catch(JSONException e){
                     Log.d("E:",e.getMessage());
                 }
             }

    private void shopListLoader(final String sub_category_id)
    {
        OkHttpClient okhttp=new OkHttpClient();
        String json=String.format("{\"sub_category_id\" : \"%s\"}", sub_category_id);
        final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/subshoplist/").post(requestbody).build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Omoyo.toast("Error in Network",getApplicationContext());
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()){
                    final String data=response.body().string();
                    Omoyo.edit.putString("shoplist", data);
                    Omoyo.edit.commit();
                    Intent intent =new Intent(getApplicationContext(),shoplist.class);
                    intent.putExtra("sub_category_id",sub_category_id);
                    startActivity(intent);
                }
            }
        });
    }

}
