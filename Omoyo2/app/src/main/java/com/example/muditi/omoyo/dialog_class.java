package com.example.muditi.omoyo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.rey.material.widget.Button;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
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
    public boolean view_flipper_status = false;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        int type_of = bundle.getInt("type_of");
        pattern = Pattern.compile(EMAIL_PATTERN);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog alertDialog =builder.create();
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL);
        LayoutInflater inflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =inflater.inflate(R.layout.dialog_layout, null);
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

                linear_layout_for_offer_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!view_flipper_status) {
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(
                                    Intent.createChooser(intent, "Select File"),
                                    Omoyo.Request_Code);
                        } else {

                            view_flipper_for_offer_upload.setInAnimation(getActivity(), R.anim.activity_transition_forword_in);
                            view_flipper_for_offer_upload.setOutAnimation(getActivity(), R.anim.activity_transition_forword_out);
                            view_flipper_status = false;
                            view_flipper_for_offer_upload.showNext();

                        }
                    }
                });

                    linear_layout_for_offer_upload_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                              dialogListener.onSubmitingOfferData(Omoyo.uri,Omoyo.offer_description,Omoyo.generatedOfferCode);
                    }
                });

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
}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dialogListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
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
                view_flipper_status = true;
              //  view_flipper_for_offer_upload.showNext();
                // userProfileUploadToServer(binary64EncodeduserProfilePic);
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
}
