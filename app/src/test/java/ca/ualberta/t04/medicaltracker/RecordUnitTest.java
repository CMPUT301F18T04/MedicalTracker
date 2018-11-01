package ca.ualberta.t04.medicaltracker;

import android.location.Location;
import android.media.Image;

import org.junit.Test;
import java.util.*;

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
        Location location = new Location("dummyprovider");
        ArrayList<Image> bodyImage = new ArrayList<>();
        Record record = new Record("RecordTest", date, "This is Unit Test For Record",bodyImage,location);

        assertTrue("Title should be 'RecordTest'", record.getTitle().equals("RecordTest"));
        assertTrue("Description should be 'This is Unit Test For Record'", record.getDescription().equals("This is Unit Test For Record"));
        assertEquals(record.getDateStart(),date);
        assertEquals(record.getBodyLocationImage(),bodyImage);
        assertEquals(record.getLocation(),location);

        //setComments and getComments test
        ArrayList<String> comments = new ArrayList<>();
        comments.add("Test1");
        comments.add("Test2");
        Doctor doctor = new Doctor("Doctor1","12345678");
        HashMap<Doctor, ArrayList<String>> doctorComments = new HashMap<>();
        doctorComments.put(doctor,comments);
        record.setComments(doctorComments);
        assertEquals(record.getComments(),doctorComments);

        //addNormalImages and removeNormalImages test
        Image image1 = null;
        Image image2 = null;
        Image image3 = null;

        record.addNormalImages(image1);
        record.addNormalImages(image2);
        record.removeNormalImages(image1);

        ArrayList<Image> testImages= new ArrayList<>();
        testImages.add(image1);
        testImages.add(image3);
        assertNotEquals(testImages,record.getNormalImages());
    }

}
