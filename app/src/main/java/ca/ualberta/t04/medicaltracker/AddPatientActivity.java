package ca.ualberta.t04.medicaltracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Adapter.PatientAdapter;

public class AddPatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        getSupportActionBar().setTitle(getString(R.string.add_patient_title));
    }

    public void searchPatient(View view){
        EditText editText = findViewById(R.id.add_patient_username);
        if(editText.getText().toString().equals("")){
            Toast.makeText(AddPatientActivity.this, "The patient's name cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        String userName = editText.getText().toString();

        ArrayList<Patient> patients = ElasticSearchController.fuzzySearchPatient(userName);

        ListView patient_list = findViewById(R.id.add_patient_list_view);
        PatientAdapter adapter = new PatientAdapter(this, R.layout.search_patient_list, patients);
        patient_list.setAdapter(adapter);
    }
}
