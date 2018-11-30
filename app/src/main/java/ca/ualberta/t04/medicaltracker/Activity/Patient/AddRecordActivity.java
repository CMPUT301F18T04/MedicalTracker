package ca.ualberta.t04.medicaltracker.Activity.Patient;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ca.ualberta.t04.medicaltracker.Activity.SlideShowActivity;
import ca.ualberta.t04.medicaltracker.BitmapHolder;
import ca.ualberta.t04.medicaltracker.BodyLocation;
import ca.ualberta.t04.medicaltracker.BodyLocationPopup;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.Util.CommonUtil;

import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Model.Record;
import ca.ualberta.t04.medicaltracker.Util.ImageUtil;
import ca.ualberta.t04.medicaltracker.Util.NetworkUtil;


/**
 * This class adds a new record for a patient user
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */

/*
  This activity is for adding a record to a problem for a patient user
 */

// This class has the layout of activity_add_record.xml
// This class is used for adding a new record
// This class implements LocationListener which is used for get the current location
public class AddRecordActivity extends AppCompatActivity implements LocationListener,GoogleApiClient.OnConnectionFailedListener {
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    // initialize
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_UPDATE_DATA = 2;
    private static final int REQUEST_MARK_IMAGE = 3;
    private static final int PLACE_PICKER_REQUEST = 4;
    private static final String TAG = "AddRecordActivity";


    private int problem_index;
    private DatePickerDialog.OnDateSetListener recordDateSetListener;
    private TimePickerDialog.OnTimeSetListener recordTimeSetListener;
    private TextView record_date;
    private TextView record_time;

    private TextView record_location;
    private ImageView imageView, addLocation;
    private LocationManager locationManager;
    private Geocoder geocoder;
    private List<Address> addresses;

    private Location location = null;
    private GoogleApiClient mGoogleApiClient;

    private TextView numPhoto;
    private HashMap<Bitmap, String> bitmaps = new HashMap<>();
    private BodyLocation bodyLocation = null;
    private BodyLocationPopup bodyLocationPopup = null;

    private Problem problem;


    private String date;
    private String time;

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        record_date = findViewById(R.id.add_record_date);
        record_time = findViewById(R.id.add_record_time);
        numPhoto = findViewById(R.id.add_record_num_photo);
        record_location = findViewById(R.id.add_record_location);

        record_location.setMovementMethod(new ScrollingMovementMethod());
        ImageButton image_button = findViewById(R.id.imageButton);

        imageView = findViewById(R.id.add_record_photo_display);
        addLocation = findViewById(R.id.record_add_location);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        recordSetDate(); // call recordSetDate
        recordSetTime(); // call recordSetTime

        // ask permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddRecordActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // get the current location
            //location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            location = null;
        }
        // Get the index of the problem list
        problem_index = getIntent().getIntExtra("index", -1);
        if(problem_index==-1){
            Toast.makeText(AddRecordActivity.this, R.string.add_record_toast, Toast.LENGTH_SHORT).show();
        }

        if (isServicesOK()){
            initMapApi();
        }

        problem = DataController.getPatient().getProblemList().getProblem(problem_index);

    }

    // recordSetDate method is used for set a date using DatePickerDialog
    public void recordSetDate(){
        record_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddRecordActivity.this, android.R.style.ThemeOverlay_Material_Dialog,
                        recordDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
            }
        });


        // recordDateSetListener gets the result of the DatePickerDialog
        // TextView record_date will be set with the date the user picked
        // date format: year + "-" + month + "-" + day
        recordDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                date = year + "-" + month + "-" + day;
                record_date.setText(date);

            }
        };
    }

    // recordSetTime method is used for set a time using TimePickerDialog
    public void recordSetTime(){
        record_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddRecordActivity.this, android.R.style.ThemeOverlay_Material_Dialog,
                        recordTimeSetListener, hour, minute, android.text.format.DateFormat.is24HourFormat(AddRecordActivity.this));
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                timePickerDialog.show();
            }
        });

        // recordTimeSetListener gets the result of the TimePickerDialog
        // TextView record_time will be set with the time the user picked
        // time format: hour + ":" + minute
        recordTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                time = hour + ":" + minute;
                record_time.setText(time);
