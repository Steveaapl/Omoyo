package com.example.muditi.omoyo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.Display;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by muditi on 04-12-2015.
 */
public class Omoyo {
    public static  int check=0;
    public static int widthofscreen;
    public static int heightofscreen;
    public static Display screendisplay;
    public static SharedPreferences shared;
    public static SharedPreferences.Editor edit;
    public  static ArrayList<Bitmap> adsforhomebitmaplist=new ArrayList<Bitmap>();
    public static void  toast(String message,Context context){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}

