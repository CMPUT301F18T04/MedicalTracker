package ca.ualberta.t04.medicaltracker.Activity.Doctor;


import android.app.Dialog;
import android.content.Intent;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


import ca.ualberta.t04.medicaltracker.Activity.MapViewActivity;

import java.util.List;
import java.util.Locale;

import ca.ualberta.t04.medicaltracker.Activity.Patient.RecordDetailActivity;
import ca.ualberta.t04.medicaltracker.Activity.SlideShowActivity;
import ca.ualberta.t04.medicaltracker.BitmapHolder;
import ca.ualberta.t04.medicaltracker.Activity.InformationActivity;
import ca.ualberta.t04.medicaltracker.Activity.Patient.RecordDetailActivity;
import ca.ualberta.t04.medicaltracker.Activity.ProfileActivity;
import ca.ualberta.t04.medicaltracker.Adapter.CommentAdapter;

import ca.ualberta.t04.medicaltracker.CommentPopup;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Listener;
import ca.ualberta.t04.medicaltracker.Model.Doctor;
import ca.ualberta.t04.medicaltracker.Model.Patient;
import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.Model.Record;
import ca.ualberta.t04.medicaltracker.Model.RecordList;
import ca.ualberta.t04.medicaltracker.R;


/**
 * This class is for displaying the information of a record and commenting for a doctor user
 */

public class DoctorRecordDetailActivity extends AppCompatActivity {

    private static final String TAG = "DoctorRecordDetail";
    private ArrayAdapter<String> adapter;
    private ImageView viewLocation;

    private Geocoder geocoder;
    private List<Address> addresses;

    private RecordList recordList;
    private Record record;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_record_detail);

        // Initialize all the variables used

        getSupportActionBar().setTitle(R.string.doctor_record_detail_title);

        final TextView title = findViewById(R.id.doctorRecordTitle);
        final TextView date = findViewById(R.id.dRecordDetailDate);
        final TextView location = findViewById(R.id.doctorRecordLocation);
        final TextView body_location = findViewById(R.id.doctorRecordBodyLocation);
        final TextView description = findViewById(R.id.dRecordDetailDescription);

        ImageView imageView = findViewById(R.id.recordImageView);

        Button commentButton = findViewById(R.id.doctorCommentButton);
        viewLocation = findViewById(R.id.doctor_record_detail_view_location);

        final int problemIndex = getIntent().getIntExtra("problem_index", -1);
        final int patientIndex = getIntent().getIntExtra("patient_index", -1);
        final int recordIndex = getIntent().getIntExtra("record_index", -1);

        Patient patient = DataController.getDoctor().getPatients().get(patientIndex);

        final Problem problem = patient.getProblemList().getProblem(problemIndex);
        recordList = DataController.getRecordList().get(problem.getProblemId());
        record = recordList.getRecord(recordIndex);

        if(!record.getPhotos().isEmpty())
            imageView.setImageBitmap(record.getPhotos().get(0));

        // When the image view is clicked
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(record.getPhotos().isEmpty()){
                    Toast.makeText(DoctorRecordDetailActivity.this, R.string.record_toast2, Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(DoctorRecordDetailActivity.this, SlideShowActivity.class);
                    BitmapHolder.setBitmaps(record.getPhotos());
                    startActivity(intent);
                }
            }
        });


        title.setText(record.getTitle());
        date.setText(record.getDateStart().toString());
        Location recordLocation = record.getLocation();
        if(recordLocation == null){
            location.setText(R.string.location_text);
        }else {

            double longitude = recordLocation.getLongitude();
            double latitude = recordLocation.getLatitude();
            geocoder = new Geocoder(DoctorRecordDetailActivity.this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address_line = addresses.get(0).getAddressLine(0); // the full address is stored in the variable address_line
                location.setText(address_line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (record.getBodyLocation() == null){
            body_location.setText("");
        }
        else {
            body_location.setText(record.getBodyLocation().name());
        }

        description.setText(record.getDescription());

        InitDoctorCommentListView();

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentPopup commentPopup = new CommentPopup(DoctorRecordDetailActivity.this, recordList, record, DataController.getDoctor());
                commentPopup.addComment();
            }
        });

        if (isServicesOK()){
            init(problemIndex, recordIndex, patientIndex);
        }



    }

    private void init(final int problemIndex, final int recordIndex, final int patientIndex){
        viewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataController.getDoctor().getPatients().get(patientIndex).getProblemList().getProblem(problemIndex).
                        getRecordList().getRecord(recordIndex).getLocation()==null){
                    Toast.makeText(DoctorRecordDetailActivity.this,"This record has no location", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(DoctorRecordDetailActivity.this, MapViewActivity.class);
                    intent.putExtra("problem_index", problemIndex);
                    intent.putExtra("record_index", recordIndex);
                    intent.putExtra("patient_index", patientIndex);
                    startActivity(intent);
                }
            }
        });
    }

    public  boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(DoctorRecordDetailActivity.this);

        if (available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServicesOK : an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(DoctorRecordDetailActivity.this,available,0001);
            dialog.show();
        }else{
            Toast.makeText(this, "you can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    // Setting up the Doctor comment list view
    private void InitDoctorCommentListView(){
        ListView commentListView = findViewById(R.id.CommentListView);

        final HashMap<String, ArrayList<String>> dComment = record.getComments();
        Log.d("Succeed", dComment.toString());

        // get all the doctor names in an array
        final ArrayList<String> doctorList = new ArrayList<>(dComment.keySet());
        final ArrayList<String> comments = new ArrayList<>(getComment(dComment, doctorList));

        final CommentAdapter adapter = new CommentAdapter(this, R.layout.doctor_comment_list, comments);
        commentListView.setAdapter(adapter);

        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String doctorUserName = comments.get(position).split(":")[0];
                if(doctorUserName.equals(DataController.getDoctor().getUserName())){
                    Intent intent = new Intent(DoctorRecordDetailActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(DoctorRecordDetailActivity.this, InformationActivity.class);
                    intent.putExtra("username", doctorUserName);
                    startActivity(intent);
                }
            }
        });

        recordList.replaceListener("ListenToComment", new Listener() {
            @Override
            public void update() {
                doctorList.clear();
                doctorList.addAll(dComment.keySet());
                comments.clear();
                comments.addAll(getComment(dComment, doctorList));
                adapter.notifyDataSetChanged();
            }
        });
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
