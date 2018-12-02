package ca.ualberta.t04.medicaltracker.Activity;


import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Activity.Doctor.DoctorRecordDetailActivity;
import ca.ualberta.t04.medicaltracker.Activity.Patient.RecordDetailActivity;
import ca.ualberta.t04.medicaltracker.Adapter.RecordAdapter;
import ca.ualberta.t04.medicaltracker.Adapter.SearchResultAdapter;
import ca.ualberta.t04.medicaltracker.BodyLocation;
import ca.ualberta.t04.medicaltracker.BodyLocationPopup;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Model.Patient;
import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.Model.ProblemList;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Model.Record;
import ca.ualberta.t04.medicaltracker.Model.RecordList;
import ca.ualberta.t04.medicaltracker.SearchType;
import ca.ualberta.t04.medicaltracker.Util.CommonUtil;

import static com.google.maps.android.SphericalUtil.computeDistanceBetween;
import static java.lang.Double.NaN;

/**
 * This activity is for searching based on a keyword
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */

/*
  This activity is for searching based on a keyword
 */

public class SearchActivity extends AppCompatActivity {

    private final static int REQUESTCODE = 9090;

    private SearchType searchType = SearchType.Problem;
    private SearchType locationType = SearchType.NoLocation;
    private BodyLocation bodyLocation = null;
    private int currentPage = 0;
    private TextView locationInfo;
    protected BodyLocationPopup popup;
    private Location mLocation;

    private ArrayList<Object[]> result = new ArrayList<>();

