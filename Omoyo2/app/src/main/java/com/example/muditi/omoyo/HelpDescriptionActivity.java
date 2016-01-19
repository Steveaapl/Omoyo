package com.example.muditi.omoyo;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class HelpDescriptionActivity extends AppCompatActivity {
    int type_of;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    HelpFragment helpFragment;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_48dp);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getResources().getString(R.string.title_activity_help));
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

}
