package com.example.muditi.deligoo;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.pkmmte.view.CircularImageView;
import com.rey.material.widget.ProgressView;
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
    @Bind(R.id.process_bar_for_search)
    ProgressView progress_bar_for_search;
    @Bind(R.id.grid_for_search)
    GridView grid_view_for_search;
    @Bind(R.id.parentScrollView)
    ScrollView scrollView ;
    @Bind(R.id.navigation_view)
    NavigationView navigation_view;
    @Bind(R.id.progress_bar_for_ads)
    ProgressView progress_bar_for_ads;
    @Bind(R.id.card_view_for_category)
    CardView card_view_for_category;
    SearchView searchView;
    Snackbar snackbar;
    String location,temp_user_name , temp_user_email;
    int count , i;
    boolean query_submit_check=false;
    CircularImageView circularImageView;
    TextView text_view_for_user_name,  text_view_for_user_mobile_number;
    String binary64EncodeduserProfilePic;
    ImageView image_view_for_mobile_number_edit;
    String path_of_offer_upload_pic_file,description_of_offer_upload,code_of_offer_uploadede;
    boolean status;
    dialog_class dialog;
    Bundle bundle , bundleForSearch;
    TextView text_view_for_offer_num,text_view_for_fav_num ,text_view_for_call_num ,text_view_for_message_num ,text_view_for_notification_num  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Omoyo.errorReportByMint(getApplicationContext());
        //gpsPosition
        if(!Omoyo.shared.contains("gpsposition")){
            downloadCoordinateOfShop();
        }

        Omoyo.checkingUserMobileSendedOrNotToServer(getApplicationContext());
        Display display=getWindowManager().getDefaultDisplay();
        Omoyo.screendisplay=display;
        Omoyo.widthofscreen=display.getWidth();
        Omoyo.heightofscreen=display.getHeight();
        Omoyo.edit.putInt("widthOfDevice", display.getWidth());
        Omoyo.edit.commit();
        Omoyo.edit.putInt("heightOfDevice", display.getHeight());
        Omoyo.edit.commit();
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setSubtitleTextColor(Color.WHITE);
        location=Omoyo.shared.getString("area","Modinager")+","+Omoyo.shared.getString("city","Ghaziabad");
        toolbar.setSubtitle(Omoyo.shared.getString("GpsLocation", location));
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




        Menu menu = navigation_view.getMenu();
        MenuItem item_offer = menu.findItem(R.id.offers);
        View offer_view = item_offer.getActionView();
        text_view_for_offer_num = ButterKnife.findById(offer_view, R.id.text_view_for_navigation_action_view);

        MenuItem item_fav = menu.findItem(R.id.favourites);
        View fav_view = item_fav.getActionView();
        text_view_for_fav_num = ButterKnife.findById(fav_view, R.id.text_view_for_navigation_action_view);

        MenuItem item_call = menu.findItem(R.id.call_log);
        View call_view = item_call.getActionView();
        text_view_for_call_num = ButterKnife.findById(call_view, R.id.text_view_for_navigation_action_view);

        MenuItem item_message = menu.findItem(R.id.message_log);
        View message_view = item_message.getActionView();
        text_view_for_message_num = ButterKnife.findById(message_view, R.id.text_view_for_navigation_action_view);

        MenuItem item_notification = menu.findItem(R.id.notification_point);
        View notification_view = item_notification.getActionView();
        text_view_for_notification_num = ButterKnife.findById(notification_view, R.id.text_view_for_navigation_action_view);
        try {
            JSONArray jsonArray_for_fav = new JSONArray(Omoyo.shared.getString("favorets", ""));
            text_view_for_fav_num.setText(""+jsonArray_for_fav.length());
        }
        catch(JSONException e){

        }
        try {
            JSONArray jsonArray_for_fav = new JSONArray(Omoyo.shared.getString("call_log", ""));
            text_view_for_call_num.setText(""+jsonArray_for_fav.length());
        }
        catch(JSONException e){

        }
        try {
            JSONArray jsonArray_for_fav = new JSONArray(Omoyo.shared.getString("sms_log", ""));
            text_view_for_message_num.setText(""+jsonArray_for_fav.length());
        }
        catch(JSONException e){

        }
        try {
            JSONArray jsonArray_for_fav = new JSONArray(Omoyo.shared.getString("system_notification", ""));
            text_view_for_notification_num.setText(""+jsonArray_for_fav.length());
        }
        catch(JSONException e){

        }

        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch(id){
                    case R.id.map_view:
                        Intent intent2 = new Intent(getApplicationContext(), MapView.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.activity_transition_forword_in, R.anim.activity_transition_forword_out);
                        break;
                    case R.id.offers:
                        Intent intent1 = new Intent(getApplicationContext(), Offer.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.activity_transition_forword_in, R.anim.activity_transition_forword_out);
                        break;
                    case R.id.are_you_a_shop:
                        menuItem.setChecked(true);
                        dialog =  new dialog_class();
                        bundle = new Bundle();
                        bundle.putInt("type_of", 2);
                        dialog.setArguments(bundle);
                        dialog.show(getSupportFragmentManager(), "Hello");
                        break;
                    case R.id.help:
                        menuItem.setChecked(true);
                        drawerlayout.closeDrawer(Gravity.LEFT);
                        Intent intent = new Intent(getApplicationContext(), Help.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_transition_forword_in, R.anim.activity_transition_forword_out);
                        break;
                    case R.id.social_media:
                        menuItem.setChecked(true);
                        dialog =  new dialog_class();
                        bundle = new Bundle();
                        bundle.putInt("type_of", 3);
                        dialog.setArguments(bundle);
                        dialog.show(getSupportFragmentManager(), "Hello");
                        break;
                    case R.id.favourites:
                        Intent intent_for_fave = new Intent(getApplicationContext(), Marked.class);
                        startActivity(intent_for_fave);
                        overridePendingTransition(R.anim.activity_transition_forword_in, R.anim.activity_transition_forword_out);
                        break;
                    case R.id.call_log:
                        Intent intent5 = new Intent(getApplicationContext(),Log_Activity.class);
                        intent5.putExtra("type_of",0);
                        startActivity(intent5);
                        break;
                    case R.id.message_log:
                        Intent intent3 = new Intent(getApplicationContext(),Log_Activity.class);
                        intent3.putExtra("type_of",1);
                        startActivity(intent3);
                        break;
                    case R.id.notification_point:
                        Intent intent4 = new Intent(getApplicationContext(),Log_Activity.class);
                        intent4.putExtra("type_of", 2);
                        startActivity(intent4);
                        break;
                    case R.id.share:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
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
                    try {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(
                                Intent.createChooser(intent, "Select File"),
                                Omoyo.Request_Code);
                    }
                    catch(Exception ex){

                    }
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
                        try {
                            Vibrator vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibrate.vibrate(200);
                        }
                        catch(Exception ex){

                        }
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



            TextView text_view_for_location = ButterKnife.findById(navigation_viewHeaderView,R.id.text_view_for_location);

            location=Omoyo.shared.getString("area","Modinager")+","+Omoyo.shared.getString("city","Ghaziabad");
            text_view_for_location.setText(Omoyo.shared.getString("GpsLocation", location));




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

        grid_view_for_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    TextView text_view_for_quick_search = ButterKnife.findById(view, R.id.text_view_for_quick_search);
                    String text_to_search = String.valueOf(text_view_for_quick_search.getText());
                    searchView.setQueryHint(text_to_search);
                    searchView.setQuery(text_to_search, false);
                    progress_bar_for_search.setVisibility(View.VISIBLE);
                    grid_view_for_search.setVisibility(View.GONE);
                    queryResponse(text_to_search);
                    hideKeyboard();
                } catch (Exception e) {
                    TextView text_view_for_shop_id = ButterKnife.findById(view, R.id.text_view_for_shop_id);
                    TextView text_view_for_type = ButterKnife.findById(view, R.id.item_type);
                    TextView text_view_for_product_id = ButterKnife.findById(view, R.id.text_view_for_product_id);
                    Omoyo.currentShopId = String.valueOf(text_view_for_shop_id.getText());
                    shopLoadForSearch(String.valueOf(text_view_for_type.getText()), String.valueOf(text_view_for_product_id.getText()));
                }
            }
        });



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
        searchView = (SearchView) item.getActionView();

        if(searchView != null) {

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    progress_bar_for_search.setVisibility(View.VISIBLE);
                    grid_view_for_search.setVisibility(View.GONE);
                    queryResponse(query);
                    query_submit_check=true;
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(newText.length()==0){
                     //   grid_view_for_search.removeAllViews();
                        grid_view_for_search.setAdapter(new SearchDataInsert("",getApplicationContext(),0));
                    }
                    else{
                        grid_view_for_search.setVisibility(View.GONE);
                        progress_bar_for_search.setVisibility(View.VISIBLE);
                        quick_search(newText);
                    }
                    return true;
                }
            });
            searchView.setIconified(true);
            searchView.setIconifiedByDefault(true);

            MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    scrollView.setVisibility(View.GONE);
                    grid_view_for_search.setVisibility(View.VISIBLE);
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.appcolor));
                    scrollView.setVisibility(View.VISIBLE);
                    grid_view_for_search.setVisibility(View.GONE);
                    progress_bar_for_search.setVisibility(View.GONE);
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
                          runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  snackBar(10);
                              }
                          });
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
                                progress_bar_for_ads.setVisibility(View.GONE);
                                horizontalscrollview.setVisibility(View.VISIBLE);
                                try {
                                        JSONArray jsonArray = new JSONArray(data);
                                    text_view_for_offer_num.setText(""+jsonArray.length());
                                    for(int i=0;i<jsonArray.length();i++){
                                        final JSONObject jsonObject=jsonArray.getJSONObject(i);
                                        LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                        View view = inflate.inflate(R.layout.adslayout, null);
                                        LinearLayout linearLayout2 = ButterKnife.findById(view, R.id.adslinearlayout);
                                        final  RelativeLayout relativeLayout = ButterKnife.findById(view, R.id.relativelayouthorizantalscroll);
                                        TextView textshopcaption=ButterKnife.findById(view,R.id.textshopcaption);
                                        textshopcaption.setText(jsonObject.getString("ads_description"));
                                        TextView textitem=ButterKnife.findById(view,R.id.textitem);
                                        final  TextView textshopid=ButterKnife.findById(view,R.id.textshopid);
                                        final  TextView text_view_for_ads_id=ButterKnife.findById(view,R.id.text_view_for_ads_id);
                                        textshopid.setText(jsonObject.getString("shop_id"));
                                        text_view_for_ads_id.setText(jsonObject.getString("ads_id"));
                                        StringBuilder stringBuilder=new StringBuilder();
                                        JSONArray jsonArray1=jsonObject.getJSONArray("ads_item");
                                        for(int k=0;k<jsonArray1.length();k++)
                                        {
                                              stringBuilder.append(jsonArray1.getJSONObject(k).getString("item_name")+"      ");
                                        }
                                        textitem.setText(stringBuilder.toString());
                                        stringBuilder.delete(0, stringBuilder.length());
                                        Glide.with(getApplicationContext()).load("https://storage.googleapis.com/deligoads/"+jsonObject.getString("ads_id")+".png")
                                                .asBitmap().into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                                relativeLayout.setBackgroundDrawable(new BitmapDrawable(
                                                        getResources(), resource));
                                                Log.d("TAGBACK", "Done");
                                            }

                                            @Override
                                            public void onLoadStarted(Drawable placeholder) {
                                                Log.d("TAGBACK", "STARTED");
                                            }

                                            @Override
                                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                                //  Log.d("TAGFAILED",""+e.getLocalizedMessage());
                                            }


                                        });

                                        linearLayout2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Omoyo.currentShopId=textshopid.getText().toString();
                                                shoploader(String.valueOf(text_view_for_ads_id.getText()));
                                                snackBar(8);
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
                       // Omoyo.toast("Error in the network", getApplicationContext());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                snackBar(10);
                            }
                        });
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

                            gridlayout.setVisibility(View.VISIBLE);
                            card_view_for_category.setVisibility(View.VISIBLE);
                           // progress_bar_for_category.setVisibility(View.GONE);
                            try {
                                JSONArray jsonarray = new JSONArray(data);
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    categoryshopcountloader(jsonobject , 0);
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
    public void categoryshopcountloader(final JSONObject category , final int type_of ) {
        try {
            Log.d("XX","X");
                                if(type_of==0) {

                                    LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                    View view = inflate.inflate(R.layout.shoppageviewadder, null);
                                    LinearLayout linearLayout2 = ButterKnife.findById(view, R.id.linearlayoutasadder);
                                    final RelativeLayout relativeLayout = ButterKnife.findById(view, R.id.relativelayoutgridlayout);
                                    TextView textcategoryname = ButterKnife.findById(view, R.id.categoryname);
                                    String value = category.getString("category_name").substring(0, 1).toUpperCase() + category.getString("category_name").substring(1, category.getString("category_name").length());
                                    textcategoryname.setText(value);
                                    StringBuilder builder = new StringBuilder();
                                    try {
                                        JSONArray jsonArray = category.getJSONArray("category_item");
                                        for (int k = 0; k < jsonArray.length(); k++) {
                                            String valueofitem = jsonArray.getJSONObject(k).getString("item_name").substring(0, 1).toUpperCase() + jsonArray.getJSONObject(k).getString("item_name").substring(1, jsonArray.getJSONObject(k).getString("item_name").length());
                                            builder.append(valueofitem + "       ");
                                        }
                                        TextView textitem = ButterKnife.findById(view, R.id.itemincategory);
                                        textitem.setText(builder.toString());
                                        builder.delete(0, builder.length());
                                    } catch (JSONException e) {

                                    }

                                    Glide.with(getApplicationContext()).load("https://storage.googleapis.com/deligocategory/"+category.getString("category_id")+".JPG").asBitmap().into(new SimpleTarget<Bitmap>() {
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
                                                 snackBar(8);
                                                shopListLoader(category.getString("category_id"),category.getString("category_name"));

                                            } catch (JSONException e) {
                                                 Log.d("EXXXX:",e.getLocalizedMessage());
                                            }
                                        }
                                    });
                                    gridlayout.addView(linearLayout2);
                                }
            if(type_of == 1){
                LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflate.inflate(R.layout.category_narrow_layout, null);
                LinearLayout linearLayout2 = ButterKnife.findById(view, R.id.linearlayoutasadder);
                final RelativeLayout relativeLayout = ButterKnife.findById(view, R.id.relativelayoutgridlayout);
                TextView textshopcount = ButterKnife.findById(view, R.id.shopcount);
                textshopcount.setText("");
                TextView textcategoryname = ButterKnife.findById(view, R.id.categoryname);
                String value = category.getString("category_name").substring(0, 1).toUpperCase() + category.getString("category_name").substring(1, category.getString("category_name").length());
                textcategoryname.setText(value);
                StringBuilder builder = new StringBuilder();
                try {
                    JSONArray jsonArray = category.getJSONArray("category_item");
                    for (int k = 0; k < jsonArray.length(); k++) {
                        String valueofitem = jsonArray.getJSONObject(k).getString("item_name").substring(0, 1).toUpperCase() + jsonArray.getJSONObject(k).getString("item_name").substring(1, jsonArray.getJSONObject(k).getString("item_name").length());
                        builder.append(valueofitem + "       ");
                    }
                    TextView textitem = ButterKnife.findById(view, R.id.itemincategory);
                    textitem.setText(builder.toString());
                    builder.delete(0, builder.length());
                } catch (JSONException e) {

                }

                Glide.with(getApplicationContext()).load("https://storage.googleapis.com/deligocategory/"+category.getString("category_id")+".JPG").asBitmap().into(new SimpleTarget<Bitmap>() {
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
                            snackBar(8);
                            shopListLoader(category.getString("category_id"),category.getString("category_name"));

                        } catch (JSONException e) {

                        }
                    }
                });
                gridlayout.addView(linearLayout2);
            }

                            } catch (JSONException e) {
                         Omoyo.toast("Error in json",getApplicationContext());
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
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       snackBar(9);
                                   }
                               });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String data = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(snackbar.isShown()){
                                snackbar.dismiss();
                            }
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
                                overridePendingTransition(R.anim.activity_transition_forword_in, R.anim.activity_transition_forword_out);
                            }
                        });
                    }
                }
            });

    }
