package ca.ualberta.t04.medicaltracker.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.R;

/**
 * This activity is for the setting of the App
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */

/*
  This activity is for the setting of the App
 */

public class SettingActivity extends AppCompatActivity {

    /**
     * oncreate
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    /**
     * change the App language
     * @param view View
     */
    public void changeLanguage(View view){
        Intent language = new Intent(SettingActivity.this,LanguageActivity.class);
        startActivity(language);
    }
}
