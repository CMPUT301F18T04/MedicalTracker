package ca.ualberta.t04.medicaltracker;

import android.location.Location;
import android.media.Image;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Record
{
    private String title;
    private Date dateStart;
    private String description;

    // We are not recommend to store images by using ElasticSearch. That's why there's a tag transient.
    private transient ArrayList<Image> normalImages;
    private transient ArrayList<Image> bodyLocationImage;


    private HashMap<Doctor, ArrayList<String>> comments;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Image> getBodyLocationImage() {
        return bodyLocationImage;
    }

    public void addBodyLocationImage(Image bodyLocationImage) {
        this.bodyLocationImage.add(bodyLocationImage);
    }

    public void removeBodyLocationImage(Image bodyLocationImage) {
        this.bodyLocationImage.remove(bodyLocationImage);
    }

    public HashMap<Doctor, ArrayList<String>> getComments() {
        if(comments == null){
            comments = new HashMap<>();
        }
        return comments;
    }

    public void addComment(Doctor doctor, String comment){
        if(getComments().containsKey(doctor)){
            getComments().get(doctor).add(comment);
        }
        else{
            ArrayList<String> newComments = new ArrayList<>();
            newComments.add(comment);
            getComments().put(doctor, newComments);
        }
    }

    public void setComments(HashMap<Doctor, ArrayList<String>> comments) {
        this.comments = comments;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArrayList<Image> getNormalImages() {
        return normalImages;
    }

    public void addNormalImages(Image normalImages) {
        this.normalImages.add(normalImages);
    }

    public void removeNormalImages(Image normalImages) {
        this.normalImages.remove(normalImages);
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
