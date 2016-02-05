package com.google.muditi.deligoo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.muditi.deligoo.*;
import com.example.muditi.deligoo.Google_analytics_class;
import com.example.muditi.deligoo.Omoyo;

import butterknife.Bind;
import butterknife.ButterKnife;

public class License extends AppCompatActivity {
@Bind(R.id.text_view_for_licenses_description)
    TextView text_view_for_licenses_description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_48dp);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getResources().getString(R.string.title_activity_license));
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(null);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);

        text_view_for_licenses_description.setText(com.google.muditi.deligoo.Omoyo.shared.getString("license_description","Nothing Soon will be uploaded"));

    }

    @Override
    protected void onResume() {
        super.onResume();
        com.google.muditi.deligoo.Google_analytics_class.getInstance().trackScreenView("License View");
    }
}
