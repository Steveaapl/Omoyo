package com.example.muditi.deligoo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;

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
    @Bind(R.id.text_input_view_for_user_mobile_number)
    TextInputLayout text_input_view_for_user_mobile_number;
    @Bind(R.id.image_view_for_sms_varification)
    ImageView image_view_for_sms_varification;
    @Bind(R.id.text_view_for_next_in_sms_varification)
    TextView text_view_for_next_in_sms_varification ;
    OTPSMSReceiverBroadCast otp;
    ArrayList<Character> filterList=new ArrayList<Character>();
    String user_mobile_number;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_varification);
        ButterKnife.bind(this);
        Omoyo.errorReportByMint(getApplicationContext());
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //BroadCast
        otp = new OTPSMSReceiverBroadCast();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(otp,filter);

        filterArrayFill();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 user_mobile_number = text_input_view_for_user_mobile_number.getEditText().getText().toString();
                 if(filterOfMobileNumber(user_mobile_number)){

                     if(Omoyo.shared.getBoolean("user_status",false)){
                              snackBar(0);
                     }
                     else {
                         hideKeyboard();
                         text_input_view_for_user_mobile_number.setErrorEnabled(false);
                         text_input_view_for_user_mobile_number.setHint("");
                         image_view_for_sms_varification.setImageDrawable(getResources().getDrawable(R.mipmap.ic_autorenew_white_48dp));
                         Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.dialog_box_cross_rotation);
                         image_view_for_sms_varification.setAnimation(animation);
                         text_view_for_next_in_sms_varification.setText(getResources().getString(R.string.varifing));

                         sendVarificationSms(user_mobile_number);
                     }
                 }
                else {
                     text_input_view_for_user_mobile_number.setError(getResources().getString(R.string.invalide_mobile_number));
                     YoYo.with(Techniques.Pulse).duration(100).playOn(findViewById(R.id.card_view));
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
                       // Omoyo.toast("Error : " + e.getMessage(), getApplicationContext());
                        snackBar(1);
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
                         Log.d("TAG",data);
                         //   Omoyo.toast(data, getApplicationContext());
                        }
                    });
                }
            }
        });
    }

    private String generateUrl( String userMobileNumber){

        String authkey = "101075AeqKWtLGO567ee5bd";

        String mobiles = userMobileNumber;

        String senderId = "Deligo";

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
        sbPostData.append("&sender=" + senderId);

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
        Omoyo.edit.putString("OTPGenerated", random);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_transition_backword_in, R.anim.activity_transition_backword_out);
    }

    private class OTPSMSReceiverBroadCast  extends BroadcastReceiver {
        private  String SMS_BUNDLE="pdus",address,messageBody;
        private Context context;

        @Override
        public void onReceive(Context context, Intent intent) {
            this.context=context;
           // Omoyo.toast("1777",context);
            Omoyo.shared=context.getSharedPreferences("omoyo", Context.MODE_PRIVATE);
            Omoyo.edit=Omoyo.shared.edit();
            Bundle smsBundle = intent.getExtras();
            if(smsBundle != null){
                Object[] sms = (Object[])smsBundle.get(SMS_BUNDLE);

                for (int i = 0; i < sms.length; ++i) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                    messageBody= smsMessage.getMessageBody().toString();
                    address = smsMessage.getOriginatingAddress();
                 //    Omoyo.toast("Message:"+messageBody+"Length:"+messageBody.length(),context);
                }
                if(varifivationOfSenderId(address))
                {
                   //  Omoyo.toast("1",context);
                    String  OTPCodeReceived = messageBody.substring(13,18);
                    //  Omoyo.toast("OTP:"+OTPCodeReceived + "Length:"+OTPCodeReceived.length(),context);
                    if(loginCheck(OTPCodeReceived))
                    {
                                       image_view_for_sms_varification.clearAnimation();
                                       image_view_for_sms_varification.setImageDrawable(getResources().getDrawable(R.mipmap.ic_check_circle_white_48dp));
                                       text_view_for_next_in_sms_varification.setText(getResources().getString(R.string.varified));
                                       Omoyo.edit.putBoolean("user_status", true);
                                       Omoyo.edit.commit();
                                       Omoyo.edit.putString("user_mobile_number", user_mobile_number);
                                       Omoyo.edit.commit();
                                       Omoyo.sendMobileNumberToServer(getApplicationContext(),user_mobile_number);
                    }
                    else
                    {
                        //     Omoyo.toast("Login Unsuccessful :" + OTPCodeReceived ,context);
                    }

                }
                else
                {
                    //Some other Source Sms Received
                }
            }
        }

        private Boolean varifivationOfSenderId(String senderId){
            if(Omoyo.shared.getString("senderIddddddd","Deligo").equals(senderId.substring(3,senderId.length())))
            {
                return true;
            }
            else
            {
                return false;
            }

        }

        private Boolean loginCheck(String OTPCodeReceived){
            if(OTPCodeReceived.equals(Omoyo.shared.getString("OTPGenerated","OTPGenerated")))
                return true;

            else
                return  false;

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(otp);
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    private  void snackBar(final int i){
        final Snackbar snackbar =Snackbar.make(findViewById(R.id.relative_layout_for_sms_varification), getResources().getString(R.string.internet_not_available), Snackbar.LENGTH_INDEFINITE);
        final View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.snackbar_back));
        final TextView textView =ButterKnife.findById(snackbarView,android.support.design.R.id.snackbar_text);
        final TextView textViewAction =ButterKnife.findById(snackbarView,android.support.design.R.id.snackbar_action);
        textView.setTextColor(Color.WHITE);
        snackbar.setText(R.string.alreadyvarified);
        snackbar.setAction(getResources().getString(R.string.welcome), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == 1) {
                    sendVarificationSms(user_mobile_number);
                }
            }
        });
        snackbar.setDuration(Snackbar.LENGTH_LONG);
        textViewAction.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        if(i==1){
            textViewAction.setText(getResources().getString(R.string.try_again));
            textView.setText(getResources().getString(R.string.internet_not_available));
        }
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Google_analytics_class.getInstance().trackScreenView("Sms Varification View");
    }
}
