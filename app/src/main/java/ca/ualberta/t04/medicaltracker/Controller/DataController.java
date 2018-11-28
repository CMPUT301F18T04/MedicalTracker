package ca.ualberta.t04.medicaltracker.Controller;

import java.util.Date;

import ca.ualberta.t04.medicaltracker.Model.Doctor;
import ca.ualberta.t04.medicaltracker.Model.Patient;
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

}
