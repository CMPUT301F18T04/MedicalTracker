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

public class AddProblemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);
    }

    public void addProblem(View view){
        EditText problem_title = findViewById(R.id.add_problem_title);
        EditText problem_date = findViewById(R.id.add_problem_date);
        EditText problem_description = findViewById(R.id.add_problem_description);

        Date dateStart = new Date();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            dateStart = format.parse(problem_date.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Problem problem = new Problem(problem_title.getText().toString(), dateStart, problem_description.getText().toString());

        DataController.getPatient().addProblem(problem);

        Toast.makeText(AddProblemActivity.this, "Added a new problem.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
