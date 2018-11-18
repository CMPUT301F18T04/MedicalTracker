package ca.ualberta.t04.medicaltracker.Activity;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Doctor;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Record;

public class DoctorAddCommentActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_add_comment);

        getSupportActionBar().setTitle(R.string.doctor_comment_title);

        final EditText addComment = findViewById(R.id.addCommentEditText);
        Button confirmButton = findViewById(R.id.confirmButton);


        final int patientPos = getIntent().getIntExtra("patient_index", -1);
        final int problemPos = getIntent().getIntExtra("problem_index", -1);
        final int recordPos = getIntent().getIntExtra("record_index", -1);





        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Doctor doctor = DataController.getDoctor();
                Record record = DataController.getDoctor().getPatients().get(patientPos).getProblemList().getProblem(problemPos).getRecordList().getRecord(recordPos);
                String currentComment = addComment.getText().toString();

                DataController.getDoctor().getPatients().get(patientPos).getProblemList().getProblem(problemPos).getRecordList().getRecord(recordPos).addComment(doctor, currentComment);
                DataController.getDoctor().getPatients().get(patientPos).getProblemList().getProblem(problemPos).getRecordList().addComment(record, doctor, currentComment);

                System.out.println(doctor.getName());
                System.out.println(record.getTitle());
                System.out.println(record.getComments().get(doctor));



                Toast.makeText(DoctorAddCommentActivity.this, "New comment added to this record", Toast.LENGTH_SHORT).show();



            }
        });


    }
}
