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

import ca.ualberta.t04.medicaltracker.BitmapHolder;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.View.CustomView;

public class MarkImageActivity extends AppCompatActivity {

    private CustomView mCustomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_image);

        Bitmap bitmap = getIntent().getParcelableExtra("image");

        mCustomView = findViewById(R.id.customView);
        mCustomView.setBitmap(bitmap);
    }

    public void finishMarking(View view){
        Intent intent = new Intent();
        intent.putExtra("data", mCustomView.getBitmap());
        setResult(RESULT_OK, intent);
    }
}
