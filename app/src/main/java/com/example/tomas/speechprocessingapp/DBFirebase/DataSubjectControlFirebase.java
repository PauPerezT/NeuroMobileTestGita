package com.example.tomas.speechprocessingapp.DBFirebase;

/**
 * Created by paulaperez on 29/05/18.
 */

public class DataSubjectControlFirebase {

    ///Variables

    private String id; //Assigned by Firebase
    private String completeName;
    private String age;
    private String idNumber; //Id number (CC/TI)

    private String dominantHand;
    private String gender;
    private String smoker; //Yes or not
    private String scholarity;


    public  DataSubjectControlFirebase(String id, String completeName, String age, String idNumber,
                                       String dominantHand, String gender, String smoker, String scholarity){
        this.id= id;
        this.completeName=completeName;
        this.age=age;
        this.idNumber=idNumber;

        this.dominantHand=dominantHand;
        this.gender=gender;
        this.smoker=smoker;
        this.scholarity=scholarity;

    }

    public String getId(){
        return id;
    }

    public void setId(){
        this.id=id;
    }

    public String getCompleteName(){
        return completeName;
    }

    public void setCompleteName(){
        this.completeName=completeName;
    }

    public String getAge(){
        return age;
    }

    public void setAge(){
        this.age=age;
    }

    public String getIdNumber(){
        return idNumber;
    }

    public void setIdNumber(){
        this.idNumber=idNumber;
    }



    public String getDominantHand(){
        return dominantHand;
    }

    public void setDominantHand(){
        this.dominantHand=dominantHand;
    }

    public String getGender(){
        return gender;
    }

    public void setGender(){
        this.gender=gender;
    }

    public String getSmoker(){
        return smoker;
    }

    public void setSmoker(){
        this.smoker=smoker;
    }

    public String getScholarity(){
        return scholarity;
    }

    public void setScolarity(){
        this.scholarity=scholarity;
    }



}
