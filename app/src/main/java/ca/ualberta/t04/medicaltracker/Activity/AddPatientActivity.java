package ca.ualberta.t04.medicaltracker.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Adapter.SearchPatientAdapter;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Patient;
import ca.ualberta.t04.medicaltracker.R;

public class AddPatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        getSupportActionBar().setTitle(getString(R.string.add_patient_title));
    }

    // Used to search patients
    public void searchPatient(View view){
        EditText editText = findViewById(R.id.add_patient_username);
        if(editText.getText().toString().equals("")){
            Toast.makeText(AddPatientActivity.this, R.string.add_patient_toast1, Toast.LENGTH_SHORT).show();
            return;
        }
        String userName = editText.getText().toString();

        ArrayList<Patient> patients = removeExistedPatient(ElasticSearchController.fuzzySearchPatient(userName));

        if(patients.size()==0){
            Toast.makeText(AddPatientActivity.this, R.string.add_patient_toast2, Toast.LENGTH_SHORT).show();
            return;
        }
        ListView patient_list = findViewById(R.id.add_patient_list_view);
        SearchPatientAdapter adapter = new SearchPatientAdapter(this, R.layout.search_patient_list, patients);
        patient_list.setAdapter(adapter);
    }

    private ArrayList<Patient> removeExistedPatient(ArrayList<Patient> patients){
        ArrayList<Patient> result = new ArrayList<>();
        for(Patient patient:patients){
            if(!DataController.getDoctor().getPatientsUserNames().contains(patient.getUserName())){
                result.add(patient);
            }
        }
        return result;
    }
}
