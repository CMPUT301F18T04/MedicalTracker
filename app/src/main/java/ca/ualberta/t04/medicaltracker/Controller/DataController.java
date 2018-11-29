package ca.ualberta.t04.medicaltracker.Controller;

import java.util.Date;
import java.util.HashMap;

import ca.ualberta.t04.medicaltracker.Model.Doctor;
import ca.ualberta.t04.medicaltracker.Model.Patient;
import ca.ualberta.t04.medicaltracker.Model.RecordList;
import ca.ualberta.t04.medicaltracker.Model.User;

public class DataController
{
    private static User user;
    private static HashMap<String, RecordList> recordList;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        DataController.user = user;
    }

    // Only used to update a user's profile
    public static void updateProfile(String nickname,Date birthday, Boolean isMale, String phoneNumber, String email, String address){
        getUser().setName(nickname);
        getUser().setBirthday(birthday);
        getUser().setMale(isMale);
        getUser().setPhoneNumber(phoneNumber);
        getUser().setEmail(email);
        getUser().setAddress(address);
        getUser().notifyAllListeners();
    }

    // Update user's language preference
    public static void updateLanguage(String lang, String district){
        getUser().setLanguage(lang);
        getUser().setDistrict(district);
        getUser().notifyAllListeners();
    }

    public static Patient getPatient(){
        return (Patient) user;
    }

    public static Doctor getDoctor(){
        return (Doctor) user;
    }

    public static HashMap<String, RecordList> getRecordList() {
        if(recordList==null)
            recordList = new HashMap<>();
        return recordList;
    }

    public static void addRecordList(String problemId, RecordList recordList) {
        if(!getRecordList().containsKey(problemId))
            getRecordList().put(problemId, recordList);
    }

    public static void updateRecordList(String problemId, RecordList recordList){
        getRecordList().put(problemId, recordList);
    }

    public static void clearRecordList(){
        getRecordList().clear();
    }
}
