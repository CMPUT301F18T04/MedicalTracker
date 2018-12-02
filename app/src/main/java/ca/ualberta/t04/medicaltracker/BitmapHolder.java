package ca.ualberta.t04.medicaltracker;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * This class is for holding all the images required for the various functionality
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */

public class BitmapHolder {
    private static ArrayList<Bitmap> bitmaps = new ArrayList<>();

    /**
     * removes the bitmap
     * @param bitmap Bitmap
     */
    public static void removeBitmap(Bitmap bitmap){
        bitmaps.remove(bitmap);
    }

    /**
     * gets the bitmaps in a list
     * @return bitmaps ArrayList
     */
    public static ArrayList<Bitmap> getBitmaps(){
        return bitmaps;
    }

    /**
     * Sets the bitmap
     * @param newBitmaps ArrayList
     */
    public static void setBitmaps(ArrayList<Bitmap> newBitmaps){
        bitmaps = newBitmaps;
    }
}
