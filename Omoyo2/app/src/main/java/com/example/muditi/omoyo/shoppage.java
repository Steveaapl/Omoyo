package com.example.muditi.omoyo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;


public class shoppage extends ActionBarActivity {
@Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsingtoolbar)
    CollapsingToolbarLayout collapsingtoolbar;
@Bind(R.id.recycleview)
    RecyclerView recyclerView;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new shoppageadapter(getApplicationContext()));

        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Glide.with(getApplicationContext()).load("https://s3-us-west-2.amazonaws.com/omoyo/nabula.gif").asBitmap()
                .into(new SimpleTarget<Bitmap>(Omoyo.screendisplay.getWidth(), 250) {

                    @Override
                    public void onResourceReady(Bitmap bitmap,
                                                GlideAnimation<? super Bitmap> arg1) {

                        appbar.setBackgroundDrawable(new BitmapDrawable(
                                getResources(), bitmap));

                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                int mutedColor = palette.getMutedColor(R.attr.colorPrimary);
                                collapsingtoolbar.setContentScrimColor(palette.getDarkMutedColor(R.color.appcolor));
                                collapsingtoolbar.setStatusBarScrimColor(palette.getDarkMutedColor(R.color.appcolor));
                            }
                        });

                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        //  Omoyo.toast("Started",getApplicationContext());
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        // Omoyo.toast(e.getMessage(),getApplicationContext());
                    }
                });
        Glide.with(this).load("https://s3-us-west-2.amazonaws.com/omoyo/nabula.gif").asGif().into(new SimpleTarget<GifDrawable>() {
            @Override
            public void onResourceReady(GifDrawable resource, GlideAnimation<? super GifDrawable> glideAnimation) {

            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
              //  Omoyo.toast(""+newState,getApplicationContext());
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
             //   Omoyo.toast("X:"+dx+"Y:"+dy,getApplicationContext());
            }
        });
//systembar
        SystemBarTintManager systembar=new SystemBarTintManager(this);
        systembar.setStatusBarTintEnabled(true);
        systembar.setStatusBarTintColor(getResources().getColor(R.color.appcolor));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
