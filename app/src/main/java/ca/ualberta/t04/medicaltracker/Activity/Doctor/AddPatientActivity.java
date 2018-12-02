package ca.ualberta.t04.medicaltracker.Activity.Doctor;

import android.graphics.drawable.ColorDrawable;
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
import ca.ualberta.t04.medicaltracker.Model.Patient;
import ca.ualberta.t04.medicaltracker.R;


/**
 * This class adds a new patient for a doctor user
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */

/*
  This activity is for adding a patient for a doctor user
 */

public class AddPatientActivity extends AppCompatActivity {

    /**
     * onCreate
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        getSupportActionBar().setTitle(getString(R.string.add_patient_title));
    }

    /**
     * Used to search patients
     * @param view View
     */
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

    /**
     * Removes existing patient
     * @param patients ArrayList
     * @return result
     */
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
