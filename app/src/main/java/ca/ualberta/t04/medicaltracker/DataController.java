package ca.ualberta.t04.medicaltracker;

public class DataController
{
    private static User user = null;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        DataController.user = user;
    }
}
