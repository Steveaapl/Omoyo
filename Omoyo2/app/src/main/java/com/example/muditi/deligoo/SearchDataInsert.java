package com.example.muditi.deligoo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

/**
 * Created by muditi on 22-01-2016.
 */
public class SearchDataInsert extends BaseAdapter {
    JSONArray jsonArray ;
    Context context;
    int type_of;
    View view_of_search;
    LayoutInflater inflate;
    public SearchDataInsert(String data ,Context context , int type_of) {
        super();
        this.type_of=type_of;
        this.context = context;
        if(type_of != 0) {
            inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            try {
                jsonArray = new JSONArray(data);
            } catch (JSONException e) {

            }
        }
    }

    @Override
    public int getCount() {
        if(type_of != 0)
        return jsonArray.length();
        else
            return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

      switch(type_of){
          case 1:
              view_of_search = inflate.inflate(R.layout.include_for_search, null);
              try {
                  JSONObject jsonObject = jsonArray.getJSONObject(i);
                  TextView text_view_for_item_name = ButterKnife.findById(view_of_search, R.id.item_name);
                  TextView text_view_for_item_type = ButterKnife.findById(view_of_search, R.id.item_type);
                  TextView text_view_for_item_offer = ButterKnife.findById(view_of_search, R.id.item_offer);
                  TextView text_view_for_item_price = ButterKnife.findById(view_of_search, R.id.item_price);
                  TextView text_view_for_item_description = ButterKnife.findById(view_of_search,R.id.item_description);
                  TextView text_view_for_item_description_of_shop = ButterKnife.findById(view_of_search,R.id.item_description_of_shop);

                  if(!jsonObject.has("product_id")) {
                      TextView text_view_for_item_shop_name = ButterKnife.findById(view_of_search, R.id.item_shop_name);
                      text_view_for_item_shop_name.setText(jsonObject.getString("shop_name"));
                      TextView text_view_for_shop_id = ButterKnife.findById(view_of_search, R.id.text_view_for_shop_id);
                      text_view_for_shop_id.setText(jsonObject.getString("shop_id"));
                      text_view_for_item_type.setText(context.getResources().getString(R.string.shop));
                      JSONArray jsonArray1 = new JSONArray(Omoyo.shared.getString("category",""));
                      for(int k=0 ;k<jsonArray1.length();k++){
                          JSONObject jsonObject1 = jsonArray1.getJSONObject(k);
                          if(jsonObject.getString("category_id").toLowerCase().equals(jsonObject1.getString("category_id").toLowerCase())){
                              text_view_for_item_name.setText(jsonObject1.getString("category_name"));
                          }
                      }
                  text_view_for_item_description_of_shop.setText(jsonObject.getString("shop_description"));
                      text_view_for_item_description_of_shop.setVisibility(View.VISIBLE);
                      text_view_for_item_offer.setVisibility(View.GONE);
                      text_view_for_item_price.setVisibility(View.GONE);
                      text_view_for_item_description.setVisibility(View.GONE);
                  }
                  else{
                      TextView text_view_for_item_shop_name = ButterKnife.findById(view_of_search, R.id.item_shop_name);
                      text_view_for_item_shop_name.setText(jsonObject.getString("product_name"));
                      TextView text_view_for_shop_id = ButterKnife.findById(view_of_search, R.id.text_view_for_shop_id);
                      text_view_for_shop_id.setText(jsonObject.getString("shop_id"));
                      TextView text_view_for_product_id = ButterKnife.findById(view_of_search, R.id.text_view_for_product_id);
                      text_view_for_product_id.setText(jsonObject.getString("product_id"));
                      JSONArray jsonArray1 = new JSONArray(Omoyo.shared.getString("category",""));
                      for(int k=0 ;k<jsonArray1.length();k++){
                          JSONObject jsonObject1 = jsonArray1.getJSONObject(k);
                          if(jsonObject.getString("category_id").toLowerCase().equals(jsonObject1.getString("category_id").toLowerCase())){
                              text_view_for_item_name.setText(jsonObject1.getString("category_name"));
                          }
                      }
                      text_view_for_item_offer.setText(jsonObject.getString("product_offer"));
                      text_view_for_item_price.setText("Price - "+jsonObject.getString("product_price"));
                      text_view_for_item_description.setText(jsonObject.getString("product_description"));
                      text_view_for_item_type.setText(context.getResources().getString(R.string.product));
                  }
              }
              catch (JSONException e){
                   Log.d("ERRORINHell",e.getLocalizedMessage());
              }
              break;
          case 2:
              view_of_search = inflate.inflate(R.layout.search_suggestion_layout, null);
              try {
                  JSONObject jsonObject = jsonArray.getJSONObject(i);
                  TextView text_view_for_quick_search = ButterKnife.findById(view_of_search,R.id.text_view_for_quick_search);
                  text_view_for_quick_search.setText(jsonObject.getString("result"));
              }
              catch(JSONException ex){

              }
              break;
          default:
              Log.d("TAG","Error");
      }


        return view_of_search;
    }
}
