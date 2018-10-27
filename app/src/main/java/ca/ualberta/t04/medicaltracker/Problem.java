package ca.ualberta.t04.medicaltracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Problem
{
    private ArrayList<Record> records;
    private String title;
    private String description;
    private Date time;
    private ArrayList<Listener> listeners;

    public Problem(String title, String description) {
        this.title = title;
        this.description = description;
        this.records = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        time = calendar.getTime();

        listeners = new ArrayList<>();
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
        listeners.add(listener);
    }

    public void notifyAllListener() {
        for (Listener listener:listeners){
            listener.update();
        }
    }
}
