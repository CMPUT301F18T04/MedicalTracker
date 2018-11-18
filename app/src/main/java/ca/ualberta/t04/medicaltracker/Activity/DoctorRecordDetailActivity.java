package ca.ualberta.t04.medicaltracker.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Doctor;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Record;

public class DoctorRecordDetailActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_record_detail);

        getSupportActionBar().setTitle(R.string.doctor_record_detail_title);

        final TextView title = findViewById(R.id.doctorRecordTitle);
        final TextView date = findViewById(R.id.dRecordDetailDate);
        final TextView description = findViewById(R.id.dRecordDetailDescription);

        Button commentButton = findViewById(R.id.doctorCommentButton);

        final int problemIndex = getIntent().getIntExtra("problem_index", -1);
        final int patientIndex = getIntent().getIntExtra("patient_index", -1);
        final int recordIndex = getIntent().getIntExtra("record_index", -1);


        title.setText(DataController.getDoctor().getPatients().get(patientIndex).getProblemList().getProblem(problemIndex).getRecordList().getRecord(recordIndex).getTitle());
        date.setText(DataController.getDoctor().getPatients().get(patientIndex).getProblemList().getProblem(problemIndex).getRecordList().getRecord(recordIndex).getDateStart().toString());
        description.setText(DataController.getDoctor().getPatients().get(patientIndex).getProblemList().getProblem(problemIndex).getRecordList().getRecord(recordIndex).getDescription());


        InitDoctorCommentListView(problemIndex, recordIndex);


        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorRecordDetailActivity.this, DoctorAddCommentActivity.class);
                intent.putExtra("problem_index", problemIndex);
                intent.putExtra("patient_index", patientIndex);
                intent.putExtra("record_index", recordIndex);
                startActivity(intent);
            }
        });

    }

    private void InitDoctorCommentListView(int i, int j){
        ListView commentListView = findViewById(R.id.CommentListView);

        // should use the the commented out version , but right now the app crashes so the new hash map is a place holder
        //final HashMap<Doctor, ArrayList<String>> dComment = DataController.getPatient().getProblemList().getProblem(i).getRecordList().getRecord(j).getComments();
        final HashMap<Doctor, ArrayList<String>> dComment = new HashMap<>();


        // get all the doctor names in an array
        Set<Doctor> doctor = dComment.keySet();
        final ArrayList<Doctor> doctorList = new ArrayList<>(doctor);

        ArrayList<String> doctorNameList = new ArrayList<>();
        for(int x = 0; x < doctorList.size(); x++){
            doctorNameList.add(doctorList.get(x).getName());
        }

        adapter = new ArrayAdapter<>(this, R.layout.doctor_comment_list, doctorNameList);
        commentListView.setAdapter(adapter);

        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DoctorRecordDetailActivity.this, DoctorCommentDetailActivity.class);
                intent.putExtra("hash_map", dComment);
                intent.putExtra("position",i);
                startActivity(intent);
            }
        });


    }


}
