package ca.ualberta.t04.medicaltracker;

import android.location.Location;
import android.media.Image;

import org.junit.Test;
import java.util.*;

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
        ArrayList<Image> bodyImage = new ArrayList<>();
        Record record = new Record("RecordTest", date, "This is Unit Test For Record",bodyImage,location);

        assertTrue("Title should be 'RecordTest'", record.getTitle().equals("RecordTest"));
        assertTrue("Description should be 'This is Unit Test For Record'", record.getDescription().equals("This is Unit Test For Record"));
        assertEquals(record.getDateStart(),date);
        assertEquals(record.getPhotos(),bodyImage);
        assertEquals(record.getLocation(),location);

        //setLocation and getLocation test
        Location testLocation = null;
        record.setLocation(testLocation);
        assertEquals(testLocation,record.getLocation());


        //setComments and getComments test
        ArrayList<String> comments = new ArrayList<>();
        comments.add("Test1");
        comments.add("Test2");
        Doctor doctor = new Doctor("Doctor1","12345678");
        HashMap<String, ArrayList<String>> doctorComments = new HashMap<>();
        doctorComments.put(doctor.getUserName(),comments);
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

        //addBodyImage and removeBodyImage test
        Image testImage1 = null;
        Image testImage2 = null;

        bodyImage.add(testImage1);
        bodyImage.add(testImage2);

        record.addImage(testImage1);
        record.addImage(testImage2);

        assertEquals(bodyImage,record.getPhotos());

        record.removeImage(testImage1);
        assertEquals(bodyImage,record.getPhotos());
    }

}
