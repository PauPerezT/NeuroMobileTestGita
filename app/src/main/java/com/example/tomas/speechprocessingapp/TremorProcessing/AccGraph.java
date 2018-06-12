package com.example.tomas.speechprocessingapp.TremorProcessing;

/**
 * Created by paulaperez on 27/04/18.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.tomas.speechprocessingapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
//package com.example.tomas.speechprocessingapp.TremorProcessing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomas.speechprocessingapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccGraph extends AppCompatActivity implements SensorEventListener {
    private Chronometer rectimer= null;
    private boolean accflag = false;
    private Sensor mAcc;
    private double[] linear_acceleration = new double[3];
    private SensorManager mSensorManager;
    private String pathData,fname, pathNew;
    private String format;
    private  String accx,accy,accz;
    private File f;
    private OutputStreamWriter fout;
    private double[] gravity = {0,0,0};
    private LineGraphSeries<DataPoint> mSeries1, mSeries2, mSeries3;
    private double graph1LastXValue = 0d;
    public int size_axis=400;
    private GraphView graph;
    String userID="", subjectType="",ID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_graph);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final String path = intent.getStringExtra("Path");//Path to store data
        userID = intent.getStringExtra("ID");//Subject ID
        subjectType = intent.getStringExtra("subjectType");//Subject Type

        final String tname = intent.getStringExtra("Task");//Task name


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        Date date = new Date();
        String format = simpleDateFormat.format(date);

     /*   if( userID.equals("") )
        {
            userID = format;
        }*/

        if(subjectType.equals("Controls")){
            ID=userID+"HC"+"_";
        }else if(subjectType.equals("Patients")){
            ID=userID+"PD"+"_";
        }


        this.pathData = path;
        this.fname = ID+tname;
        //this.format = format;

        //Set text for ID
        TextView idtext = (TextView) findViewById(R.id.accgraph_id);
        idtext.setText(userID);
        //Set text for Task
        TextView tasktext = (TextView) findViewById(R.id.accgraph_task);
        tasktext.setText(tname);

        //super(context);
        //this.mSensorManager = mSensorManager;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);



        //Chrono
        rectimer = (Chronometer) findViewById(R.id.accgraph_chrono);


        //Graph
        graph = (GraphView) findViewById(R.id.accgraph_graph);

        Button bt_start_acc = (Button) findViewById(R.id.accgraph_start);
        bt_start_acc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                init_plot();
                capturedata();
            }
        });

        Button bt_saveTxt=(Button) findViewById(R.id.main_saveTxt);
        bt_saveTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ruta",String.valueOf(pathNew));
                if(pathNew!=null){
                    Log.e("ruta",String.valueOf(pathNew));
                    StorageUpload(pathNew);
                }else{Log.e("ruta","Path Null");}
            }
        });

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        final double alpha = 0.8;
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //gravity = {0, 0, 0};
            // Isolate the force of gravity with the low-pass filter.
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            // Remove the gravity contribution with the high-pass filter, i.e, mean subtraction

            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

            graph1LastXValue += 1d;
            mSeries1.appendData(new DataPoint(graph1LastXValue, linear_acceleration[0]), true, size_axis);
            mSeries2.appendData(new DataPoint(graph1LastXValue, linear_acceleration[1]), true, size_axis);
            mSeries3.appendData(new DataPoint(graph1LastXValue, linear_acceleration[2]), true, size_axis);


            accx = String.valueOf(linear_acceleration[0]);
            accy = String.valueOf(linear_acceleration[1]);
            accz = String.valueOf(linear_acceleration[2]);
            //data_sensors = data_sensors + accx + '\t' + accy + '\t' + accz + "\n\r";
            generateNoteOnSD( accx + '\t' + accy + '\t' + accz + "\n\r");
            //Log.e("Accelerometer", data_sensors);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public  void startAcc()
    {
        graph.removeAllSeries();
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.addSeries(mSeries1);
        graph.addSeries(mSeries2);
        graph.addSeries(mSeries3);
        graph.getViewport().setXAxisBoundsManual(true);
        //graph.getViewport().setMinX(0);
        graph.getViewport().setMinX(graph1LastXValue - size_axis);

        //Text file
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyhhmmss");
        Date date = new Date();
        String format = simpleDateFormat.format(date);
        format = simpleDateFormat.format(date);
        f = new File(pathData, fname +"_"+format+".txt");
        pathNew=pathData+"/"+fname +"_"+format+".txt";
        try {
            fout = new OutputStreamWriter(new FileOutputStream(f));
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Start ACC
        mSensorManager.registerListener(this,mAcc, SensorManager.SENSOR_DELAY_GAME);
        //generateNoteOnSD(pathData,"testRecord" + format + ".txt",data_sensors);
    }
    public  void stopAcc()
    {
        mSensorManager.unregisterListener(this,mAcc);
        try {
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void init_plot() {
        //graph1LastXValue = 0d;
        mSeries1 = new LineGraphSeries<>();
        mSeries2 = new LineGraphSeries<>();
        mSeries3 = new LineGraphSeries<>();
        mSeries1.setTitle("accX");
        mSeries2.setTitle("accY");
        mSeries3.setTitle("accZ");
        mSeries1.setColor(Color.BLUE);
        mSeries2.setColor(Color.GREEN);
        mSeries3.setColor(Color.RED);
    }

    private void capturedata() {
        Button pb = (Button) findViewById(R.id.accgraph_start);
        rectimer.setBase(SystemClock.elapsedRealtime());
        accflag = !accflag;
        if (accflag) {
            pb.setText("Stop");
            rectimer.start();
            startAcc();


        } else {
            pb.setText("Start");
            rectimer.stop();
            rectimer.setBase(SystemClock.elapsedRealtime());
            stopAcc();


        }

    }

    public void generateNoteOnSD(String data_sensors){

        try {
            fout.write(data_sensors);
        } catch (Exception e) {
            e.printStackTrace();
        }
                /*
                outputStream = openFileOutput    (Environment.getExternalStorageDirectory().getAbsolutePath() + path_name+fileName, Context.MODE_PRIVATE);

                outputStream.write(data_sensors.getBytes());
                outputStream.close();*/

    }

    private void StorageUpload(String path){
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        StorageReference storageReference= firebaseStorage.getReference();
        DatabaseReference databaseReference;
        databaseReference= FirebaseDatabase.getInstance().getReference();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        Date date = new Date();
        String format = simpleDateFormat.format(date);

        String pathName;
        Uri file = Uri.fromFile(new File(path));
        //Log.d("path pathall",pathall);
        if(!userID.equals("")){
            pathName=subjectType+"/"+userID+"/"+format+"/"+"movement_files"+"/";
        }else{
            pathName=subjectType+"/"+format+"/"+"movement_files"+"/";
        }
        Log.d("path",file.getLastPathSegment());
        StorageReference riversRef = storageReference.child(pathName+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("Error",exception.getMessage());
                Toast.makeText(AccGraph.this, "Error in File uploading",
                        Toast.LENGTH_SHORT).show();
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Success",taskSnapshot.getMetadata().getDownloadUrl().toString());
                Toast.makeText(AccGraph.this, "File uploading Successful",
                        Toast.LENGTH_SHORT).show();
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });


    }
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }

}
