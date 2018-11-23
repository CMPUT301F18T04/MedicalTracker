package ca.ualberta.t04.medicaltracker.Controller;

import android.widget.TextView;

import java.util.Date;

import ca.ualberta.t04.medicaltracker.Doctor;
import ca.ualberta.t04.medicaltracker.Patient;
import ca.ualberta.t04.medicaltracker.User;

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

    public static Patient getPatient(){
        return (Patient) user;
    }

    public static Doctor getDoctor(){
        return (Doctor) user;
    }

}
