package com.example.muditi.deligoo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

/**
 * Created by muditi on 19-01-2016.
 */
public class Faq_Recycle_View_Adapter extends  RecyclerView.Adapter<Faq_Recycle_View_Adapter.CustomViewHolder> {

    Context context;
    JSONArray jsonArray ;
    JSONObject jsonObject;
    View view;
    CustomViewHolder viewholder;
   public Faq_Recycle_View_Adapter(Context context){
     this.context = context ;
     try{
         jsonArray = new JSONArray(Omoyo.shared.getString("data_for_faq","opus"));
         Log.d("TAG",jsonArray.toString());
     }
     catch (JSONException jsonex){
         Log.d("TAG",jsonex.getLocalizedMessage());
     }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        try{
            jsonObject = jsonArray.getJSONObject(position);
            holder.text_view_for_question.setText(jsonObject.getString("faq_question"));
            holder.text_view_for_answer.setText(jsonObject.getString("faq_answer"));
        }
        catch (JSONException jsonex){
            Log.d("TAG",jsonex.getLocalizedMessage());
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_recycle_view_adapter_layout, null);

            viewholder = new CustomViewHolder(view);

        return viewholder;

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView text_view_for_question;
        TextView text_view_for_answer;
        public CustomViewHolder(View view) {
            super(view);
            text_view_for_answer = ButterKnife.findById(view,R.id.text_view_help_feq_answer);
            text_view_for_question = ButterKnife.findById(view,R.id.text_view_help_feq_question);
        }
    }
}
