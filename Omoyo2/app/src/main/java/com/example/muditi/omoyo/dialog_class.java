package com.example.muditi.omoyo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rey.material.widget.Button;

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

    public dialog_class(){
        super();
    }

public DialogListener dialogListener;

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
                LayoutInflater inflate = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view_for_child = inflate.inflate(R.layout.activity_for_user_name_enter,null);
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
}
