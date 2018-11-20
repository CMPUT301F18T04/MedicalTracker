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
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Record;
import ca.ualberta.t04.medicaltracker.Util;

public class AddRecordActivity extends AppCompatActivity implements LocationListener {

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
        recordSetDate();
        recordSetTime();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(location);
        // Get the index of the problem list
        problem_index = getIntent().getIntExtra("index", -1);
        if(problem_index==-1){
            Toast.makeText(AddRecordActivity.this, "An error occurs.", Toast.LENGTH_SHORT).show();
        }
    }

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

        recordDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = year + "-" + month + "-" + day;
                record_date.setText(date);
            }
        };
    }

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
        recordTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String time = hour + ":" + minute;
                record_time.setText(time);
            }
        };
    }

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap image_bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(image_bitmap);
        }
    }

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
            String address_line = addresses.get(0).getAddressLine(0);
            record_location.setText(address_line);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    // Used to add a record for a problem
    public void addRecord(View view){
        EditText record_title = findViewById(R.id.add_record_title);
        EditText record_description = findViewById(R.id.add_record_description);

        if(record_title.getText().toString().equals("") || record_description.getText().toString().equals("")){
            Toast.makeText(AddRecordActivity.this, "The title/description cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        Date dateStart = new Date();

        SimpleDateFormat format = new SimpleDateFormat(Util.TIME_FORMAT, Locale.getDefault());
        try {
            String tempTime = record_date.getText().toString()+"T"+record_time.getText().toString();
            dateStart = format.parse(tempTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // create a new record
        Record record = new Record(record_title.getText().toString(), dateStart, record_description.getText().toString(), null, null);

        DataController.getPatient().getProblemList().getProblem(problem_index).getRecordList().addRecord(record);

        Toast.makeText(AddRecordActivity.this, "Added a new record.", Toast.LENGTH_SHORT).show();
        finish();
    }

}
