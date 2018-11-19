package ca.ualberta.t04.medicaltracker.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import ca.ualberta.t04.medicaltracker.CommentPopup;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Listener;
import ca.ualberta.t04.medicaltracker.Patient;
import ca.ualberta.t04.medicaltracker.Problem;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Record;
import ca.ualberta.t04.medicaltracker.RecordList;

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

        Patient patient = DataController.getDoctor().getPatients().get(patientIndex);
        Problem problem = patient.getProblemList().getProblem(problemIndex);
        final RecordList recordList = problem.getRecordList();
        final Record record = recordList.getRecord(recordIndex);


        title.setText(record.getTitle());
        date.setText(record.getDateStart().toString());
        description.setText(record.getDescription());

        InitDoctorCommentListView(recordList, record, patient);

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentPopup commentPopup = new CommentPopup(DoctorRecordDetailActivity.this, recordList, record, DataController.getDoctor());
                commentPopup.addComment();
            }
        });

    }

    private void InitDoctorCommentListView(RecordList recordList, Record record, final Patient patient){
        ListView commentListView = findViewById(R.id.CommentListView);

        final HashMap<String, ArrayList<String>> dComment = record.getComments();

        // get all the doctor names in an array
        final ArrayList<String> doctorList = new ArrayList<>(dComment.keySet());
        final ArrayList<String> comments = getComment(dComment, doctorList);

        adapter = new ArrayAdapter<>(this, R.layout.doctor_comment_list, comments);
        commentListView.setAdapter(adapter);

        recordList.addListener("ListenToComment", new Listener() {
            @Override
            public void update() {
                comments.clear();
                comments.addAll(getComment(dComment, doctorList));
                adapter.notifyDataSetChanged();
                ElasticSearchController.updateUser(patient);
            }
        });
    }

    private ArrayList<String> getComment(HashMap<String, ArrayList<String>> dComment, ArrayList<String> doctorList){
        final ArrayList<String> comments = new ArrayList<>();
        String doctorUserName;
        for(int i=0; i<doctorList.size(); i++ ){
            for(int j= 0; j<dComment.get(doctorList.get(i)).size(); j++) {
                doctorUserName = doctorList.get(i);
                String comment = doctorUserName + ": " + dComment.get(doctorUserName).get(j);
                comments.add(comment);
            }
        }
        return comments;
    }
}