private void shopListLoader(final String category_id , final String category_name)
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
                   snackBar(8);
                }
            });
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if (response.isSuccessful()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(snackbar.isShown()){
                            snackbar.dismiss();
                        }
                    }
                });
                final String data = response.body().string();
                Omoyo.edit.putString("shoplist", data);
                Omoyo.edit.commit();
                Intent intent = new Intent(getApplicationContext(), shoplist.class);
                intent.putExtra("category_id", category_id);
                intent.putExtra("category_name", category_name);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_transition_forword_in, R.anim.activity_transition_forword_out);
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
                    progress_bar_for_search.setVisibility(View.GONE);
                //    grid_view_for_search.setVisibility(View.GONE);
                    snackBar(7);
                }
            });
        }

        @Override
        public void onResponse( Response response) throws IOException {
            if(response.isSuccessful()){
                final String data = response.body().string();
                Omoyo.currentSerachData = data;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress_bar_for_search.setVisibility(View.GONE);
                        grid_view_for_search.setVisibility(View.VISIBLE);
                        grid_view_for_search.setAdapter(new SearchDataInsert(data,getApplicationContext(),1));
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
        Call call = okhttp.newCall(request);
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
        snackbar =Snackbar.make(findViewById(R.id.drawerlayout), getResources().getString(R.string.internet_not_available), Snackbar.LENGTH_INDEFINITE);
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
                    case 4:
                        uploadingOfferToServer(path_of_offer_upload_pic_file, description_of_offer_upload, code_of_offer_uploadede);
                        break ;
                    case 5:
                        makeCallToOMOYoo();
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
            case 5:
                textView.setText("Successfully submited /n OFFER CODE - "+ code_of_offer_uploadede );
                textViewAction.setText(getResources().getString(R.string.contact_us));
                snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                break;
            case 6:
                textView.setText(getResources().getString(R.string.submiting)+"  ...");
                textView.setTextSize(20);
                textViewAction.setText(getResources().getString(R.string.welcome));
                break;
            case 7:
                textViewAction.setText("");
                break;
            case 8:
                textView.setText(getResources().getString(R.string.loading));
                textView.setTextSize(20);
                textViewAction.setText(getResources().getString(R.string.welcome));
                snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                break;
            case 9:
                textViewAction.setText(getResources().getString(R.string.welcome));
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
        Call call = okhttp.newCall(request);
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
        path_of_offer_upload_pic_file= getPath(uri);
        description_of_offer_upload = string;
        code_of_offer_uploadede = offerCode;
        snackBar(6);
        uploadingOfferToServer(path_of_offer_upload_pic_file,description_of_offer_upload,code_of_offer_uploadede);
        Log.d("TAG", path_of_offer_upload_pic_file);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Google_analytics_class.getInstance().trackScreenView("Main Activity View");

        try {
            JSONArray jsonArray_for_fav = new JSONArray(Omoyo.shared.getString("favorets", ""));
            text_view_for_fav_num.setText(""+jsonArray_for_fav.length());
        }
        catch(JSONException e){

        }
        try {
            JSONArray jsonArray_for_fav = new JSONArray(Omoyo.shared.getString("call_log", ""));
            text_view_for_call_num.setText(""+jsonArray_for_fav.length());
        }
        catch(JSONException e){

        }
        try {
            JSONArray jsonArray_for_fav = new JSONArray(Omoyo.shared.getString("sms_log", ""));
            text_view_for_message_num.setText(""+jsonArray_for_fav.length());
        }
        catch(JSONException e){

        }
        try {
            JSONArray jsonArray_for_fav = new JSONArray(Omoyo.shared.getString("system_notification", ""));
            text_view_for_notification_num.setText(""+jsonArray_for_fav.length());
        }
        catch (JSONException e){

        }

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
            snackBar(4);
        }

        @Override
        public void onResponse(Response response) throws IOException {

            if (response.isSuccessful()) {
                String data = response.body().string();
                Log.d("TAG", data);
                snackBar(5);
            }
        }
    });

}

    private void makeCallToOMOYoo(){

        CallToOMOYooStateListener phoneListener = new CallToOMOYooStateListener();
        TelephonyManager telephonyManager =
                (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
        try {
            String uri = "tel:"+Omoyo.shared.getString("OMOYoo_contact_number","100");
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
            startActivity(callIntent);
        }catch(Exception e) {
            Log.d("TAG","Error:"+e.getMessage());
            e.printStackTrace();
        }
    }


    private class CallToOMOYooStateListener extends android.telephony.PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            Log.d("TAGFORCALL",incomingNumber+"STATEOFCALL "+state);
        }
    }

    @Override
    public void onSubmitingFilterData(String category, Integer distance) {

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
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                             //  snackBar(7);
                           }
                       });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                          final String data = response.body().string();
                          runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  Omoyo.edit.putString("gpsposition", data);
                                  Omoyo.edit.commit();
                                 // calculateDistance();
                              }
                          });
                }
            });
        }
        catch(JSONException ex) {

        }
    }


    private void calculateDistance(){
               final JSONArray putArray = new JSONArray();

        try {
            final JSONArray jsonArray = new JSONArray(Omoyo.shared.getString("coordinateOfShop", "coordinate"));
            for(i=0;i<jsonArray.length();i++) {
                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                OkHttpClient okhttp = new OkHttpClient();
                StringBuilder builder = new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json?");
                builder.append("origins=" + Omoyo.shared.getString("latitude", "41.000") + "," + Omoyo.shared.getString("longitude", "41.0"));
                builder.append("&");
                builder.append("destinations=" +jsonObject.getString("shop_latitude")+","+jsonObject.getString("shop_longitude"));
                builder.append("&");
                builder.append("language=en");
                builder.append("&");
                builder.append("mode=driving");
                builder.append("&");
                builder.append("key="+getResources().getString(R.string.app_key));
                Request request = new Request.Builder().url(builder.toString()).get().build();
                Call call = okhttp.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        i=jsonArray.length();
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {

                        if(response.isSuccessful()) {
                            final String data = response.body().string();
                            try {
                                JSONObject putObject = new JSONObject();
                                JSONObject jsonObject1 = new JSONObject(data);
                                if(jsonObject1.getString("status").equals("OK"))
                                try {
                                        putObject.put("shop_id", jsonObject.getString("shop_id"));
                                        putObject.put("distance", jsonObject1.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getString("value"));
                                        putArray.put(putArray.length(),putObject);
                                } catch (JSONException jx) {

                                }
                            }
                            catch(JSONException jj){

                            }


                            if(i==jsonArray.length()-1){
                                Omoyo.edit.putString("distance",putArray.toString());
                                Omoyo.edit.commit();
                            }
                        }
                    }
                });
            }
        }
        catch(JSONException jx){

        }

    }
