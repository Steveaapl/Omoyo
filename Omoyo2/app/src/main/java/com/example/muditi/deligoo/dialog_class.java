package com.example.muditi.deligoo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.rey.material.widget.Button;
import com.rey.material.widget.Slider;
import com.rey.material.widget.Spinner;
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
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;


/**
 * Created by muditi on 15-01-2016.
 */
public class dialog_class extends DialogFragment {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern ;
    private Matcher matcher;
    private  LayoutInflater inflate;
    private View view_for_child;
    public dialog_class(){
        super();
    }
    public ImageView image_view_for_offer_upload;
    public TextView text_view_for_next_in_offer_upload;
    public DialogListener dialogListener;
    public  ViewFlipper view_flipper_for_offer_upload ;
    public boolean view_flipper_status = true;
    private int distance=0 ;
    private String category;
    Bundle bundle;
    private  boolean flager =true;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bundle = getArguments();
        int type_of = bundle.getInt("type_of");
        pattern = Pattern.compile(EMAIL_PATTERN);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog alertDialog =builder.create();
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL);
        LayoutInflater inflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =inflater.inflate(R.layout.dialog_layout, null);
        TextView text_view_for_welcome_in_dialog = ButterKnife.findById(view,R.id.text_view_for_welcome_in_dialog);
        LinearLayout linear_layout_for_enter_child =ButterKnife.findById(view,R.id.linear_layout_for_dialog_child_insert);
        ImageView image_view_for_cross = ButterKnife.findById(view, R.id.image_view_for_cross);
        Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.dialog_box_cross_rotation);
        image_view_for_cross.setAnimation(animation);
        image_view_for_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
                    alertDialog.setView(view);
                    setCancelable(false);

        switch(type_of){
            case 1:
                inflate = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view_for_child = inflate.inflate(R.layout.activity_for_user_name_enter,null);
                Button button = ButterKnife.findById(view_for_child, R.id.doneenteringdata);
                final TextInputLayout text_input_view_for_user_name = ButterKnife.findById(view_for_child,R.id.text_input_view_for_user_name);
                final TextInputLayout text_input_view_for_user_email = ButterKnife.findById(view_for_child,R.id.text_input_view_for_user_email_address);
                text_input_view_for_user_email.getEditText().setText(getEmail(getActivity().getApplicationContext()));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideKeyboard();
                        String user_name = text_input_view_for_user_name.getEditText().getText().toString();
                        String user_email = text_input_view_for_user_email.getEditText().getText().toString();
                        if(!validateEmail(user_email)){
                            text_input_view_for_user_email.setError("Not a valid Email Address");
                        }
                        else if(user_name.length() > 7){
                            text_input_view_for_user_name.setError("User Name < 8 char");
                        }
                        else if(user_name.length()<2){
                            text_input_view_for_user_name.setError("User Name  > 2 char");
                        }
                        else if(!valideUserName(user_name)){
                            text_input_view_for_user_name.setError("User Name should not contain special character");
                        }
                        else {
                            text_input_view_for_user_email.setErrorEnabled(false);
                            text_input_view_for_user_name.setErrorEnabled(false);
                            dialogListener.onSubmitOfUserData(dialog_class.this,user_name,user_email);
                            alertDialog.cancel();
                        }

                    }
                });
                linear_layout_for_enter_child.addView(view_for_child);
                break;
            case 2:
                inflate = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view_for_child = inflate.inflate(R.layout.shop_offer_upload, null);
                view_flipper_for_offer_upload = ButterKnife.findById(view_for_child, R.id.view_flipper_for_offer_upload);
                LinearLayout linear_layout_for_offer_upload = ButterKnife.findById(view_for_child,R.id.linear_layout_for_offer_upload);
                image_view_for_offer_upload=ButterKnife.findById(view_for_child,R.id.image_view_for_image_upload);
                text_view_for_next_in_offer_upload = ButterKnife.findById(view_for_child,R.id.text_view_for_next_in_offer_upload);
                LinearLayout linear_layout_for_offer_upload_description = ButterKnife.findById(view_for_child,R.id.linear_layout_for_offer_upload_description);
                LinearLayout linear_layout_for_offer_upload_send = ButterKnife.findById(view_for_child,R.id.linear_layout_for_offer_upload_send);
                final TextView text_view_for_next_in_offer_upload_description = ButterKnife.findById(view_for_child,R.id.text_view_for_next_in_offer_upload_description);
                final AppCompatEditText edit_text_for_offer_upload_description = ButterKnife.findById(view_for_child,R.id.edit_view_for_offer_upload_description);
                final TextView text_view_for_shop_alert_on_uploaded_offer = ButterKnife.findById(view_for_child,R.id.text_view_for_shop_alert_on_uploaded_offer);
                edit_text_for_offer_upload_description.setText(Omoyo.offer_description);
                linear_layout_for_offer_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!view_flipper_status){
                            view_flipper_for_offer_upload.setInAnimation(getActivity(), R.anim.activity_transition_forword_in);
                            view_flipper_for_offer_upload.setOutAnimation(getActivity(), R.anim.activity_transition_forword_out);
                            view_flipper_status = false;
                            view_flipper_for_offer_upload.showNext();
                        }
                    }
                });
                linear_layout_for_offer_upload_description.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(edit_text_for_offer_upload_description.getText().toString().length()>10){
                            Omoyo.offer_description = edit_text_for_offer_upload_description.getText().toString();
                                     dialogListener.onDescriptionSubmited();
                                     view_flipper_for_offer_upload.showNext();
                            Omoyo.generatedOfferCode = OfferCodeGenerator();
                            text_view_for_shop_alert_on_uploaded_offer.setText("Offer Code - " + Omoyo.generatedOfferCode + "\n" + getResources().getString(R.string.contact_us_after_submiting));
                        }
                        else{
                            text_view_for_next_in_offer_upload_description.setText(getActivity().getResources().getString(R.string.say_something_atleast));
                        }

                    }
                });

                edit_text_for_offer_upload_description.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if (edit_text_for_offer_upload_description.getText().toString().length() >= 10) {
                            text_view_for_next_in_offer_upload_description.setText(getActivity().getResources().getString(R.string.next));
                        }
                        return false;
                    }
                });

                image_view_for_offer_upload.setOnClickListener(new View.OnClickListener() {
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
                    linear_layout_for_offer_upload_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                              dialogListener.onSubmitingOfferData(Omoyo.uri,Omoyo.offer_description,Omoyo.generatedOfferCode);
                              alertDialog.cancel();
                    }
                });
                linear_layout_for_enter_child.addView(view_for_child);
                break;
            case 3:
                inflate = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view_for_child = inflate.inflate(R.layout.social_media_layout, null);
                ImageView circular_image_view_for_facebook = ButterKnife.findById(view_for_child,R.id.circular_image_view_for_facebook);
                ImageView circular_image_view_for_twitter = ButterKnife.findById(view_for_child,R.id.circular_image_view_for_twitter);
                ImageView circular_image_view_for_pinterest = ButterKnife.findById(view_for_child,R.id.circular_image_view_for_pinterest);
                ImageView circular_image_view_for_google = ButterKnife.findById(view_for_child,R.id.circular_image_view_for_google);
                ImageView circular_image_view_for_instagram = ButterKnife.findById(view_for_child,R.id.circular_image_view_for_instagram);
                ImageView circular_image_view_for_blog = ButterKnife.findById(view_for_child,R.id.circular_image_view_for_blog);
                circular_image_view_for_facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + getResources().getString(R.string.facebook_page_link)));
                        startActivity(browserIntent);
                    }
                });
                circular_image_view_for_twitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+getResources().getString(R.string.facebook_page_link)));
                        startActivity(browserIntent);
                    }
                });
                circular_image_view_for_pinterest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+getResources().getString(R.string.pinterest_page_link)));
                        startActivity(browserIntent);
                    }
                });
                circular_image_view_for_google.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+getResources().getString(R.string.google_page_link)));
                        startActivity(browserIntent);
                    }
                });
                circular_image_view_for_instagram.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+getResources().getString(R.string.instagram_page_link)));
                        startActivity(browserIntent);
                    }
                });
                circular_image_view_for_blog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+getResources().getString(R.string.blog_page_link)));
                        startActivity(browserIntent);
                    }
                });
                linear_layout_for_enter_child.addView(view_for_child);
                break;
            case 4:
                inflate = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view_for_child = inflate.inflate(R.layout.offer_filter_dialog_layout, null);
               final TextView text_view_for_offer_filter_by_distance = ButterKnife.findById(view_for_child,R.id.text_view_for_offer_filter_by_distance);
                final TextView text_view_for_offer_filter_by_category = ButterKnife.findById(view_for_child,R.id.text_view_for_offer_filter_by_category);
                Slider slider_for_offer_filter_by_category = ButterKnife.findById(view_for_child,R.id.slider_for_offer_filter_by_distance);
                final ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("Restaurants");
                arrayList.add("Entertainment");
                arrayList.add("Medical");
                arrayList.add("Travel");
                arrayList.add("Flight's");
                arrayList.add("Bus");
                arrayList.add("Hotel");
                arrayList.add("Car , Cab , Taxi");
                arrayList.add("Movie");
                arrayList.add("Doctor");
                arrayList.add("Hospital");
                arrayList.add("Chemists");
                arrayList.add("Flowers");
                arrayList.add("Labs");
                arrayList.add("Daily Needs");
                arrayList.add("Personal Care");
                arrayList.add("Courier");
                Spinner spinner_for_filter_of_offer = ButterKnife.findById(view_for_child,R.id.spinner_for_offer_filter_by_category);
                ArrayAdapter adapterforcity=new firstpagespinneradapter("City",getContext(),R.layout.offer_filter_spinner_layout,arrayList);
                spinner_for_filter_of_offer.setAdapter(adapterforcity);
                text_view_for_welcome_in_dialog.setText(getResources().getString(R.string.apply_filter));
                text_view_for_welcome_in_dialog.setTextColor(getResources().getColor(R.color.ebonyclay));
                linear_layout_for_enter_child.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                linear_layout_for_enter_child.addView(view_for_child);


                spinner_for_filter_of_offer.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(Spinner parent, View view, int position, long id) {
                        text_view_for_offer_filter_by_category.setText(getResources().getString(R.string.category) + " " + arrayList.get(position));
                        category = arrayList.get(position);
                        dialogListener.onSubmitingFilterData(category, Integer.valueOf(distance));
                    }
                });


                slider_for_offer_filter_by_category.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
                    @Override
                    public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                           text_view_for_offer_filter_by_distance.setText(getResources().getString(R.string.distance)+" "+ newValue +"  meters");
                           distance = newValue;
                        dialogListener.onSubmitingFilterData(category,Integer.valueOf(distance));
                    }
                });
               break;
            case 5:
                final String id = bundle.getString("_id");
                inflate = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view_for_child = inflate.inflate(R.layout.ads_show_on_dialog_layout, null);
                try{
                    JSONArray jsonArray = new JSONArray(Omoyo.shared.getString("ads","ads"));
                    for(int i =0; i<jsonArray.length() ; i++){
                        final JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(jsonObject.getString("ads_id").equals(id)){
                            JSONArray jsonArray1 = jsonObject.getJSONArray("ads_item");
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                            TextView text_view_for_item_name_dialog_of_shop_page = ButterKnife.findById(view_for_child, R.id.text_view_for_item_name_dialog_of_shop_page);
                          text_view_for_item_name_dialog_of_shop_page.setText(jsonObject1.getString("item_name"));
                            TextView text_view_for_item_price_dialog_of_shop_page = ButterKnife.findById(view_for_child,R.id.text_view_for_item_price_dialog_of_shop_page);
                           text_view_for_item_price_dialog_of_shop_page.setText("Price - "+jsonObject1.getString("item_price"));
                            TextView text_view_for_item_offer_dialog_of_shop_page = ButterKnife.findById(view_for_child,R.id.text_view_for_item_offer_dialog_of_shop_page);
                         text_view_for_item_offer_dialog_of_shop_page.setText(jsonObject1.getString("item_offer"));
                            TextView text_view_for_item_description_dialog_of_shop_page = ButterKnife.findById(view_for_child,R.id.text_view_for_item_description_dialog_of_shop_page);
                        text_view_for_item_description_dialog_of_shop_page.setText(jsonObject.getString("ads_description"));
                            final ImageView image_view_for_adding_favorets = ButterKnife.findById(view_for_child,R.id.image_view_for_adding_offer_as_favorets);

                            try{
                                JSONArray jsonArray2 = new JSONArray(Omoyo.shared.getString("favorets",""));
                                Log.d("X",jsonArray2.toString());
                                for(int k=0 ; k<jsonArray2.length() ;k++){
                                    JSONObject jsonObject2 = jsonArray2.getJSONObject(k);
                                    Log.d("XX",jsonObject2.toString());
                                    if(jsonObject2.getString("type_of").equals("0")){
                                        JSONObject jsonObject3 = jsonObject2.getJSONObject("data");
                                        Log.d("XXX",jsonObject3.toString());
                                        if(jsonObject3.getString("ads_id").equals(jsonObject.getString("ads_id"))){
                                            Log.d("XXXX","XX");
                                            image_view_for_adding_favorets.setImageDrawable(getResources().getDrawable(R.mipmap.ic_favorite_black_48dp));
                                            saveUserDataToServerOfAds(id);
                                            flager=false;
                                        }
                                    }
                                }
                            }
                            catch(JSONException jx){
                                Log.d("TAGER",jx.getMessage());
                            }
                            if(flager)
                            image_view_for_adding_favorets.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(flager) {
                                        image_view_for_adding_favorets.setImageDrawable(getResources().getDrawable(R.mipmap.ic_favorite_black_48dp));
                                        Omoyo.addtofavorets(0, jsonObject);
                                        flager=false;
                                    }

                                }
                            });
                            final RelativeLayout relativeLayout= ButterKnife.findById(view_for_child, R.id.relative_layout_for_item_show);
                            Glide.with(getContext()).load(jsonObject.getString("ads_bitmap_url")).asBitmap().into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    relativeLayout.setBackgroundDrawable(new BitmapDrawable(
                                            getResources(), resource));
                                }
                            });
                        }
                    }
                }
                catch(JSONException jx){

                }
                linear_layout_for_enter_child.addView(view_for_child);
                break;
            case 6:
              final   String ids = bundle.getString("_id");
                inflate = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view_for_child = inflate.inflate(R.layout.ads_show_on_dialog_layout, null);
                try{
                    JSONArray jsonArray = new JSONArray(Omoyo.currentSerachData);
                    for(int i =0; i<jsonArray.length() ; i++){
                      final   JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(jsonObject.has("product_id"))
                        if(jsonObject.getString("product_id").equals(ids)){
                            TextView text_view_for_item_name_dialog_of_shop_page = ButterKnife.findById(view_for_child, R.id.text_view_for_item_name_dialog_of_shop_page);
                            text_view_for_item_name_dialog_of_shop_page.setText(jsonObject.getString("product_name"));
                            TextView text_view_for_item_price_dialog_of_shop_page = ButterKnife.findById(view_for_child,R.id.text_view_for_item_price_dialog_of_shop_page);
                            text_view_for_item_price_dialog_of_shop_page.setText("Price - "+jsonObject.getString("product_price"));
                            TextView text_view_for_item_offer_dialog_of_shop_page = ButterKnife.findById(view_for_child,R.id.text_view_for_item_offer_dialog_of_shop_page);
                            text_view_for_item_offer_dialog_of_shop_page.setText(jsonObject.getString("product_offer"));
                            TextView text_view_for_item_description_dialog_of_shop_page  =  ButterKnife.findById(view_for_child,R.id.text_view_for_item_description_dialog_of_shop_page);
                            text_view_for_item_description_dialog_of_shop_page.setText(jsonObject.getString("product_description"));
                            final RelativeLayout relativeLayout= ButterKnife.findById(view_for_child, R.id.relative_layout_for_item_show);
                            final ImageView image_view_for_adding_favorets = ButterKnife.findById(view_for_child,R.id.image_view_for_adding_offer_as_favorets);

                            try{
                                JSONArray jsonArray2 = new JSONArray(Omoyo.shared.getString("favorets",""));
                                Log.d("X",jsonArray2.toString());
                                for(int k=0 ; k<jsonArray2.length() ;k++){
                                    JSONObject jsonObject2 = jsonArray2.getJSONObject(k);
                                    Log.d("XX",jsonObject2.toString());
                                    if(jsonObject2.getString("type_of").equals("1")){
                                        JSONObject jsonObject3 = jsonObject2.getJSONObject("data");
                                        Log.d("XXX",jsonObject3.toString());
                                        if(jsonObject3.getString("product_id").equals(jsonObject.getString("product_id"))){
                                            Log.d("XXXXX","true");
                                            image_view_for_adding_favorets.setImageDrawable(getResources().getDrawable(R.mipmap.ic_favorite_black_48dp));
                                            saveUserDataToServerOfProduct(ids);
                                            flager=false;
                                        }
                                    }
                                }
                            }
                            catch(JSONException jx){
                                Log.d("EROR",jx.getMessage());
                            }
                            if(flager)
                                image_view_for_adding_favorets.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(flager) {
                                            image_view_for_adding_favorets.setImageDrawable(getResources().getDrawable(R.mipmap.ic_favorite_black_48dp));
                                            Omoyo.addtofavorets(1, jsonObject);
                                            flager=false;
                                        }
                                        else{
                                            flager = false;
                                        }
                                    }
                                });
                            Glide.with(getContext()).load(jsonObject.getString("product_bitmap_url")).asBitmap().into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    relativeLayout.setBackgroundDrawable(new BitmapDrawable(
                                            getResources(), resource));
                                }
                            });
                        }
                    }
                }
                catch(JSONException jx){

                }
                linear_layout_for_enter_child.addView(view_for_child);
                break;
            default:
                Log.d("TAG", "Child Attached !");
        }


                    return alertDialog;
                }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean valideUserName(String userName){
        String rex ="[[a-z][A-Z][0-9][@$]]{2,7}";
        Pattern patt = Pattern.compile(rex);
        Log.d("TAG",""+patt.matcher(userName).matches());
        return   patt.matcher(userName).matches();
    }

    static String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);

        if (account == null) {
            return "";
        } else {
            return account.name;
        }
    }

    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

