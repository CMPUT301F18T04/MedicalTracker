package ca.ualberta.t04.medicaltracker;

import java.util.Date;

public class DataController
{
    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        DataController.user = user;
    }

    public static void updateProfile(Date birthday, Boolean isMale, String phoneNumber, String email, String address){
        getUser().setBirthday(birthday);
        getUser().setMale(isMale);
        getUser().setPhoneNumber(phoneNumber);
        getUser().setEmail(email);
        getUser().setAddress(address);
        getUser().notifyAllListeners();
    }
}
