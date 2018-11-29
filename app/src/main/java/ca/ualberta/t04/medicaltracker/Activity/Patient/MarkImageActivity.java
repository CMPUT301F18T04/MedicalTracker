package ca.ualberta.t04.medicaltracker.Activity.Patient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.View.MarkImageView;

public class MarkImageActivity extends AppCompatActivity {

    private MarkImageView MarkImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_image);

        // Hide the action bar
        getSupportActionBar().hide();
        // Set full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bitmap bitmap = getIntent().getParcelableExtra("image");

        MarkImageView = findViewById(R.id.markView);

        MarkImageView.setBitmap(bitmap);
    }

    public void finishMarking(View view){
        Intent intent = new Intent();
        intent.putExtra("data", MarkImageView.getBitmap());
        setResult(RESULT_OK, intent);
        finish();
    }
}
