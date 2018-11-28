package ca.ualberta.t04.medicaltracker.Activity;

import android.content.Intent;
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

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Activity.Doctor.DoctorRecordDetailActivity;
import ca.ualberta.t04.medicaltracker.Activity.Patient.RecordDetailActivity;
import ca.ualberta.t04.medicaltracker.Adapter.RecordAdapter;
import ca.ualberta.t04.medicaltracker.Adapter.SearchResultAdapter;
import ca.ualberta.t04.medicaltracker.BodyLocation;
import ca.ualberta.t04.medicaltracker.BodyLocationPopup;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Model.Patient;
import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Model.Record;
import ca.ualberta.t04.medicaltracker.Model.RecordList;
import ca.ualberta.t04.medicaltracker.SearchType;

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

    private SearchType searchType = SearchType.Problem;
    private SearchType locationType = SearchType.NoLocation;
    private BodyLocation bodyLocation = null;
    private int currentPage = 0;
    private TextView locationInfo;

    private ArrayList<Object[]> result = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        locationInfo = findViewById(R.id.search_location_info);

        initPage();
    }

    private void initPage(){
        initSearchSpinner();
        initLocationSpinner();

        // init the search view
        final SearchView searchView = findViewById(R.id.search_searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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

    private void initInformationTextView(final TextView textView){
        Button button = findViewById(R.id.search_button_choose_location);
        if(locationType.equals(SearchType.BodyLocation)){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initPopupWindow();
                }
            });
        }
    }

    private void initPopupWindow(){
        LayoutInflater layoutInflater = LayoutInflater.from(SearchActivity.this);
        final View promptView = layoutInflater.inflate(R.layout.body_location_popup, null);

        Button head = promptView.findViewById(R.id.body_location_head);
        Button leftArm = promptView.findViewById(R.id.body_location_left_arm);
        Button rightArm = promptView.findViewById(R.id.body_location_right_arm);
        Button torso = promptView.findViewById(R.id.body_location_torso);
        Button leftLeg = promptView.findViewById(R.id.body_location_left_leg);
        Button rightLeg = promptView.findViewById(R.id.body_location_right_leg);

        final AlertDialog ad = new AlertDialog.Builder(SearchActivity.this)
                .setView(promptView)
                .create();

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignBodyLocation(BodyLocation.Head, ad);
            }
        });

        leftArm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignBodyLocation(BodyLocation.LeftArm, ad);
            }
        });

        rightArm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignBodyLocation(BodyLocation.RightArm, ad);
            }
        });

        torso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignBodyLocation(BodyLocation.Torso, ad);
            }
        });

        leftLeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignBodyLocation(BodyLocation.LeftLeg, ad);
            }
        });

        rightLeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignBodyLocation(BodyLocation.RightLeg, ad);
            }
        });
        ad.show();
    }

    private void assignBodyLocation(BodyLocation bodyLocation, AlertDialog ad){
        this.bodyLocation = bodyLocation;
        locationInfo.setText(bodyLocation.name());
        ad.dismiss();
    }

    // init the first spinner
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

    // init the second spinner
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
                    bodyLocation = null;
                } else {
                    locationInfo.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                }
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
                    refreshRecordListView(((Problem) object).getRecordList(), objects);
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

    // Used to search problems or records for a specific keyword
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
                        ArrayList<Record> records = problem.getRecordList().getRecords();
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
                                } else if(bodyLocation==null){
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
                    ArrayList<Record> records = problem.getRecordList().getRecords();
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
                            } else if(bodyLocation==null){
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
        return result;
    }
}
