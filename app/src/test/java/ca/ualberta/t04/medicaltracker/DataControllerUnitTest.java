package ca.ualberta.t04.medicaltracker;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DataControllerUnitTest {
    //This method is used to test DataController class
    @Test
    public void dataController_test(){

        User user = new User("user1", "123", true);
        DataController.setUser(user);
        assertEquals(DataController.getUser(), user);

    }
}
