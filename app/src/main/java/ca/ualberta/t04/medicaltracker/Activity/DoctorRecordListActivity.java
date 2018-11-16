package ca.ualberta.t04.medicaltracker.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.t04.medicaltracker.Adapter.RecordAdapter;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Record;

public class DoctorRecordListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_record_list);

        getSupportActionBar().setTitle(R.string.doctor_record_list_title);

        int problemIndex = getIntent().getIntExtra("problem_index", -1);
        int patientIndex = getIntent().getIntExtra("patient_index", -1);

        initDoctorRecordList(patientIndex, problemIndex);

    }

    private void initDoctorRecordList(int position, final int problemPosition){
        ListView recordListView = findViewById(R.id.doctor_record_list_list_view);
        ArrayList<Record> records = DataController.getDoctor().getPatients().get(position).getProblemList().getProblem(problemPosition).getRecordList().getRecords();
        final RecordAdapter adapter = new RecordAdapter(this, R.layout.record_list, records);
        recordListView.setAdapter(adapter);

        if(records.size() == 0){
            Toast.makeText(DoctorRecordListActivity.this, "There is no record for this problem", Toast.LENGTH_SHORT).show();
        }

        recordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //DataController.setCurrentProblem(problems.get(position));
                Intent intent = new Intent(DoctorRecordListActivity.this, DoctorRecordDetailActivity.class);
                intent.putExtra("patient_index", position);
                intent.putExtra("problem_index", problemPosition);
                startActivity(intent);
            }
        });


    }
}
