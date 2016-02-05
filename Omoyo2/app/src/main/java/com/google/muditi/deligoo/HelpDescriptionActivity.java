package com.google.muditi.deligoo;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.muditi.deligoo.*;
import com.example.muditi.deligoo.Google_analytics_class;
import com.example.muditi.deligoo.HelpFragment;

import butterknife.ButterKnife;

public class HelpDescriptionActivity extends AppCompatActivity implements com.example.muditi.deligoo.HelpFragment.Fragment_Interface {
    int type_of;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    com.example.muditi.deligoo.HelpFragment helpFragment;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_description);
        Omoyo.errorReportByMint(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getResources().getString(R.string.title_activity_help));
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_36dp);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        helpFragment = new HelpFragment();
        bundle = new Bundle();
        type_of = getIntent().getIntExtra("type_of",0);
        Log.d("TAGOFFRAG",""+type_of);
        switch(type_of){
            case 0:

                setTitle(getResources().getString(R.string.about));
                bundle.putInt("type_of", 0);
                helpFragment.setArguments(bundle);

                fragmentTransaction.add(R.id.fragment_for_help, helpFragment);
               // fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 1:
                setTitle(getResources().getString(R.string.faq));
                bundle.putInt("type_of", 1);
                helpFragment.setArguments(bundle);

                fragmentTransaction.add(R.id.fragment_for_help, helpFragment);
               // fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break ;
            case 2:
                setTitle(getResources().getString(R.string.contact_us));
                bundle.putInt("type_of", 2);
                helpFragment.setArguments(bundle);

                fragmentTransaction.add(R.id.fragment_for_help, helpFragment);
             //   fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 3:
                setTitle(getResources().getString(R.string.submit_query));
                bundle.putInt("type_of", 3);
                helpFragment.setArguments(bundle);

                fragmentTransaction.add(R.id.fragment_for_help, helpFragment);
             //   fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            default:
                Log.d("TAGFORFRAGMENT","@Null");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_transition_backword_in, R.anim.activity_transition_backword_out);
    }
    private  void snackBar(final int i ){
        final Snackbar snackbar =Snackbar.make(findViewById(R.id.relative_layout_for_query_submit_check_temp), getResources().getString(R.string.internet_not_available), Snackbar.LENGTH_INDEFINITE);
        final View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.snackbar_back));
        final TextView textView = ButterKnife.findById(snackbarView, android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.setText(R.string.internet_not_available);
        snackbar.setDuration(Snackbar.LENGTH_LONG);


if(i==1){
    textView.setText(getResources().getString(R.string.successfully_submit));
}
        if(i==2){
            textView.setText(getResources().getString(R.string.submiting)+"...");
        }

        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Google_analytics_class.getInstance().trackScreenView("HelpDescriptionActivity View");
    }

    @Override
    public void onerror() {
          snackBar(0);
    }

    @Override
    public void onsuccess() {
        snackBar(1);
    }

    @Override
    public void onsubmitting() {
        snackBar(2);
    }
}
