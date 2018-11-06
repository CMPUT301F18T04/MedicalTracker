package ca.ualberta.t04.medicaltracker;

import org.junit.Test;

import java.util.Date;

import static junit.framework.TestCase.assertTrue;

public class RecordListUnitTest
{
    @Test
    public void recordListTest(){
        RecordList recordList = new RecordList();
        recordList.addListener(new Listener() {
            @Override
            public void update() {
                System.out.println("The message is sent by recordList listeners.");
            }
        });
        Record record = new Record("Test", new Date(), "Description", null, null);
        recordList.addRecord(record);
        assertTrue("The record that gets from recordList should equal to the original record", recordList.getRecord(0).equals(record));
    }
}
