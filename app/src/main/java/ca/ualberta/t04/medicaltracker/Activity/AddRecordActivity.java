package ca.ualberta.t04.medicaltracker.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ca.ualberta.t04.medicaltracker.DataController;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Record;
import ca.ualberta.t04.medicaltracker.Util;

public class AddRecordActivity extends AppCompatActivity {

    private int problem_index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        problem_index = getIntent().getIntExtra("index", -1);
        if(problem_index==-1){
            Toast.makeText(AddRecordActivity.this, "An error occurs.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addRecord(View view){
        EditText record_title = findViewById(R.id.add_record_title);
        EditText record_date = findViewById(R.id.add_record_date);
        EditText record_description = findViewById(R.id.add_record_description);

        Date dateStart = new Date();

        SimpleDateFormat format = new SimpleDateFormat(Util.DATE_FORMAT, Locale.getDefault());
        try {
            dateStart = format.parse(record_date.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Record record = new Record(record_title.getText().toString(), dateStart, record_description.getText().toString(), null, null);

        DataController.getPatient().getProblemList().getProblem(problem_index).getRecordList().addRecord(record);

        Toast.makeText(AddRecordActivity.this, "Added a new record.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
