package ca.ualberta.t04.medicaltracker;

import android.graphics.Bitmap;

public class Photo {
    String base64Bitmap;
    String path;
    public Photo(String bitmap, String path){
        this.base64Bitmap = bitmap;
        this.path = path;
    }

    public String getBase64Bitmap() {
        return base64Bitmap;
    }

    public String getPath() {
        return path;
    }

    public void setBase64Bitmap(String bitmap) {
        this.base64Bitmap = bitmap;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
