package ca.ualberta.t04.medicaltracker.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Adapter.PatientListAdapter;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Listener;
import ca.ualberta.t04.medicaltracker.Patient;
import ca.ualberta.t04.medicaltracker.R;

public class DoctorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        initPage();
    }

    private void initPage(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.doctor_page_title));
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ListView patientListView = findViewById(R.id.main_page_list_view);
        final ArrayList<Patient> patients = DataController.getDoctor().getPatients();
        final PatientListAdapter adapter = new PatientListAdapter(this, R.layout.patient_list, patients);
        patientListView.setAdapter(adapter);
        DataController.getDoctor().addListener("UpdateListView", new Listener() {
            @Override
            public void update() {
                patients.clear();
                patients.addAll(DataController.getDoctor().getPatients());
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void onStart()
    {
        super.onStart();

        // Update the userName that is in the NavigationView
        if(DataController.getUser()!=null)
        {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View headerView = navigationView.getHeaderView(0);

            final TextView userDisplayName = headerView.findViewById(R.id.nav_bar_username);

            TextView userRole = headerView.findViewById(R.id.nav_bar_role);
            if(DataController.getUser().isDoctor())
                userRole.setText("Doctor");
            else
                userRole.setText("Patient");

            userDisplayName.setText(DataController.getUser().getName());

            DataController.getUser().addListener("SaveData", new Listener() {
                @Override
                public void update() {
                    userDisplayName.setText(DataController.getUser().getName());
                    ElasticSearchController.updateUser(DataController.getUser());
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(DoctorActivity.this, AddPatientActivity.class);
            startActivity(intent);
            return true;
        } else if(id == R.id.action_search){
            Intent intent = new Intent(DoctorActivity.this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile)
        {
            Intent intent = new Intent(DoctorActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(DoctorActivity.this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            DataController.setUser(null);
            Intent intent = new Intent(DoctorActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if(id == R.id.nav_about) {
            Intent intent = new Intent(DoctorActivity.this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
