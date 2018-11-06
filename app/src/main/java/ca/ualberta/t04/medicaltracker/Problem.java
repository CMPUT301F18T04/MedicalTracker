package ca.ualberta.t04.medicaltracker;

import java.util.Date;

public class Problem
{
    private RecordList recordList = null;
    private String title;
    private String description;
    private Date time;

    public Problem(String title, Date dateStart, String description) {
        this.title = title;
        this.description = description;
        if(recordList == null)
            recordList = new RecordList();

        time = dateStart;
    }

    public RecordList getRecordList() {
        return recordList;
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


}
