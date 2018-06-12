package com.example.tomas.speechprocessingapp;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomas.speechprocessingapp.TabsMeasures.MovementMeasure;
import com.example.tomas.speechprocessingapp.TabsMeasures.SpeechMeasures;

import java.nio.BufferUnderflowException;

/**
 * Created by PAULA on 10/05/2018.
 */
public class DisplayMeasures extends AppCompatActivity   {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private  String ID_text="", subjectType="";
    private String id1;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_measures);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Bundle fromDataSubject = getIntent().getExtras();
        if (fromDataSubject != null) {
            ID_text = fromDataSubject.getString("ID", "");
            subjectType=fromDataSubject.getString("subjectType","");



        }






        //TODO Tapping SPeed by mean of the diffeences and mean




        // Create the adapter that will return a fragment for each of the three
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/




    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_measures, menu);
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
*/



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("ID", ID_text);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            switch (position)
            {
                case 0:


                    //movementBundle.putString("PATH", pathAll + File.separator + "ACC");
                    MovementMeasure tab_mov=new MovementMeasure();
                    tab_mov.setArguments(bundle);
                    //transaction.add(R.id.container, movementMeasure).commit();
                    //transaction.replace(R.id.container, tab_mov); //donde fragmentContainer_id es el ID del FrameLayout donde tu Fragment está contenido.
                    transaction.commit();
                    return  tab_mov;
                case 1:



                    SpeechMeasures tab_speech=new SpeechMeasures();
                    tab_speech.setArguments(bundle);
                    //FragmentTransaction transaction_speech = getSupportFragmentManager().beginTransaction();
                    //transaction.add(R.id.container, movementMeasure).commit();
                    //transaction.replace(R.id.container, tab_speech); //donde fragmentContainer_id es el ID del FrameLayout donde tu Fragment está contenido.
                    transaction.commit();
                    return  tab_speech;



                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

    @Override
    public void onBackPressed() {
        Intent principalReturn= new Intent(DisplayMeasures.this, MainActivity.class);
        principalReturn.putExtra("ID",ID_text);
        principalReturn.putExtra("subjectType", subjectType);

        startActivity(principalReturn);
        finish();

    }


}
