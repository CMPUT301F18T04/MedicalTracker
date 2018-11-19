package ca.ualberta.t04.medicaltracker.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Adapter.DoctorListAdapter;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Doctor;
import ca.ualberta.t04.medicaltracker.Listener;
import ca.ualberta.t04.medicaltracker.R;

public class DoctorViewActivity extends AppCompatActivity {
    public ListView doctorListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doctor_view);
        doctorListView = (ListView)findViewById(R.id.doctor_listview);

        initPage();
    }


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
