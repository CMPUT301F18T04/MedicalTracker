package ca.ualberta.t04.medicaltracker;

import android.content.Context;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Model.Doctor;
import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.Model.ProblemList;
import ca.ualberta.t04.medicaltracker.Model.Record;
import ca.ualberta.t04.medicaltracker.Model.RecordList;
import ca.ualberta.t04.medicaltracker.Model.User;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ProblemListTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ca.ualberta.t04.medicaltracker", appContext.getPackageName());
    }

    @Test
    public void problemListTest(){
        ProblemList problemList = new ProblemList();
        problemList.addListener("1", new Listener() {
            @Override
            public void update() {
                System.out.println("The message is sent by listeners.");
            }
        });
        Problem problem = new Problem("Test", new Date(), "Description");
        problemList.addProblem(problem);
        assertTrue("The problem that gets from problemList should equal to the original problem", problemList.getProblem(0).equals(problem));

        Record record = new Record("a", null, "c", null, null, null, BodyLocation.RightLeg);
        problemList.addRecord(problem, record);
        Date date = new Date();
        problemList.setDateStart(problem, date);
        problemList.setDescription(problem, "ccc");
        problemList.setTitle(problem, "ttt");

        Problem problem1 = problemList.getProblem(0);
        Assert.assertEquals(problem, problem1);
    }
}
