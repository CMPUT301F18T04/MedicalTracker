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
import ca.ualberta.t04.medicaltracker.Problem;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Util;

public class AddProblemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);

        EditText problem_title = findViewById(R.id.add_problem_title);
        problem_title.requestFocus();
    }

    public void addProblem(View view){
        // get title, date and description
        EditText problem_title = findViewById(R.id.add_problem_title);
        EditText problem_date = findViewById(R.id.add_problem_date);
        EditText problem_description = findViewById(R.id.add_problem_description);

        // init date is now
        Date dateStart = new Date();

        // if the date that the user inputs is not correct, then use the default date
        SimpleDateFormat format = new SimpleDateFormat(Util.DATE_FORMATE, Locale.getDefault());
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
