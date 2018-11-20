package ca.ualberta.t04.medicaltracker;

import android.location.Location;
import android.media.Image;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * This class contains all attributes and functionality for a certain record
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 04 1.0
 * @since 1.0
 */

public class Record
{
    private String title;
    private Date dateStart;
    private String description;

    // We are not recommend to store images by using ElasticSearch. That's why there's a tag transient.
    private transient ArrayList<Image> normalImages;
    private transient ArrayList<Image> bodyLocationImage;


    private HashMap<String, ArrayList<String>> comments;
    private Location location;
    private transient ArrayList<Listener> listeners = new ArrayList<>();

    public Record(String title, Date dateStart, String description, ArrayList<Image> bodyLocationImage, Location location)
    {
        this.title = title;
        this.dateStart = dateStart;
        this.description = description;
        this.bodyLocationImage = bodyLocationImage;
        this.location = location;

        comments = new HashMap<>();
        normalImages = new ArrayList<>();
    }

    /**
     * Gets the title of a record
     * @return String title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of a record
     * @param title String
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the date of a record
     * @return Date dateStart
     */
    public Date getDateStart() {
        return dateStart;
    }

    /**
     * Sets the date of a record
     * @param dateStart Date
     */
    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    /**
     * Gets the description of a record
     * @return String description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of a record
     * @param description String
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the body location image of a record
     * @return ArrayList<Image> bodyLocationImage
     */
    public ArrayList<Image> getBodyLocationImage() {
        return bodyLocationImage;
    }

    /**
     * Adds a body location image of a record
     * @param bodyLocationImage Image
     */
    public void addBodyLocationImage(Image bodyLocationImage) {
        this.bodyLocationImage.add(bodyLocationImage);
    }

    /**
     * removes a body location image of a record
     * @param bodyLocationImage Image
     */
    public void removeBodyLocationImage(Image bodyLocationImage) {
        this.bodyLocationImage.remove(bodyLocationImage);
    }

    /**
     * Gets the doctor's comments of a record
     * @return HashMap comments
     */
    public HashMap<String, ArrayList<String>> getComments() {
        if(comments == null){
            comments = new HashMap<>();
        }
        return comments;
    }

    /**
     * Adds doctors' comments to a record
     * @param doctor Doctor
     * @param comment String
     */
    public void addComment(Doctor doctor, String comment){
        if(getComments().containsKey(doctor.getUserName())){
            getComments().get(doctor.getUserName()).add(comment);
        }
        else{
            ArrayList<String> newComments = new ArrayList<>();
            newComments.add(comment);
            getComments().put(doctor.getUserName(), newComments);
        }
    }

    /**
     * Sets the comments of a record
     * @param comments HashMap
     */
    public void setComments(HashMap<String, ArrayList<String>> comments) {
        this.comments = comments;
    }

    /**
     * Gets the location of a record
     * @return Location location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the location of a record
     * @param location Location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Gets the list of camera photo of a record
     * @return ArrayList<Image> normalImage
     */
    public ArrayList<Image> getNormalImages() {
        return normalImages;
    }

    /**
     * Adds a camera photo to a record
     * @param normalImages Image
     */
    public void addNormalImages(Image normalImages) {
        this.normalImages.add(normalImages);
    }

    /**
     * Removes a camera photo of a record
     * @param normalImages Image
     */
    public void removeNormalImages(Image normalImages) {
        this.normalImages.remove(normalImages);
    }

    /**
     * Adds a listener
     * @param listener Listener
     */
    public void addListener(Listener listener){
        if(listeners==null)
            listeners = new ArrayList<>();
        listeners.add(listener);
    }

    /**
     * Notifies all the listeners
     */
    public void notifyAllListener() {
        if(listeners==null)
            listeners = new ArrayList<>();
        for (Listener listener:listeners){
            listener.update();
        }
    }
}
