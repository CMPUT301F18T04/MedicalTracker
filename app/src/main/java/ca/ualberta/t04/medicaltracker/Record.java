package ca.ualberta.t04.medicaltracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.Image;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
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
    private ArrayList<String> images;

    private HashMap<String, ArrayList<String>> comments;
    private Location location;

    public Record(String title, Date dateStart, String description, ArrayList<Bitmap> images, Location location)
    {
        this.title = title;
        this.dateStart = dateStart;
        this.description = description;
        this.images = new ArrayList<>();
        for(Bitmap bitmap:images){
            String string = ImageUtil.convertBitmapToString(bitmap);
            if(string.length()<65536){
                this.images.add(string);
            } else {
                Log.d("Succeed", "Too long" + String.valueOf(string.length()));
            }
        }
        this.location = location;

        comments = new HashMap<>();
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
    public ArrayList<Bitmap> getImages() {
        if(images==null){
            images = new ArrayList<>();
        }
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for(String str:images){
            bitmaps.add(ImageUtil.convertStringToBitmap(str));
        }
        return bitmaps;
    }



    /**
     * Adds a body location image of a record
     * @param image Image
     */
    public void addImage(Bitmap image) {
        if(this.images==null){
            this.images = new ArrayList<>();
        }
        String string = ImageUtil.convertBitmapToString(image);
        if(string.length()<65536){
            this.images.add(string);
        } else {
            Log.d("Succeed", "Too long");
        }
    }

    /**
     * removes a body location image of a record
     * @param image Image
     */
    public void removeImage(Bitmap image) {
        if(this.images==null){
            this.images = new ArrayList<>();
        }
        String string = ImageUtil.convertBitmapToString(image);
        if(this.images.contains(string)){
            this.images.remove(string);
        }
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
}
