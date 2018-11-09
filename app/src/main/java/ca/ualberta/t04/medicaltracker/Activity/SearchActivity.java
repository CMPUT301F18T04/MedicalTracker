package ca.ualberta.t04.medicaltracker.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Adapter.SearchResultAdapter;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Patient;
import ca.ualberta.t04.medicaltracker.Problem;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Record;
import ca.ualberta.t04.medicaltracker.SearchType;

public class SearchActivity extends AppCompatActivity {

    private SearchType searchType = SearchType.Problem;
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
                ArrayList<Object[]> result = search(searchView.getQuery().toString(), searchType);
                if(result.size()==0){
                    Toast.makeText(SearchActivity.this, "No results responds.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                ListView listView = findViewById(R.id.search_result_list_view);
                SearchResultAdapter adapter = new SearchResultAdapter(SearchActivity.this, R.layout.search_result_list, result, searchType);
                listView.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private ArrayList<Object[]> search(String keyword, SearchType searchType){
        ArrayList<Object[]> result = new ArrayList<>();

        if(searchType.equals(SearchType.Problem)){
            for(Patient patient: DataController.getDoctor().getPatients()){
                for(Problem problem:patient.getProblemList().getProblems()){
                    if(problem.getTitle().contains(keyword) || problem.getDescription().contains(keyword)){
                        Object[] searchedProblem = new Object[2];
                        searchedProblem[0] = patient.getUserName();
                        searchedProblem[1] = problem;
                        result.add(searchedProblem);
                    }
                }
            }
        } else if(searchType.equals(SearchType.Record)){
            for(Patient patient: DataController.getDoctor().getPatients()){
                for(Problem problem:patient.getProblemList().getProblems()){
                    for(Record record:problem.getRecordList().getRecords()){
                        if(record.getTitle().contains(keyword) || record.getDescription().contains(keyword)){
                            Object[] searchedRecord = new Object[2];
                            searchedRecord[0] = patient.getUserName();
                            searchedRecord[1] = record;
                            result.add(searchedRecord);
                        }
                    }

                }
            }
        }
        return result;
    }
}
