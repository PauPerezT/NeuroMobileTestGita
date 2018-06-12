
package com.example.tomas.speechprocessingapp;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomas.speechprocessingapp.MainTasks.MovementTasks;
import com.example.tomas.speechprocessingapp.MainTasks.SpeechTasks;
import com.example.tomas.speechprocessingapp.SpeechProcessing.SpeechRec;
import com.example.tomas.speechprocessingapp.TabsMeasures.MovementMeasure;
import com.example.tomas.speechprocessingapp.TremorProcessing.AccGraph;
import com.example.tomas.speechprocessingapp.TremorProcessing.ActivateAcc;

import java.io.File;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity{
    private boolean recflag = false;
    private boolean gaitflag = false;
    String pathData = null;
    private Chronometer rectimer= null;
    private String ID_text="", subjectType="";
    private int facc=1,fspee = 1;
    private SensorManager mSensorManager;
    public ActivateAcc acc;
    public SpeechRec spe;
    TextView tasktext = null;

    //private int time = (int) System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.brain2);

        //ID from DataSubject to rename files
        Bundle fromDataSubject= getIntent().getExtras();
        if(fromDataSubject!=null){
            ID_text=fromDataSubject.getString("ID", "");
            subjectType=fromDataSubject.getString("subjectType","");



            //Toast.makeText(ActivityProfile.this, "me acabaron de mandar:"+username+"-"+password,Toast.LENGTH_LONG).show();

            //printResults();

//TODO Mejorar la letra
        }




        //Create app folders to store data
        pathData = Environment.getExternalStorageDirectory() + File.separator + "AppSpeechData";
        File datafolder = new File(pathData);
        boolean checkF = datafolder.exists();
        //if (checkF == false) {
        datafolder.mkdirs();
        datafolder = new File(pathData + File.separator + "WAV");
        datafolder.mkdirs();
        datafolder = new File(pathData + File.separator + "UBM");
        datafolder.mkdirs();
        datafolder = new File(pathData + File.separator + "ACC");//Folder to save data form acceler
        datafolder.mkdirs();
        datafolder = new File(pathData + File.separator + "TAP");//Folder to save data from fingertapping
        datafolder.mkdirs();

        //}


        // Get an instance of the SensorManager. ACCELEROMETER
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //acc = new SetAcc(this);
        acc =  new ActivateAcc(this,mSensorManager);
        //ID_text  = (EditText)findViewById(R.id.main_subjectID);

        //Chronometer
        rectimer = (Chronometer) findViewById(R.id.chrono);
        //tasktext = (TextView)findViewById(R.id.Timer_text);



        Button bt_speech=(Button)  findViewById(R.id.main_speech);
        bt_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent taskN = new Intent(MainActivity.this, SpeechTasks.class);
                Intent taskNT = new Intent(MainActivity.this, MovementMeasure.class);
                Intent taskMov = new Intent(MainActivity.this, MovementTasks.class);
                taskMov.putExtra("ID",ID_text);
                taskN.putExtra("subjectType", subjectType);


                taskNT.putExtra("ID",ID_text);
                taskN.putExtra("ID",ID_text);
                startActivity(taskN);
                finish();

            }
        });

        Button bt_movement=(Button)  findViewById(R.id.main_movement);
        bt_movement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent taskN = new Intent(MainActivity.this, MovementTasks.class);
                Intent taskNT = new Intent(MainActivity.this, MovementMeasure.class);
                Intent taskSpeech = new Intent(MainActivity.this, SpeechTasks.class);
                taskNT.putExtra("ID",ID_text);
                taskN.putExtra("ID",ID_text);
                taskSpeech.putExtra("ID",ID_text);
                taskN.putExtra("subjectType", subjectType);

                startActivity(taskN);
                finish();

            }
        });
        Button bt_next = (Button) findViewById(R.id.main_btNext);
        bt_next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void  onClick(View view){
                Intent taskN = new Intent(MainActivity.this, DisplayMeasures.class);
                Intent taskNT = new Intent(MainActivity.this, MovementMeasure.class);
                taskNT.putExtra("ID",ID_text);
                taskN.putExtra("ID",ID_text);
                taskN.putExtra("subjectType", subjectType);

                startActivity(taskN);
                finish();
            }
        });

    }

   /* public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }*/




    //Request for permission Android >= 6
    private static final int REQUEST_PERMISSIONS = 0;
    private int RequestPermissions() {
        //----------------------------------------------------------------
        //Request permission to RECORD AUDIO and STORE DATA on the phone
        //----------------------------------------------------------------
        //The app had permission to record audio?
        int audio_perm = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        //int storage_per = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (audio_perm != PackageManager.PERMISSION_GRANTED) {
            //If there is not permission to record and store audio files, then ask to the user for it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.MODIFY_AUDIO_SETTINGS}, REQUEST_PERMISSIONS);
        }
        return audio_perm;
    }//END REQUEST PERMISSION
    //@Override
    //public void onBackPressed() {
        //Intent principalReturn= new Intent(MainActivity.this, DataSubject.class);

        //startActivity(principalReturn);
        //finish();
    //}
        @Override
        public void onBackPressed() {
            moveTaskToBack(true);
        }


}

