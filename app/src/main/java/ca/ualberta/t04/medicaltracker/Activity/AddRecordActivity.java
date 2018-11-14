package ca.ualberta.t04.medicaltracker.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Record;
import ca.ualberta.t04.medicaltracker.Util;

public class AddRecordActivity extends AppCompatActivity {

    private int problem_index;
    private DatePickerDialog.OnDateSetListener recordDateSetListener;
    private TimePickerDialog.OnTimeSetListener recordTimeSetListener;
    private TextView record_date;
    private TextView record_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        record_date = findViewById(R.id.add_record_date);
        record_time = findViewById(R.id.add_record_time);
        recordSetDate();
        recordSetTime();
        // Get the index of the problem list
        problem_index = getIntent().getIntExtra("index", -1);
        if(problem_index==-1){
            Toast.makeText(AddRecordActivity.this, "An error occurs.", Toast.LENGTH_SHORT).show();
        }
    }

    public void recordSetDate(){
        record_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddRecordActivity.this, android.R.style.ThemeOverlay_Material_Dialog,
                        recordDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
            }
        });

        recordDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = year + "-" + month + "-" + day;
                record_date.setText(date);
            }
        };

    }

    public void recordSetTime(){
        record_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddRecordActivity.this, android.R.style.ThemeOverlay_Material_Dialog,
                        recordTimeSetListener, hour, minute, android.text.format.DateFormat.is24HourFormat(AddRecordActivity.this));
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                timePickerDialog.show();
            }
        });
        recordTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String time = hour + ":" + minute;
                record_time.setText(time);
            }
        };
    }

    // Used to add a record for a problem
    public void addRecord(View view){
        EditText record_title = findViewById(R.id.add_record_title);
        EditText record_description = findViewById(R.id.add_record_description);

        Date dateStart = new Date();

        SimpleDateFormat format = new SimpleDateFormat(Util.DATE_FORMAT, Locale.getDefault());
        try {
            dateStart = format.parse(record_date.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // create a new record
        Record record = new Record(record_title.getText().toString(), dateStart, record_description.getText().toString(), null, null);

        DataController.getPatient().getProblemList().getProblem(problem_index).getRecordList().addRecord(record);

        Toast.makeText(AddRecordActivity.this, "Added a new record.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
