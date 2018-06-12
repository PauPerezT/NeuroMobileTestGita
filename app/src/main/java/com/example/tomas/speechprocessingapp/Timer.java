package com.example.tomas.speechprocessingapp;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import android.hardware.SensorManager;
import com.example.tomas.speechprocessingapp.TremorProcessing.ActivateAcc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timer extends AppCompatActivity {
    private int timef = 0;
    private OutputStreamWriter fout;
    private String path = "";
    TextView timer=null;
    private String format;
    private File f;
    public ActivateAcc acc;
    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Intent intent = getIntent();

        //Save folder
        path = intent.getStringExtra(AlarmClock.EXTRA_MESSAGE);
        String fname = intent.getStringExtra("Button");

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acc =  new ActivateAcc(this,mSensorManager);
        acc.startAcc(path,fname);


        timer = (TextView)findViewById(R.id.Timer_timer);

        new CountDownTimer(11000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf((millisUntilFinished / 1000)-1));
            }

            public void onFinish() {
                try {
                    fout.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();//close current activity
                //timer.setText("0");;
            }
        }.start();

    }

}
