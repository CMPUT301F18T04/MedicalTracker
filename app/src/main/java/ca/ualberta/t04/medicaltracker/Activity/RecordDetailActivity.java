package ca.ualberta.t04.medicaltracker.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;


import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Problem;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Record;
import ca.ualberta.t04.medicaltracker.RecordList;

public class RecordDetailActivity extends AppCompatActivity {

    public static ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        getSupportActionBar().setTitle(R.string.edit_doctor_record_detail);

        Intent mIntent = getIntent();
        final int problemIndex = mIntent.getIntExtra("p_index", -1);
        final int recordIndex = mIntent.getIntExtra("r_index",-1);

        final EditText title = findViewById(R.id.addCommentEditText);
        final TextView date = findViewById(R.id.dateTextView);
        final EditText description = findViewById(R.id.descriptionEditText);

        Button saveButton = findViewById(R.id.saveButton);

        final Problem problem = DataController.getPatient().getProblemList().getProblem(problemIndex);
        RecordList recordList = problem.getRecordList();
        Record record = recordList.getRecord(recordIndex);

        // set the information
        title.setText(record.getTitle());
        date.setText(record.getDateStart().toString());
        description.setText(record.getDescription());

        // when save button is pressed, new changes for a record get saved
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                problem.getRecordList().getRecord(recordIndex).setTitle(title.getText().toString());

                problem.getRecordList().getRecord(recordIndex).setDescription(description.getText().toString());

                problem.getRecordList().setTitle(problem.getRecordList().getRecord(recordIndex), title.getText().toString());

                problem.getRecordList().setDescription(problem.getRecordList().getRecord(recordIndex), description.getText().toString());

                Toast.makeText(RecordDetailActivity.this, "New edits saved", Toast.LENGTH_SHORT).show();

            }
        });


        InitCommentListView(record);
    }


    private void InitCommentListView(Record record){
        ListView commentListView = findViewById(R.id.CommentListView);

        final HashMap<String, ArrayList<String>> dComment = record.getComments();

        // get all the doctor names in an array
        final ArrayList<String> doctorList = new ArrayList<>(dComment.keySet());
        final ArrayList<String> comments = getComment(dComment, doctorList);

        adapter = new ArrayAdapter<>(this, R.layout.doctor_comment_list, comments);
        commentListView.setAdapter(adapter);
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
