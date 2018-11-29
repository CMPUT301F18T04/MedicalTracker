package ca.ualberta.t04.medicaltracker.Activity.Patient;


import android.app.Dialog;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;



import ca.ualberta.t04.medicaltracker.Activity.MapViewActivity;
import ca.ualberta.t04.medicaltracker.Activity.SlideShowActivity;
import ca.ualberta.t04.medicaltracker.BitmapHolder;

import ca.ualberta.t04.medicaltracker.Activity.InformationActivity;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Model.Patient;
import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Model.Record;
import ca.ualberta.t04.medicaltracker.Model.RecordList;
import ca.ualberta.t04.medicaltracker.Util.ImageUtil;


/**
 * This class is for displaying and editing the information of a record for a patient user
 */

public class RecordDetailActivity extends AppCompatActivity {

    public static ArrayAdapter<String> adapter;

    private static final String TAG = "RecordDetailActivity";
    private ImageView viewLocation;
    private ListView commentListView;
    private Patient mPatient;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_UPDATE_DATA = 2;
    static final int REQUEST_MARK_IMAGE = 3;
    private Geocoder geocoder;
    private List<Address> addresses;
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private ImageView recordImageView;
    private HashMap<Bitmap, String> bitmapHashMap = new HashMap<>();
    private ArrayList<Bitmap> originBitmaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        //widgets
        commentListView = (ListView) findViewById(R.id.CommentListView);
        viewLocation= (ImageView) findViewById(R.id.view_location);

        // Initialize all the variables used
        getSupportActionBar().setTitle(R.string.edit_doctor_record_detail);
        Intent mIntent = getIntent();

        //vars
        final int problemIndex = mIntent.getIntExtra("problem_index", -1);
        final int recordIndex = mIntent.getIntExtra("record_index", -1);


       //objects

        final EditText title = findViewById(R.id.addCommentEditText);
        final TextView date = findViewById(R.id.dateTextView);
        final TextView location = findViewById(R.id.locationTextView);
        final TextView body_location = findViewById(R.id.bodyLocationTextView);
        final EditText description = findViewById(R.id.descriptionEditText);
        Button uploadButton = findViewById(R.id.uploadButton);


        Button saveButton = findViewById(R.id.saveButton);
        recordImageView = findViewById(R.id.recordImageView);

        mPatient = DataController.getPatient();

        final Problem problem = DataController.getPatient().getProblemList().getProblem(problemIndex);
        final RecordList recordList = DataController.getRecordList().get(problem.getProblemId());
        final Record record = recordList.getRecord(recordIndex);

        originBitmaps = (ArrayList<Bitmap>) record.getPhotos().clone();

        bitmaps = (ArrayList<Bitmap>) record.getPhotos().clone();
        Log.d("Succeed", String.valueOf(bitmaps.size()));

        // set the information
        title.setText(record.getTitle());
        date.setText(record.getDateStart().toString());

        Location recordLocation = record.getLocation();
        if (recordLocation == null) {
            location.setText(R.string.location_text);
        } else {

            double longitude = recordLocation.getLongitude();
            double latitude = recordLocation.getLatitude();
            geocoder = new Geocoder(RecordDetailActivity.this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address_line = addresses.get(0).getAddressLine(0); // the full address is stored in the variable address_line
                location.setText(address_line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (record.getBodyLocation() == null) {
            body_location.setText("");
        } else {
            body_location.setText(record.getBodyLocation().name());
        }

        description.setText(record.getDescription());

        if (!bitmaps.isEmpty())
            recordImageView.setImageBitmap(bitmaps.get(0));

        // When the image view is clicked
        recordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmaps.isEmpty()) {
                    Toast.makeText(RecordDetailActivity.this, R.string.record_toast2, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(RecordDetailActivity.this, SlideShowActivity.class);
                    BitmapHolder.setBitmaps(bitmaps);
                    intent.putExtra("recordIndex", recordIndex);
                    intent.putExtra("problemIndex", problemIndex);
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

                Log.d("Succeed", String.valueOf(bitmapHashMap.size()));
                for(Bitmap bitmap:bitmapHashMap.keySet()){
                    record.addImage(bitmap, bitmapHashMap.get(bitmap));
                }

                for(Bitmap bitmap:originBitmaps){
                    if(!BitmapHolder.getBitmaps().contains(bitmap)){
                        record.removeImage(originBitmaps.indexOf(bitmap));
                    }
                }

                ElasticSearchController.updateRecord(record);

                Toast.makeText(RecordDetailActivity.this, R.string.record_toast1, Toast.LENGTH_SHORT).show();

                finish();
            }
        });


        if (isServicesOK()){
            init(problemIndex, recordIndex);
        }

        // When the upload Button is clicked
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(RecordDetailActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(RecordDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RecordDetailActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    if (bitmaps.size() < 10) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    } else {
                        Toast.makeText(RecordDetailActivity.this, getString(R.string.add_record_toast3), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        InitCommentListView(record);
    }

    private void init(final int problem_index, final int recordIndex){
        viewLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (DataController.getPatient().getProblemList().getProblem(problem_index).
                        getRecordList().getRecord(recordIndex).getLocation()==null){
                    Toast.makeText(RecordDetailActivity.this,"This record has no location", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(RecordDetailActivity.this, MapViewActivity.class);
                    intent.putExtra("problem_index", problem_index);
                    intent.putExtra("record_index", recordIndex);
                    startActivity(intent);
                }
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

        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String doctorUserName = comments.get(position).split(":")[0];
                Intent intent = new Intent(RecordDetailActivity.this, InformationActivity.class);
                intent.putExtra("username", doctorUserName);
                startActivity(intent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");

            Log.d("Succeed", "Compressed:" + String.valueOf(ImageUtil.convertBitmapToString(bitmap).length()));

            Intent intent = new Intent(RecordDetailActivity.this, MarkImageActivity.class);

            intent.putExtra("image", bitmap);
            startActivityForResult(intent, REQUEST_MARK_IMAGE);
        }
        else if(requestCode == REQUEST_UPDATE_DATA && resultCode == RESULT_OK) {
            bitmaps = BitmapHolder.getBitmaps();
            if(bitmaps.isEmpty()){
                recordImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_gallery));
            } else {
                recordImageView.setImageBitmap(bitmaps.get(0));
            }

        } else if(requestCode == REQUEST_MARK_IMAGE && resultCode == RESULT_OK) {
            Bitmap bitmap = data.getParcelableExtra("data");
            String path = data.getStringExtra("path");
            bitmapHashMap.put(bitmap, path);
            bitmaps.add(bitmap);
            recordImageView.setImageBitmap(bitmap);
        }
    }
}
