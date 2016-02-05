package com.google.muditi.deligoo;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.EditText;

import com.example.muditi.deligoo.Faq_Recycle_View_Adapter;
import com.example.muditi.deligoo.License;
import com.example.muditi.deligoo.Omoyo;
import com.example.muditi.deligoo.R;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.ButterKnife;

/**
 * Created by muditi on 19-01-2016.
 */
public class HelpFragment extends Fragment {
    int type_of;
    View view;
    LayoutInflater layoutInflater;
    String user_query;
    public Fragment_Interface fragment_interface;
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
                layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.contact_layout,null);
                LinearLayout linearLayout = ButterKnife.findById(view,R.id.linear_layout_for_call);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        makeCallToOMOYoo();
                    }
                });
                break;
            case 3:
                layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.submit_query_layout,null);
                final TextView text_view_for_submiting_query = ButterKnife.findById(view,R.id.text_view_for_submiting_query);
                LinearLayout linear_layout_for_submiting_query=ButterKnife.findById(view,R.id.linear_layout_for_submiting_query);
                final EditText edit_view_for_submiting_query=ButterKnife.findById(view,R.id.edit_view_for_submiting_query);
                linear_layout_for_submiting_query.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                     //   text_view_for_submiting_query.setText(getResources().getString(R.string.submiting)+" ...");
                        user_query = edit_view_for_submiting_query.getText().toString();
                        sendingQueryToServer(user_query);
                        fragment_interface.onsubmitting();
                        edit_view_for_submiting_query.setText("");
                    }
                });
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

    private void makeCallToOMOYoo(){

        CallToOMOYooStateListener phoneListener = new CallToOMOYooStateListener();
        TelephonyManager telephonyManager =
                (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        try {
            String uri = "tel:"+Omoyo.shared.getString("OMOYoo_contact_number","100");
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
            startActivity(callIntent);
        }catch(Exception e) {
            Log.d("TAG","Error:"+e.getMessage());
            e.printStackTrace();
        }
    }

    private class CallToOMOYooStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            Log.d("TAGFORCALL",incomingNumber+"STATEOFCALL "+state);
        }
    }

    private void sendingQueryToServer(String query_string){
        OkHttpClient okhttp=new OkHttpClient();
        String json=String.format("{\"user_id\" : \"%s\",\"user_query\" : \"%s\"}", Omoyo.shared.getString("user_id", "1007"), query_string);
        final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/userSubmitingQuery/").post(requestbody).build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                  fragment_interface.onerror();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful())
                fragment_interface.onsuccess();
            }
        });
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragment_interface = (Fragment_Interface)activity;
    }

    public interface  Fragment_Interface {
        public  void onerror();
        public void onsubmitting();
        public void onsuccess();
   }

}
