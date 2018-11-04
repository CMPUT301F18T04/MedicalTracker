package ca.ualberta.t04.medicaltracker;

import java.util.ArrayList;
import java.util.Date;

public class Problem
{
    private ArrayList<Record> records;
    private String title;
    private String description;
    private Date time;
    private transient ArrayList<Listener> listeners = new ArrayList<>();

    public Problem(String title, Date dateStart, String description) {
        this.title = title;
        this.description = description;
        this.records = new ArrayList<>();

        time = dateStart;

    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void addRecord(Record record) {
        this.records.add(record);
    }

    public void removeRecord(Record record) {
        this.records.remove(record);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void addListener(Listener listener){
        if(listeners==null)
            listeners = new ArrayList<>();
        listeners.add(listener);
    }

    public void notifyAllListener() {
        if(listeners==null)
            listeners = new ArrayList<>();
        for (Listener listener:listeners){
            listener.update();
        }
    }
}
