package ca.ualberta.t04.medicaltracker;

public class Photo {
    private String base64Bitmap;
    private String path;
    private Boolean isBack;
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

    public Boolean isBack(){
        if(isBack==null)
            isBack = false;
        return isBack;
    }

    public void setBackStatus(Boolean isBack){
        this.isBack = isBack;
    }
}
