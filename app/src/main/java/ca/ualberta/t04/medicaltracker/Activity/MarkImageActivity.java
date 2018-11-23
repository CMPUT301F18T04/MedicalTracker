package ca.ualberta.t04.medicaltracker.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Util;

public class MarkImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_image);

        String path = getIntent().getStringExtra("image");

        Bitmap bitmap;
        try {
            bitmap = Util.compressImageFile(this, path);

            String fileName = System.currentTimeMillis()+".jpg";

            Util.saveImage(bitmap, fileName);
            String photoPath = Environment.getExternalStorageDirectory() + "/MedicalTracker/photo/" + fileName;
            Log.d("Succeed", photoPath);

            bitmap.recycle();
            bitmap = Util.compressImageFile(this, photoPath);

            ImageView imageView = findViewById(R.id.mark_image_view);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
