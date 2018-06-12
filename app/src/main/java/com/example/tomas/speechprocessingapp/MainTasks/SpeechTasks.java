package com.example.tomas.speechprocessingapp.MainTasks;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomas.speechprocessingapp.DataSubject;
import com.example.tomas.speechprocessingapp.FingerTapping;
import com.example.tomas.speechprocessingapp.MainActivity;
import com.example.tomas.speechprocessingapp.R;
import com.example.tomas.speechprocessingapp.RecordingsList;
import com.example.tomas.speechprocessingapp.SpeechProcessing.SpeechRec;
import com.example.tomas.speechprocessingapp.TabsMeasures.MovementMeasure;
import com.example.tomas.speechprocessingapp.TremorProcessing.AccGraph;
import com.example.tomas.speechprocessingapp.TremorProcessing.ActivateAcc;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * Created by paulaperez on 28/04/18.
 */
public class SpeechTasks extends AppCompatActivity {
    private boolean recflag = false;
    private boolean gaitflag = false;
    String pathData = null, pathNew=null;
    private Chronometer rectimer= null;
    boolean mStartPlaying = true;
    private MediaPlayer mPlayer = null;
    String taskFlag;
    private static final String LOG_TAG = "AudioRecordTest";

    private String ID_text="", subjectType="";
    private int facc=1,fspee = 1;
    private SensorManager mSensorManager;
    public ActivateAcc acc, acc2;
    public SpeechRec spe;
    TextView tasktext = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_tasks);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //ID from DataSubject to rename files
        Bundle fromDataSubject= getIntent().getExtras();
        if(fromDataSubject!=null){


            ID_text=fromDataSubject.getString("ID","");
            subjectType=fromDataSubject.getString("subjectType","");


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
        datafolder = new File(pathData + File.separator + "SPEECHTASKS"+ File.separator + "VOWELS");//Folder to save data from fingertapping
        datafolder.mkdirs();
        datafolder = new File(pathData + File.separator + "SPEECHTASKS"+ File.separator + "READINGTEXT");//Folder to save data from fingertapping
        datafolder.mkdirs();
        datafolder = new File(pathData + File.separator + "SPEECHTASKS"+ File.separator + "DDK");//Folder to save data from fingertapping
        datafolder.mkdirs();

        //}
        //}


        // Get an instance of the SensorManager. ACCELEROMETER
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //acc = new SetAcc(this);
        acc =  new ActivateAcc(this,mSensorManager);
        //ID_text  = (EditText)findViewById(R.id.main_subjectID);

        //Chronometer
        rectimer = (Chronometer) findViewById(R.id.chrono);
        //tasktext = (TextView)findViewById(R.id.Timer_text);

        /*Button bt_listrec = (Button) findViewById(R.id.main_listrec);
        bt_listrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpeechTasks.this, RecordingsList.class);
                intent.putExtra(EXTRA_MESSAGE, pathData+File.separator+"WAV");
                intent.putExtra("wavFlag", 1);
                startActivity(intent);
            }
        });
*/
        Button bt_speechrec = (Button) findViewById(R.id.main_speechrec);
        bt_speechrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button pb = (Button) findViewById(R.id.main_speechrec);
                rectimer = (Chronometer) findViewById(R.id.chrono);
                pathNew=Startrec("READINGTEXT","READINGTEXT",ID_text,pb);
                taskFlag="RTEXT";

            }
        });

        Button bt_Arec = (Button) findViewById(R.id.main_Arec);
        bt_Arec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button pb = (Button) findViewById(R.id.main_Arec);
                rectimer = (Chronometer) findViewById(R.id.chrono_A);
                pathNew=Startrec("VOWELS","AVowel",ID_text,pb);
                taskFlag="A";

            }
        });

        Button bt_Patakarec = (Button) findViewById(R.id.main_Patakarec);
        bt_Patakarec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button pb = (Button) findViewById(R.id.main_Patakarec);
                rectimer = (Chronometer) findViewById(R.id.chrono_Pataka);
                pathNew=Startrec("DDK","PATAKA",ID_text,pb);
                taskFlag="PATAKA";
            }
        });

        Button bt_Pakatarec = (Button) findViewById(R.id.main_Pakatarec);
        bt_Pakatarec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button pb = (Button) findViewById(R.id.main_Pakatarec);
                rectimer = (Chronometer) findViewById(R.id.chrono_Pakata);
                pathNew=Startrec("DDK","PAKATA",ID_text,pb);
                taskFlag="PAKATA";

            }
        });

        Button bt_Petakarec = (Button) findViewById(R.id.main_Petakarec);
        bt_Petakarec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button pb = (Button) findViewById(R.id.main_Petakarec);
                rectimer = (Chronometer) findViewById(R.id.chrono_Petaka);
                pathNew=Startrec("DDK","PETAKA",ID_text,pb);
                taskFlag="PETAKA";

            }
        });
        Button bt_Parec = (Button) findViewById(R.id.main_Parec);
        bt_Parec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button pb = (Button) findViewById(R.id.main_Parec);
                rectimer = (Chronometer) findViewById(R.id.chrono_Pa);
                pathNew=Startrec("DDK","PA",ID_text,pb);
                taskFlag="PA";

            }
        });

        Button bt_Karec = (Button) findViewById(R.id.main_Karec);
        bt_Karec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button pb = (Button) findViewById(R.id.main_Karec);
                rectimer = (Chronometer) findViewById(R.id.chrono_Ka);
                pathNew=Startrec("DDK","KA",ID_text,pb);
                taskFlag="KA";

            }
        });
        Button bt_Tarec = (Button) findViewById(R.id.main_Tarec);
        bt_Tarec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button pb = (Button) findViewById(R.id.main_Tarec);
                rectimer = (Chronometer) findViewById(R.id.chrono_Ta);
                pathNew=Startrec("DDK","TA",ID_text,pb);
                taskFlag="TA";
            }
        });

        ////////////////////77/UPLOAD INFORMATION/////////////////////////


        ///READ TEXT//
        Button bt_saveReadText=(Button) findViewById(R.id.main_saveReadText);
        bt_saveReadText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("ruta",String.valueOf(pathNew));
                if(pathNew!=null && taskFlag.equals("RTEXT")){
                    Log.e("ruta",String.valueOf(pathNew));
                    StorageUpload(pathNew);
                }else{Log.e("ruta","Path Null");}
            }
        });

        ///A VOWEL//
        Button bt_saveAvowel=(Button) findViewById(R.id.main_saveAvowel);
        bt_saveAvowel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ruta",String.valueOf(pathNew));
                if(pathNew!=null && taskFlag.equals("A")){
                    Log.e("ruta",String.valueOf(pathNew));
                    StorageUpload(pathNew);
                }else{Log.e("ruta","Path Null");}

            }
        });

        //PATAKA//

        Button bt_savePataka=(Button) findViewById(R.id.main_savePataka);
        bt_savePataka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ruta",String.valueOf(pathNew));
                if(pathNew!=null && taskFlag.equals("PATAKA")){
                    Log.e("ruta",String.valueOf(pathNew));
                    StorageUpload(pathNew);
                }else{Log.e("ruta","Path Null");}

            }
        });


        //PAKATA//

        Button bt_savePakata=(Button) findViewById(R.id.main_savePakata);
        bt_savePakata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ruta",String.valueOf(pathNew));
                if(pathNew!=null && taskFlag.equals("PAKATA")){
                    Log.e("ruta",String.valueOf(pathNew));
                    StorageUpload(pathNew);
                }else{Log.e("ruta","Path Null");}

            }
        });

        //PETAKA//

        Button bt_savePetaka=(Button) findViewById(R.id.main_savePetaka);
        bt_savePetaka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ruta",String.valueOf(pathNew));
                if(pathNew!=null && taskFlag.equals("PETAKA")){
                    Log.e("ruta",String.valueOf(pathNew));
                    StorageUpload(pathNew);
                }else{Log.e("ruta","Path Null");}

            }
        });

        //PA//

        Button bt_savePa=(Button) findViewById(R.id.main_savePa);
        bt_savePa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ruta",String.valueOf(pathNew));
                if(pathNew!=null && taskFlag.equals("PA")){
                    Log.e("ruta",String.valueOf(pathNew));
                    StorageUpload(pathNew);
                }else{Log.e("ruta","Path Null");}

            }
        });


        //TA//

        Button bt_saveTa=(Button) findViewById(R.id.main_saveTa);
        bt_saveTa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ruta",String.valueOf(pathNew));
                if(pathNew!=null && taskFlag.equals("TA")){
                    Log.e("ruta",String.valueOf(pathNew));
                    StorageUpload(pathNew);
                }else{Log.e("ruta","Path Null");}

            }
        });

        //KA//

        Button bt_saveKa=(Button) findViewById(R.id.main_saveKa);
        bt_saveKa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ruta",String.valueOf(pathNew));
                if(pathNew!=null && taskFlag.equals("KA")){
                    Log.e("ruta",String.valueOf(pathNew));
                    StorageUpload(pathNew);
                }else{Log.e("ruta","Path Null");}

            }
        });


        final Button btPlayRText=(Button) findViewById(R.id.main_RTextPlay);
        btPlayRText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pathNew!=null && taskFlag.equals("RTEXT")){

                    if (mStartPlaying) {
                        plays(mStartPlaying, btPlayRText);
                        mStartPlaying = false;
                        Log.e("cosa", "Entró");
                    } else {
                        plays(mStartPlaying, btPlayRText);
                        mStartPlaying = true;
                    }



                }else{Log.e("ruta","No recorded audio yet");}


            }
        });

        final Button btPlayA=(Button) findViewById(R.id.main_APlay);
        btPlayA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pathNew!=null && taskFlag.equals("A")){

                    if (mStartPlaying) {
                        plays(mStartPlaying, btPlayA);
                        mStartPlaying = false;
                        Log.e("cosa", "Entró");
                    } else {
                        plays(mStartPlaying, btPlayA);
                        mStartPlaying = true;
                    }



                }else{Log.e("ruta","No recorded audio yet");}


            }
        });


        final Button btPlayPataka=(Button) findViewById(R.id.main_PatakaPlay);
        btPlayPataka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pathNew!=null && taskFlag.equals("PATAKA")){

                    if (mStartPlaying) {
                        plays(mStartPlaying, btPlayPataka);
                        mStartPlaying = false;
                        Log.e("cosa", "Entró");
                    } else {
                        plays(mStartPlaying, btPlayPataka);
                        mStartPlaying = true;
                    }



                }else{Log.e("ruta","No recorded audio yet");}


            }
        });


        final Button btPlayPakata=(Button) findViewById(R.id.main_PakataPlay);
        btPlayPakata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pathNew!=null && taskFlag.equals("PAKATA")){

                    if (mStartPlaying) {
                        plays(mStartPlaying, btPlayPakata);
                        mStartPlaying = false;
                        Log.e("cosa", "Entró");
                    } else {
                        plays(mStartPlaying, btPlayPakata);
                        mStartPlaying = true;
                    }



                }else{Log.e("ruta","No recorded audio yet");}


            }
        });


        final Button btPlayPetaka=(Button) findViewById(R.id.main_PetakaPlay);
        btPlayPetaka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pathNew!=null && taskFlag.equals("PETAKA")){

                    if (mStartPlaying) {
                        plays(mStartPlaying, btPlayPetaka);
                        mStartPlaying = false;
                        Log.e("cosa", "Entró");
                    } else {
                        plays(mStartPlaying, btPlayPetaka);
                        mStartPlaying = true;
                    }



                }else{Log.e("ruta","No recorded audio yet");}


            }
        });



        final Button btPlayPa=(Button) findViewById(R.id.main_PaPlay);
        btPlayPa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pathNew!=null && taskFlag.equals("PA")){

                    if (mStartPlaying) {
                        plays(mStartPlaying, btPlayPa);
                        mStartPlaying = false;
                        Log.e("cosa", "Entró");
                    } else {
                        plays(mStartPlaying, btPlayPa);
                        mStartPlaying = true;
                    }



                }else{Log.e("ruta","No recorded audio yet");}


            }
        });

        final Button btPlayTa=(Button) findViewById(R.id.main_TaPlay);
        btPlayTa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pathNew!=null && taskFlag.equals("TA")){

                    if (mStartPlaying) {
                        plays(mStartPlaying, btPlayTa);
                        mStartPlaying = false;
                        Log.e("cosa", "Entró");
                    } else {
                        plays(mStartPlaying, btPlayTa);
                        mStartPlaying = true;
                    }



                }else{Log.e("ruta","No recorded audio yet");}


            }
        });


        final Button btPlayKa=(Button) findViewById(R.id.main_KaPlay);
        btPlayKa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pathNew!=null && taskFlag.equals("KA")){

                    if (mStartPlaying) {
                        plays(mStartPlaying, btPlayKa);
                        mStartPlaying = false;
                        Log.e("cosa", "Entró");
                    } else {
                        plays(mStartPlaying, btPlayKa);
                        mStartPlaying = true;
                    }



                }else{Log.e("ruta","No recorded audio yet");}


            }
        });




    }

    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
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
        if(!ID_text.equals("")){
        pathName=subjectType+"/"+ID_text+"/"+format+"/"+"speech_files"+"/";
        }else{
            pathName=subjectType+"/"+format+"/"+"speech_files"+"/";
        }
        Log.d("path",file.getLastPathSegment());
        StorageReference riversRef = storageReference.child(pathName+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("Error",exception.getMessage());
                Toast.makeText(SpeechTasks.this, "Error in File uploading",
                        Toast.LENGTH_SHORT).show();
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Success",taskSnapshot.getMetadata().getDownloadUrl().toString());
                Toast.makeText(SpeechTasks.this, "File uploading Successful",
                        Toast.LENGTH_SHORT).show();
                // ...
            }
        });


    }


    //Start recording when the "Record" button is clicked
    private String Startrec(String fileName,String task, String id, Button pb ) {
        //Request permissions to record and audio files
        int record_perm = RequestPermissions();

        String pathNewAcc=null;
        rectimer.setBase(SystemClock.elapsedRealtime());
        if (record_perm == PackageManager.PERMISSION_GRANTED) {
            String ID="";

            //Log.d("path pathall",pathall);
            /*if(!ID_text.equals("")){
                ID=id+"_";
            }else{
                ID=id;
            }*/

            if(subjectType.equals("Controls")){
                ID=ID_text+"HC"+"_";
            }else if(subjectType.equals("Patients")){
                ID=ID_text+"PD"+"_";
            }

            recflag = !recflag;
            if (recflag) {
                pb.setText("Stop");
                rectimer.start();
                //Microphone
                //if (fspee==1) {
                //  setMicrophone();
                //startSpeechRecording();
                spe = new SpeechRec(this,pathData,ID+task, fileName);
                spe.start();


                //}
                //Accelerometer
                //if (facc==1) {
                //acc.startAcc(pathData+File.separator+"WAV",ID_text);
                acc.startAcc(pathData+File.separator+"WAV"+File.separator+task,ID);
                //}

            } else {
                pb.setText("Start");
                rectimer.stop();
                rectimer.setBase(SystemClock.elapsedRealtime());
                //if (fspee==1) {
                //stopSpeechRecording();//Microphone

                spe.stopSpeechRecording();
                //}
                //if (facc==1) {

                acc.stopAcc();

                //}
            }
        } else {
            Toast.makeText(this, "You need to grant permission to record and store audio files", Toast.LENGTH_LONG).show();
        }
        pathNewAcc=spe.returnPath();
        return pathNewAcc;
    }

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
    @Override
    public void onBackPressed() {
        Intent principalReturn= new Intent(SpeechTasks.this, MainActivity.class);
        principalReturn.putExtra("subjectType", subjectType);

        principalReturn.putExtra("ID",ID_text);
        startActivity(principalReturn);

        //finish();
    }





    public void plays(boolean mStartPlaying, Button btPlays) {
        onPlay(mStartPlaying, btPlays);
        if (mStartPlaying) {
            btPlays.setText("Stop");
        } else {
            btPlays.setText("Play");
        }
        mStartPlaying = !mStartPlaying;
    }

    private void onPlay(boolean start, Button btPlays) {
        if (start) {
            startPlaying(btPlays);
        } else {
            stopPlaying();
        }
    }

    private void startPlaying(final Button btPlays ) {
        String WAVspath=pathNew;
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(WAVspath);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.stop();
                //Button pb = (Button) findViewById(R.id.main_speechPlay);
                btPlays.setText("Play");
                mStartPlaying = true;
                if (mediaPlayer != null) {
                    mPlayer.release();
                }

            }
        });

    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }



}

