package ca.ualberta.t04.medicaltracker;

import org.junit.Test;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.Model.RecordList;
import ca.ualberta.t04.medicaltracker.Model.User;

import static org.junit.Assert.assertEquals;

public class DataControllerUnitTest {
    //This method is used to test DataController class
    @Test
    public void dataController_test(){

        User user = new User("user1", true);
        DataController.setUser(user);
        assertEquals(DataController.getUser(), user);

        Problem problem = new Problem("aaa", null, "ccc");
        DataController.addRecordList(problem.getProblemId(), problem.getRecordList());

        assertEquals(DataController.getRecordList().get(problem.getProblemId()), problem.getRecordList());
    }
}
