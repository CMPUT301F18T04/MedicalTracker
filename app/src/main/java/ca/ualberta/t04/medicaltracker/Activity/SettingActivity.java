package ca.ualberta.t04.medicaltracker.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ca.ualberta.t04.medicaltracker.DataController;
import ca.ualberta.t04.medicaltracker.R;

public class SettingActivity extends AppCompatActivity {

    private EditText name_edit_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        name_edit_text = findViewById(R.id.setting_name_editText);
        name_edit_text.setText(DataController.getUser().getName());
    }

    public void apply(View view)
    {
        String newName = name_edit_text.getText().toString();
        if(!newName.equals(""))
            DataController.getUser().setName(newName);
        else
            Toast.makeText(SettingActivity.this, "Your name cannot be empty!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
