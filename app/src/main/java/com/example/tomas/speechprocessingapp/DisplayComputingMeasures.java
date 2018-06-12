package com.example.tomas.speechprocessingapp;

import android.content.Intent;
import android.os.Environment;
import android.provider.AlarmClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomas.speechprocessingapp.SpeechProcessing.tools.Matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DisplayComputingMeasures extends AppCompatActivity {

    File file;
    private String taskFlag, ID_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_computing_measures);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String path = intent.getStringExtra(AlarmClock.EXTRA_MESSAGE);
        taskFlag = intent.getStringExtra("task");



        file=new File(path);

        //Toast.makeText(DisplayComputingMeasures.this, lines[0],Toast.LENGTH_LONG).show();
        //String readTacc=readtxt(file);

        List[] valsAcc = readFileIntoArrayAcc(file);
        //Log.d("idato",String.valueOf(valsAcc[0]));
        double[] accX = listtoarray(valsAcc[0]);
        double[] accY = listtoarray(valsAcc[1]);
        double[] accZ = listtoarray(valsAcc[2]);



        TextView tvResultTitle = (TextView)findViewById(R.id.dcm_resultsTitle);
        TextView tvResult = (TextView)findViewById(R.id.dcm_resultPrinting);

        if(taskFlag.equals("TAP")){
            String readT=readtxt(file);
            String[] lines = readT.split("\n");
            List<Float> vals = readFileIntoArray(file);
            double[] pr = listtoarray(vals);
        tvResultTitle.setText("Tapping Results");

        tvResult.setText("Taps Counting: "+String.valueOf(pr.length));
        }else if(taskFlag.equals("ACC")){

            double mean=0.0;
           for(int i=0; i<accY.length; i++){
            mean=mean+(Math.sqrt((accX[i]*accX[i])+(accY[i]*accY[i])+(accZ[i]*accZ[i])));

            }
            mean=mean/accY.length;
            DecimalFormat df = new DecimalFormat("##.000");
            Log.d("idato",String.valueOf(df.format(mean)));
            tvResultTitle.setText("Acceleration Results");

            tvResult.setText("Acceleration Mean: "+"\r\n\r"+String.valueOf(df.format(mean))+"m/s²");
            Log.e("ACC",String.valueOf(df.format(mean)));
            Log.e("ACC",String.valueOf(mean));




        }/*else if(taskFlag=="SPEED"){


        }else if(taskFlag=="PHONATION") {
        }else if(taskFlag=="ART"){

        }else if(taskFlag=="PROSODY"){

        }*/



    }



    private String readtxt(File file) {

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            String ret = text.toString();

            return ret;
        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
            e.printStackTrace();
        }

        return "0";
    }

    private List<Float> readFileIntoArray (File filepath) {
        String file = readtxt(filepath);
        List<Float> ret = new ArrayList<>();
        String[] lines = file.split("\n");
        for (String line : lines) {
            String[] values = line.split(" ");

            for (String value : values){
                if(!value.isEmpty()){
                ret.add(Float.valueOf(value));}
            }
        }
        return ret;
    }



    private List[]  readFileIntoArrayAcc (File filepath) {
        String file = readtxt(filepath);
        int flagRow=0,flagAxis=0;
       Matrix featmat = null;//Feature matrix

        List<Float> retX = new ArrayList<>();
        List<Float> retY = new ArrayList<>();
        List<Float> retZ = new ArrayList<>();
        //List retT = new ArrayList<>();
        String[] lines = file.split("\n");




        //String[] lines = file.split("\n");
        for (String line : lines) {
            //Log.e("i-data","-------------"+String.valueOf(1));
            if (!line.isEmpty()) {
                //Log.d("id-data",String.valueOf(line));


                //ret.add(Float.valueOf(line));


            String[] values = line.split("\t");

            for (String value : values){//Log.d("id-data",value);


                // Log.e("i-data","-------------"+String.valueOf(2));
                //Log.d("id-data",String.valueOf(value));
                //String[] rows = value.split("\t");
                //Log.d("id-data","-----");





                        //Log.e("i-data","-------------"+String.valueOf(3));
                        //Log.d("id-data",String.valueOf(row));

                        if(flagAxis==3){
                            flagAxis=1;
                        }else{flagAxis = flagAxis + 1;}
                        flagRow = flagRow + 1;


                        //flagAxis = flagAxis + 1;
                        if (flagAxis == 1) {
                            //Log.d("id-data", "X" + row);
                            Log.d("id-data", "X " + value);
                            retX.add(Float.valueOf(value));

                            //flagAxis = flagAxis + 1;
                        }
                        if (flagAxis == 2) {
                            //Log.d("id-data", "Y" + row);
                            //flagAxis = flagAxis + 1;

                            retY.add(Float.valueOf(value));
                            Log.d("id-data", "Y " + String.valueOf(value));
                        }
                        if (flagAxis == 3) {
                            //Log.d("id-data", "Z" + row);
                            retZ.add(Float.valueOf(value));
                            Log.d("id-data", "Z " + String.valueOf(value));
                            //flagAxis = 1;
                            //Log.d("id-data", String.valueOf(flagAxis));

                        }

                        //ret.add(Float.valueOf(row));
                        //Log.d("id-data",row );




            }
   }
        }
        List[] retT={retX,retY,retZ};
        Log.e("i-data",String.valueOf(retY));

        return retT;
    }



