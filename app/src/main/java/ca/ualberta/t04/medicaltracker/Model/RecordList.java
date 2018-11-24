package ca.ualberta.t04.medicaltracker.Model;

import android.graphics.Bitmap;
import android.location.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ca.ualberta.t04.medicaltracker.Listener;

/**
 * This class contains all attributes and functionality for record list
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 04 1.0
 * @since 1.0
 */

public class RecordList
{
    private ArrayList<Record> records;
    private transient HashMap<String, Listener> listeners = new HashMap<>();

    public RecordList(){
        records = new ArrayList<>();
    }

    /**
     * Adds a record to the list
     * @param record Record
     */
    public void addRecord(Record record) {
        this.records.add(record);
        notifyAllListener();
    }

    /**
     * Removes a record from the list
     * @param record Record
     */
    public void removeRecord(Record record) {
        this.records.remove(record);
        notifyAllListener();
    }

    /**
     * Gets the record list
     * @return ArrayList<Record> records
     */
    public ArrayList<Record> getRecords() {
        return records;
    }

    /**
     * Gets a record from the list
     * @param index int
     * @return Record
     */
    public Record getRecord(int index){
        return records.get(index);
    }

    /**
     * Sets the title of a record
     * @param record Record
     * @param title String
     */
    public void setTitle(Record record, String title) {
        if(records.contains(record)){
            record.setTitle(title);
        }
        notifyAllListener();
    }

    /**
     * Sets the date of a record
     * @param record Record
     * @param dateStart Date
     */
    public void setDateStart(Record record, Date dateStart) {
        if(records.contains(record)){
            record.setDateStart(dateStart);
        }
        notifyAllListener();
    }

    /**
     * Sets the description of a record
     * @param record Record
     * @param description String
     */
    public void setDescription(Record record, String description) {
        if(records.contains(record)){
            record.setDescription(description);
        }
        notifyAllListener();
    }

    /**
     * Adds a bodylocation image
     * @param record Record
     * @param bodyLocationImage Image
     */
    public void addBodyLocationImage(Record record, Bitmap bodyLocationImage) {
        if(records.contains(record)){
            record.addImage(bodyLocationImage);
        }
        notifyAllListener();
    }

    /**
     * Removes a bodylocation image
     * @param record Record
     * @param bodyLocationImage Image
     */
    public void removeBodyLocationImage(Record record, Bitmap bodyLocationImage) {
        if(records.contains(record)){
            record.removeImage(bodyLocationImage);
        }
        notifyAllListener();
    }

    /**
     * Adds a comment to a record
     * @param record Record
     * @param doctor Doctor
     * @param comment String
     */
    public void addComment(Record record, Doctor doctor, String comment){
        record.addComment(doctor, comment);
        notifyAllListener();
    }

    /**
     * Sets the Comment of a record
     * @param record Record
     * @param comments HashMap
     */
    public void setComments(Record record, HashMap<String, ArrayList<String>> comments) {
        if(records.contains(record)){
            record.setComments(comments);
        }
        notifyAllListener();
    }

    /**
     * Sets the location of a record
     * @param record Record
     * @param location Location
     */
    public void setLocation(Record record, Location location) {
        if(records.contains(record)){
            record.setLocation(location);
        }
        notifyAllListener();
    }

    /**
     * Removes a listener
     * @param key String
     */
    public void addListener(String key, Listener listener){
        if(listeners==null)
            listeners = new HashMap<>();
        if(!listeners.containsKey(key))
            listeners.put(key, listener);
    }

    /**
     * notifies all the listeners
     */
    public void notifyAllListener() {
        if(listeners==null)
            listeners = new HashMap<>();
        for (Listener listener:listeners.values()){
            listener.update();
        }
    }
}
