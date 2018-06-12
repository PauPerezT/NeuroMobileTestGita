package com.example.tomas.speechprocessingapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomas.speechprocessingapp.DBFirebase.DataSubjectControlFirebase;
import com.example.tomas.speechprocessingapp.DBFirebase.DataSubjectFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by PAULA on 02/05/2018.
 */



public class DataSubject extends AppCompatActivity {


    private  EditText etname, etAge, etID, etTimeDiagnosis, etMedicines, etMedicinesDose, etMedicinesTime;
    private Button btnextTaks;
    private RadioButton rbLeft, rbRight, rbMale, rbFemale, rbYesSmoker, rbNoSmoker;
    private Spinner spScholarity;
    public static final String dsId="id";
    private String completeName="", age="", idNumber="", timeDiagnosis="", dominantHand="", gender="", smoker="", scholarity="", medicines="", medicinesDose="", medicinesTime="", tInfoMedicines="";
///Firebase
    private DatabaseReference databaseReference;
    private TextView tvMedicines;
    private String subjectType;
    //////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_subject);
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.brain2);
        //Firebase
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        //////////






        spScholarity=(Spinner) findViewById(R.id.spinnerScholDegree);
        btnextTaks=(Button)findViewById(R.id.data_subject_btNext);


        ///Spinner
        ArrayAdapter<String> scholarityAdapter = new ArrayAdapter<String>(DataSubject.this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.scholarity));
        spScholarity.setAdapter(scholarityAdapter);

        etTimeDiagnosis=(EditText) findViewById(R.id.etTimeDiagnosis);
        etMedicines=(EditText) findViewById(R.id.etMedicines);
        etMedicinesDose=(EditText) findViewById(R.id.etDose);
        etMedicinesTime=(EditText) findViewById(R.id.etMedicinesTime);
        tvMedicines=(TextView) findViewById(R.id.tvMedicines);


        RadioButton rbPatient=(RadioButton) findViewById(R.id.rbPatient);
        RadioButton rbControl=(RadioButton) findViewById(R.id.rbControl);

        rbPatient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etTimeDiagnosis.setVisibility(View.GONE);
                etMedicines.setVisibility(View.GONE);
                etMedicinesDose.setVisibility(View.GONE);
                etMedicinesTime.setVisibility(View.GONE);
                tvMedicines.setVisibility(View.GONE);
            }
        });

        rbControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                etTimeDiagnosis.setVisibility(View.VISIBLE);
                etMedicines.setVisibility(View.VISIBLE);
                etMedicinesDose.setVisibility(View.VISIBLE);
                etMedicinesTime.setVisibility(View.VISIBLE);
                tvMedicines.setVisibility(View.VISIBLE);

            }
        });








        btnextTaks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //////////////////////////////////////
                etname=(EditText)findViewById(R.id.etName);
                completeName=etname.getText().toString();

                etAge=(EditText) findViewById(R.id.etAge);
                age=etAge.getText().toString();

                etID=(EditText)findViewById(R.id.etDS_ID);
                idNumber=etID.getText().toString();


                rbLeft=(RadioButton) findViewById(R.id.rbLeft);
                rbRight=(RadioButton) findViewById(R.id.rbRight);

                RadioButton rbPatient=(RadioButton) findViewById(R.id.rbPatient);
                RadioButton rbControl=(RadioButton) findViewById(R.id.rbControl);

                if(rbPatient.isChecked()){
                    subjectType="Patients";
                    etTimeDiagnosis=(EditText) findViewById(R.id.etTimeDiagnosis);
                    timeDiagnosis=etTimeDiagnosis.getText().toString();

                    etMedicines=(EditText) findViewById(R.id.etMedicines);
                    medicines=etMedicines.getText().toString();


                    etMedicinesDose=(EditText) findViewById(R.id.etDose);
                    medicinesDose=etMedicinesDose.getText().toString();

                    etMedicinesTime=(EditText) findViewById(R.id.etMedicinesTime);
                    medicinesTime=etMedicinesTime.getText().toString();

                    tInfoMedicines="Medicines: "+medicines+" "+"Dose: "+ medicinesDose+" "+"Time: "+medicinesTime;


                }else {
                    subjectType="Controls";

                }


                if(rbLeft.isChecked()){
                    dominantHand="Left";

                }else {
                    dominantHand="Right";

                }


                rbMale=(RadioButton) findViewById(R.id.rbMale) ;
                rbFemale=(RadioButton) findViewById(R.id.rbFemale);

                if(rbMale.isChecked()){
                    gender="Male";

                }else {
                    gender="Female";

                }

                rbYesSmoker=(RadioButton) findViewById(R.id.rbSmokerYes);
                rbNoSmoker=(RadioButton) findViewById(R.id.rbSmokerNo);

                if(rbYesSmoker.isChecked()){
                    smoker="Yes";

                }else {
                    smoker="Not";

                }

                scholarity=spScholarity.getSelectedItem().toString();

                ////////////////////////////////////


                FirebaseDatabase.getInstance();
                databaseReference= FirebaseDatabase.getInstance().getReference();


                if(subjectType.equals("Patients")) {

                    if(checkEmptyFieldPatient(completeName, age, idNumber,medicines, medicinesDose,
                            medicinesTime, timeDiagnosis)) {
                        DataSubjectFirebase databases = new DataSubjectFirebase(databaseReference.push().getKey(),
                                completeName,
                                age,
                                idNumber,
                                timeDiagnosis,
                                dominantHand,
                                gender,
                                smoker,
                                scholarity,
                                tInfoMedicines);
                        databaseReference.child("DataPDSubject").child(databases.getId()).setValue(databases);
                        Intent taskN = new Intent(DataSubject.this, MainActivity.class);
                        taskN.putExtra("ID", etID.getText().toString());
                        taskN.putExtra("subjectType", subjectType);
                        startActivity(taskN);
                        finish();

                    }else{
                        checkEmptyFieldPatient(completeName, age, idNumber,medicines, medicinesDose,
                                medicinesTime, timeDiagnosis);
                    }

                }else if(subjectType.equals("Controls")){

                    if(checkEmptyFieldControl(completeName, age, idNumber)) {

                        DataSubjectControlFirebase databases = new DataSubjectControlFirebase(databaseReference.push().getKey(),
                                completeName,
                                age,
                                idNumber,
                                dominantHand,
                                gender,
                                smoker,
                                scholarity);
                        databaseReference.child("DataControlSubject").child(databases.getId()).setValue(databases);
                        Intent taskN = new Intent(DataSubject.this, MainActivity.class);
                        taskN.putExtra("ID", etID.getText().toString());
                        taskN.putExtra("subjectType", subjectType);

                        startActivity(taskN);
                        finish();
                    }else{
                        checkEmptyFieldControl(completeName, age, idNumber);
                    }
                }


            }
        });




    }

    private boolean checkEmptyFieldControl(String name, String age, String ID){
        if( name.isEmpty() || age.isEmpty() || ID.isEmpty()){
            Toast.makeText(DataSubject.this, "Please fill the blanks", Toast.LENGTH_LONG ).show();
            return false;
        }
        return true;
    }

    private boolean checkEmptyFieldPatient(String name, String age, String ID, String TD,
                                           String medicineName, String medicinesDose, String medicinesTime){
        if( name.isEmpty() || age.isEmpty() || ID.isEmpty() || medicineName.isEmpty() || medicinesDose.isEmpty()
                || medicinesTime.isEmpty() || TD.isEmpty()){
            Toast.makeText(DataSubject.this, "Please fill the blanks", Toast.LENGTH_LONG ).show();
            return false;
        }
        return true;
    }


}
