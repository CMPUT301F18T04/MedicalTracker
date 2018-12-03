package ca.ualberta.t04.medicaltracker;

import android.graphics.Bitmap;
import android.location.Location;
import android.media.Image;

import org.junit.Test;
import java.util.*;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Model.Doctor;
import ca.ualberta.t04.medicaltracker.Model.Record;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class RecordUnitTest {

    @Test
    public void record_test(){

        //Constructor test
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        Location location = null;
        HashMap<Bitmap, String> bodyImage = new HashMap<>();
        BodyLocation bodyLocation = null;
        Record record = new Record("RecordTest", date, "This is Unit Test For Record",bodyImage,null, location, bodyLocation);

        assertTrue("Title should be 'RecordTest'", record.getTitle().equals("RecordTest"));
        assertTrue("Description should be 'This is Unit Test For Record'", record.getDescription().equals("This is Unit Test For Record"));
        assertEquals(record.getDateStart(),date);
        assertEquals(record.getPhotos(), new ArrayList<Bitmap>(bodyImage.keySet()));
        assertEquals(record.getLocation(), location);

        //setLocation and getLocation test
        Location testLocation = null;
        record.setLocation(testLocation);
        assertEquals(testLocation,record.getLocation());


        //setComments and getComments test
        ArrayList<String> comments = new ArrayList<>();
        comments.add("Test1");
        comments.add("Test2");
        Doctor doctor = new Doctor("Doctor1");
        HashMap<String, ArrayList<String>> doctorComments = new HashMap<>();
        doctorComments.put(doctor.getUserName(),comments);
        record.setComments(doctorComments);
        assertEquals(record.getComments(),doctorComments);

        record.setRecordId("aa");
        assertEquals("aa", record.getRecordId());

        record.setProblemId("bb");
        assertEquals("bb", record.getProblemId());

        record.setBodyLocation(BodyLocation.RightLeg);
        assertEquals(BodyLocation.RightLeg, record.getBodyLocation());

        Date date1 = new Date();
        record.setDateStart(date1);
        assertEquals(date1, record.getDateStart());

        record.setDescription("cc");
        assertEquals("cc", record.getDescription());

        Location location1 = new Location("");
        record.setLocation(location1);
        assertEquals(location1, record.getLocation());

        record.setTitle("dd");
        assertEquals("dd", record.getTitle());

        record.addComment(doctor, "cccc");
        assertEquals("cccc", record.getComments().get(doctor.getUserName()).get(2));
    }

}
