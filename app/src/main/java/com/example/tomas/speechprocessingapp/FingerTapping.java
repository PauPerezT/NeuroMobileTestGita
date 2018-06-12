package com.example.tomas.speechprocessingapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomas.speechprocessingapp.MainTasks.MovementTasks;
import com.example.tomas.speechprocessingapp.MainTasks.SpeechTasks;
import com.example.tomas.speechprocessingapp.TremorProcessing.AccGraph;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class FingerTapping extends AppCompatActivity {
    private int timef = 0;
    private OutputStreamWriter fout;
    private String TAPpath = "";
    TextView timer=null;
    private String format;
    String subjectType;
    private File f;
    String ID ;
    String ID_Text="", fname;
    String pathNew=null;
    boolean flagOpen=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_tapping);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();


        //Save folder
        TAPpath = intent.getStringExtra(AlarmClock.EXTRA_MESSAGE);
        //Toast.makeText(FingerTapping.this, TAPpath,Toast.LENGTH_LONG).show();
         ID= intent.getStringExtra("ID");
        fname = intent.getStringExtra("Button");
        subjectType=intent.getStringExtra("subjectType");


        if(subjectType.equals("Controls")){
            ID_Text=ID+"HC"+"_";
        }else if(subjectType.equals("Patients")){
            ID_Text=ID+"PD"+"_";
        }


        TextView tvID=(TextView)findViewById(R.id.fingertap_task);
        tvID.setText(ID);





        final Button btStart=(Button) findViewById(R.id.fingertap_start);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                btStart.setEnabled(false);

                //////////////////////77

                Date date = new Date();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyhhmmss");
                //String format = simpleDateFormat.format(date);
                format = simpleDateFormat.format(date);
                f = new File(TAPpath, ID_Text+fname+"_"+format + ".txt");
                pathNew=TAPpath+File.separator+ID_Text+fname+"_"+format + ".txt";





                try {
                    fout = new OutputStreamWriter(new FileOutputStream(f));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                timer = (TextView)findViewById(R.id.fingertap_timer);
                final CountDownTimer time=new CountDownTimer(11000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        timer.setText(String.valueOf((millisUntilFinished / 1000)-1));
                        if (String.valueOf((millisUntilFinished / 1000)-1)=="0")
                        {

                            timer.setText("Stop");


                        }else{

                            timef = (int) SystemClock.currentThreadTimeMillis();
                            Button tapme = (Button) findViewById(R.id.fingertap_FinTap);
                            tapme.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                    // Vibrate for 500 milliseconds
                                    vib.vibrate(100);
                                    int time2 = (int) SystemClock.currentThreadTimeMillis()-timef;
                                    String timeStr = String.valueOf(time2);

                                    generateNoteOnSD( timeStr  + "\n\r");

                                }
                            });


                        }
                    }
                    public void onFinish() {
                        try {
                            fout.close();

                            btStart.setEnabled(true);



                        } catch (Exception e) {
                            e.printStackTrace();
                        }






                        //nameEncoding= fout.getEncoding();
                        //Toast.makeText(FingerTapping.this,nameEncoding,Toast.LENGTH_LONG).show();
                        //Log.e("taps",nameEncoding);
                        //SystemClock.sleep(100);
                        //finish();//close current activity
                        //timer.setText("0");;



                    }
                }.start();







                //////////////////////////777





            }
        });


        Button btUpload=(Button)findViewById(R.id.fingertap_saveTxt);
        btUpload.setOnClickListener(new View.OnClickListener() {
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



public void generateNoteOnSD(String data_sensors){

        try {
            fout.write(data_sensors);

        } catch (Exception e) {
            e.printStackTrace();
        }

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
        if(!ID.equals("")){
            pathName=subjectType+"/"+ID+"/"+format+"/"+"movement_files"+"/";
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
                Toast.makeText(FingerTapping.this, "Error in File uploading",
                        Toast.LENGTH_SHORT).show();
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Success",taskSnapshot.getMetadata().getDownloadUrl().toString());
                Toast.makeText(FingerTapping.this, "File uploading Successful",
                        Toast.LENGTH_SHORT).show();
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });


    }


    private void timerDialog() {





    }
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }


    public String returnPath(){return pathNew;}
}
