package ca.ualberta.t04.medicaltracker.Model;

import java.util.Date;

/**
 * This class contains all attributes and functionality for a certain problem
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 04 1.0
 * @since 1.0
 */

public class Problem
{
    private String problemId;
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

    /**
     * Gets the corresponding record list of the problem
     * @return RecordList recordList
     */
    public RecordList getRecordList() {
        return recordList;
    }

    /**
     * Gets the title of the problem
     * @return String title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the problem
     * @param title String
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description of the problem
     * @return String description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the problem
     * @param description String
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the time of a the problem
     * @return Date time
     */
    public Date getTime() {
        return time;
    }

    /**
     * Sets the time of the problem
     * @param time Date
     */
    public void setTime(Date time) {
        this.time = time;
    }


    /**
     * Get Problem Id
     * @return problemId String
     */
    public String getProblemId() {
        return problemId;
    }

    /**
     * Set Problem Id
     * @param problemId String
     */
    public void setProblemId(String problemId) {
        this.problemId = problemId;
        recordList.setProblemId(problemId);
    }
}
