package ca.ualberta.t04.medicaltracker.Activity.Patient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ca.ualberta.t04.medicaltracker.R;

/**
 * This activity is for displaying the uploaded photos of a patient user
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
    }
}
