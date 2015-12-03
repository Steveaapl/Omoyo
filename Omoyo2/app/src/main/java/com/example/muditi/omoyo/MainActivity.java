package com.example.muditi.omoyo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import android.widget.Toast;

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
        toolbar.setSubtitle(getResources().getString(R.string.subtitle));
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
            }
        });
        //horizontalscrollview.scrollTo(160,0);

       // new lightpop().show(this.getSupportFragmentManager(),"Hello");

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
