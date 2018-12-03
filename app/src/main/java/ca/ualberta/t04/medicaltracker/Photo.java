package ca.ualberta.t04.medicaltracker;

/**
 * This class is for getter and setters for photos uploaded
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */

public class Photo {
    private String base64Bitmap;
    private String path;
    private Boolean isBack;
    public Photo(String bitmap, String path){
        this.base64Bitmap = bitmap;
        this.path = path;
    }

    /**
     * getBase64Bitmap
     * @return base64Bitmap String
     */
    public String getBase64Bitmap() {
        return base64Bitmap;
    }

    /**
     * Gets the path of photo
     * @return path String
     */
    public String getPath() {
        return path;
    }

    /**
     * setBase64Bitmap
     * @param bitmap String
     */
    public void setBase64Bitmap(String bitmap) {
        this.base64Bitmap = bitmap;
    }

    /**
     * Sets the path for photo
     * @param path String
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * isBack
     * @return isBack Boolean
     */
    public Boolean isBack(){
        if(isBack==null)
            isBack = false;
        return isBack;
    }

    /**
     * setBackStatus
     * @param isBack Boolean
     */
    public void setBackStatus(Boolean isBack){
        this.isBack = isBack;
    }
}
