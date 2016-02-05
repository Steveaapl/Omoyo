package com.example.muditi.deligoo;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
    @Bind(R.id.grid_for_category_list)
    GridView grid_for_category_list;
    @Bind(R.id.card_view_for_offer)
    CardView card_view_for_offer;
    dialog_class dialog;
    Bundle bundle;
    JSONArray jsonArray;
    JSONArray jsonArray1 ;
    JSONArray jsonArray2;
    SearchView searchView;
    int visi=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        ButterKnife.bind(this);
        Omoyo.errorReportByMint(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.title_activity_offer);
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_36dp);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
                visi=2;
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
                visi=1;
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
                visi = 0;
            }
        });
        try{
            jsonArray2 = new JSONArray(Omoyo.shared.getString("category", ""));
            jsonArray = new JSONArray(Omoyo.shared.getString("ads","ads"));
            grid_for_offer_wide.setAdapter(new gridViewAdapter(0 ,jsonArray));
            grid_for_offer_narrow.setAdapter(new gridViewAdapter(1,jsonArray));
            grid_for_offer_grid.setAdapter(new gridViewAdapter(2,jsonArray));
        }
        catch(JSONException es){
       Log.d("OfferX:",es.getLocalizedMessage());
        }

      grid_for_category_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              hideKeyboard();
              jsonArray1 = new JSONArray();
              TextView textView = ButterKnife.findById(view,R.id.text_view_for_quick_search);
              String text = textView.getText().toString();
             // searchView.setQuery(text,false);
              String id = null ;
              try{

                  for(int k =0;k<jsonArray2.length();k++){
                      JSONObject jsonObject = jsonArray2.getJSONObject(k);
                     if(jsonObject.getString("category_name").equals(text)){
                         id = jsonObject.getString("category_id");
                         k=jsonArray2.length();
                     }

                  }

                  for(int k =0 ;k<jsonArray.length();k++){
                      JSONObject jsonObject = jsonArray.getJSONObject(k);
                      if(jsonObject.getString("category_id").equals(id)){
                          jsonArray1.put(jsonArray1.length(),jsonObject);
                      }
                  }

                  grid_for_offer_wide.setAdapter(new gridViewAdapter(0 ,jsonArray1));
                  grid_for_offer_narrow.setAdapter(new gridViewAdapter(1,jsonArray1));
                  grid_for_offer_grid.setAdapter(new gridViewAdapter(2,jsonArray1));
                  grid_for_category_list.setVisibility(View.GONE);
                  card_view_for_offer.setVisibility(View.VISIBLE);
if(visi==0){
    grid_for_offer_wide.setVisibility(View.VISIBLE);
}
                  else if(visi==1){
    grid_for_offer_narrow.setVisibility(View.VISIBLE);
                  }
                  else{
    grid_for_offer_grid.setVisibility(View.VISIBLE);
                  }
              }
              catch (JSONException jx){

              }
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

                    final JSONObject jsonObject=jsonArray.getJSONObject(i);
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
                   case 3:
                       view = inflate.inflate(R.layout.search_suggestion_layout, null);
                   default:
                       Log.d("TAG","E!");
               }
