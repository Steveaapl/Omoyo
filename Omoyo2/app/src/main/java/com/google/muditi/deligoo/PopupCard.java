package com.google.muditi.deligoo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.example.muditi.deligoo.R;

/**
 * Created by muditi on 30-12-2015.
 */
public class PopupCard extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog .Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflate=(LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.activity_sms_varification,null,true);
        dialog.setView(view);
        return dialog.create();
    }
}
