package com.example.muditi.omoyo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.ButterKnife;


public class shoppage extends ActionBarActivity {
@Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsingtoolbar)
    CollapsingToolbarLayout collapsingtoolbar;
@Bind(R.id.recycleview)
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppage);
        ButterKnife.bind(this);
        //toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.showOverflowMenu();
        collapsingtoolbar.setTitle(getResources().getString(R.string.shopname));
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(getResources().getString(R.string.status));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        collapsingtoolbar.setCollapsedTitleTextColor(Color.WHITE);
        collapsingtoolbar.setExpandedTitleColor(Color.WHITE);
        collapsingtoolbar.setExpandedTitleTextAppearance(R.style.collapsebartitleexpanding);
        collapsingtoolbar.setCollapsedTitleTextAppearance(R.style.collapsebartitlecollapsing);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
              int   mutedColor = palette.getMutedColor(R.attr.colorPrimary);
                collapsingtoolbar.setContentScrimColor(mutedColor);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new shoppageadapter());
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shoppage, menu);
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
