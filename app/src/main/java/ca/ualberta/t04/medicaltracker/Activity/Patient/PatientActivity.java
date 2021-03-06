package ca.ualberta.t04.medicaltracker.Activity.Patient;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Activity.AboutActivity;
import ca.ualberta.t04.medicaltracker.Activity.Doctor.ScanActivity;
import ca.ualberta.t04.medicaltracker.Activity.LoginActivity;
import ca.ualberta.t04.medicaltracker.Activity.ProfileActivity;
import ca.ualberta.t04.medicaltracker.Activity.SearchActivity;
import ca.ualberta.t04.medicaltracker.Activity.SettingActivity;
import ca.ualberta.t04.medicaltracker.Adapter.ProblemAdapter;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Listener;
import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.Model.Record;
import ca.ualberta.t04.medicaltracker.Model.RecordList;
import ca.ualberta.t04.medicaltracker.QRCodePopup;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Util.NetworkUtil;
import ca.ualberta.t04.medicaltracker.Util.QRCodeUtil;

/**
 * This activity is for the main page of a patient user
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */

/*
  This activity is for the main page of a patient user
 */

// This class has the layout of activity_patient.xml
// This class contains the problem list
public class PatientActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean offline = false;
    private BroadcastReceiver connectionReceiver;

    /**
     * onCreate
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.patient_page_title);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20B2AA")));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initProblemListView(this);

        connectionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(NetworkUtil.isNetworkConnected(context) && offline){
                    ElasticSearchController.updateUser(DataController.getPatient());
                    for(Problem problem:DataController.getPatient().getProblemList().getProblems()){
                        RecordList recordList = problem.getRecordList();
                        ArrayList<Record> records = recordList.getOfflineRecords();
                        for(Record record:records){
                            ElasticSearchController.createRecord(record);
                        }
                    }
                    Toast.makeText(context, R.string.patient_toast1, Toast.LENGTH_SHORT).show();
                    offline = false;
                } else if(!NetworkUtil.isNetworkConnected(context)){
                    offline = true;
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, intentFilter);

    }

    /**
     * Init list view of patients
     */
    // Init list view of patients
    private void initProblemListView(final Activity activity){
        ListView listView = activity.findViewById(R.id.main_page_list_view);
        final ArrayList<Problem> problems = DataController.getPatient().getProblemList().getProblems();
        final ProblemAdapter adapter = new ProblemAdapter(activity, R.layout.problem_list, problems);
        listView.setAdapter(adapter);
        activity.registerForContextMenu(listView);

        // when you click one of the element in the listView, another activity will come up
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, RecordHistoryActivity.class);

                intent.putExtra("index", position);
                activity.startActivity(intent);
            }
        });

        // notify the change
        DataController.getPatient().getProblemList().addListener("ProblemListener1", new Listener() {
            @Override
            public void update() {
                adapter.notifyDataSetChanged();
                ElasticSearchController.updateUser(DataController.getPatient());
            }
        });
    }



    // Method onStart
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

            userRole.setText(getText(R.string.nav_header_subtitle_patient));

            userDisplayName.setText(DataController.getUser().getName());

            ImageView icon = headerView.findViewById(R.id.nav_bar_icon);

            icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.patient));

            DataController.getUser().addListener("PatientListener1", new Listener() {
                @Override
                public void update() {
                    userDisplayName.setText(DataController.getUser().getName());
                    ElasticSearchController.updateUser(DataController.getUser());
                }
            });

            ArrayList<String> notifyDoctors = DataController.getPatient().getNotifyDoctors();
            if(notifyDoctors!=null && notifyDoctors.size()>0){
                String doctors = "";
                for(int i=0; i<notifyDoctors.size(); i++){
                    String doctorUserName = notifyDoctors.get(i);
                    doctors += doctorUserName;
                    if(i!=notifyDoctors.size()-1){
                        doctors += ", ";
                    }
                }
                AlertDialog ad = new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.patient_extra_toast2) + doctors + getString(R.string.patient_extra_toast1))
                        .setPositiveButton(android.R.string.ok, null)
                        .create();
                ad.show();
                DataController.getPatient().clearNotifyDoctors();
            }
        }
    }

    // Method onBackPressed
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
        menu.removeItem(R.id.action_album);
        return true;
    }

    // Method onOptionsItemSelected
    // when add button is clicked, AddProblemActivity will come up
    // when search button is clicked, SearchActivity will come up
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) { // add button is clicked
            Intent intent = new Intent(PatientActivity.this, AddProblemActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_search){ // search button is clicked
            Intent intent = new Intent(PatientActivity.this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Method onCreateContextMenu
    // get connected with the problem_long_click_selection menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.problem_long_click_selection, menu);
    }

    // Method onContextItemSelected
    // There are two options for user when user long click one of the problems
    // Two options: edit / delete
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int index = info.position;
        int id = item.getItemId();

        if (id == R.id.option_edit){ // when edit is clicked, a notification is pop up
            // still need to fill out this part
            // start another activity of editing problem
            Toast.makeText(this, R.string.patient_toast_edit, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PatientActivity.this, EditProblemActivity.class);
            intent.putExtra("problem_index", index);
            startActivity(intent);

        } else if (id == R.id.option_delete){ // when delete is clicked, an alert dialog is pop up
            AlertDialog.Builder a_builder = new AlertDialog.Builder(PatientActivity.this);
            a_builder.setMessage(getString(R.string.patient_extra_toast3)).setCancelable(false) // ask you if you want to delete the problem you just clicked
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() { // you can choose YES to delete the problem
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { // notify the DataController that a problem is deleted
                            Problem problem = DataController.getPatient().getProblemList().getProblem(index);
                            DataController.getPatient().getProblemList().removeProblem(problem);
                            Toast.makeText(PatientActivity.this, getString(R.string.patient_toast2), Toast.LENGTH_SHORT).show(); // notify the user a problem is deleted
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() { // you can choose CANCEL if you do not want to delete the problem you just clicked
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alert = a_builder.create();
            alert.show();
        }
        return super.onContextItemSelected(item);
    }

    // Called when a button in the navigation is clicked
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) // if the button profile is clicked, ProfileActivity will come up
        {
            if(!NetworkUtil.isNetworkConnected(this)){
                Toast.makeText(this, getString(R.string.common_string_no_network), Toast.LENGTH_SHORT).show();
                return true;
            }
            Intent intent = new Intent(PatientActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_doctor) { // if the button doctor is clicked, DoctorViewActivity will come up
            if(!NetworkUtil.isNetworkConnected(this)){
                Toast.makeText(this, getString(R.string.common_string_no_network), Toast.LENGTH_SHORT).show();
                return true;
            }
            Intent intent = new Intent(PatientActivity.this, DoctorViewActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) { // if the button setting is clicked, SettingActivity will come up
            Intent intent = new Intent(PatientActivity.this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) { // if the button setting is clicked, LoginActivity will come up
            DataController.setUser(null); // notify the DataController to set the user as null
            DataController.clearRecordList();
            Intent intent = new Intent(PatientActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if(id == R.id.nav_about) { // if the button about is clicked, AboutActivity will come up
            Intent intent = new Intent(PatientActivity.this, AboutActivity.class);
            startActivity(intent);
        } else if(id == R.id.nav_qr_code){
            QRCodePopup qrCodePopup = new QRCodePopup(this, DataController.getPatient().getUserName());

            qrCodePopup.showQRCode();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(connectionReceiver!=null)
            unregisterReceiver(connectionReceiver);
    }
}
