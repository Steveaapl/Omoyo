package com.example.muditi.omoyo;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rey.material.app.Dialog;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SmsVarification extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.logintextview)
    TextView loginTextView;
    @Bind(R.id.card_view)
    CardView cardView;
    @Bind(R.id.next)
    LinearLayout next;
    @Bind(R.id.mobilenumberofuser)
    EditText userMobileNumber;
    ArrayList<Character> filterList=new ArrayList<Character>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_varification);
        ButterKnife.bind(this);
        LayoutInflater inflate =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.snak_bar_,null);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        filterArrayFill();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  Snackbar snackbar =Snackbar.make(findViewById(R.id.card_view),"Hello Snack",Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OMOYoo!", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Omoyo.toast("Hello Snack", getApplicationContext());
                    }
                });
                snackbar.setActionTextColor(Color.RED);
                 snackbar.show();
                 String user_mobile_number = userMobileNumber.getText().toString();
                 if(filterOfMobileNumber(user_mobile_number)){
                    // String message = String.format("Your OTP is %s \n \" Hope The Force Is With You \" ",OTPGenerator());
                  //   Omoyo.toast(message,getApplicationContext());
                    sendVarificationSms(user_mobile_number);
                 }
                else {
                     YoYo.with(Techniques.Pulse).duration(100).playOn(findViewById(R.id.card_view));
                     Omoyo.toast("Fill Valide Mobile Number Sir/Mam",getApplicationContext());
                 }
            }
        });
    }


    private  Boolean filterOfMobileNumber(String mobileNumber)
    {
        if( mobileNumber.length() !=10 || filterList.contains(mobileNumber.charAt(0)))
            return false;
        else
            return true;
    }

    private void filterArrayFill()
    {
        filterList.add('0');
        filterList.add('1');
        filterList.add('2');
        filterList.add('3');
        filterList.add('4');
        filterList.add('5');
        filterList.add('6');
    }

    private void sendVarificationSms(String userMobileNumber)
    {
        String uri = generateUrl(userMobileNumber);
        OkHttpClient okhttp=new OkHttpClient();
        Request request=new Request.Builder().url(uri).build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Omoyo.toast("Error : " + e.getMessage(), getApplicationContext());
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String data = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Omoyo.toast(data, getApplicationContext());
                        }
                    });
                }
            }
        });
    }

    private String generateUrl( String userMobileNumber){

        String authkey = "101075AeqKWtLGO567ee5bd";

        String mobiles = userMobileNumber;

        String senderId = "OMOYoO";

        String message = String.format("Your OTP is .%s. \n \" Hope The Force Is With You \" ",OTPGenerator());
     //    Omoyo.toast(message,getApplicationContext());
        String route="4";

        String encoded_message= URLEncoder.encode(message);

        String mainUrl="https://control.msg91.com/api/sendhttp.php?";


        StringBuilder sbPostData= new StringBuilder(mainUrl);
        sbPostData.append("authkey="+authkey);
        sbPostData.append("&mobiles="+mobiles);
        sbPostData.append("&message="+encoded_message);
        sbPostData.append("&route="+route);
        sbPostData.append("&sender="+senderId);

        mainUrl = sbPostData.toString();

        return mainUrl;
    }

    private String OTPGenerator()
    {
            String random=null;
            SecureRandom sr = new SecureRandom();
           Integer value =new Integer(sr.nextInt());
        if(value<0){
            String tmp=value.toString();
            if(tmp.length()>6){
                random=tmp.substring(1,6);
            }
            else{
                random=tmp.substring(1,tmp.length());
            }
        }
        else{
            String tmp=value.toString();
            if(tmp.length()>5){
                random=tmp.substring(0,5);
            }
            else{
                random=tmp.substring(0,tmp.length());
            }
        }
        Omoyo.edit.putString("OTPGenerated",random);
        Omoyo.edit.commit();
        return random;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sms_varification, menu);
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
}
