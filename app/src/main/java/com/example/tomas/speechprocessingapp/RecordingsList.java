package com.example.tomas.speechprocessingapp;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tomas.speechprocessingapp.MainTasks.MovementTasks;
import com.example.tomas.speechprocessingapp.TabsMeasures.MovementMeasure;
import com.example.tomas.speechprocessingapp.TabsMeasures.SpeechMeasures;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class RecordingsList extends AppCompatActivity {
    File[] FilesRec = null;
    ListView listView;
    String WAVspath,task;
    int wavFlag=0;
    int pos =0; //Position of selected wav file
    String ID_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings_list);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        /*Bundle fromButtons= getIntent().getExtras();
        if(fromButtons!=null) {
            WAVspath = fromButtons.getString(EXTRA_MESSAGE, "");
        }*/

        //ID from DataSubject to rename files
        Bundle fromDataSubject= getIntent().getExtras();
        if(fromDataSubject!=null){
            ID_text=fromDataSubject.getString("ID", "");

        }

        Intent intent = getIntent();
        String WAVspath = intent.getStringExtra(AlarmClock.EXTRA_MESSAGE);
        wavFlag = intent.getIntExtra("wavFlag",0);
        task=intent.getStringExtra("taskFlag");
        //intentDR = new Intent(this, DisplayResults.class);
        File pathRec = new File(WAVspath);
        FilesRec = pathRec.listFiles();

        listView = (ListView) findViewById(R.id.listView);
        final List<String> fname = new ArrayList<String>();
        for (int i = 0; i < FilesRec.length; i++) {
            fname.add(FilesRec[i].getName());
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, fname);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                pos = position;
                dresult();
/*                view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                //fname.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });*/
            }

        });
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    private void dresult(){
        //Get selected wav file
        if(wavFlag==1) {
            String SelPathFile = FilesRec[pos].getPath();
            Intent intent = new Intent(this, DisplayResults.class);
            intent.putExtra(EXTRA_MESSAGE, SelPathFile);
            intent.putExtra("task", task);

            startActivity(intent);
        }else{
            String SelPathFile = FilesRec[pos].getPath();
            Intent intent = new Intent(this, DisplayComputingMeasures.class);
            intent.putExtra(EXTRA_MESSAGE, SelPathFile);
            intent.putExtra("task", task);

            startActivity(intent);
            //Toast.makeText(RecordingsList.this, "No movement metrics yet"+SelPathFile,
                    //Toast.LENGTH_LONG).show();
        }
    }
    /*@Override
    public void onBackPressed() {
        if (wavFlag == 1) {

        Intent principalReturn = new Intent(RecordingsList.this, SpeechMeasures.class);
        principalReturn.putExtra("ID", ID_text);
        startActivity(principalReturn);
    }else{Intent principalReturn = new Intent(RecordingsList.this, MovementMeasure.class);
            principalReturn.putExtra("ID", ID_text);
            startActivity(principalReturn);}
        //finish();
    }*/
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
