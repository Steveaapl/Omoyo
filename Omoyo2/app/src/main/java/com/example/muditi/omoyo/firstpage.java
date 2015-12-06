package com.example.muditi.omoyo;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;


public class firstpage extends Activity {
    @Bind(R.id.linearlayoutfooter)
    LinearLayout linearlayoutforfooter;
    @Bind(R.id.linearlayoutforlocation)
    LinearLayout linearlayoutforlocation;
    @Bind(R.id.spinnerforarea)
    Spinner spinnerforarea;
    @Bind(R.id.spinnerforcity)
    Spinner spinnerforcity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        ButterKnife.bind(this);
        linearlayoutforlocation.setBackgroundColor(Color.argb(100, 255, 255, 255));
        ArrayAdapter adapterforcity=new firstpagespinneradapter("City",getApplicationContext(),R.layout.firstpagespinnerlayout,getResources().getStringArray(R.array.firstpagespinnerdata));
       spinnerforcity.setAdapter(adapterforcity);
        ArrayAdapter adapterforarea=new firstpagespinneradapter("Area",getApplicationContext(),R.layout.firstpagespinnerlayout,getResources().getStringArray(R.array.firstpagespinnerdata));
       spinnerforarea.setAdapter(adapterforarea);
        spinnerforarea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
if(position==0){
   Omoyo.spinnerfirstpagecheck=1;
}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
}
