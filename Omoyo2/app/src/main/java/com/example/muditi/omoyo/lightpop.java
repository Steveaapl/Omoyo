package com.example.muditi.omoyo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by muditi on 03-12-2015.
 */
public class lightpop extends DialogFragment {

    public lightpop(){

    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder light=new AlertDialog.Builder(getActivity());
        light.setView(getActivity().getLayoutInflater().inflate(R.layout.lightpop, null));
        return light.create();
    }
}
