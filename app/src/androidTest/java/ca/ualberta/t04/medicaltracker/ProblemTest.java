package ca.ualberta.t04.medicaltracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.Model.ProblemList;
import ca.ualberta.t04.medicaltracker.Model.Record;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ProblemTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ca.ualberta.t04.medicaltracker", appContext.getPackageName());
    }

    @Test
    public void problem_test() {

        //Constructor test
        Problem problem = new Problem("Headache", new Date(), "My head is not comfortable");
        org.junit.Assert.assertTrue("Title should be 'Headache'", problem.getTitle().equals("Headache"));
        org.junit.Assert.assertTrue("Title should be 'My head is not comfortable'", problem.getDescription().equals("My head is not comfortable"));

        problem.setTitle("New title for problem test");
        assertEquals("New title for problem test",problem.getTitle());

        problem.setDescription("New description for problem test");
        assertNotEquals("New description for proble test",problem.getDescription());

        //addRecord,getRecord and remove Record test
        Calendar calendar = Calendar.getInstance();

        //setTime and getTime test
        Date time = calendar.getTime();
        problem.setTime(time);

        assertEquals(time,problem.getTime());

        problem.setProblemId("aaa");
        assertEquals("aaa", problem.getProblemId());

    }
}
