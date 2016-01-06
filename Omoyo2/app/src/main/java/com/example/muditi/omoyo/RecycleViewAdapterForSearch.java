package com.example.muditi.omoyo;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;

/**
 * Created by muditi on 04-01-2016.
 */
public class RecycleViewAdapterForSearch extends RecyclerView.Adapter<RecycleViewAdapterForSearch.CustomViewHolder> {
                  private View view;
                  protected CustomViewHolder customViewHolder;
    public RecycleViewAdapterForSearch()
    {

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_adapter_for_search, null);

        customViewHolder=new CustomViewHolder(view);

        return null;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        public CustomViewHolder(View view) {
            super(view);
        }
    }
}
