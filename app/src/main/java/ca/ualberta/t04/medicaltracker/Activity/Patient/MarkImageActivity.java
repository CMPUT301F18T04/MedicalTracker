package ca.ualberta.t04.medicaltracker.Activity.Patient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.View.CustomView;

public class MarkImageActivity extends AppCompatActivity {

    private CustomView mCustomView;

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

        mCustomView = (CustomView) findViewById(R.id.customView);
    }
}
