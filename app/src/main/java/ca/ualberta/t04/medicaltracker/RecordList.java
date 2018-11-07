package ca.ualberta.t04.medicaltracker;

import android.location.Location;
import android.media.Image;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RecordList
{
    private ArrayList<Record> records;
    private transient HashMap<String, Listener> listeners = new HashMap<>();

    public RecordList(){
        records = new ArrayList<>();
    }

    public void addRecord(Record record) {
        this.records.add(record);
        notifyAllListener();
    }

    public void removeRecord(Record record) {
        this.records.remove(record);
        notifyAllListener();
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public Record getRecord(int index){
        return records.get(index);
    }

    public void setTitle(Record record, String title) {
        if(records.contains(record)){
            record.setTitle(title);
        }
        notifyAllListener();
    }

    public void setDateStart(Record record, Date dateStart) {
        if(records.contains(record)){
            record.setDateStart(dateStart);
        }
        notifyAllListener();
    }

    public void setDescription(Record record, String description) {
        if(records.contains(record)){
            record.setDescription(description);
        }
        notifyAllListener();
    }

    public void addBodyLocationImage(Record record, Image bodyLocationImage) {
        if(records.contains(record)){
            record.addBodyLocationImage(bodyLocationImage);
        }
        notifyAllListener();
    }

    public void removeBodyLocationImage(Record record, Image bodyLocationImage) {
        if(records.contains(record)){
            record.removeBodyLocationImage(bodyLocationImage);
        }
        notifyAllListener();
    }

    public void setComments(Record record, HashMap<Doctor, ArrayList<String>> comments) {
        if(records.contains(record)){
            record.setComments(comments);
        }
        notifyAllListener();
    }

    public void setLocation(Record record, Location location) {
        if(records.contains(record)){
            record.setLocation(location);
        }
        notifyAllListener();
    }

    public void addNormalImages(Record record, Image normalImages) {
        if(records.contains(record)){
            record.addNormalImages(normalImages);
        }
        notifyAllListener();
    }

    public void removeNormalImages(Record record, Image normalImages) {
        if(records.contains(record)){
            record.removeNormalImages(normalImages);
        }
        notifyAllListener();
    }

    public void addListener(String key, Listener listener){
        if(listeners==null)
            listeners = new HashMap<>();
        if(!listeners.containsKey(key))
            listeners.put(key, listener);
    }

    public void notifyAllListener() {
        if(listeners==null)
            listeners = new HashMap<>();
        for (Listener listener:listeners.values()){
            listener.update();
        }
    }
}