public interface  DialogListener{
    public void onSubmitOfUserData(DialogFragment dialog , String user_name , String user_email);
    public void onDescriptionSubmited();
    public void onSubmitingOfferData(Uri uri, String string , String offerCode);
    public void onSubmitingFilterData(String category , Integer distance);
}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dialogListener = (DialogListener) activity;
        } catch (ClassCastException e) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Omoyo.Request_Code && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

            Omoyo.uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),Omoyo.uri);
                image_view_for_offer_upload.setImageBitmap(bitmap);
                text_view_for_next_in_offer_upload.setText(getActivity().getResources().getString(R.string.next));
                view_flipper_status = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String OfferCodeGenerator()
    {
        String random=null;
        SecureRandom sr = new SecureRandom();
        Integer value =new Integer(sr.nextInt());
        if(value<0){
            String tmp=value.toString();
            if(tmp.length()>6){
                random=tmp.substring(1,6);
            }
            else{
                random=tmp.substring(1,tmp.length());
            }
        }
        else{
            String tmp=value.toString();
            if(tmp.length()>5){
                random=tmp.substring(0,5);
            }
            else{
                random=tmp.substring(0,tmp.length());
            }
        }
        Omoyo.edit.putString("OTPGenerated", random);
        Omoyo.edit.commit();
        return random;

    }


    public void saveUserDataToServerOfProduct(String product_id){
        OkHttpClient okhttp=new OkHttpClient();
        String json=String.format("{\"user_id\" : \"%s\",\"product_id\" : \"%s\",\"time\" : \"%s\"}",Omoyo.shared.getString("user_id","1007"),product_id,Omoyo.Date());
        final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/userProductFaverote/").post(requestbody).build();
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


    public void saveUserDataToServerOfAds(String ads_id){
        OkHttpClient okhttp=new OkHttpClient();
        String json=String.format("{\"user_id\" : \"%s\",\"ads_id\" : \"%s\",\"time\" : \"%s\"}",Omoyo.shared.getString("user_id","1007"),ads_id,Omoyo.Date());
        final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/userAdsFaverote/").post(requestbody).build();
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
}