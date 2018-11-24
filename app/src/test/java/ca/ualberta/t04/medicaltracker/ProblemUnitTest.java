package ca.ualberta.t04.medicaltracker;

import android.location.Location;
import android.media.Image;

import org.junit.Test;
import java.util.*;

import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.Model.Record;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ProblemUnitTest {

    @Test
    public void problem_test() {

        //Constructor test
        Problem problem = new Problem("Headache", new Date(), "My head is not comfortable");
        assertTrue("Title should be 'Headache'", problem.getTitle().equals("Headache"));
        assertTrue("Title should be 'My head is not comfortable'", problem.getDescription().equals("My head is not comfortable"));

        problem.setTitle("New title for problem test");
        assertEquals("New title for problem test",problem.getTitle());

        problem.setDescription("New description for problem test");
        assertNotEquals("New description for proble test",problem.getDescription());

        //addRecord,getRecord and remove Record test
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        Location location = null;
        ArrayList<Image> bodyImage = new ArrayList<>();
        Record record = new Record("ProblemTest", date, "This is Unit Test For Problem",bodyImage,location);
        Record record1 = new Record("ProblemTest1", date, "This is Unit Test For Problem1",bodyImage,location);

        problem.getRecordList().addRecord(record);
        problem.getRecordList().addRecord(record1);

        assertEquals(record,problem.getRecordList().getRecords().get(0));
        problem.getRecordList().removeRecord(record);
        assertNotEquals(record,problem.getRecordList().getRecords().get(0));

        //setTime and getTime test
        Date time = calendar.getTime();
        problem.setTime(time);

        assertEquals(time,problem.getTime());

    }

}