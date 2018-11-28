package ca.ualberta.t04.medicaltracker.Activity.Patient;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.HashMap;


import ca.ualberta.t04.medicaltracker.Activity.MapViewActivity;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Model.Patient;
import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Model.Record;
import ca.ualberta.t04.medicaltracker.Model.RecordList;

/**
 * This class is for displaying and editing the information of a record for a patient user
 */

public class RecordDetailActivity extends AppCompatActivity {

    public static ArrayAdapter<String> adapter;
    private static final String TAG = "RecordDetailActivity";
    private ImageView viewLocation;
    private ListView commentListView;
    private Patient mPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        //widgets
        final EditText title = (EditText) findViewById(R.id.addCommentEditText);
        final TextView date = (TextView) findViewById(R.id.dateTextView);
        final EditText description = (EditText) findViewById(R.id.descriptionEditText);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        commentListView = (ListView) findViewById(R.id.CommentListView);
        viewLocation= (ImageView) findViewById(R.id.view_location);

        // Initialize all the variables used
        getSupportActionBar().setTitle(R.string.edit_doctor_record_detail);
        Intent mIntent = getIntent();

        //vars
        final int problemIndex = mIntent.getIntExtra("problem_index", -1);
        final int recordIndex = mIntent.getIntExtra("record_index",-1);

        //objects
        mPatient = DataController.getPatient();
        final Problem problem = mPatient.getProblemList().getProblem(problemIndex);
        final RecordList recordList = problem.getRecordList();
        final Record record = recordList.getRecord(recordIndex);

        // set the information
        title.setText(record.getTitle());
        date.setText(record.getDateStart().toString());
        description.setText(record.getDescription());

        // when save button is pressed, new changes for a record get saved
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordList.setTitle(record, title.getText().toString());

                recordList.setDescription(record, description.getText().toString());

                ElasticSearchController.updateRecord(record);

                Toast.makeText(RecordDetailActivity.this, "New edits saved", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        if (isServicesOK()){
            init(problemIndex, recordIndex);
        }

        InitCommentListView(record);
    }

    private void init(final int problem_index, final int recordIndex){
        viewLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(RecordDetailActivity.this, MapViewActivity.class);
                intent.putExtra("problem_index", problem_index);
                intent.putExtra("record_index", recordIndex);
                startActivity(intent);
            }
        });
    }

    public  boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(RecordDetailActivity.this);

        if (available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServicesOK : an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(RecordDetailActivity.this,available,0001);
            dialog.show();
        }else{
            Toast.makeText(this, "you can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    // Setting up the Doctor comment list view
    private void InitCommentListView(Record record){

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
