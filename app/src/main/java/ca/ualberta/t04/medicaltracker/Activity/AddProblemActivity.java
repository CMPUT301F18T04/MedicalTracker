package ca.ualberta.t04.medicaltracker.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Problem;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Util;

public class AddProblemActivity extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener problemDateSetListener;
    private TextView problem_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);
        problem_date = findViewById(R.id.add_problem_date);
        EditText problem_title = findViewById(R.id.add_problem_title);
        problemSetDate();
        problem_title.requestFocus();
    }

    public void problemSetDate(){
        problem_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddProblemActivity.this, android.R.style.ThemeOverlay_Material_Dialog,
                        problemDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        problemDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = year + "-" + month + "-" + day;
                problem_date.setText(date);
            }
        };
    }

    // Used to add a problem
    public void addProblem(View view){
        // get title, date and description
        EditText problem_title = findViewById(R.id.add_problem_title);
        EditText problem_description = findViewById(R.id.add_problem_description);

        // init date is now
        Date dateStart = new Date();

        // if the date that the user inputs is not correct, then use the default date
        SimpleDateFormat format = new SimpleDateFormat(Util.DATE_FORMAT, Locale.getDefault());
        try {
            dateStart = format.parse(problem_date.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // create a new problem
        Problem problem = new Problem(problem_title.getText().toString(), dateStart, problem_description.getText().toString());

        DataController.getPatient().getProblemList().addProblem(problem);

        Toast.makeText(AddProblemActivity.this, "Added a new problem.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