//                date += "T" + time;
            }
        };
    }

    // Method dispatchTakePictureIntent starts the activity of launch the camera of the phone
    public void dispatchTakePictureIntent(View view) {
        if (ContextCompat.checkSelfPermission(AddRecordActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||  ContextCompat.checkSelfPermission(AddRecordActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddRecordActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else{
            if(bitmaps.size()<10){
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
            else {
                Toast.makeText(AddRecordActivity.this, getString(R.string.add_record_toast3), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // This method will be called automatically after startActivityForResult
    // This method returns the result of the activity
    // in this case, the result of taking a  photo is a picture
    // The picture the user just taken will be displayed in a imageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");

            numPhoto.setText(String.valueOf(bitmaps.size()+1));

            Log.d("Succeed", "Compressed:" + String.valueOf(ImageUtil.convertBitmapToString(bitmap).length()));

            Intent intent = new Intent(AddRecordActivity.this, MarkImageActivity.class);

            intent.putExtra("image", bitmap);
            startActivityForResult(intent, REQUEST_MARK_IMAGE);
        }
        else if(requestCode == REQUEST_UPDATE_DATA && resultCode == RESULT_OK) {
            HashMap<Bitmap, String> temp = (HashMap<Bitmap, String>) bitmaps.clone();
            for(Bitmap bitmap:temp.keySet()){
                if(!BitmapHolder.getBitmaps().contains(bitmap)){
                    bitmaps.remove(bitmap);
                }
            }

            numPhoto.setText(String.valueOf(bitmaps.size()));
            if(bitmaps.isEmpty()){
                imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_gallery));
            } else {
                imageView.setImageBitmap((Bitmap) bitmaps.keySet().toArray()[0]);
            }
        } else if(requestCode == REQUEST_MARK_IMAGE && resultCode == RESULT_OK) {
            Bitmap bitmap = data.getParcelableExtra("data");
            String path = data.getStringExtra("path");
            bitmaps.put(bitmap, path);
            imageView.setImageBitmap(bitmap);
        }
        else if (requestCode == PLACE_PICKER_REQUEST ) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace( this,data);

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, place.getId());
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        }
    }

    // This method will be called when the provider status changes.
    // This method is called when a provider is unable to fetch a location or
    // if the provider has recently become available after a period of unavailability.
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    // Called when the provider is enabled by the user
    @Override
    public void onProviderEnabled(String s) {

    }

    // Called when the provider is disabled by the user.
    // If requestLocationUpdates is called on an already disabled provider,
    // this method is called immediately.
    @Override
    public void onProviderDisabled(String s) {

    }

    // Used to add a record for a problem
    public void addRecord(View view){
        EditText record_title = findViewById(R.id.add_record_title);
        EditText record_description = findViewById(R.id.add_record_description);

        // check if the title and description are both filled
        if(record_title.getText().toString().equals("") || record_description.getText().toString().equals("")){
            Toast.makeText(AddRecordActivity.this, "The title/description cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        TextView bodyLocationHint = findViewById(R.id.add_record_body_location_hint);
        if(bodyLocation==null){
            if(bodyLocationPopup==null || bodyLocationPopup.getBodyLocation()==null)
            {
                Toast.makeText(AddRecordActivity.this, "You didn\'t choose body location.", Toast.LENGTH_SHORT).show();
                bodyLocationHint.setTextColor(Color.RED);
                return;
            } else {
                bodyLocation = bodyLocationPopup.getBodyLocation();
            }
        }
        Date problemDate = DataController.getPatient().getProblemList().getProblem(problem_index).getTime();
        Date dateStart = new Date();

        // if the date that the user inputs is not correct, then use the default date
        SimpleDateFormat format = new SimpleDateFormat(CommonUtil.DATE_FORMAT, Locale.getDefault());
        try {
            date += "T" + time;
            dateStart = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(dateStart.after(new Date())){
            Toast.makeText(AddRecordActivity.this, "You cannot choose a future time.", Toast.LENGTH_SHORT).show();
            return;
        } else if(dateStart.before(problemDate)){
            Toast.makeText(AddRecordActivity.this, "Record time cannot be before the problem time", Toast.LENGTH_SHORT).show();
            return;
        }

        if(bitmaps.size()<2){
            Toast.makeText(AddRecordActivity.this, "You need to take at least two photos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // create a new record
        Record record = new Record(record_title.getText().toString(), dateStart, record_description.getText().toString(), bitmaps, location, bodyLocation);

        // if no network, then add the record in the offline record and wait for reconnecting
        if(!NetworkUtil.isNetworkConnected(this)){
            DataController.getRecordList().get(problem.getProblemId()).addOfflineRecord(record);
        }
        // use dataController to notify the change of record
        DataController.getRecordList().get(problem.getProblemId()).addRecord(record);

        // notification message
        Toast.makeText(AddRecordActivity.this, R.string.add_record_toast2, Toast.LENGTH_SHORT).show();
        finish();
    }

    public void viewImages(View view){
        if(bitmaps.isEmpty()){
            Toast.makeText(this, R.string.record_toast2, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, SlideShowActivity.class);
        /*
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        ArrayList<byte[]> bytesBitmaps = new ArrayList<>();
        for (Bitmap bitmap:bitmaps){
            byte[] bytesBitmap = convertBitmapToBytes(bitmap, byteArrayOutputStream);
            bytesBitmaps.add(bytesBitmap);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("image", bytesBitmaps);
        intent.putExtras(bundle);
        */
        ArrayList<Bitmap> slideShowBitmaps = new ArrayList<>();
        for(Bitmap bitmap:bitmaps.keySet()){
            slideShowBitmaps.add(bitmap);
        }
        BitmapHolder.setBitmaps(slideShowBitmaps);

        startActivityForResult(intent, REQUEST_UPDATE_DATA);
    }

    public void chooseBodyLocation(View view){
        TextView bodyLocationHint = findViewById(R.id.add_record_body_location_hint);
        bodyLocationPopup = new BodyLocationPopup(this, bodyLocationHint);
        bodyLocationPopup.chooseBodyLocation();
    }


    /*
    -----------------------------------Map and Location methods-----------------------------------
     */

    public  boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(AddRecordActivity.this);

        if (available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServicesOK : an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(AddRecordActivity.this,available,9001);
            dialog.show();
        }else{
            Toast.makeText(this, "you can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    // This method get the current address of the user
    // The address got will be displayed in the TextView record_location
    // This method will be called when the location has changed.
    @Override
    public void onLocationChanged(Location location) {
        if(location==null){
            record_location.setText("");
            return;
        }
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address_line = addresses.get(0).getAddressLine(0); // the full address is stored in the variable address_line
            record_location.setText(address_line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully" + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            Location chosenLocation = new Location("");
            chosenLocation.setLatitude(place.getLatLng().latitude);
            chosenLocation.setLongitude(place.getLatLng().longitude);

            location = chosenLocation;
            onLocationChanged(chosenLocation);

            places.release();

        }
    };

    private void initMapApi(){

        Log.d(TAG, "init: initializing");
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try{
                    startActivityForResult(builder.build(AddRecordActivity.this), PLACE_PICKER_REQUEST);
                }catch(GooglePlayServicesRepairableException e){
                    Log.d(TAG, "onClick: GooglePlayServicesRepairableException"+ e.getMessage());
                }catch (GooglePlayServicesNotAvailableException e){
                    Log.d(TAG, "onClick: GooglePlayServicesNotAvailableException"+  e.getMessage());;
                }
            }
        });
    }
}
