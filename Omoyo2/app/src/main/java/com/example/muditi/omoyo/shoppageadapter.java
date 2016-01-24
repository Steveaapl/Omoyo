package com.example.muditi.omoyo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.pkmmte.view.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

/**
 * Created by muditi on 03-12-2015.
 */
public class shoppageadapter extends RecyclerView.Adapter<shoppageadapter.CustomViewHolder> implements OnMapReadyCallback {
Context context;
    String[] textdata;
    CustomViewHolder viewholder;
    View view;
    JSONObject jsonobject;
    String item;
    FragmentManager fragmentManager;
    public shoppageadapter(Context context,FragmentManager fragmentManager){
        this.context=context;
        this.fragmentManager =fragmentManager;
        try {
             jsonobject=new JSONObject(Omoyo.shared.getString("shop", "shop"));
            JSONArray jsonArray=jsonobject.getJSONArray("shop_item");
            StringBuilder stringBuilder=new StringBuilder();
            for(int i=0 ; i<jsonArray.length();i++){
                stringBuilder.append(jsonArray.getJSONObject(i).getString("item")+"    ");
            }
            item=stringBuilder.toString();
        }
        catch(JSONException e){

        }
        textdata=context.getResources().getStringArray(R.array.shoppagedatatext);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
       // Omoyo.toast("Running count :"+count,context);

        switch(i){
            case 0:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.call_sms_layout, null);
                CircularImageView circularImageView_for_call = ButterKnife.findById(view,R.id.circular_image_view_for_call_to_shop);
                CircularImageView circularImageView_for_sms = ButterKnife.findById(view,R.id.circular_image_view_for_sms_to_shop);
                 if(Omoyo.shared.getBoolean("user_status",false)){
                     circularImageView_for_call.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                             try {
                                 Omoyo.addtoCall(Omoyo.currentShopId, jsonobject.getString("shop_name"));
                             }
                             catch(JSONException ex){

                             }
                         }
                     });

                     circularImageView_for_sms.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                             try {
                                 Omoyo.addtoSms(Omoyo.currentShopId, jsonobject.getString("shop_name"));
                             }
                             catch(JSONException ex){

                             }
                         }
                     });
                 }
                else{
                     alert();
                 }
                break;
            case 1:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.address_of_shop_layout, null);
                TextView text_view_for_adress_of_shop = ButterKnife.findById(view,R.id.text_view_for_address_of_shop);
                if(Omoyo.shared.getBoolean("user_status",false)) {
                    try {
                        text_view_for_adress_of_shop.setText(jsonobject.getString("shop_address"));
                    } catch (JSONException jx) {

                    }
                }
                else{
                    text_view_for_adress_of_shop.setText(context.getResources().getString(R.string.click_to_login));
                    CardView card_view_for_address = ButterKnife.findById(view,R.id.card_view_for_address_of_shop);
                    card_view_for_address.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            context.startActivity(new Intent(context,SmsVarification.class));
                        }
                    });
                }
                break;
            case 2:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.google_map_position_of_shop, null);
                CardView card_view_for_map_image_view = ButterKnife.findById(view,R.id.card_view_google_map_image_of_shop);
                break;
            default:
                Log.d("Hello","Null");
        }


        viewholder = new CustomViewHolder(view,i);
        return viewholder;
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, int i) {

    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public CustomViewHolder(View view,int position) {
            super(view);

        }
    }

   private void alert(){
       final AlertDialog.Builder builder = new AlertDialog.Builder(context);
       builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               context.startActivity(new Intent(context,SmsVarification.class));
           }
       });
       builder.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {

           }
       });
       builder.create().show();
   }


}
