package ca.ualberta.t04.medicaltracker.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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

import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Record;
import ca.ualberta.t04.medicaltracker.Util;

/*
  This activity is for adding a record to a problem for a patient user
 */

// This class has the layout of activity_add_record.xml
// This class is used for adding a new record
// This class implements LocationListener which is used for get the current location
public class AddRecordActivity extends AppCompatActivity implements LocationListener {

    // initialize
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int problem_index;
    private DatePickerDialog.OnDateSetListener recordDateSetListener;
    private TimePickerDialog.OnTimeSetListener recordTimeSetListener;
    private TextView record_date;
    private TextView record_time;
    private EditText record_location;
    private ImageView imageView;
    private LocationManager locationManager;
    private Geocoder geocoder;
    private List<Address> addresses;
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        record_date = findViewById(R.id.add_record_date);
        record_time = findViewById(R.id.add_record_time);
        record_location = findViewById(R.id.add_record_location);
        ImageButton image_button = findViewById(R.id.imageButton);
        imageView = findViewById(R.id.add_record_photo_display);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        recordSetDate(); // call recordSetDate
        recordSetTime(); // call recordSetTime

        // ask permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        } else{
            // get the current location
            Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            onLocationChanged(location); // call onLocationChanged
        }
        // Get the index of the problem list
        problem_index = getIntent().getIntExtra("index", -1);

        if(problem_index==-1){
            Toast.makeText(AddRecordActivity.this, R.string.add_record_toast, Toast.LENGTH_SHORT).show();
        }
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
                String date = year + "-" + month + "-" + day;
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
                String time = hour + ":" + minute;
                record_time.setText(time);
            }
        };
    }

    // Method dispatchTakePictureIntent starts the activity of launch the camera of the phone
    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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
            Bitmap image_bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(image_bitmap);
            bitmaps.add(image_bitmap);
        }
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

        Date dateStart = new Date();

        // if the date that the user inputs is not correct, then use the default date
        SimpleDateFormat format = new SimpleDateFormat(Util.TIME_FORMAT, Locale.getDefault());
        try {
            String tempTime = record_date.getText().toString()+"T"+record_time.getText().toString();
            dateStart = format.parse(tempTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // create a new record
        Record record = new Record(record_title.getText().toString(), dateStart, record_description.getText().toString(), null, null);

        // use dataController to notify the change of record
        DataController.getPatient().getProblemList().getProblem(problem_index).getRecordList().addRecord(record);

        // notification message
        Toast.makeText(AddRecordActivity.this, R.string.add_record_toast2, Toast.LENGTH_SHORT).show();
        finish();
    }

    public void viewImages(View view){
        if(bitmaps.isEmpty()){
            Toast.makeText(this, "No photos!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, SlideShowActivity.class);
        intent.putExtra("image", new Gson().toJson(bitmaps));
        startActivity(intent);
    }
}
