package com.example.muditi.omoyo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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

public class Marked extends AppCompatActivity {
    @Bind(R.id.grid_for_ads)
    GridView grid_for_ads;
    @Bind(R.id.grid_for_product)
    GridView grid_for_product;
    @Bind(R.id.grid_for_shop)
    GridView grid_for_shop;
    @Bind(R.id.image_view_for_ads)
    ImageView image_view_for_ads;
    @Bind(R.id.image_view_for_product)
    ImageView image_view_for_product;
    @Bind(R.id.image_view_for_shop)
    ImageView image_view_for_shop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marked);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.favorite_offer));
        image_view_for_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grid_for_ads.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn).duration(500).playOn(grid_for_ads);
                grid_for_shop.setVisibility(View.GONE);
                grid_for_product.setVisibility(View.GONE);
                image_view_for_ads.setBackgroundColor(Color.WHITE);
                image_view_for_shop.setBackground(new ColorDrawable(getResources().getColor(R.color.madang)));
                image_view_for_product.setBackground(new ColorDrawable(getResources().getColor(R.color.madang)));
                setTitle(getResources().getString(R.string.favorite_offer));
            }
        });
        image_view_for_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grid_for_product.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn).duration(500).playOn(grid_for_product);
                grid_for_shop.setVisibility(View.GONE);
                grid_for_ads.setVisibility(View.GONE);
                image_view_for_ads.setBackground(new ColorDrawable(getResources().getColor(R.color.madang)));
                image_view_for_shop.setBackgroundColor(Color.WHITE);
                image_view_for_product.setBackground(new ColorDrawable(getResources().getColor(R.color.madang)));
                setTitle(getResources().getString(R.string.favorite_product));
            }
        });
        image_view_for_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grid_for_shop.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn).duration(500).playOn(grid_for_shop);
                grid_for_ads.setVisibility(View.GONE);
                grid_for_product.setVisibility(View.GONE);
                image_view_for_ads.setBackground(new ColorDrawable(getResources().getColor(R.color.madang)));
                image_view_for_shop.setBackground(new ColorDrawable(getResources().getColor(R.color.madang)));
                image_view_for_product.setBackgroundColor(Color.WHITE);
                setTitle(getResources().getString(R.string.favorite_shop));
            }
        });
        grid_for_ads.setAdapter(new GridViewAdapterForMark(0, getApplicationContext()));
        grid_for_product.setAdapter(new GridViewAdapterForMark(1,getApplicationContext()));
        grid_for_shop.setAdapter(new GridViewAdapterForMark(2,getApplicationContext()));


        grid_for_ads.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = ButterKnife.findById(view,R.id.textshopid);
                Intent intent = new Intent(getApplicationContext(), shoppage.class);
                intent.putExtra("type_of",0);
                intent.putExtra("_id", textView.getText());
                try{
                    JSONArray jsonArray2 = new JSONArray(Omoyo.shared.getString("favorets",""));
                    for(int k=0 ; k<jsonArray2.length() ;k++){
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(k);
                        if(jsonObject2.getString("type_of").equals("0")){
                           if(jsonObject2.getJSONObject("data").getString("ads_id").equals(textView.getText())){
                               shoploader(jsonObject2.getJSONObject("data").getString("shop_id"),intent);
                           }
                        }
                    }
                }
                catch(JSONException jx){

                }
            }
        });

        grid_for_shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = ButterKnife.findById(view,R.id.textshopid);
                Intent intent = new Intent(getApplicationContext(), shoppage.class);
                intent.putExtra("type_of",2);
                intent.putExtra("_id", textView.getText().toString());
                try{
                    JSONArray jsonArray2 = new JSONArray(Omoyo.shared.getString("favorets",""));
                    for(int k=0 ; k<jsonArray2.length() ;k++){
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(k);
                        if(jsonObject2.getString("type_of").equals("2")){
                            if(jsonObject2.getJSONObject("data").getString("shop_id").equals(textView.getText())){
                                shoploader(jsonObject2.getJSONObject("data").getString("shop_id"),intent);
                            }
                        }
                    }
                }
                catch(JSONException jx){

                }
            }
        });

        grid_for_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = ButterKnife.findById(view,R.id.textshopid);
                Intent intent = new Intent(getApplicationContext(), shoppage.class);
                intent.putExtra("type_of", 1);
                intent.putExtra("_id", textView.getText());
                JSONArray jsonArray = new JSONArray();
                try{
                    JSONArray jsonArray2 = new JSONArray(Omoyo.shared.getString("favorets",""));
                    for(int k=0 ; k<jsonArray2.length() ;k++){
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(k);
                        if(jsonObject2.getString("type_of").equals("1")){
                            if(jsonObject2.getJSONObject("data").getString("product_id").equals(textView.getText())){
                                shoploader(jsonObject2.getJSONObject("data").getString("product_shop_id"),intent);
                            }
                            jsonArray.put(jsonArray.length(),jsonObject2.getJSONObject("data"));
                        }
                    }
                }
                catch(JSONException jx){

                }
                Omoyo.currentSerachData = jsonArray.toString();
              //  startActivity(intent);
            }
        });

    }

    public  void shoploader(String id , final Intent intent){
        OkHttpClient okhttp=new OkHttpClient();
        String json=String.format("{\"shop_id\" : \"%s\"}", id);
        final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/shop/").post(requestbody).build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        snackBar(1);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String data = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(data);
                        Omoyo.edit.putString("shop", jsonArray.getJSONObject(0).toString());
                        Omoyo.edit.commit();
                    } catch (JSONException jx) {

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }
    private  void snackBar(final int i){
        final Snackbar snackbar =Snackbar.make(findViewById(R.id.relative_layout_for_sms_varification), getResources().getString(R.string.internet_not_available), Snackbar.LENGTH_INDEFINITE);
        final View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.snackbar_back));
        final TextView textView =ButterKnife.findById(snackbarView,android.support.design.R.id.snackbar_text);
        final TextView textViewAction =ButterKnife.findById(snackbarView, android.support.design.R.id.snackbar_action);
        textView.setTextColor(Color.WHITE);
        snackbar.setDuration(Snackbar.LENGTH_LONG);
        textViewAction.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        if(i==1){
            textViewAction.setText(getResources().getString(R.string.try_again));
            textView.setText(getResources().getString(R.string.internet_not_available));
        }
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        grid_for_ads.setAdapter(new GridViewAdapterForMark(0, getApplicationContext()));
        grid_for_product.setAdapter(new GridViewAdapterForMark(1, getApplicationContext()));
        grid_for_shop.setAdapter(new GridViewAdapterForMark(2, getApplicationContext()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_transition_backword_in, R.anim.activity_transition_backword_out);
    }
}
