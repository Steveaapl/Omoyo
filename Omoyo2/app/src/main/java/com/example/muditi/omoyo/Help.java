package com.example.muditi.omoyo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Help extends AppCompatActivity {
    @Bind(R.id.card_view_for_help_about)
    CardView card_view_for_help_about ;
    @Bind(R.id.card_view_for_help_faq)
    CardView card_view_for_help_faq;
    @Bind(R.id.card_view_for_help_contact)
    CardView card_view_for_help_contact;
    @Bind(R.id.card_view_for_help_submit_query)
    CardView card_view_for_help_submit_query;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_48dp);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getResources().getString(R.string.title_activity_help));
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(null);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        card_view_for_help_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), HelpDescriptionActivity.class);
                intent.putExtra("type_of",0);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_transition_forword_in, R.anim.activity_transition_forword_out);
            }
        });
        card_view_for_help_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), HelpDescriptionActivity.class);
                intent.putExtra("type_of",1);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_transition_forword_in, R.anim.activity_transition_forword_out);
            }
        });
        card_view_for_help_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), HelpDescriptionActivity.class);
                intent.putExtra("type_of",2);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_transition_forword_in, R.anim.activity_transition_forword_out);
            }
        });
        card_view_for_help_submit_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), HelpDescriptionActivity.class);
                intent.putExtra("type_of",3);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_transition_forword_in, R.anim.activity_transition_forword_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_transition_backword_in, R.anim.activity_transition_backword_out);
    }

    public void loadFaq(){

    }

}
