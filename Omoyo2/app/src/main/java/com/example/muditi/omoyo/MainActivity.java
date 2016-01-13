package com.example.muditi.omoyo;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.rey.material.app.Dialog;
import com.rey.material.app.TimePickerDialog;
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


public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.slidemenu)
    ListView slidemenu;
    @Bind(R.id.hozintalscroolview)
    HorizontalScrollView horizontalscrollview;
    @Bind(R.id.drawerlayout)
    DrawerLayout drawerlayout;
    @Bind(R.id.gridlayout)
    GridLayout gridlayout;
    @Bind(R.id.adslayout)
    LinearLayout adslayout;
    @Bind(R.id.linear_layout_for_search_result)
    LinearLayout linear_layout_for_search;
    @Bind(R.id.parentScrollView)
    ScrollView scrollView ;
    String location;
    int count;
    boolean query_submit_check=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        materialDialog();
        Display display=getWindowManager().getDefaultDisplay();
        Omoyo.screendisplay=display;
        Omoyo.widthofscreen=display.getWidth();
        Omoyo.heightofscreen=display.getHeight();
        Omoyo.edit.putInt("widthOfDevice",display.getWidth());
        Omoyo.edit.commit();
        Omoyo.edit.putInt("heightOfDevice", display.getHeight());
        Omoyo.edit.commit();
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setSubtitleTextColor(Color.WHITE);
        location=Omoyo.shared.getString("area","Modinager")+","+Omoyo.shared.getString("city","Ghaziabad");
        toolbar.setSubtitle(Omoyo.shared.getString("GpsLocation",location));
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
        slidemenu.setAdapter(new slidemenuadapter(getApplicationContext()));
        slidemenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(getApplicationContext(),SmsVarification.class));
                    drawerlayout.closeDrawer(Gravity.LEFT);
                }
            }
        });
        //request for category
          categoryloader();
          adsloader();
        //grid layout
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
MenuItem item = menu.findItem(R.id.searchItem);
        SearchManager searchManager =(SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();
        if(searchView != null) {

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    queryResponse(query);
                    query_submit_check=true;
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(newText.length()==0){
                        linear_layout_for_search.removeAllViews();
                    }
                    return true;
                }
            });
            searchView.setIconified(true);
            searchView.setIconifiedByDefault(true);

            MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    horizontalscrollview.setVisibility(View.GONE);
                    gridlayout.setVisibility(View.GONE);
                    linear_layout_for_search.setVisibility(View.VISIBLE);
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.appcolor));
                    horizontalscrollview.setVisibility(View.VISIBLE);
                    gridlayout.setVisibility(View.VISIBLE);
                    linear_layout_for_search.setVisibility(View.GONE);
                    scrollView.setBackgroundColor(getResources().getColor(R.color.appcolor));
                    return true;
                }
            });

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        else{
            Omoyo.toast("NUll", getApplicationContext());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.home)
        {
            drawerlayout.openDrawer(Gravity.LEFT);
        }

        if(id==R.id.searchItem){

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
                                try {
                                        JSONArray jsonArray = new JSONArray(data);
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject jsonObject=jsonArray.getJSONObject(i);
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
                                }
                                catch(JSONException e){

                                }
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
                                           else  {
                                                shopListLoader(category.getString("category_id"));
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
                            startActivity(new Intent(getApplicationContext(), shoppage.class));
                        }
                    });
                    try {
                        JSONArray jsonArray = new JSONArray(data);
                        Omoyo.edit.putString("shop",jsonArray.getJSONObject(0).toString());
                        Omoyo.edit.commit();
                    }
                    catch(JSONException e){

                    }

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
                                Intent intent = new Intent(getApplicationContext(), subshopcategory.class);
                                intent.putExtra("category_id", category_id);
                                startActivity(intent);
                            }
                        });
                    }
                }
            });

    }
private void shopListLoader(final String category_id)
{
    OkHttpClient okhttp=new OkHttpClient();
    String json=String.format("{\"category_id\" : \"%s\"}", category_id);
    final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
    RequestBody requestbody=RequestBody.create(JSON, json);
    Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/shoplist/").post(requestbody).build();
    Call call=okhttp.newCall(request);

    call.enqueue(new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Omoyo.toast("Error in Network", getApplicationContext());
                }
            });
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if (response.isSuccessful()) {
                final String data = response.body().string();
                Omoyo.edit.putString("shoplist", data);
                Omoyo.edit.commit();
                Intent intent = new Intent(getApplicationContext(), shoplist.class);
                intent.putExtra("category_id", category_id);
                startActivity(intent);
            }
        }
    });
}
private  void queryResponse(String query){

    OkHttpClient okhttp=new OkHttpClient();
    String json=String.format("{\"query\" : \"%s\"}", query.toLowerCase());
    final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
    RequestBody requestbody=RequestBody.create(JSON, json);
    Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/searchquery/").post(requestbody).build();
    Call call=okhttp.newCall(request);
    call.enqueue(new Callback() {
        @Override
        public void onFailure(Request request, final IOException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Omoyo.toast("Error:"+e.getMessage(), getApplicationContext());
                }
            });
        }

        @Override
        public void onResponse( Response response) throws IOException {
            if(response.isSuccessful()){
                final String data = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                View view = inflate.inflate(R.layout.include_for_search, null);
                                linear_layout_for_search.addView(view);
                            }
                        } catch (JSONException jsonException) {

                        }
                    }
                });
            }
        }
    });
}
private void materialDialog(){
    TimePickerDialog dialog =new TimePickerDialog(getApplicationContext());
    dialog.show();
}
}
