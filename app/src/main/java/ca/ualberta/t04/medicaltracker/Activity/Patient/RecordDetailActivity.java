package ca.ualberta.t04.medicaltracker.Activity.Patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;


import ca.ualberta.t04.medicaltracker.Activity.SlideShowActivity;
import ca.ualberta.t04.medicaltracker.BitmapHolder;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Model.Record;
import ca.ualberta.t04.medicaltracker.Model.RecordList;

import static ca.ualberta.t04.medicaltracker.Activity.Patient.AddRecordActivity.REQUEST_UPDATE_DATA;

/**
 * This class is for displaying and editing the information of a record for a patient user
 */

public class RecordDetailActivity extends AppCompatActivity {

    public static ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        // Initialize all the variables used

        getSupportActionBar().setTitle(R.string.edit_doctor_record_detail);

        Intent mIntent = getIntent();
        final int problemIndex = mIntent.getIntExtra("problem_index", -1);
        final int recordIndex = mIntent.getIntExtra("record_index",-1);

        final EditText title = findViewById(R.id.addCommentEditText);
        final TextView date = findViewById(R.id.dateTextView);
        final TextView location = findViewById(R.id.locationTextView);
        final TextView body_location = findViewById(R.id.bodyLocationTextView);
        final EditText description = findViewById(R.id.descriptionEditText);


        Button saveButton = findViewById(R.id.saveButton);
        ImageView recordImageView = findViewById(R.id.recordImageView);

        final Problem problem = DataController.getPatient().getProblemList().getProblem(problemIndex);
        final RecordList recordList = problem.getRecordList();
        final Record record = recordList.getRecord(recordIndex);

        // set the information
        title.setText(record.getTitle());
        date.setText(record.getDateStart().toString());
        //location.setText(record.getLocation().toString());
        //body_location.setText(record.getBodyLocation().toString());
        description.setText(record.getDescription());

        if(!record.getPhotos().isEmpty())
            recordImageView.setImageBitmap(record.getPhotos().get(0));

        // When the image view is clicked
        recordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(record.getPhotos().isEmpty()){
                    Toast.makeText(RecordDetailActivity.this, R.string.record_toast2, Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(RecordDetailActivity.this, SlideShowActivity.class);
                    BitmapHolder.setBitmaps(record.getPhotos());
                    startActivityForResult(intent, REQUEST_UPDATE_DATA);
                }
            }
        });


        // when save button is pressed, new changes for a record get saved
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordList.setTitle(record, title.getText().toString());

                recordList.setDescription(record, description.getText().toString());

                ElasticSearchController.updateRecord(record);

                Toast.makeText(RecordDetailActivity.this, R.string.record_toast1, Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        InitCommentListView(record);
    }


    // Setting up the Doctor comment list view
    private void InitCommentListView(Record record){
        ListView commentListView = findViewById(R.id.CommentListView);

        final HashMap<String, ArrayList<String>> dComment = record.getComments();

        // get all the doctor names in an array
        final ArrayList<String> doctorList = new ArrayList<>(dComment.keySet());
        final ArrayList<String> comments = getComment(dComment, doctorList);

        adapter = new ArrayAdapter<>(this, R.layout.doctor_comment_list, comments);
        commentListView.setAdapter(adapter);
    }

    // Formatting the list for the commentListView and return it
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
