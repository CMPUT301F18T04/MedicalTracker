package ca.ualberta.t04.medicaltracker.Activity.Patient;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Util.CommonUtil;

/*
  This activity is for adding a problem for a patient user
 */

// This class has the layout of activity_add_problem.xml
// This class is used for adding a new problem
public class AddProblemActivity extends AppCompatActivity {
    // initialize
    private DatePickerDialog.OnDateSetListener problemDateSetListener;
    private TextView problem_date;

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);
        problem_date = findViewById(R.id.add_problem_date);
        EditText problem_title = findViewById(R.id.add_problem_title);
        problemSetDate();
        problem_title.requestFocus();
    }

    // problemSetDate method is used for set a date using DatePickerDialog
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

        // problemDateSetListener gets the result of the DatePickerDialog
        // TextView problem_date will be set with the date the user picked
        // date format: year + "-" + month + "-" + day
        problemDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Date currentDate = new Date();
                String date = year + "-" + month + "-" + day;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date selectedDate = sdf.parse(date);
                    if (currentDate.after(selectedDate)){
                        problem_date.setText(date);
                    } else {
                        Toast.makeText(AddProblemActivity.this, "You can not select future time", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    // Used to add a problem
    public void addProblem(View view){
        // get title, date and description
        EditText problem_title = findViewById(R.id.add_problem_title);
        EditText problem_description = findViewById(R.id.add_problem_description);

        // check if the title and description are both filled
        if(problem_title.getText().toString().equals("") || problem_description.getText().toString().equals("")){
            Toast.makeText(AddProblemActivity.this, "The title/description cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        // init date is now
        Date dateStart = new Date();

        // if the date that the user inputs is not correct, then use the default date
        SimpleDateFormat format = new SimpleDateFormat(CommonUtil.DATE_FORMAT, Locale.getDefault());
        try {
            dateStart = format.parse(problem_date.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // create a new problem
        Problem problem = new Problem(problem_title.getText().toString(), dateStart, problem_description.getText().toString());

        // use dataController to notify the change of problem
        DataController.getPatient().getProblemList().addProblem(problem);

        // notification message
        Toast.makeText(AddProblemActivity.this, R.string.add_problem_toast, Toast.LENGTH_SHORT).show();
        finish();
    }
}
