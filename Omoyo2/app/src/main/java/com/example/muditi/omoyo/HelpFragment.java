package com.example.muditi.omoyo;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;

/**
 * Created by muditi on 19-01-2016.
 */
public class HelpFragment extends Fragment {
    int type_of;
    View view;
    LayoutInflater layoutInflater;
    public HelpFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        type_of = getArguments().getInt("type_of");
        switch(type_of){
            case 0:
                 layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 view = layoutInflater.inflate(R.layout.about_layout,null);
                 TextView text_view_for_help_license = ButterKnife.findById(view,R.id.text_view_for_help_licenses);
                 text_view_for_help_license.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity().getApplicationContext(),License.class);
                        startActivity(intent);
                    }
                });
                break;
            case 1:
                layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.faq_layout,null);
                RecyclerView recycle_view_for_faq = ButterKnife.findById(view,R.id.recycle_view_for_faq);
                TextView text_view_help_faq_show_not_loaded = ButterKnife.findById(view,R.id.text_view_help_faq_show_not_loaded);
                if(Omoyo.shared.contains("data_for_faq")){
                    recycle_view_for_faq.setLayoutManager(new LinearLayoutManager(getContext()));
                  recycle_view_for_faq.setAdapter(new Faq_Recycle_View_Adapter(getContext()));
                }
                else{
                    text_view_help_faq_show_not_loaded.setVisibility(View.VISIBLE);
                    text_view_help_faq_show_not_loaded.setText(getResources().getString(R.string.loaded_soon));
                    recycle_view_for_faq.setVisibility(View.GONE);
                }
                break;
            case 2:

                break;
            case 3:
                break;
            default:
                Log.d("TAGFORFRAGMENT","DONE");
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
    }
}
