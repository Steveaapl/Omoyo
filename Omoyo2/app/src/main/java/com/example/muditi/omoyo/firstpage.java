package com.example.muditi.omoyo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okio.BufferedSink;


public class firstpage extends Activity {
    @Bind(R.id.linearlayoutfooter)
    LinearLayout linearlayoutforfooter;
    @Bind(R.id.linearlayoutforlocation)
    LinearLayout linearlayoutforlocation;
    @Bind(R.id.spinnerforarea)
    Spinner spinnerforarea;
    @Bind(R.id.spinnerforcity)
    Spinner spinnerforcity;
    @Bind(R.id.processbarfirstpagenetwork)
    ProgressBar progressbarnetworkcheck;
    JSONObject jsonobject;
    JSONArray jsonarray;
    ObjectAnimator areaselectionanimation;
    ArrayList<String> listforcity =new ArrayList<>();
    ArrayList<String> listforarea =new ArrayList<>();
    String city,area;
    @Bind(R.id.doneselectingarea)
    Button done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        ButterKnife.bind(this);
        Omoyo.shared=getSharedPreferences("Omoyocity", Context.MODE_PRIVATE);
        Omoyo.edit=Omoyo.shared.edit();
    //    linearlayoutforlocation.setBackgroundColor(Color.argb(100,0,0, 0));

//        ArrayAdapter adapterforarea=new firstpagespinneradapter("Area",getApplicationContext(),R.layout.firstpagespinnerlayout,getResources().getStringArray(R.array.firstpagespinnerdata));
  //     spinnerforarea.setAdapter(adapterforarea);
        spinnerforarea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
if(position!=0){
area=listforarea.get(position);
    Omoyo.check=1;
}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //  Omoyo.toast("Nothing", getApplicationContext());
            }
        });

        spinnerforcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    city=listforcity.get(position);
                    arealoader(city);
                 //   Omoyo.toast(city,getApplicationContext());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
done.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(Omoyo.check==1) {
           final String location=city.replace(" ","").toLowerCase()+"#"+area.replace(" ","").toLowerCase();
          //  Omoyo.toast(location,getApplicationContext());
            Omoyo.shared=getSharedPreferences("omoyo", Context.MODE_PRIVATE);
            Omoyo.edit=Omoyo.shared.edit();
            Omoyo.edit.putString("city",city);
            Omoyo.edit.putString("area", area);
            Omoyo.edit.commit();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    loadadsimages(location);
                }
            }).start();


        }
        else
            Omoyo.toast("Area need to be Selected",getApplicationContext());
    }
});
        areaselectionanimation=ObjectAnimator.ofFloat(linearlayoutforlocation,"alpha",0,1);
        areaselectionanimation.setStartDelay(500);
        areaselectionanimation.setDuration(500);
        areaselectionanimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                linearlayoutforlocation.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

             progressbarnetworkcheck.setProgress(20);

cityloader();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_firstpage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadadsimages(String location){
        OkHttpClient okhttp=new OkHttpClient();
        String json=String.format("{\"location\" : \"%s\"}",location);
       // Omoyo.toast(json,getApplicationContext());
        final MediaType JSON=MediaType.parse("application/json;charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://192.168.0.120:15437/bitmapforads/").post(requestbody).build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Omoyo.toast("Error in the network",getApplicationContext());
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException  {
                         if(response.isSuccessful()){
                            final  String data=response.body().string();
                             Omoyo.edit.putString("ads", data);
                             Omoyo.edit.commit();

runOnUiThread(new Runnable() {
    @Override
    public void run() {
       // Omoyo.toast(Omoyo.shared.getString("ads","f"),getApplicationContext());
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
      //  Omoyo.toast("Response:"+data,getApplicationContext());
    }
});
                         }
            }
        });
    }

    public void arealoader(String city){
        OkHttpClient okhttp=new OkHttpClient();
        String json = String.format("{\"city\" : \"%s\"}", city);
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestbody=RequestBody.create(JSON, json);
        Request request=new Request.Builder().url("http://192.168.0.120:15437/getarea/").post(requestbody).build();
        Call call = okhttp.newCall(request);

        call.enqueue(new Callback() {

            @Override
            public void onFailure(Request request, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressbarnetworkcheck.setVisibility(View.INVISIBLE);
                        Omoyo.toast("Error - In the Network ",getApplicationContext());
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()) {
                    listforarea.clear();
                    listforarea.add("area");
                    final String data = response.body().string();

                    //data=[{"area":[{"area":"Green Park","_id":"56675528f3ca917c166cca9c"},{"area":"Quatub Minar","_id":"56675528f3ca917c166cca9b"}]}]
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //progressbarnetworkcheck.setVisibility(View.INVISIBLE);
                            try {
                                jsonarray = new JSONArray(data);
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    jsonobject = jsonarray.getJSONObject(i);
                                    jsonarray = jsonobject.getJSONArray("area");
                                    for (int k = 0; k < jsonarray.length(); k++) {
                                        jsonobject = jsonarray.getJSONObject(k);
                                        listforarea.add(jsonobject.getString("area"));
                                    }
                                }
                            } catch (JSONException jsonex) {

                            }

                            //  areaselectionanimation.start();
                            ArrayAdapter adapterforarea = new firstpagespinneradapter("Area", getApplicationContext(), R.layout.firstpagespinnerlayout, listforarea);
                            spinnerforarea.setAdapter(adapterforarea);
                        }
                    });
                }

            }
        });

    }




    public void cityloader(){
        OkHttpClient okhttp=new OkHttpClient();

        Request request=new Request.Builder().url("http://192.168.0.120:15437/getcity/").build();

        Call call = okhttp.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressbarnetworkcheck.setVisibility(View.INVISIBLE);
                        Omoyo.toast("Error - In the Network ",getApplicationContext());
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                listforcity.add("city");
                if(response.isSuccessful()) {
                    final String data = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressbarnetworkcheck.setVisibility(View.INVISIBLE);
                            try {
                                jsonarray = new JSONArray(data);
                                for(int i=0 ; i<jsonarray.length() ; i++){
                                    jsonobject=jsonarray.getJSONObject(i);
                                    listforcity.add(jsonobject.getString("city"));
                                }
                            }
                            catch(JSONException jsonex){

                            }

                            areaselectionanimation.start();
                            ArrayAdapter adapterforcity=new firstpagespinneradapter("City",getApplicationContext(),R.layout.firstpagespinnerlayout,listforcity);
                            spinnerforcity.setAdapter(adapterforcity);

                        }
                    });
                }

            }
        });

    }
}
