package ca.ualberta.t04.medicaltracker.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Doctor;
import ca.ualberta.t04.medicaltracker.Problem;
import ca.ualberta.t04.medicaltracker.R;


/**
 * data transfer tested with string instead of doctor and everything works
 * editing saves and works good
 * need to change the hashmap type to doctor after finishing the setcomment functionality of a doctor
 * and test again
 */

public class RecordDetailActivity extends AppCompatActivity {

    public static ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        getSupportActionBar().setTitle(R.string.edit_doctor_record_detail);

        Intent mIntent = getIntent();
        final int index = mIntent.getIntExtra("p_index", -1);
        final int r_pos = mIntent.getIntExtra("r_index",-1);

        final EditText title = findViewById(R.id.addCommentEditText);
        final EditText date = findViewById(R.id.dateEditText);
        final EditText description = findViewById(R.id.descriptionEditText);

        Button saveButton = findViewById(R.id.saveButton);

        final Problem problem = DataController.getPatient().getProblemList().getProblem(index);


        // set the information
        title.setText(problem.getRecordList().getRecord(r_pos).getTitle());
        date.setText(problem.getRecordList().getRecord(r_pos).getDateStart().toString());
        description.setText(problem.getRecordList().getRecord(r_pos).getDescription());

        // when save button is pressed, new changes for a record get saved
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                problem.getRecordList().getRecord(r_pos).setTitle(title.getText().toString());

                problem.getRecordList().getRecord(r_pos).setDescription(description.getText().toString());

                problem.getRecordList().setTitle(problem.getRecordList().getRecord(r_pos), title.getText().toString());


                problem.getRecordList().setDescription(problem.getRecordList().getRecord(r_pos), description.getText().toString());
                Toast.makeText(RecordDetailActivity.this, "New edits saved", Toast.LENGTH_SHORT).show();

            }
        });


        InitCommentListView(index, r_pos);
        //InitCommentListView();

    }


    private void InitCommentListView(int i, int j) {
        ListView commentListView = findViewById(R.id.CommentListView);
        // get the comments as a hash map
        final HashMap dComment = DataController.getPatient().getProblemList().getProblem(i).getRecordList().getRecord(j).getComments();


        System.out.println(dComment);


        // get all the doctor names in an array
        Set<Doctor> doctor = dComment.keySet();
        final ArrayList<Doctor> doctorList = new ArrayList<>(doctor);

        ArrayList<String> doctorNameList = new ArrayList<>();
        for(int x = 0; x < doctorList.size(); x++){
            doctorNameList.add(doctorList.get(x).getName());
        }
        //Collections.sort(doctorNameList, String.CASE_INSENSITIVE_ORDER);

        System.out.println(doctorList);

        adapter = new ArrayAdapter<>(this, R.layout.doctor_comment_list, doctorNameList);
        commentListView.setAdapter(adapter);

        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent intent = new Intent(RecordDetailActivity.this, DoctorCommentDetailActivity.class);
                intent.putExtra("hash_map", dComment);
                intent.putExtra("doctorKey", doctorList.get(pos).getName());
                intent.putExtra("position",pos);
                startActivity(intent);
            }
        });


        //https://stackoverflow.com/questions/18680542/how-to-get-the-arraylist-from-the-hashmap-in-java
    }



}