    /**
     * onCreate
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        locationInfo = findViewById(R.id.search_location_info);

        double latitude = getIntent().getDoubleExtra("search_latitude", -1);
        initPage();
    }

    /**
     * Initialize the page
     */
    private void initPage(){
        initSearchSpinner();
        initLocationSpinner();

        // init the search view
        final SearchView searchView = findViewById(R.id.search_searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(locationType.equals(SearchType.BodyLocation)) {
                    if (popup == null || popup.getBodyLocation() == null) {
                        Toast.makeText(SearchActivity.this, R.string.search_toast2, Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        bodyLocation = popup.getBodyLocation();
                    }
                } else if(locationType.equals(SearchType.GeoLocation)){
                    if(mLocation==null){
                        Toast.makeText(SearchActivity.this, "You didn\'t choose a geo-location", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }

                // Object[] -> (userName, Problem/Record)
                result = search(searchView.getQuery().toString(), searchType);

                if(result.size()==0){
                    Toast.makeText(SearchActivity.this, R.string.search_toast1, Toast.LENGTH_SHORT).show();
                    return false;
                }
                refreshSearchResultListView(result);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    /**
     * Initialize the text view
     * @param textView TextView
     */
    private void initInformationTextView(final TextView textView){
        Button button = findViewById(R.id.search_button_choose_location);
        if(locationType.equals(SearchType.BodyLocation)){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //initPopupWindow();
                    popup = new BodyLocationPopup(SearchActivity.this, textView);
                    popup.chooseBodyLocation();
                }
            });
        }else if(locationType.equals(SearchType.GeoLocation)){
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(SearchActivity.this, SearchLocationMapActivity.class);
                    startActivityForResult(intent, REQUESTCODE);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode==REQUESTCODE && resultCode == RESULT_OK){
            Double lat = data.getDoubleExtra("search_latitude", NaN);
            Double lon = data.getDoubleExtra("search_longitude", NaN);
            if (lat.equals(NaN) || lon.equals(NaN)){
                locationInfo.setText("No location selected");
                locationInfo.setTextColor(Color.RED);
                return;
            }
            mLocation = new Location("");
            mLocation.setLatitude(lat);
            mLocation.setLongitude(lon);
            locationInfo.setText("Latitude: " + String.valueOf(lat) + ", Longitude: " + lon);
            locationInfo.setTextColor(Color.BLACK);
        }
    }


    /**
     * init the first spinner
     */
    private void initSearchSpinner(){
        final String[] types = new String[]{SearchType.Problem.name(), SearchType.Record.name()};

        Spinner spinner = findViewById(R.id.search_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchType = SearchType.valueOf(types[position]);
                if(searchType.equals(SearchType.Problem)){
                    Spinner locationSpinner = findViewById(R.id.search_spinner2);
                    locationSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * init the second spinner
     */
    private void initLocationSpinner(){
        final String[] locationTypes = new String[]{SearchType.NoLocation.name(), SearchType.GeoLocation.name(), SearchType.BodyLocation.name()};
        Spinner locationSpinner = findViewById(R.id.search_spinner2);
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locationTypes);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        locationSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationType = SearchType.valueOf(locationTypes[position]);

                Button button = findViewById(R.id.search_button_choose_location);
                if(!locationType.equals(SearchType.NoLocation)){
                    locationInfo.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                    locationInfo.setText("");
                    initInformationTextView(locationInfo);
                    Spinner searchSpinner = findViewById(R.id.search_spinner);
                    searchSpinner.setSelection(1);
                } else {
                    locationInfo.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                }
                bodyLocation = null;
                mLocation = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(currentPage>0){
            if(currentPage==1){
                refreshSearchResultListView(result);
            }
            currentPage --;
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Refreshes the search results
     * @param result ArrayList
     */
    private void refreshSearchResultListView(final ArrayList<Object[]> result){
        ListView listView = findViewById(R.id.search_result_list_view);
        SearchResultAdapter adapter = new SearchResultAdapter(SearchActivity.this, R.layout.search_result_list, result, searchType);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object[] objects = result.get(position);
                Object object = objects[1];
                if(object instanceof Problem){
                    Problem problem = (Problem) object;
                    DataController.addRecordList(problem.getProblemId(), problem.getRecordList());
                    refreshRecordListView(DataController.getRecordList().get(problem.getProblemId()), objects);
                } else if (object instanceof Record){
                    Intent intent;
                    if(DataController.getUser().isDoctor())
                        intent = new Intent(SearchActivity.this, DoctorRecordDetailActivity.class);
                    else
                        intent = new Intent(SearchActivity.this, RecordDetailActivity.class);
                    intent.putExtra("problem_index", (Integer) objects[3]);
                    intent.putExtra("patient_index", (Integer) objects[2]);
                    intent.putExtra("record_index", (Integer) objects[4]);
                    startActivity(intent);
                }
            }
        });
    }
    /*
    private void refreshProblemListView(ProblemList problemList){
        ListView listView = findViewById(R.id.main_page_list_view);
        final ArrayList<Problem> problems = problemList.getProblems();
        final ProblemAdapter adapter = new ProblemAdapter(this, R.layout.problem_list, problems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                refreshRecordListView(problems.get(position).getRecordList());
                currentPage ++;
                problemIndex = position;
            }
        });
        unregisterForContextMenu(listView);
    }
    */

    /**
     * Refreshes the list view for records
     * @param recordList RecordList
     * @param data Object[]
     */
    private void refreshRecordListView(final RecordList recordList, final Object[] data){
        ListView listView = findViewById(R.id.search_result_list_view);
        final ArrayList<Record> records = recordList.getRecords();
        final RecordAdapter adapter = new RecordAdapter(this, R.layout.record_list, records);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    Intent intent;
                    if(DataController.getUser().isDoctor())
                        intent = new Intent(SearchActivity.this, DoctorRecordDetailActivity.class);
                    else
                        intent = new Intent(SearchActivity.this, RecordDetailActivity.class);
                    intent.putExtra("problem_index", (Integer) data[3]);
                    intent.putExtra("patient_index", (Integer) data[2]);
                    intent.putExtra("record_index", position);
                    startActivity(intent);
                } catch (NullPointerException e){
                    Log.d("Error", "Problem/record/patient does not exist!");
                }
            }
        });
        currentPage ++;
    }

    /**
     * Used to search problems or records for a specific keyword
     * @param keyword String
     * @param searchType SearchType
     * @return result ArrayList
     */
    private ArrayList<Object[]> search(String keyword, SearchType searchType){
        ArrayList<Object[]> result = new ArrayList<>();
      
        if (DataController.getUser().isDoctor()) {
            ArrayList<Patient> patients = DataController.getDoctor().getPatients();
            if (searchType.equals(SearchType.Problem)) {
                for (Patient patient : patients) {
                    ArrayList<Problem> problems = patient.getProblemList().getProblems();
                    for (Problem problem : problems) {
                        if (problem.getTitle().contains(keyword) || problem.getDescription().contains(keyword)) {
                            Object[] searchedProblem = new Object[4];
                            searchedProblem[0] = patient.getUserName();
                            searchedProblem[1] = problem;
                            searchedProblem[2] = patients.indexOf(patient);
                            searchedProblem[3] = problems.indexOf(problem);
                            result.add(searchedProblem);
                        }
                    }
                }
            } else if (searchType.equals(SearchType.Record)) {
                for (Patient patient : patients) {
                    ArrayList<Problem> problems = patient.getProblemList().getProblems();
                    for (Problem problem : problems) {
                        DataController.addRecordList(problem.getProblemId(), problem.getRecordList());
                        RecordList recordList = DataController.getRecordList().get(problem.getProblemId());
                        ArrayList<Record> records = recordList.getRecords();
                        for (Record record : records) {
                            if (record.getTitle().contains(keyword) || record.getDescription().contains(keyword)) {
                                if(bodyLocation!=null && record.getBodyLocation().equals(bodyLocation)){
                                    Object[] searchedRecord = new Object[5];
                                    searchedRecord[0] = patient.getUserName();
                                    searchedRecord[1] = record;
                                    searchedRecord[2] = patients.indexOf(patient);
                                    searchedRecord[3] = problems.indexOf(problem);
                                    searchedRecord[4] = records.indexOf(record);
                                    result.add(searchedRecord);
                                } else if(bodyLocation==null && mLocation==null){
                                    Object[] searchedRecord = new Object[5];
                                    searchedRecord[0] = patient.getUserName();
                                    searchedRecord[1] = record;
                                    searchedRecord[2] = patients.indexOf(patient);
                                    searchedRecord[3] = problems.indexOf(problem);
                                    searchedRecord[4] = records.indexOf(record);
                                    result.add(searchedRecord);
                                } else if(mLocation!=null){
                                    if(record.getLocation()==null){
                                        continue;
                                    }
                                    LatLng searchedLocation = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                                    LatLng recordLocation = new LatLng(record.getLocation().getLatitude(), record.getLocation().getLongitude());
                                    if(computeDistanceBetween(searchedLocation, recordLocation)<= CommonUtil.LOCATION_DISTANCE){
                                        Object[] searchedRecord = new Object[5];
                                        searchedRecord[0] = patient.getUserName();
                                        searchedRecord[1] = record;
                                        searchedRecord[2] = patients.indexOf(patient);
                                        searchedRecord[3] = problems.indexOf(problem);
                                        searchedRecord[4] = records.indexOf(record);
                                        result.add(searchedRecord);
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }else {
            Patient patient = DataController.getPatient();
            if (searchType.equals(SearchType.Problem)) {
                ArrayList<Problem> problems = patient.getProblemList().getProblems();
                for (Problem problem : problems) {
                    if (problem.getTitle().contains(keyword) || problem.getDescription().contains(keyword)) {
                        Object[] searchedProblem = new Object[4];
                        searchedProblem[0] = patient.getUserName();
                        searchedProblem[1] = problem;
                        searchedProblem[2] = -1;
                        searchedProblem[3] = problems.indexOf(problem);
                        result.add(searchedProblem);
                    }
                }
            } else if(searchType.equals(SearchType.Record)){
                ArrayList<Problem> problems = patient.getProblemList().getProblems();
                for (Problem problem : problems) {
                    DataController.addRecordList(problem.getProblemId(), problem.getRecordList());
                    RecordList recordList = DataController.getRecordList().get(problem.getProblemId());
                    ArrayList<Record> records = recordList.getRecords();
                    for (Record record : records) {
                        if (record.getTitle().contains(keyword) || record.getDescription().contains(keyword)) {
                            if(bodyLocation!=null && record.getBodyLocation().equals(bodyLocation)){
                                Object[] searchedRecord = new Object[5];
                                searchedRecord[0] = patient.getUserName();
                                searchedRecord[1] = record;
                                searchedRecord[2] = -1;
                                searchedRecord[3] = problems.indexOf(problem);
                                searchedRecord[4] = records.indexOf(record);
                                result.add(searchedRecord);
                            } else if(bodyLocation==null && mLocation==null){
                                Object[] searchedRecord = new Object[5];
                                searchedRecord[0] = patient.getUserName();
                                searchedRecord[1] = record;
                                searchedRecord[2] = -1;
                                searchedRecord[3] = problems.indexOf(problem);
                                searchedRecord[4] = records.indexOf(record);
                                result.add(searchedRecord);
                            } else if(mLocation!=null){
                                if(record.getLocation()==null){
                                    continue;
                                }
                                LatLng searchedLocation = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                                LatLng recordLocation = new LatLng(record.getLocation().getLatitude(), record.getLocation().getLongitude());
                                if(computeDistanceBetween(searchedLocation, recordLocation)<= CommonUtil.LOCATION_DISTANCE){
                                    Object[] searchedRecord = new Object[5];
                                    searchedRecord[0] = patient.getUserName();
                                    searchedRecord[1] = record;
                                    searchedRecord[2] = -1;
                                    searchedRecord[3] = problems.indexOf(problem);
                                    searchedRecord[4] = records.indexOf(record);
                                    result.add(searchedRecord);
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
