package com.example.tomas.speechprocessingapp.TabsMeasures;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tomas.speechprocessingapp.DataSubject;
import com.example.tomas.speechprocessingapp.R;
import com.example.tomas.speechprocessingapp.RecordingsList;

import java.io.File;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * Created by paulaperez on 28/04/18.
 */

public class SpeechMeasures extends Fragment {

    String pathData = null;
    String ID;


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
        View rootView = inflater.inflate(R.layout.fragment_display_measures_speech, container, false);

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

        Button btProsody=(Button) rootView.findViewById(R.id.speech_btProsody) ;
        btProsody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecordingsList.class);
                intent.putExtra(EXTRA_MESSAGE, pathData+ File.separator + "SPEECHTASKS"+ File.separator + "READINGTEXT");
                intent.putExtra("wavFlag", 1);
                intent.putExtra("taskFlag", "PROSODY");
                getActivity().startActivity(intent);
            }
        });

        Button btPhonation=(Button) rootView.findViewById(R.id.speech_btPhonation) ;
        btPhonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecordingsList.class);
                intent.putExtra(EXTRA_MESSAGE, pathData+ File.separator + "SPEECHTASKS"+ File.separator + "VOWELS");
                intent.putExtra("wavFlag", 1);
                intent.putExtra("taskFlag", "PHONATION");
                getActivity().startActivity(intent);
            }
        });

        Button btArt=(Button) rootView.findViewById(R.id.speech_btArt) ;
        btArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecordingsList.class);
                intent.putExtra(EXTRA_MESSAGE, pathData+ File.separator + "SPEECHTASKS"+ File.separator + "DDK");
                intent.putExtra("wavFlag", 1);
                intent.putExtra("taskFlag", "ART");
                getActivity().startActivity(intent);
            }
        });

        Button btRestart=(Button) rootView.findViewById(R.id.speech_btRestart) ;
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
    public SpeechMeasures(){

    }
}
//TODO apenas voy en los fragment para las tabs
