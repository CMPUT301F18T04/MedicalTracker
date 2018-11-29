package ca.ualberta.t04.medicaltracker.Activity.Patient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.View.MarkImageView;
import ca.ualberta.t04.medicaltracker.Util.ImageUtil;

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
      
        String fileName = System.currentTimeMillis() + ".jpg";
        Boolean succeed = ImageUtil.saveImage(MarkImageView.getBitmap(), fileName);
        String path = null;
        if(succeed){
            path = ImageUtil.PHOTO_DIRECTORY + fileName;
        }
        intent.putExtra("path", path);

        setResult(RESULT_OK, intent);
        finish();
    }
}
