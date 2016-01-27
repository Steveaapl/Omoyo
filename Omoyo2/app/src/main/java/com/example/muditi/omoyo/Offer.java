package com.example.muditi.omoyo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Offer extends AppCompatActivity implements dialog_class.DialogListener {
    @Bind(R.id.grid_for_offer_grid)
    GridView grid_for_offer_grid;
    @Bind(R.id.grid_for_offer_narrow)
    GridView grid_for_offer_narrow;
    @Bind(R.id.grid_for_offer_wide)
    GridView grid_for_offer_wide;
    @Bind(R.id.image_view_for_filter_grid_list)
    ImageView image_view_for_filter_grid_list;
    @Bind(R.id.image_view_for_filter_wide_list)
    ImageView image_view_for_filter_wide_list;
    @Bind(R.id.image_view_for_filter_narrow_list)
    ImageView image_view_for_filter_narrow_list;
    dialog_class dialog;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_48dp);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.title_activity_offer);
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        image_view_for_filter_grid_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grid_for_offer_grid.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn).duration(500).playOn(grid_for_offer_grid);
                grid_for_offer_wide.setVisibility(View.GONE);
                grid_for_offer_narrow.setVisibility(View.GONE);
                image_view_for_filter_grid_list.setBackgroundColor(Color.WHITE);
                image_view_for_filter_narrow_list.setBackground(new ColorDrawable(getResources().getColor(R.color.appcolor)));
                image_view_for_filter_wide_list.setBackground(new ColorDrawable(getResources().getColor(R.color.appcolor)));
            }
        });
        image_view_for_filter_narrow_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grid_for_offer_narrow.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn).duration(500).playOn(grid_for_offer_narrow);
                grid_for_offer_wide.setVisibility(View.GONE);
                grid_for_offer_grid.setVisibility(View.GONE);
                image_view_for_filter_grid_list.setBackground(new ColorDrawable(getResources().getColor(R.color.appcolor)));
                image_view_for_filter_narrow_list.setBackgroundColor(Color.WHITE);
                image_view_for_filter_wide_list.setBackground(new ColorDrawable(getResources().getColor(R.color.appcolor)));
            }
        });
        image_view_for_filter_wide_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grid_for_offer_wide.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn).duration(500).playOn(grid_for_offer_wide);
                grid_for_offer_grid.setVisibility(View.GONE);
                grid_for_offer_narrow.setVisibility(View.GONE);
                image_view_for_filter_grid_list.setBackground(new ColorDrawable(getResources().getColor(R.color.appcolor)));
                image_view_for_filter_narrow_list.setBackground(new ColorDrawable(getResources().getColor(R.color.appcolor)));
                image_view_for_filter_wide_list.setBackgroundColor(Color.WHITE);
            }
        });
        try{
            JSONArray jsonArray = new JSONArray(Omoyo.shared.getString("ads","ads"));
            grid_for_offer_wide.setAdapter(new gridViewAdapter(0 ,jsonArray));
            grid_for_offer_narrow.setAdapter(new gridViewAdapter(1,jsonArray));
            grid_for_offer_grid.setAdapter(new gridViewAdapter(2,jsonArray));
        }
        catch(JSONException es){

        }


        grid_for_offer_narrow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_transition_backword_in, R.anim.activity_transition_backword_out);
    }

    private class gridViewAdapter extends BaseAdapter{
        JSONArray jsonArray;
        int inden ;
        View view;
        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public gridViewAdapter(int idein , JSONArray jsonArray) {
            super();
            this.inden = idein;
            this.jsonArray = jsonArray;
        }

        @Override
        public int getCount() {
            return jsonArray.length();
        }

        @Override
        public View getView(int i, View viewP, ViewGroup viewGroup) {
           try{

                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

               switch(inden){
                   case 0:
                       view = inflate.inflate(R.layout.offer_wide_layout, null);
                       break;
                   case 1:
                       view = inflate.inflate(R.layout.offer_narrow_layout, null);
                       break;
                   case 2:
                       view = inflate.inflate(R.layout.offer_grid_layout, null);
                       break;
                   default:
                       Log.d("TAG","E!");
               }

                    LinearLayout linearLayout2 = ButterKnife.findById(view, R.id.adslinearlayout);
                    final RelativeLayout relativeLayout = ButterKnife.findById(view, R.id.relativelayouthorizantalscroll);
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
                    Glide.with(getApplicationContext()).load("http://"+getResources().getString(R.string.ip)+"/bitmap/ads/ads.jpg").asBitmap().into(new SimpleTarget<Bitmap>() {
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
                            //  shoploader();
                        }
                    });

               return view;
            }
            catch(JSONException e){
return null;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.offer_filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.offer_filter){
            dialog =  new dialog_class();
            bundle = new Bundle();
            bundle.putInt("type_of", 4);
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), "Hello");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSubmitOfUserData(DialogFragment dialog, String user_name, String user_email) {

    }

    @Override
    public void onDescriptionSubmited() {

    }

    @Override
    public void onSubmitingOfferData(Uri uri, String string, String offerCode) {

    }

    @Override
    public void onSubmitingFilterData(String category, Integer distance) {

        try{
            JSONArray putArray =new JSONArray();
            JSONArray jsonArray = new JSONArray(Omoyo.shared.getString("ads","ads"));

            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String cate = "";
                if(category.length()>0) {
                    JSONArray jsonArray2 = new JSONArray(Omoyo.shared.getString("category",""));
                    for(int t=0;t<jsonArray2.length();t++){
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(t);
                        if(jsonObject2.getString("category_id").toLowerCase().equals(jsonObject.getString("category_id").toLowerCase())){
                            cate =  jsonObject2.getString("category_name");
                        }
                    }
                    if (distance > 0) {
                        try {
                            JSONArray jsonArray1 = new JSONArray(Omoyo.shared.getString("distance", "5"));

                            for (int k = 0; k < jsonArray1.length(); k++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(k);
                                if (jsonObject1.getString("shop_id").equals(jsonObject.getString("shop_id"))) {
                                    try {
                                        Integer length = Integer.valueOf(jsonObject1.getString("distance"));
                                        if (cate.equals(category.toLowerCase()) || length > distance) {

                                        } else {
                                            putArray.put(putArray.length(), jsonObject);
                                        }
                                    } catch (NumberFormatException numex) {

                                    }
                                }
                            }
                        } catch (JSONException e) {
                            if (cate.equals(category.toLowerCase())) {

                            } else {
                                putArray.put(putArray.length(), jsonObject);
                            }
                        }
                    } else if (cate.equals(category.toLowerCase())) {

                    } else {
                        putArray.put(putArray.length(), jsonObject);
                    }
                }
                else
                if(distance>0) {
                    try {
                        JSONArray jsonArray1 = new JSONArray(Omoyo.shared.getString("distance", "5"));
                        for (int k = 0; k < jsonArray1.length(); k++) {
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(k);
                            if (jsonObject1.getString("shop_id").equals(jsonObject.getString("shop_id"))) {
                                try {
                                    Integer length = Integer.valueOf(jsonObject1.getString("distance"));
                                    if ( length > distance) {

                                    } else {
                                        putArray.put(putArray.length(), jsonObject);
                                    }
                                } catch (NumberFormatException numex) {

                                }
                            }
                        }
                    }
                    catch(JSONException e){
                        if(jsonObject.getString("category_name").toLowerCase().equals(category.toLowerCase())){

                        }
                        else{
                            putArray.put(putArray.length(),jsonObject);
                        }
                    }
                }


                if(i == jsonArray.length()-1){
                    grid_for_offer_wide.setAdapter(new gridViewAdapter(0 ,putArray));
                    grid_for_offer_narrow.setAdapter(new gridViewAdapter(1,putArray));
                    grid_for_offer_grid.setAdapter(new gridViewAdapter(2,putArray));
                }
            }

        }
        catch(JSONException es){

        }
    }
}
