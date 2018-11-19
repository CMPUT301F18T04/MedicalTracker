package ca.ualberta.t04.medicaltracker.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Adapter.PatientListAdapter;
import ca.ualberta.t04.medicaltracker.Adapter.ProblemAdapter;
import ca.ualberta.t04.medicaltracker.Adapter.RecordAdapter;
import ca.ualberta.t04.medicaltracker.Adapter.SearchResultAdapter;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Listener;
import ca.ualberta.t04.medicaltracker.Patient;
import ca.ualberta.t04.medicaltracker.Problem;
import ca.ualberta.t04.medicaltracker.ProblemList;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Record;
import ca.ualberta.t04.medicaltracker.RecordList;
import ca.ualberta.t04.medicaltracker.SearchType;

public class SearchActivity extends AppCompatActivity {

    private SearchType searchType = SearchType.Problem;
    private int currentPage = 0;

    private ArrayList<Object[]> result = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initPage();
    }

    private void initPage(){
        // init the spinner
        final String[] types = new String[]{SearchType.Problem.name(), SearchType.Record.name()};

        Spinner spinner = findViewById(R.id.search_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchType = SearchType.valueOf(types[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // init the search view
        final SearchView searchView = findViewById(R.id.search_searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Object[] -> (userName, Problem/Record)
                result = search(searchView.getQuery().toString(), searchType);
                if(result.size()==0){
                    Toast.makeText(SearchActivity.this, "No results responds.", Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(SearchActivity.this, DoctorRecordDetailActivity.class);
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
                    Intent intent = new Intent(SearchActivity.this, DoctorRecordDetailActivity.class);
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
      
        if (DataController.getUser().isDoctor() == true) {
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
        }else if (DataController.getUser().isDoctor() == false) {
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
        return result;
    }
}
