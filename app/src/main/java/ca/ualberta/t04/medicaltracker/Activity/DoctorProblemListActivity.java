package ca.ualberta.t04.medicaltracker.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Adapter.ProblemAdapter;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Problem;
import ca.ualberta.t04.medicaltracker.R;

public class DoctorProblemListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_problem_list);

        getSupportActionBar().setTitle(R.string.doctor_problem_list_title);

        int patientIndex = getIntent().getIntExtra("index", -1);

        InitDoctorProblemList(patientIndex);

    }

    private void InitDoctorProblemList(final int patientPosition){
        ListView problemListView = findViewById(R.id.problem_list_list_view);
        final ArrayList<Problem> problems = DataController.getDoctor().getPatients().get(patientPosition).getProblemList().getProblems();
        final ProblemAdapter adapter = new ProblemAdapter(this, R.layout.problem_list, problems);
        problemListView.setAdapter(adapter);

        if(problems.size() == 0){
            Toast.makeText(DoctorProblemListActivity.this, "There is no problem for this patient", Toast.LENGTH_SHORT).show();
        }

        problemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DoctorProblemListActivity.this, DoctorRecordListActivity.class);
                intent.putExtra("patient_index", patientPosition);
                intent.putExtra("problem_index", position);
                startActivity(intent);
            }
        });

    }
}




























