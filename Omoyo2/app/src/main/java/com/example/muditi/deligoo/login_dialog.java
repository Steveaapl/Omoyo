package com.example.muditi.deligoo;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by muditi on 04-02-2016.
 */
public class login_dialog extends DialogFragment {

    login_interface login;

    public interface login_interface{
        public void oncancle();
        public void onlogin();
    }

    public login_dialog(){
        super();
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.AlertDialog.Builder  dialog = new android.app.AlertDialog.Builder(getContext());
        dialog.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setMessage(getResources().getString(R.string.message_for_login));
        dialog.setCancelable(false);
        dialog.setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                login.onlogin();
            }
        });
        dialog.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                login.oncancle();
            }
        });
        Dialog dialog1 = dialog.create();
        dialog1.setCancelable(false);
        return dialog1;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        login = (login_interface)activity;
    }
}