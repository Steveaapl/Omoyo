package com.example.muditi.omoyo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Log_Activity extends AppCompatActivity {
    @Bind(R.id.grid_for_connection)
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getIntent().getIntExtra("type_of",5)==0){
            toolbar.setTitle(getResources().getString(R.string.call_log));
            setTitle(getResources().getString(R.string.call_log));
        }
        if(getIntent().getIntExtra("type_of",5)==1){
            toolbar.setTitle(getResources().getString(R.string.messsge_log));
            setTitle(getResources().getString(R.string.messsge_log));
        }
        if(getIntent().getIntExtra("type_of",5)==2){
            toolbar.setTitle(getResources().getString(R.string.notification_point));
            setTitle(getResources().getString(R.string.notification_point));
        }
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_keyboard_backspace_white_48dp));
        Log.d("TYPE",""+getIntent().getIntExtra("type_of",2));
        gridView.setAdapter(new BaseAdapterForConnection(getIntent().getIntExtra("type_of", 5), getApplicationContext()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView text_id = ButterKnife.findById(view,R.id.text_view_for_id_connection);
                Intent intent = new Intent(getApplicationContext(), shoppage.class);
                intent.putExtra("type_of",2);
                intent.putExtra("_id", text_id.getText().toString());
                if(getIntent().getIntExtra("type_of",5)==0 || getIntent().getIntExtra("type_of",5)==1){

                        shoploader(text_id.getText().toString(),intent);

                }
                else{
                    Log.d("TAG","CHiKo");
                }
            }
        });
    }
    public  void shoploader(String id , final Intent intent){
        OkHttpClient okhttp=new OkHttpClient();
        String json=String.format("{\"shop_id\" : \"%s\"}", id);
        final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://"+getResources().getString(R.string.ip)+"/shop/").post(requestbody).build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        snackBar(1);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String data = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(data);
                        Omoyo.edit.putString("shop", jsonArray.getJSONObject(0).toString());
                        Omoyo.edit.commit();
                    } catch (JSONException jx) {

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }
    private  void snackBar(final int i){
        final Snackbar snackbar =Snackbar.make(findViewById(R.id.relative_layout_for_sms_varification), getResources().getString(R.string.internet_not_available), Snackbar.LENGTH_INDEFINITE);
        final View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.snackbar_back));
        final TextView textView =ButterKnife.findById(snackbarView,android.support.design.R.id.snackbar_text);
        final TextView textViewAction =ButterKnife.findById(snackbarView, android.support.design.R.id.snackbar_action);
        textView.setTextColor(Color.WHITE);
        snackbar.setDuration(Snackbar.LENGTH_LONG);
        textViewAction.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        if(i==1){
            textViewAction.setText(getResources().getString(R.string.try_again));
            textView.setText(getResources().getString(R.string.internet_not_available));
        }
        snackbar.show();
    }
}
