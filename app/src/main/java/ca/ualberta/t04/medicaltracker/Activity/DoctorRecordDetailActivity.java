package ca.ualberta.t04.medicaltracker.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import ca.ualberta.t04.medicaltracker.R;

public class DoctorRecordDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_record_detail);

        getSupportActionBar().setTitle(R.string.doctor_record_detail_title);

        final TextView title = findViewById(R.id.doctorRecordTitle);
        final TextView date = findViewById(R.id.dRecordDetailDate);
        final TextView description = findViewById(R.id.dRecordDetailDescription);

        ListView commentListView = findViewById(R.id.CommentListView);

        Button commentButton = findViewById(R.id.doctorCommentButton);


        /**
         * need to set the textviews and the comment listview
         * pretty much the same as patient version
         */


        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorRecordDetailActivity.this, DoctorAddCommentActivity.class);
                startActivity(intent);
            }
        });






    }
}
