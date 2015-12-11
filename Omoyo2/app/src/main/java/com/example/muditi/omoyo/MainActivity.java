package com.example.muditi.omoyo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends ActionBarActivity {
@Bind(R.id.toolbar)
    Toolbar toolbar;
@Bind(R.id.slidemenu)
    ListView slidemenu;
    @Bind(R.id.hozintalscroolview)
    HorizontalScrollView horizontalscrollview;
    @Bind(R.id.drawerlayout)
    DrawerLayout drawerlayout;
    @Bind(R.id.relativelayoutgridlayout1)
    RelativeLayout relativelayoutgridlayout1;
    @Bind(R.id.relativelayoutgridlayout2)
    RelativeLayout relativelayoutgridlayout2;
    @Bind(R.id.relativelayoutgridlayout3)
    RelativeLayout relativelayoutgridlayout3;
    @Bind(R.id.relativelayoutgridlayout4)
    RelativeLayout relativelayoutgridlayout4;
    @Bind(R.id.relativelayoutgridlayout5)
    RelativeLayout relativelayoutgridlayout5;
    @Bind(R.id.relativelayouthorizantalscroll1)
    RelativeLayout relativelayouthorizontalscroll1;
    @Bind(R.id.relativelayouthorizantalscroll2)
    RelativeLayout relativelayouthorizontalscroll2;
    @Bind(R.id.relativelayouthorizantalscroll3)
    RelativeLayout relativelayouthorizontalscroll3;
    @Bind(R.id.relativelayouthorizantalscroll4)
    RelativeLayout relativelayouthorizontalscroll4;
    @Bind(R.id.relativelayouthorizantalscroll5)
    RelativeLayout relativelayouthorizontalscroll5;
    @Bind(R.id.relativelayouthorizantalscroll6)
    RelativeLayout relativelayouthorizontalscroll6;
    String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Display display=getWindowManager().getDefaultDisplay();
        Omoyo.screendisplay=display;
        Omoyo.widthofscreen=display.getWidth();
        Omoyo.heightofscreen=display.getHeight();
      //  drawerlayout.openDrawer(Gravity.LEFT);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setSubtitleTextColor(Color.WHITE);
        location=Omoyo.shared.getString("area","Ghaziabad")+","+Omoyo.shared.getString("city","Ghaziabad");
        toolbar.setSubtitle(location);
        toolbar.showOverflowMenu();
        View view=getLayoutInflater().inflate(R.layout.serachbox, null);
        final EditText searchboxedittext=ButterKnife.findById(view, R.id.searchboxedittext);
        searchboxedittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchboxedittext.setFocusableInTouchMode(true);
                searchboxedittext.setFocusable(true);
            }
        });
       // toolbar.addView(view);
      //  toolbar.setNavigationOnClickListener(null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        slidemenu.setAdapter(new slidemenuadapter(getApplicationContext()));
        slidemenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    startActivity(new Intent(getApplicationContext(),shoppage.class));
                    drawerlayout.closeDrawer(Gravity.LEFT);
                }
                if(position==1){
                    startActivity(new Intent(getApplicationContext(),shoplist.class));
                    drawerlayout.closeDrawer(Gravity.LEFT);
                }
                if(position==2){
                    startActivity(new Intent(getApplicationContext(),subshopcategory.class));
                    drawerlayout.closeDrawer(Gravity.LEFT);
                }
                if(position==3){
                    startActivity(new Intent(getApplicationContext(),firstpage.class));
                    drawerlayout.closeDrawer(Gravity.LEFT);
                }
            }
        });
        //horizontalscrollview.scrollTo(160,0);

       // new lightpop().show(this.getSupportFragmentManager(),"Hello");
        Glide.with(getApplicationContext()).load("https://s3-us-west-2.amazonaws.com/omoyo/omoyo.jpg").asBitmap()
                .into(new SimpleTarget<Bitmap>(Omoyo.screendisplay.getWidth(),250) {

                    @Override
                    public void onResourceReady(Bitmap bitmap,
                                                GlideAnimation<? super Bitmap> arg1) {

                        relativelayoutgridlayout1.setBackgroundDrawable(new BitmapDrawable(
                                getResources(), bitmap));
                        relativelayoutgridlayout2.setBackgroundDrawable(new BitmapDrawable(
                                getResources(), bitmap));
                        relativelayoutgridlayout3.setBackgroundDrawable(new BitmapDrawable(
                                getResources(), bitmap));
                        relativelayoutgridlayout4.setBackgroundDrawable(new BitmapDrawable(
                                getResources(), bitmap));
                        relativelayoutgridlayout5.setBackgroundDrawable(new BitmapDrawable(
                                getResources(), bitmap));
                        relativelayouthorizontalscroll1.setBackgroundDrawable(new BitmapDrawable(
                                getResources(), bitmap));
                        relativelayouthorizontalscroll2.setBackgroundDrawable(new BitmapDrawable(
                                getResources(), bitmap));
                        relativelayouthorizontalscroll3.setBackgroundDrawable(new BitmapDrawable(
                                getResources(), bitmap));
                        relativelayouthorizontalscroll4.setBackgroundDrawable(new BitmapDrawable(
                                getResources(), bitmap));
                        relativelayouthorizontalscroll5.setBackgroundDrawable(new BitmapDrawable(
                                getResources(), bitmap));
                        relativelayouthorizontalscroll6.setBackgroundDrawable(new BitmapDrawable(
                                getResources(), bitmap));

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
int home=item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
if(home==R.id.home){
    drawerlayout.openDrawer(Gravity.LEFT);
}
        return super.onOptionsItemSelected(item);
    }


}
