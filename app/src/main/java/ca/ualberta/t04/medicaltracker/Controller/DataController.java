package ca.ualberta.t04.medicaltracker.Controller;

import java.util.Date;
import java.util.HashMap;

import ca.ualberta.t04.medicaltracker.Model.Doctor;
import ca.ualberta.t04.medicaltracker.Model.Patient;
import ca.ualberta.t04.medicaltracker.Model.RecordList;
import ca.ualberta.t04.medicaltracker.Model.User;

/**
 * This class is the central controller for all the data of the entire application
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */

public class DataController
{
    private static User user;
    private static HashMap<String, RecordList> recordList;

    /**
     * Gets the user
     * @return user User
     */
    public static User getUser() {
        return user;
    }

    /**
     * Sets the user
     * @param user
     */
    public static void setUser(User user) {
        DataController.user = user;
    }


    /**
     * Only used to update a user's profile
     * @param nickname String
     * @param birthday Date
     * @param isMale Boolean
     * @param phoneNumber String
     * @param email String
     * @param address String
     */
    public static void updateProfile(String nickname,Date birthday, Boolean isMale, String phoneNumber, String email, String address){
        getUser().setName(nickname);
        getUser().setBirthday(birthday);
        getUser().setMale(isMale);
        getUser().setPhoneNumber(phoneNumber);
        getUser().setEmail(email);
        getUser().setAddress(address);
        getUser().notifyAllListeners();
    }

    /**
     * Update user's language preference
     * @param lang String
     * @param district String
     */
    public static void updateLanguage(String lang, String district){
        getUser().setLanguage(lang);
        getUser().setDistrict(district);
        getUser().notifyAllListeners();
    }

    /**
     * gets the patient
     * @return user patient
     */
    public static Patient getPatient(){
        return (Patient) user;
    }

    /**
     * gets the doctor
     * @return user Doctor
     */
    public static Doctor getDoctor(){
        return (Doctor) user;
    }

    /**
     * gets the record list
     * @return HashMap
     */
    public static HashMap<String, RecordList> getRecordList() {
        if(recordList==null)
            recordList = new HashMap<>();
        return recordList;
    }

    /**
     * adds the record list to a problem
     * @param problemId String
     * @param recordList RecordList
     */
    public static void addRecordList(String problemId, RecordList recordList) {
        if(!getRecordList().containsKey(problemId))
            getRecordList().put(problemId, recordList);
    }

    /**
     * updates the record lsit
     * @param problemId String
     * @param recordList RecordList
     */
    public static void updateRecordList(String problemId, RecordList recordList){
        getRecordList().put(problemId, recordList);
    }

    /**
     * Clears the record list of a problem
     */
    public static void clearRecordList(){
        getRecordList().clear();
    }
}