//Convert list of feature vector to array
            private double[] featstoarray(List xlist,int nfeats)
            {
                //x is a list with the feature vector
                double[] x = new double[xlist.size()*nfeats];
                for(int i=0;i<xlist.size();i++)
                {
                    float[] temp = (float[]) xlist.get(i);
                    int ini = i*nfeats;
                    int t= 0;
                    for (int j = ini;j<ini+nfeats;j++)
                    {
                        x[j] = temp[t];
                        t = t+1;
                    }
                }
                return x;
            }







    /*private List[]  readFileIntoArrayAcc (File filepath) {
        String file = readtxt(filepath);
        int flagRow=0,flagAxis=0;
        List<Float> retX = new ArrayList<>();
        List<Float> retY = new ArrayList<>();
        List<Float> retZ = new ArrayList<>();
        //List retT = new ArrayList<>();
        String[] lines = file.split("\n");
        for (String line : lines) {
            if (!line.isEmpty()) {
                //ret.add(Float.valueOf(line));
                }
            flagRow=flagRow+1;
            Log.d("id-data",String.valueOf(flagRow));
            String[] values = line.split("\t");

            for (String value : values){//Log.d("id-data",value);

                String[] rows = value.split("\n");
                Log.d("id-data","-----");
                if(!value.isEmpty()) {
                    for (String row : rows) {
                        flagAxis = flagAxis + 1;
                        if (flagAxis == 1) {
                            Log.d("id-data", "X" + row);
                            retX.add(Float.valueOf(row));
                            ///flagAxis = flagAxis + 1;
                        }
                        if (flagAxis == 2) {
                            Log.d("id-data", "Y" + row);
                            //flagAxis = flagAxis + 1;
                            retY.add(Float.valueOf(row));
                        }
                        if (flagAxis == 3) {
                            Log.d("id-data", "Z" +
                                    "" + row);
                            retZ.add(Float.valueOf(row));
                            flagAxis = 0;
                        }

                        //ret.add(Float.valueOf(row));
                        //Log.d("id-data",row );





                    }
                }
            }*/
     /*   }
        List[] retT={retX,retY,retZ};

        return retT;
    }*/

    private double[] listtoarray(List vals)
    {
        double[] means = new double[vals.size()];
        for(int i=0;i<vals.size();i++)
        {
            float temp = (float) vals.get(i);
            means[i] =  temp;
        }
        return means;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }
}
