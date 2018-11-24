package ca.ualberta.t04.medicaltracker.Util;

import android.content.Context;
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

import id.zelory.compressor.Compressor;

public class ImageUtil {
    private static String PHOTO_DIRECTORY = Environment.getExternalStorageDirectory() + "/MedicalTracker/photo/";

    public static Bitmap convertStringToBitmap(String string){
        byte[] bytes = Base64.decode(string, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    public static String convertBitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static Bitmap compressImageFile(Context context, String path) throws IOException {
        File file = new File(path);
        Bitmap compressedImage = new Compressor(context)
                .setMaxWidth(600)
                .setMaxHeight(480)
                .setQuality(30)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setDestinationDirectoryPath(PHOTO_DIRECTORY)
                .compressToBitmap(file);
        return compressedImage;
    }

    public static File saveImage(Bitmap bmp, String fileName) {
        File photoDir = new File(PHOTO_DIRECTORY);
        if (!photoDir.exists()) {
            if(!photoDir.mkdir()){
                Log.d("Succeed", "Error!");
            }
        }
        File file = new File(photoDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
