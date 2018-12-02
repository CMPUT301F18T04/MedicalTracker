package ca.ualberta.t04.medicaltracker.Activity.Patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Activity.InformationActivity;
import ca.ualberta.t04.medicaltracker.Adapter.DoctorListAdapter;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Model.Doctor;
import ca.ualberta.t04.medicaltracker.Listener;
import ca.ualberta.t04.medicaltracker.R;

/**
 * This activity displays all doctors of a certain patient
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */

/*This activity displays all doctors of a certain patient.*/

public class DoctorViewActivity extends AppCompatActivity {
    public ListView doctorListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doctor_view);
        doctorListView = (ListView)findViewById(R.id.doctor_listview);

        initPage();
    }

    @Override
    protected void onStart(){
        super.onStart();

        doctorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DoctorViewActivity.this, InformationActivity.class);
                intent.putExtra("username", DataController.getPatient().getDoctors().get(i).getUserName());
                startActivity(intent);
            }
        });
    }

    /**
     * Set the adapter for doctors list view and add listener.
     */
    private void initPage(){

        TextView doctorViewTitle = findViewById(R.id.doctor_view_title);
        doctorViewTitle.setText(getString(R.string.doctor_view_page_title));
        final ArrayList<Doctor> doctors = DataController.getPatient().getDoctors();
        final DoctorListAdapter adapter = new DoctorListAdapter(this, R.layout.doctor_list, doctors);
        doctorListView.setAdapter(adapter);
        DataController.getPatient().addListener("UpdateListView", new Listener() {
            @Override
            public void update() {
                doctors.clear();
                doctors.addAll(DataController.getPatient().getDoctors());
                adapter.notifyDataSetChanged();
            }
        });
    }
}
