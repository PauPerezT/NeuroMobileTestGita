package com.example.tomas.speechprocessingapp.DBFirebase;
/**
 * Created by PAULA on 12/05/2018.
 */
public class DataSubjectFirebase {

    ///Variables

    private String id; //Assigned by Firebase
    private String completeName;
    private String age;
    private String idNumber; //Id number (CC/TI)
    private String timeDiagnosis;
    private String dominantHand;
    private String gender;
    private String smoker; //Yes or not
    private String scholarity;
    private String medicines; // it contains dose and time too

    public  DataSubjectFirebase(String id, String completeName, String age, String idNumber, String timeDiagnosis, String dominantHand,
                                String gender, String smoker, String scholarity, String medicines){
        this.id= id;
        this.completeName=completeName;
        this.age=age;
        this.idNumber=idNumber;
        this.timeDiagnosis=timeDiagnosis;
        this.dominantHand=dominantHand;
        this.gender=gender;
        this.smoker=smoker;
        this.scholarity=scholarity;
        this.medicines=medicines;
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

    public String getTimeDiagnosis(){
        return timeDiagnosis;
    }

    public void setTimeDiagnosis(){
        this.timeDiagnosis=timeDiagnosis;
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

    public String getMedicines(){
        return medicines;
    }

    public void setMedicines(){
        this.medicines=medicines;
    }

}
