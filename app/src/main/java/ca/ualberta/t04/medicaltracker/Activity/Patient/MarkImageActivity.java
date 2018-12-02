package ca.ualberta.t04.medicaltracker.Activity.Patient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Switch;

import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.View.MarkImageView;
import ca.ualberta.t04.medicaltracker.Util.ImageUtil;

/**
 * This activity is for marking a uploaded photo for a patient user
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */

public class MarkImageActivity extends AppCompatActivity {

    private MarkImageView MarkImageView;

    /**
     * onCreate
     * @param savedInstanceState Bundle
     */
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

    /**
     * final image with the marking
     * @param view View
     */
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

        Switch bodyLocationSwitch = findViewById(R.id.mark_image_switch);
        if(bodyLocationSwitch.isChecked()){
            intent.putExtra("isBack", true);
        }

        setResult(RESULT_OK, intent);
        finish();
    }
}
