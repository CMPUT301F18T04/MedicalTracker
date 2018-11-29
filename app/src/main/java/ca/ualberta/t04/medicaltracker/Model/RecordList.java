package ca.ualberta.t04.medicaltracker.Model;

import android.graphics.Bitmap;
import android.location.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Listener;
import ca.ualberta.t04.medicaltracker.Util.NetworkUtil;

/**
 * This class contains all attributes and functionality for record list
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 04 1.0
 * @since 1.0
 */

public class RecordList
{
    private int currentId = 1;
    private String problemId;
    private ArrayList<String> recordIds;
    private transient ArrayList<Record> records;
    private transient HashMap<String, Listener> listeners = new HashMap<>();
    private transient ArrayList<Record> offlineRecords;

    public RecordList(){
        records = new ArrayList<>();
        recordIds = new ArrayList<>();
    }

    /**
     * Adds a record to the list
     * @param record Record
     */
    public void addRecord(Record record) {
        String recordId = DataController.getUser().getUserName() + problemId + String.valueOf(currentId);
        record.setRecordId(recordId);
        record.setProblemId(problemId);
        this.records.add(record);
        addRecordId(recordId);
        currentId += 1;
        ElasticSearchController.createRecord(record);
        notifyAllListener();
    }

    /**
     * Removes a record from the list
     * @param record Record
     */
    public void removeRecord(Record record) {
        this.records.remove(record);
        String recordId = record.getRecordId();
        removeRecordId(recordId);
        ElasticSearchController.deleteRecord(recordId);
        notifyAllListener();
    }

    /**
     * Gets the record list
     * @return ArrayList<Record> records
     */
    public ArrayList<Record> getRecords() {
        if(records==null || records.size()==0){
            records = ElasticSearchController.searchRecordList(recordIds);
        }
        return records;
    }

    public void updateRecords(int index, String recordId){
        records = ElasticSearchController.searchRecordList(recordIds);
    }

    /**
     * Gets a record from the list
     * @param index int
     * @return Record
     */
    public Record getRecord(int index){
        return getRecords().get(index);
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
     * Adds a photo
     * @param record Record
     * @param photo Bitmap
     * @param path String
     */
    public void addPhoto(Record record, Bitmap photo, String path) {
        if(records.contains(record)){
            record.addImage(photo, path);
        }
        notifyAllListener();
    }

    /**
     * Removes a photo
     * @param record Record
     * @param index int
     */
    public void removePhoto(Record record, int index) {
        if(records.contains(record)){
            record.removeImage(index);
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
        ElasticSearchController.updateRecord(record);
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

    public void replaceListener(String key, Listener listener){
        if(listeners==null)
            listeners = new HashMap<>();
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

    public ArrayList<String> getRecordIds() {
        if(recordIds == null){
            return new ArrayList<>();
        }
        return recordIds;
    }

    private void addRecordId(String recordId) {
        getRecordIds().add(recordId);
    }

    private void removeRecordId(String recordId){
        if(getRecordIds().contains(recordId)){
            getRecordIds().remove(recordId);
        }
    }

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public ArrayList<Record> getOfflineRecords() {
        if(offlineRecords==null)
            offlineRecords = new ArrayList<>();
        return offlineRecords;
    }

    public void addOfflineRecord(Record record) {
        if(offlineRecords==null)
            offlineRecords = new ArrayList<>();
        offlineRecords.add(record);
    }

    public void updateComment(Record record){
        record.updateComment();
    }
}
