package ca.ualberta.t04.medicaltracker.Activity.Patient;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.ualberta.t04.medicaltracker.Adapter.RecordAdapter;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Listener;
import ca.ualberta.t04.medicaltracker.Model.Patient;
import ca.ualberta.t04.medicaltracker.Model.Problem;
import ca.ualberta.t04.medicaltracker.Model.RecordList;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Model.Record;

/*
  This activity is for displaying the record history list for a patient user
 */

// This class has the layout of activity_record_history.xml
// This class is used for the record list
public class RecordHistoryActivity extends AppCompatActivity {

    // initialize
    private int problem_index;
    private static final String TAG = "RecordHistoryActivity";

    private ListView listView;
    private Patient mPatient;
    private ArrayList<Record> mRecords;
    private RecordList mRecordList;

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_history);

        listView = findViewById(R.id.record_history_list_view);

        getSupportActionBar().setTitle(R.string.record_history_title);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        problem_index = getIntent().getIntExtra("index", -1);

        //initListView(DataController.getPatient().getProblemList().getProblem(problem_index));
        mPatient = DataController.getPatient();
        mRecordList = mPatient.getProblemList().getProblem(problem_index).getRecordList();
        mRecords = mRecordList.getRecords();
        initListView();
    }

    // init the problem list view
    private void initListView(){
        Log.d(TAG, "initListView: 0");
        final RecordAdapter adapter = new RecordAdapter(this, R.layout.record_list, mRecords);
        listView.setAdapter(adapter);

        // added in the record list item click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Record record = mRecords.get(position);
                Intent intent = new Intent(RecordHistoryActivity.this, RecordDetailActivity.class);
                mRecordList.updateRecord(position, record.getRecordId());
                intent.putExtra("problem_index", problem_index);
                intent.putExtra("record_index", position);
                startActivity(intent);
            }
        });

        // add listener to the record list
        mRecordList.addListener("RecordListener1", new Listener() {
            @Override
            public void update() {

                adapter.notifyDataSetChanged();
                // We don't need it anymore, since we only need to update user once.
                //ElasticSearchController.updateUser(DataController.getUser());
                DataController.getPatient().getProblemList().notifyAllListener();
            }
        });

        // setOnItemLongClickListener
        // When you long click an item in the record list, you have an option to delete the record
        // when long click one of the elements in the listView, there will be an alert dialog pop up
        // and ask you if you want to delete the record
        // you can click YES to delete the record
        // or cancel to cancel the alert dialog
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int index = position;
                AlertDialog.Builder a_builder = new AlertDialog.Builder(RecordHistoryActivity.this);
                a_builder.setMessage("ARE YOU SURE TO DELETE THIS RECORD ?").setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Record temp = mRecords.get(index);
                                mRecordList.removeRecord(temp);
                                DataController.getPatient().getProblemList().notifyAllListener();
                                adapter.notifyDataSetChanged();
                                // make notification for user
                                Toast.makeText(RecordHistoryActivity.this, R.string.record_history_toast1, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alert = a_builder.create();
                alert.show(); // show the alert
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.removeItem(R.id.action_search);
        return true;
    }

    // when the add button is clicked, another activity comes up
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(RecordHistoryActivity.this, AddRecordActivity.class);

            intent.putExtra("index", problem_index);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
