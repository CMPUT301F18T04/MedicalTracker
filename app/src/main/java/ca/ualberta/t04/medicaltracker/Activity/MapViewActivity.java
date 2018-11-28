package ca.ualberta.t04.medicaltracker.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.Model.Record;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Activity.Doctor.DoctorRecordDetailActivity;
import ca.ualberta.t04.medicaltracker.Activity.Patient.RecordDetailActivity;


public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener {
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1111;
    private static final String TAG = "MapViewActivity";
    private static final float DEFAULT_ZOOM = 15f;

    //widgets
    private GoogleMap mMap;
    private ImageView mGps, mAllLocations;

    //vars
    private boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLocation;
    private ArrayList<Location> locations  = new ArrayList<>();
    private int patientIndex;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map_view);

        Intent mIntent = getIntent();
        final int problemIndex = mIntent.getIntExtra("problem_index", -1);
        final int recordIndex = mIntent.getIntExtra("record_index",-1);
        patientIndex = getIntent().getIntExtra("patient_index", -1);

        if (DataController.getUser().isDoctor()){
            mLocation = DataController.getDoctor()
                    .getPatients().get(patientIndex)
                    .getProblemList().getProblem(problemIndex)
                    .getRecordList().getRecord(recordIndex)
                    .getLocation();
        }else{
            mLocation = DataController.getPatient()
                    .getProblemList().getProblem(problemIndex)
                    .getRecordList().getRecord(recordIndex)
                    .getLocation();
        }
        mGps = (ImageView)findViewById(R.id.my_location);
        mAllLocations = (ImageView)findViewById(R.id.all_locations);

        getLocationPermission();
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission");
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this, permissions ,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this, permissions ,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void init(){
        Log.d(TAG,"init: initializing");

        //mMap.clear();
        moveCamera(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), DEFAULT_ZOOM, "Record Location");

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });

        mAllLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocations();
            }
        });
    }

    private void getLocations(){
        if (DataController.getUser().isDoctor()){
            ArrayList<Problem> problems = DataController.getDoctor().getPatients().get(patientIndex).getProblemList().getProblems();
            for (Problem problem: problems){
                ArrayList<Record> records = problem.getRecordList().getRecords();
                for (Record record: records){
                    if(record.getLocation() != null){
                        locations.add(record.getLocation());
                    }
                }
            }
        }else {
            ArrayList<Problem> problems = DataController.getPatient().getProblemList().getProblems();
            for (Problem problem: problems){
                ArrayList<Record> records = problem.getRecordList().getRecords();
                for (Record record: records){
                    if(record.getLocation() != null){
                        locations.add(record.getLocation());
                    }
                }
            }
        }

        for (Location location: locations){
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng));
        }
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the device current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM,"My Location");
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapViewActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void initMap() {
        Log.d(TAG, "initMap");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapViewActivity.this);
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to : lat" + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if (!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(options);
        }else{
            mMap.addMarker(new MarkerOptions().position(latLng));
        }
    }

}