private void quick_search(String s){
    OkHttpClient okhttp=new OkHttpClient();
    String json=String.format("{\"quick_query\" : \"%s\"}", s );
    final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
    RequestBody requestbody=RequestBody.create(JSON, json);
    Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/quickSearch/").post(requestbody).build();
    Call call=okhttp.newCall(request);
    call.enqueue(new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    snackBar(7);
                    progress_bar_for_search.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onResponse(Response response) throws IOException {
                 if(response.isSuccessful())
                 {
                  final  String data = response.body().string();
                     Log.d("TAG",data) ;
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             grid_view_for_search.setVisibility(View.VISIBLE);
                             progress_bar_for_search.setVisibility(View.GONE);
                             grid_view_for_search.setAdapter(new SearchDataInsert(data, getApplicationContext(), 2));
                         }
                     });
                 }
        }
    });
}


    private void shopLoadForSearch(final String type_of , final String product_id){
        OkHttpClient okhttp=new OkHttpClient();
        String json=String.format("{\"shop_id\" : \"%s\"}", Omoyo.currentShopId);
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
                              snackBar(7);
                          }
                      });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(data);
                        Omoyo.edit.putString("shop", jsonArray.getJSONObject(0).toString());
                        Omoyo.edit.commit();
                    } catch (JSONException e) {

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), shoppage.class);
                            if(type_of.toLowerCase().equals("shop")){
                                intent.putExtra("type_of",2);
                                intent.putExtra("_id",Omoyo.currentShopId);
                            }
                            else{
                                intent.putExtra("type_of",1);
                                intent.putExtra("_id",product_id);
                            }
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_transition_forword_in, R.anim.activity_transition_forword_out);
                        }
                    });
                }
            }
        });
    }

}
