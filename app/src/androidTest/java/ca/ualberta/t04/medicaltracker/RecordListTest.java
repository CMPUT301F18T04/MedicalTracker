package ca.ualberta.t04.medicaltracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.helpers.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Model.Doctor;
import ca.ualberta.t04.medicaltracker.Model.Record;
import ca.ualberta.t04.medicaltracker.Model.RecordList;
import ca.ualberta.t04.medicaltracker.Model.User;
import ca.ualberta.t04.medicaltracker.Util.CommonUtil;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RecordListTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ca.ualberta.t04.medicaltracker", appContext.getPackageName());
    }

    @Test
    public void recordListTest(){
        User user = new User("aaa", false);
        DataController.setUser(user);
        RecordList recordList = new RecordList();
        recordList.addListener("1", new Listener() {
            @Override
            public void update() {
                System.out.println("The message is sent by recordList listeners.");
            }
        });
        Record record = new Record("Test", new Date(), "Description", null, null, null, null);
        recordList.addRecord(record);
        assertTrue("The record that gets from recordList should equal to the original record", recordList.getRecord(0).equals(record));

        recordList.setProblemId("aaa");
        assertEquals("aaa", recordList.getProblemId());

        HashMap<String, ArrayList<String>> comments = new HashMap<>();
        recordList.setComments(record, comments);
        assertEquals(record.getComments(), comments);

        Date date = new Date();
        recordList.setDateStart(record, date);
        assertEquals(date, record.getDateStart());

        recordList.setDescription(record, "aa");
        assertEquals(record.getDescription(), "aa");

        Location location = new Location("");
        recordList.setLocation(record, location);
        assertEquals(record.getLocation(), location);

        recordList.setTitle(record, "bb");
        assertEquals(record.getTitle(), "bb");

        Doctor doctor = new Doctor("aa");
        recordList.addComment(record, doctor, "haha");
        assertEquals(record.getComments().get(doctor.getUserName()).get(0), "haha");

        Record offlineRecord = new Record("OfflineTest", new Date(), "Description", null, null, null, null);
        recordList.addOfflineRecord(offlineRecord);
        assertEquals(recordList.getOfflineRecords().get(0), offlineRecord);

        recordList.removeRecord(record);
        assertEquals(recordList.getRecords().size(), 0);
    }
}
