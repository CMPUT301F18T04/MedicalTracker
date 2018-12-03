package ca.ualberta.t04.medicaltracker.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Stores the image utils used throughout the project
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 04 1.0
 * @since 1.0
 */

public class ImageUtil {
    public static String PHOTO_DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.MEDIA_SHARED) + "/";

    /**
     * convertStringToBitmap
     * @param string String
     * @return Bitmap
     */
    public static Bitmap convertStringToBitmap(String string){
        byte[] bytes = Base64.decode(string, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    /**
     * convertBitmapToString
     * @param bitmap Bitmap
     * @return String
     */
    public static String convertBitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * saves the images
     * @param bmp Bitmap
     * @param fileName String
     * @return Boolean
     */
    public static Boolean saveImage(Bitmap bmp, String fileName) {
        Log.d("Succeed", "Directory:" + PHOTO_DIRECTORY);
        File photoDir = new File(PHOTO_DIRECTORY);
        if (!photoDir.exists()) {
            if(!photoDir.mkdir()){
                Log.d("Succeed", "Error to create directory!");
            }
        }
        File file = new File(photoDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
