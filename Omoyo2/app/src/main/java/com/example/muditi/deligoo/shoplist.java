package com.example.muditi.deligoo;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;


public class shoplist extends ActionBarActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.grid_view_for_shop_list_wide)
    GridView gridView;
    @Bind(R.id.grid_view_for_shop_list_grid)
    GridView gridView2;
    @Bind(R.id.grid_view_for_shop_list_narrow)
    GridView gridView1;
    @Bind(R.id.image_view_for_shop_list_grid)
    ImageView imageView2;
    @Bind(R.id.image_view_for_shop_list_narrow)
    ImageView imageView1;
    @Bind(R.id.image_view_for_shop_list_wide)
    ImageView imageView;
    JSONArray jsonarray1;
    JSONArray jsonArrayTemp=new JSONArray();
    int type_of =0;
    boolean query_submit_check = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoplist);
        ButterKnife.bind(this);
        Omoyo.errorReportByMint(getApplicationContext());
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getIntent().getStringExtra("category_name"));
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setSubtitle(getResources().getString(R.string.shop));
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
        try {
            jsonarray1 = new JSONArray(Omoyo.shared.getString("shoplist", "f"));
            jsonArrayTemp = jsonarray1;
        }
        catch(JSONException jx){

        }
                gridView.setAdapter(new AdapterForShop(jsonarray1,0));
                gridView2.setAdapter(new AdapterForShop(jsonarray1,2));
                gridView1.setAdapter(new AdapterForShop(jsonarray1,1));
               gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                   }
               });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridView2.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn).duration(500).playOn(gridView2);
                gridView.setVisibility(View.GONE);
                gridView1.setVisibility(View.GONE);
                imageView2.setBackgroundColor(Color.WHITE);
                imageView.setBackground(new ColorDrawable(getResources().getColor(R.color.appcolor)));
                imageView1.setBackground(new ColorDrawable(getResources().getColor(R.color.appcolor)));
                type_of = 2;
            }
        });
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridView1.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn).duration(500).playOn(gridView1);
                gridView.setVisibility(View.GONE);
                gridView2.setVisibility(View.GONE);
                imageView2.setBackground(new ColorDrawable(getResources().getColor(R.color.appcolor)));
                imageView1.setBackgroundColor(Color.WHITE);
                imageView.setBackground(new ColorDrawable(getResources().getColor(R.color.appcolor)));
                type_of = 1;
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridView.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn).duration(500).playOn(gridView);
                gridView1.setVisibility(View.GONE);
                gridView2.setVisibility(View.GONE);
                imageView2.setBackground(new ColorDrawable(getResources().getColor(R.color.appcolor)));
                imageView1.setBackground(new ColorDrawable(getResources().getColor(R.color.appcolor)));
                imageView.setBackgroundColor(Color.WHITE);
                type_of = 1;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shoplist, menu);

        MenuItem item = menu.findItem(R.id.searchItem);
        SearchManager searchManager =(SearchManager)getSystemService(Context.SEARCH_SERVICE);
      SearchView  searchView = (SearchView) item.getActionView();

        if(searchView != null) {

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    try{
                        searchForShop(query.trim());
                    }
                    catch(Exception ex){
                        searchForShop(query);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(newText.length()==0){
                          gridView.setAdapter(new AdapterForShop(jsonarray1,0));
                          gridView2.setAdapter(new AdapterForShop(jsonarray1,2));
                          gridView1.setAdapter(new AdapterForShop(jsonarray1,1));
                    }
                    else{
                        try{
                            searchForShop(newText.trim());
                        }
                        catch(Exception ex){
                            searchForShop(newText);
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
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    gridView.setAdapter(new AdapterForShop(jsonarray1,0));
                    gridView1.setAdapter(new AdapterForShop(jsonarray1,1));
                    gridView2.setAdapter(new AdapterForShop(jsonarray1,2));
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

    private class AdapterForShop extends BaseAdapter{
        JSONArray jsonArray = new JSONArray();
        int type_of;
        public AdapterForShop(JSONArray jsonArray ,int type_of){
         this.jsonArray = jsonArray;
            this.type_of =type_of;
        }


        @Override
        public View getView(int i, View viewi, ViewGroup viewGroup) {
            try {
               final  JSONObject jsonobject = jsonArray.getJSONObject(i);
                LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = null;

                if(type_of==0){
                   view = inflate.inflate(R.layout.shoppageviewadder, null);
                }

                if(type_of == 1){
                   view = inflate.inflate(R.layout.shop_list_narrow, null);
                }
                if(type_of == 2){
                   view = inflate.inflate(R.layout.shop_list_grid, null);
                }




                LinearLayout linearLayout2 = ButterKnife.findById(view, R.id.linearlayoutasadder);
                final RelativeLayout relativeLayout = ButterKnife.findById(view, R.id.relativelayoutgridlayout);
                Glide.with(getApplicationContext()).load(jsonobject.getString("shop_bitmap_url")).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                        relativeLayout.setBackgroundDrawable(new BitmapDrawable(
                                getResources(), resource));

                    }
                });
                TextView textshopname = ButterKnife.findById(view, R.id.categoryname);
                textshopname.setText(jsonobject.getString("shop_name"));
                TextView textshopitem = ButterKnife.findById(view, R.id.itemincategory);
                StringBuilder stringBuilder = new StringBuilder();
                JSONArray jsonArray1 = jsonobject.getJSONArray("shop_item");
                for (int k = 0; k < jsonArray1.length(); k++) {
                    stringBuilder.append(jsonArray1.getJSONObject(k).getString("item_name") + "      ");
                }
                textshopitem.setText(stringBuilder.toString());
                stringBuilder.delete(0, stringBuilder.length());
                linearLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Omoyo.edit.putString("shop", jsonobject.toString());
                        Omoyo.edit.commit();
                        Intent intent = new Intent(getApplicationContext(), shoppage.class);
                        intent.putExtra("type_of", 2);
                        try {
                            intent.putExtra("_id", jsonobject.getString("shop_id"));
                        }
                        catch(JSONException jx){

                        }
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_transition_forword_in, R.anim.activity_transition_forword_out);
                    }
                });
                return view;
            }
            catch(JSONException jx){
                Log.d("TAGER",jx.getLocalizedMessage());
                return null;
            }

        }

        @Override
        public int getCount() {
            return jsonArray.length();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }
    }

    private void searchForShop(String key){
        JSONArray jsonArray = new JSONArray();
        for(int i=0;i<jsonarray1.length();i++){
            try {
                JSONObject jsonObject = jsonarray1.getJSONObject(i);
                if(jsonObject.getString("shop_name").toLowerCase().contains(key.toLowerCase())){
                    jsonArray.put(jsonArray.length(),jsonObject);
                }
            }
            catch(JSONException jx){

            }
        }
        jsonArrayTemp=jsonArray;
        gridView2.setAdapter(new AdapterForShop(jsonArray,type_of));
        gridView.setAdapter(new AdapterForShop(jsonArray,type_of));
        gridView1.setAdapter(new AdapterForShop(jsonArray,type_of));
    }

}

