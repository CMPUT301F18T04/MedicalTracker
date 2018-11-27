package ca.ualberta.t04.medicaltracker;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class BitmapHolder {
    private static ArrayList<Bitmap> bitmaps = new ArrayList<>();

    public static void addBitmap(Bitmap bitmap){
        bitmaps.add(bitmap);
    }

    public static void clearBitmap(){
        bitmaps.clear();
    }

    public static void removeBitmap(Bitmap bitmap){
        bitmaps.remove(bitmap);
    }

    public static ArrayList<Bitmap> getBitmaps(){
        return bitmaps;
    }

    public static void setBitmaps(ArrayList<Bitmap> newBitmaps){
        bitmaps = newBitmaps;
    }
}
