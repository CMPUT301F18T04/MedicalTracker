package ca.ualberta.t04.medicaltracker.Activity;

import android.content.Intent;
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

import ca.ualberta.t04.medicaltracker.ImageUtil;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Util;

public class MarkImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_image);

        Bitmap bitmap = getIntent().getParcelableExtra("image");

        ImageView imageView = findViewById(R.id.mark_image_view);
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        Intent intent = new Intent();
        intent.putExtra("data", bitmap);
        setResult(RESULT_OK, intent);
    }
}
