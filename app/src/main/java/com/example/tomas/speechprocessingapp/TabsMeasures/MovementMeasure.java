package com.example.tomas.speechprocessingapp.TabsMeasures;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomas.speechprocessingapp.DataSubject;
import com.example.tomas.speechprocessingapp.DisplayMeasures;
import com.example.tomas.speechprocessingapp.MainActivity;
import com.example.tomas.speechprocessingapp.R;
import com.example.tomas.speechprocessingapp.RecordingsList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * Created by paulaperez on 28/04/18.
 */

public class MovementMeasure extends Fragment {

    public int intentFlag=0;
    public String ID,ID2;
    public int flag=0;
    String pathData = null;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /*    if (getArguments() != null) {
                ID = getArguments().getString("ID");
                //Toast.makeText(getContext(), "Entró " + ID, Toast.LENGTH_LONG).show();

            }
*/


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_display_measures_mov, container, false);

        Bundle dM = getArguments();
        if (dM!=null) {
           ID = dM.getString("ID");
            //String id_text = getArguments() != null ? getArguments().getString("message") : "id";
            //
            // id_text = getArguments().getString("ID") ;
            Log.d("id-data", ID);
        } else {
            Log.d("id-data", "vacio");
        }
        pathData = Environment.getExternalStorageDirectory() + File.separator + "AppSpeechData";

        /*Button btSpeed=(Button) rootView.findViewById(R.id.mov_btSpeed) ;
        btSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecordingsList.class);
                intent.putExtra(EXTRA_MESSAGE, pathData+ File.separator+"ACC");
                intent.putExtra("taskFlag", "SPEED");

                getActivity().startActivity(intent);
            }
        });*/

        Button btACC=(Button) rootView.findViewById(R.id.mov_btAcc) ;
        btACC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecordingsList.class);
                intent.putExtra(EXTRA_MESSAGE, pathData+ File.separator+"ACC");
                intent.putExtra("taskFlag", "ACC");
                getActivity().startActivity(intent);
            }
        });

        Button btTap=(Button) rootView.findViewById(R.id.mov_btTapp) ;
        btTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecordingsList.class);
                intent.putExtra(EXTRA_MESSAGE, pathData+ File.separator+"TAP");
                intent.putExtra("taskFlag", "TAP");
                getActivity().startActivity(intent);
            }
        });


 Button btRestart=(Button) rootView.findViewById(R.id.mov_btRestart) ;
        btRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DataSubject.class);

                getActivity().startActivity(intent);

                getActivity().finish();

            }
        });
















        return rootView;
    }

    public MovementMeasure(){

    }







}
