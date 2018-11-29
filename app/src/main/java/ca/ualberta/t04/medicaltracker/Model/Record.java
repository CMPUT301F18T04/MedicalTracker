package ca.ualberta.t04.medicaltracker.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ca.ualberta.t04.medicaltracker.BodyLocation;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Photo;
import ca.ualberta.t04.medicaltracker.Util.ImageUtil;

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
    private String recordId;
    private String problemId;

    private ArrayList<Photo> photos;
    private transient ArrayList<Bitmap> bitmaps;

    private HashMap<String, ArrayList<String>> comments;
    private Location location;
    private BodyLocation bodyLocation;

    public Record(String title, Date dateStart, String description, HashMap<Bitmap, String> bitmaps, Location location, BodyLocation bodyLocation)
    {
        this.title = title;
        this.dateStart = dateStart;
        this.description = description;
        this.photos = new ArrayList<>();
        if(bitmaps!=null){
            for(Bitmap bitmap:bitmaps.keySet()){
                String string = ImageUtil.convertBitmapToString(bitmap);
                if(string.length()<65536){
                    String path = bitmaps.get(bitmap);
                    Photo photo = new Photo(string, path);
                    this.photos.add(photo);
                } else {
                    Log.d("Succeed", "Too long" + String.valueOf(string.length()));
                }
            }
        }

        this.location = location;
        this.bodyLocation = bodyLocation;

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
    public ArrayList<Bitmap> getPhotos() {
        if(photos==null){
            photos = new ArrayList<>();
        }

        if(bitmaps!=null){
            return bitmaps;
        }

        Boolean update = false;
        bitmaps = new ArrayList<>();
        for(Photo photo:photos){
            String path = photo.getPath();
            Bitmap bitmap = null;
            if(path!=null){
                 bitmap = BitmapFactory.decodeFile(path);
            }
            if(bitmap==null){
                bitmap = ImageUtil.convertStringToBitmap(photo.getBase64Bitmap());
                String fileName = System.currentTimeMillis() + ".jpg";
                Boolean succeed = ImageUtil.saveImage(bitmap, fileName);
                Log.d("Succeed", String.valueOf(succeed));
                if(succeed){
                    String localPath = ImageUtil.PHOTO_DIRECTORY + fileName;
                    photo.setPath(localPath);
                    update = true;
                }
            }
            bitmaps.add(bitmap);
        }
        if(update){
            ElasticSearchController.updateRecord(this);
        }
        return bitmaps;
    }

    /**
     * Adds a body location image of a record
     * @param image Image
     * @param path String
     */

    public void addImage(Bitmap image, String path) {
        if(this.photos==null){
            this.photos = new ArrayList<>();
        }
        String string = ImageUtil.convertBitmapToString(image);
        if(string.length()<65536){
            Photo photo = new Photo(string, path);
            this.photos.add(photo);
            this.bitmaps.add(image);
        } else {
            Log.d("Succeed", "Too long");
        }
    }


    /**
     * removes a body location image of a record
     * @param index int
     */
    public void removeImage(int index) {
        if(this.photos==null){
            this.photos = new ArrayList<>();
        }
        if(photos.size()>index){
            Photo photo = photos.get(index);
            photos.remove(index);
            bitmaps.remove(ImageUtil.convertStringToBitmap(photo.getBase64Bitmap()));
        }
    }

    public void removeImage(Bitmap bitmap){
        if(this.photos==null){
            this.photos = new ArrayList<>();
        }
        for(Photo photo:photos){
            if(photo.getBase64Bitmap().equals(ImageUtil.convertBitmapToString(bitmap))){
                photos.remove(bitmap);
            }
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

    public BodyLocation getBodyLocation() {
        return bodyLocation;
    }

    public void setBodyLocation(BodyLocation bodyLocation) {
        this.bodyLocation = bodyLocation;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public void updateComment(){
        HashMap<String, ArrayList<String>> comments = ElasticSearchController.searchRecordComment(getRecordId());
        setComments(comments);
    }
}