if(inden !=3) {
    LinearLayout linearLayout2 = ButterKnife.findById(view, R.id.adslinearlayout);
    final RelativeLayout relativeLayout = ButterKnife.findById(view, R.id.relativelayouthorizantalscroll);
    TextView textshopcaption = ButterKnife.findById(view, R.id.textshopcaption);
    textshopcaption.setText(jsonObject.getString("ads_description"));
    TextView textitem = ButterKnife.findById(view, R.id.textitem);
    final TextView textshopid = ButterKnife.findById(view, R.id.textshopid);
    textshopid.setText(jsonObject.getString("shop_id"));
    StringBuilder stringBuilder = new StringBuilder();
    JSONArray jsonArray1 = jsonObject.getJSONArray("ads_item");
    for (int k = 0; k < jsonArray1.length(); k++) {
        stringBuilder.append(jsonArray1.getJSONObject(k).getString("item_name") + "      ");
    }
    textitem.setText(stringBuilder.toString());
    stringBuilder.delete(0, stringBuilder.length());
    Glide.with(getApplicationContext()).load(jsonObject.getString("ads_bitmap_url")).asBitmap().into(new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            relativeLayout.setBackgroundDrawable(new BitmapDrawable(
                    getResources(), resource));
        }
    });

    linearLayout2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Omoyo.currentShopId = textshopid.getText().toString();
            try {
                shoploader(jsonObject.getString("ads_id"));
            }
            catch(JSONException jx){

            }
        }
    });
}
               else{
                    TextView textView = ButterKnife.findById(view,R.id.text_view_for_quick_search);
                    textView.setText(jsonObject.getString("category_name"));
               }
               return view;
            }
            catch(JSONException e){
                Log.d("OfferXXX:",e.getLocalizedMessage());
return null;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shoplist, menu);

        MenuItem item = menu.findItem(R.id.searchItem);
        SearchManager searchManager =(SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) item.getActionView();

        if(searchView != null) {

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    try{
                        searchCategory(query.trim());
                    }
                    catch(Exception ex){
                        searchCategory(query);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(newText.length()==0){
                        jsonArray1 = jsonArray2;
                        grid_for_category_list.setAdapter(new gridViewAdapter(3,jsonArray1));
                    }
                    else{
                        grid_for_category_list.setVisibility(View.VISIBLE);
                        grid_for_offer_narrow.setVisibility(View.GONE);
                        grid_for_offer_grid.setVisibility(View.GONE);
                        grid_for_offer_wide.setVisibility(View.GONE);
                        card_view_for_offer.setVisibility(View.GONE);
                        try{
                         searchCategory(newText.trim());
                        }
                        catch(Exception ex){
                         searchCategory(newText);
                        }
                    }
                    return true;
                }
            });
            searchView.setIconified(true);
            searchView.setIconifiedByDefault(true);

            MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    grid_for_category_list.setVisibility(View.VISIBLE);
                    grid_for_offer_narrow.setVisibility(View.GONE);
                    grid_for_offer_grid.setVisibility(View.GONE);
                    grid_for_offer_wide.setVisibility(View.GONE);
                    card_view_for_offer.setVisibility(View.GONE);
                        jsonArray1 = jsonArray2;
                        grid_for_category_list.setAdapter(new gridViewAdapter(3,jsonArray1));
                       Log.d("OfferXX:",jsonArray1.toString()+"XX");
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    grid_for_offer_wide.setAdapter(new gridViewAdapter(0, jsonArray));
                    grid_for_offer_narrow.setAdapter(new gridViewAdapter(1, jsonArray));
                    grid_for_offer_grid.setAdapter(new gridViewAdapter(2, jsonArray));
                    grid_for_category_list.setVisibility(View.GONE);
                    if(visi==0){
                        grid_for_offer_wide.setVisibility(View.VISIBLE);
                    }
                    else if(visi==1){
                        grid_for_offer_narrow.setVisibility(View.VISIBLE);
                    }
                    else{
                        grid_for_offer_grid.setVisibility(View.VISIBLE);
                    }
                    card_view_for_offer.setVisibility(View.VISIBLE);
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

        return super.onOptionsItemSelected(item);
    }


    private void searchCategory(String chr){
        jsonArray1 = new JSONArray();
        try {
            for(int i =0; i<jsonArray2.length();i++){
                JSONObject jsonObject = jsonArray2.getJSONObject(i);
                if(jsonObject.getString("category_name").toLowerCase().contains(chr.toLowerCase())){
                    jsonArray1.put(jsonArray1.length(),jsonObject);
                }
            }
            grid_for_category_list.setAdapter(new gridViewAdapter(3,jsonArray1));
        }
        catch(JSONException xj){

        }
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
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
    public  void shoploader(final String ads_id){
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
                            Intent intent = new Intent(getApplicationContext(), shoppage.class);
                            intent.putExtra("type_of",0);
                            intent.putExtra("_id",ads_id);
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_transition_forword_in, R.anim.activity_transition_forword_out);
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

    @Override
    protected void onResume() {
        super.onResume();
        Google_analytics_class.getInstance().trackScreenView("Offer View");
    }
}
