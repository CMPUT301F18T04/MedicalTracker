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
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Adapter.PatientListAdapter;
import ca.ualberta.t04.medicaltracker.Adapter.ProblemAdapter;
import ca.ualberta.t04.medicaltracker.Adapter.RecordAdapter;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Listener;
import ca.ualberta.t04.medicaltracker.Patient;
import ca.ualberta.t04.medicaltracker.Problem;
import ca.ualberta.t04.medicaltracker.ProblemList;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Record;
import ca.ualberta.t04.medicaltracker.RecordList;

public class DoctorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public ArrayList<Patient> patients;
    public PatientListAdapter adapter;

    private int currentPage = 0;
    private Patient currentPatient = null;
    private int problemIndex = -1;
    private int patientIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        ListView patientListView = findViewById(R.id.main_page_list_view);
        ArrayList<Patient> patients = DataController.getDoctor().getPatients();
        PatientListAdapter adapter = new PatientListAdapter(this, R.layout.patient_list, patients);
        patientListView.setAdapter(adapter);

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
                refreshPatientListView();
                Snackbar.make(view, "Refresh completed", Snackbar.LENGTH_LONG)
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
        refreshPatientListView();
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

            userRole.setText(getText(R.string.nav_header_subtitle_doctor));

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
        } else if(currentPage>0){
            if(currentPage==1){
                refreshPatientListView();
            } else if(currentPage==2){
                refreshProblemListView(currentPatient.getProblemList());
            }
            currentPage --;
        } else {
            super.onBackPressed();
        }
    }

    private void refreshPatientListView(){
        ListView listView = findViewById(R.id.main_page_list_view);
        final ArrayList<Patient> patients = DataController.getDoctor().getPatients();
        final PatientListAdapter adapter = new PatientListAdapter(this, R.layout.patient_list, patients);
        listView.setAdapter(adapter);
        DataController.getDoctor().removeListener("UpdateListView");
        DataController.getDoctor().addListener("UpdateListView", new Listener() {
            @Override
            public void update() {
                patients.clear();
                patients.addAll(DataController.getDoctor().getPatients());
                adapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPatient = patients.get(position);
                refreshProblemListView(currentPatient.getProblemList());
                currentPage ++;
                patientIndex = position;
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentPage==0){
                    Patient patient = patients.get(position);
                    initMenu(view, patient);
                }
                return false;
            }
        });
    }

    private void initMenu(View view, final Patient patient){
        PopupMenu popupMenu = new PopupMenu(DoctorActivity.this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.doctor_page_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.doctor_page_menu_detail){
                    Intent intent = new Intent(DoctorActivity.this, InformationActivity.class);
                    intent.putExtra("username", patient.getUserName());
                    startActivity(intent);
                } else if(item.getItemId()==R.id.doctor_page_menu_delete){
                    DataController.getDoctor().removePatient(patient);
                    Toast.makeText(DoctorActivity.this, R.string.doctor_page_toast, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        popupMenu.show();
    }

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
    }

    private void refreshRecordListView(RecordList recordList){
        ListView listView = findViewById(R.id.main_page_list_view);
        final ArrayList<Record> records = recordList.getRecords();
        final RecordAdapter adapter = new RecordAdapter(this, R.layout.record_list, records);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    Intent intent = new Intent(DoctorActivity.this, DoctorRecordDetailActivity.class);
                    intent.putExtra("problem_index", problemIndex);
                    intent.putExtra("patient_index", patientIndex);
                    intent.putExtra("record_index", position);
                    startActivity(intent);
                } catch (NullPointerException e){
                    Log.d("Error", "Problem/record/patient does not exist!");
                }
            }
        });
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
