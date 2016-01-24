package com.example.muditi.omoyo;

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
                  if(jsonObject.has("shop_id")) {
                      TextView text_view_for_item_shop_name = ButterKnife.findById(view_of_search, R.id.item_shop_name);
                      text_view_for_item_shop_name.setText(jsonObject.getString("shop_name"));
                      TextView text_view_for_shop_id = ButterKnife.findById(view_of_search, R.id.text_view_for_shop_id);
                      text_view_for_shop_id.setText(jsonObject.getString("shop_id"));
                  }
                  else{
                      TextView text_view_for_item_shop_name = ButterKnife.findById(view_of_search, R.id.item_shop_name);
                      text_view_for_item_shop_name.setText(jsonObject.getString("product_name"));
                      TextView text_view_for_shop_id = ButterKnife.findById(view_of_search, R.id.text_view_for_shop_id);
                      text_view_for_shop_id.setText(jsonObject.getString("product_shop_id"));
                      TextView text_view_for_product_id = ButterKnife.findById(view_of_search, R.id.text_view_for_product_id);
                      text_view_for_product_id.setText(jsonObject.getString("product_id"));
                  }
              }
              catch (JSONException e){

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
