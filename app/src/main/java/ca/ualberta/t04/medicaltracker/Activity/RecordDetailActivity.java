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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Problem;
import ca.ualberta.t04.medicaltracker.R;


/**
 * data transfer tested with string instead of doctor and everything works
 * editing saves and works good
 * need to change the hashmap type to doctor after finishing the setcomment functionality of a doctor
 * and test again
 */

public class RecordDetailActivity extends AppCompatActivity {

    //public static ArrayList<String> commentList;
    //public static ArrayAdapter<Doctor> adapter;
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
                //problem.getRecordList().getRecord(r_pos).setDateStart(convertedDate = dateFormat.parse(date.getText().toString()));
                problem.getRecordList().getRecord(r_pos).setDescription(description.getText().toString());


                problem.getRecordList().setTitle(problem.getRecordList().getRecord(r_pos), title.getText().toString());
                //problem.getRecordList().getRecord(r_pos).setDateStart(convertedDate = dateFormat.parse(date.getText().toString()));


                problem.getRecordList().setDescription(problem.getRecordList().getRecord(r_pos), description.getText().toString());
                Toast.makeText(RecordDetailActivity.this, "New edits saved", Toast.LENGTH_SHORT).show();

            }
        });


        //InitCommentListView(index, r_pos);
        InitCommentListView();

    }


    public void InitCommentListView() {
        ListView commentListView = findViewById(R.id.CommentListView);
        // get the comments as a hash map
        //final HashMap dComment = DataController.getPatient().getProblemList().getProblem(i).getRecordList().getRecord(j).getComments();

        final HashMap<String, ArrayList<String>> dComment = new HashMap<>();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Hello");
        arrayList.add("World.");
        dComment.put("dd",arrayList);

        arrayList.add("hey");
        arrayList.add("whattup");
        dComment.put("dd",arrayList);

        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("Hello");
        arrayList1.add("World.");
        dComment.put("ff",arrayList1);


        System.out.println(dComment);


        // get all the doctor names in an array
        Set<String> doctor = dComment.keySet();
        final List<String> doctorList = new ArrayList<>(doctor);
        Collections.sort(doctorList, String.CASE_INSENSITIVE_ORDER);

        System.out.println(doctorList);

        adapter = new ArrayAdapter<>(this, R.layout.doctor_comment_list, doctorList);
        commentListView.setAdapter(adapter);

        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RecordDetailActivity.this, DoctorCommentDetailActivity.class);
                intent.putExtra("hash_map", dComment);
                intent.putExtra("position",i);
                startActivity(intent);
            }
        });


        //https://stackoverflow.com/questions/18680542/how-to-get-the-arraylist-from-the-hashmap-in-java
    }



}
