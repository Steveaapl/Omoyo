package com.example.muditi.omoyo;

import android.content.Context;
import android.view.Display;
import android.widget.Toast;

/**
 * Created by muditi on 04-12-2015.
 */
public class Omoyo {
    public static  int spinnerfirstpagecheck=0;
    public static int widthofscreen;
    public static int heightofscreen;
    public static Display screendisplay;
    public static void  toast(String message,Context context){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}

