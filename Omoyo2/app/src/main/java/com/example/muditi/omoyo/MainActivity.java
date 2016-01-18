package com.example.muditi.omoyo;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pkmmte.view.CircularImageView;
import com.rey.material.app.Dialog;
import com.rey.material.app.TimePickerDialog;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.MultipartBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements dialog_class.DialogListener{
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
    @Bind(R.id.linear_layout_for_search_result)
    LinearLayout linear_layout_for_search;
    @Bind(R.id.parentScrollView)
    ScrollView scrollView ;
    @Bind(R.id.navigation_view)
    NavigationView navigation_view;
    String location,temp_user_name , temp_user_email;
    int count;
    boolean query_submit_check=false;
    CircularImageView circularImageView;
    TextView text_view_for_user_name,  text_view_for_user_mobile_number;
    String binary64EncodeduserProfilePic;
    ImageView image_view_for_mobile_number_edit;
    boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Omoyo.checkingUserMobileSendedOrNotToServer(getApplicationContext());
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

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerlayout,toolbar,
                R.string.open_of_navigation_view_slider, R.string.close_of_navigation_view_slider){

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {


                super.onDrawerOpened(drawerView);
            }
        };


        drawerlayout.setDrawerListener(actionBarDrawerToggle);


        actionBarDrawerToggle.syncState();


        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch(id){
                    case R.id.are_you_a_shop:
                        menuItem.setChecked(true);
                        dialog_class dialog =  new dialog_class();
                        Bundle bundle = new Bundle();
                        bundle.putInt("type_of", 2);
                        dialog.setArguments(bundle);
                        dialog.show(getSupportFragmentManager(), "Hello");
                        break;
                    default:
                        Log.d("TAG","Checked");
                }
                return true;
            }
        });

        int headerCount =navigation_view.getHeaderCount();
        if(headerCount>0){
            Log.d("TAG", "HEADER_OF_NAVI");
            View navigation_viewHeaderView = navigation_view.getHeaderView(0);
            navigation_viewHeaderView.setBackgroundColor(getResources().getColor(R.color.appcolor));
            circularImageView = ButterKnife.findById(navigation_viewHeaderView,R.id.circular_image_view_for_dialog_header_user_profile);
            text_view_for_user_name=ButterKnife.findById(navigation_viewHeaderView,R.id.text_view_for_user_name);
            text_view_for_user_mobile_number=ButterKnife.findById(navigation_viewHeaderView,R.id.text_view_for_mobile_number);

            if(Omoyo.shared.contains("user_name")){
                text_view_for_user_name.setText(Omoyo.shared.getString("user_name","Hello Folks"));
            }
            if(Omoyo.shared.contains("userProfileImage")){
                byte[] decodedByte = Base64.decode(Omoyo.shared.getString("userProfileImage","1"), 0);
                Bitmap bit = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
                circularImageView.setImageBitmap(bit);
            }
            circularImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            Omoyo.Request_Code);

                }
            });
            ImageView image_view_for_user_name_edit = ButterKnife.findById(navigation_viewHeaderView,R.id.image_view_for_user_name_edit);
            image_view_for_user_name_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_class dialog =  new dialog_class();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type_of", 1);
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), "Hello");
                }
            });

           image_view_for_mobile_number_edit = ButterKnife.findById(navigation_viewHeaderView,R.id.image_view_for_mobile_number_edit);
            image_view_for_mobile_number_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Omoyo.shared.getBoolean("user_status",false)){
                        snackBar(3);
                        image_view_for_mobile_number_edit.setImageDrawable(getResources().getDrawable(R.mipmap.ic_lock_open_white_48dp));
                        text_view_for_user_mobile_number.setText(getResources().getString(R.string.welcome));
                        Omoyo.edit.putBoolean("user_status", false);
                        Omoyo.edit.commit();
                        Omoyo.edit.putString("user_mobile_number", getResources().getString(R.string.welcome));
                        Omoyo.edit.commit();
                        Omoyo.edit.putBoolean("user_mobile_number_sended_to_server_successfully", false);
                        Omoyo.edit.commit();
                    }
                    else {
                        drawerlayout.closeDrawer(Gravity.LEFT);
                        Intent intent = new Intent(getApplicationContext(), SmsVarification.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_transition_forword_in, R.anim.activity_transition_forword_out);
                    }
                }
            });


            ImageView image_view_for_locaiton = ButterKnife.findById(navigation_viewHeaderView,R.id.image_view_for_location);
            TextView text_view_for_location = ButterKnife.findById(navigation_viewHeaderView,R.id.text_view_for_location);

            location=Omoyo.shared.getString("area","Modinager")+","+Omoyo.shared.getString("city","Ghaziabad");
            text_view_for_location.setText(Omoyo.shared.getString("GpsLocation",location));

            image_view_for_locaiton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), firstpage.class);
                    intent.putExtra("fromMain", true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                 //   startActivity(intent);
                    overridePendingTransition(R.anim.activity_transition_forword_in, R.anim.activity_transition_forword_out);
                }
            });


            if(Omoyo.shared.contains("user_status")){
                status = Omoyo.shared.getBoolean("user_status",false);
                if(status){
                    image_view_for_mobile_number_edit.setImageDrawable(getResources().getDrawable(R.mipmap.ic_lock_outline_white_48dp));
                    text_view_for_user_mobile_number.setText(Omoyo.shared.getString("user_mobile_number",getResources().getString(R.string.welcome)));
                }
                else{
                    Log.d("TAG","Status"+status);
                }
            }

        }

        //request for category
          categoryloader();
          adsloader();
        //grid layout
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Omoyo.Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                bitmap.recycle();;
                bitmap=null;
                byte[] b = baos.toByteArray();
                binary64EncodeduserProfilePic= Base64.encodeToString(b, Base64.DEFAULT);
                Omoyo.edit.putString("userProfileImage",binary64EncodeduserProfilePic);
                Omoyo.edit.commit();
                byte[] decodedByte = Base64.decode(binary64EncodeduserProfilePic, 0);
                Bitmap bit = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
                circularImageView.setImageBitmap(bit);
                snackBar(2);
               // userProfileUploadToServer(binary64EncodeduserProfilePic);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    private class UriSerializer implements JsonSerializer<Uri> {
        public JsonElement serialize(Uri src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

   private  class UriDeserializer implements JsonDeserializer<Uri> {
        @Override
        public Uri deserialize(final JsonElement src, final Type srcType,
                               final JsonDeserializationContext context) throws JsonParseException {
            return Uri.parse(src.getAsString());
        }
    }

    @Override
    public void onSubmitOfUserData(DialogFragment dialog, String user_name, String user_email) {
        temp_user_email=user_email;
        temp_user_name=user_name;
        saveUserDataToServer();
    }

    public void saveUserDataToServer(){
        OkHttpClient okhttp=new OkHttpClient();
        String json=String.format("{\"user_id\" : \"%s\",\"user_name\" : \"%s\",\"user_email\" : \"%s\"}",Omoyo.shared.getString("user_id","1007"),temp_user_name,temp_user_email);
        final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/userNameDataEntry/").post(requestbody).build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        snackBar(0);
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String data = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("TAG", data);
                            text_view_for_user_name.setText(temp_user_name);
                        }
                    });
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_transition_backword_in, R.anim.activity_transition_backword_out);
    }

    private  void snackBar(final int i){
        final Snackbar snackbar =Snackbar.make(findViewById(R.id.drawerlayout), getResources().getString(R.string.internet_not_available), Snackbar.LENGTH_INDEFINITE);
        final View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.snackbar_back));
        final TextView textView =ButterKnife.findById(snackbarView,android.support.design.R.id.snackbar_text);
        final TextView textViewAction =ButterKnife.findById(snackbarView,android.support.design.R.id.snackbar_action);
        textView.setTextColor(Color.WHITE);
        snackbar.setText(R.string.internet_not_available);
        snackbar.setAction(getResources().getString(R.string.try_again), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(i){
                    case 0:
                        saveUserDataToServer();
                        break;
                    case 1:
                        userProfileUploadToServer(binary64EncodeduserProfilePic);
                        break;
                    default:
                        Log.d("TAG","Null");
                }

            }
        });
        snackbar.setDuration(Snackbar.LENGTH_LONG);
        textViewAction.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));

        switch(i){
            case 2:
                textView.setText(getResources().getString(R.string.profile_pic_changed));
                textViewAction.setText(getResources().getString(R.string.welcome));
                break;
            case 3:
                textView.setText(getResources().getString(R.string.logout_successfully));
                textViewAction.setText(getResources().getString(R.string.welcome));
                break;
            default:
                Log.d("TAG","Done null");

        }

        snackbar.show();
    }

    private void userProfileUploadToServer(final String binary64Encode){
        OkHttpClient okhttp=new OkHttpClient();
        String json=String.format("{\"user_id\" : \"%s\",\"user_profile_pic_binary64encoded\" : \"%s\"}", Omoyo.shared.getString("user_id", "1007"), binary64Encode);
        final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/userProfilePicDataEntry/").post(requestbody).build();
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
            public void onResponse(final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String data = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("TAG", data);
                            Omoyo.edit.putString("userProfileImage", binary64Encode);
                            Omoyo.edit.commit();
                            byte[] decodedByte = Base64.decode(binary64Encode, 0);
                            Bitmap bit = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
                            circularImageView.setImageBitmap(bit);
                            snackBar(2);
                        }
                    });
                }
            }
        });
    }


    @Override
    public void onDescriptionSubmited() {
        hideKeyboard();
        Log.d("TAG", "HIDE");
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }



    @Override
    public void onSubmitingOfferData(Uri uri, String string, String offerCode) {
        String getPath = getPath(uri);
        uploadingOfferToServer(getPath,string,offerCode);
        Log.d("TAG",getPath);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Omoyo.shared.contains("user_status")){
            status = Omoyo.shared.getBoolean("user_status",false);
            if(status){
                image_view_for_mobile_number_edit.setImageDrawable(getResources().getDrawable(R.mipmap.ic_lock_outline_white_48dp));
                text_view_for_user_mobile_number.setText(Omoyo.shared.getString("user_mobile_number",getResources().getString(R.string.welcome)));
            }
            else{
                Log.d("TAG","Status"+status);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
private void uploadingOfferToServer(String path , String description_of_offer , String offerCode ){

    final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

    RequestBody requestBody = new MultipartBuilder()
            .type(MultipartBuilder.FORM)
            .addFormDataPart("user_id",Omoyo.shared.getString("user_id","1007"))
            .addFormDataPart("description_of_offer", description_of_offer)
            .addFormDataPart("offer_code", offerCode)
            .addFormDataPart("offerpic", "offerpic.jpg", RequestBody.create(MEDIA_TYPE_JPG, new File(path)))
            .build();

    Request request=new Request.Builder().url("http://" + getResources().getString(R.string.ip) + "/offerDataEntry/").post(requestBody).build();
    OkHttpClient okhttp = new OkHttpClient();
    Call call=okhttp.newCall(request);
    call.enqueue(new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(Response response) throws IOException {

                      if(response.isSuccessful()){
                          String data = response.body().string();
                          Log.d("TAG",data);
                      }
        }
    });

}

}
