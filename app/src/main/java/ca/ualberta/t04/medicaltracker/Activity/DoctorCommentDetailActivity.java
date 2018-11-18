package ca.ualberta.t04.medicaltracker.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ca.ualberta.t04.medicaltracker.Doctor;
import ca.ualberta.t04.medicaltracker.R;

/**
 * get the data from hashmap ok
 * need to change the hashmap type to doctor after finishing the setcomment functionality of a doctor
 * and test again
 */

//https://stackoverflow.com/questions/17313495/how-to-display-multiline-from-array-list-in-single-textview

public class DoctorCommentDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_comment_detail);

        getSupportActionBar().setTitle(R.string.doctor_comment_detail_title);

        TextView commentDetail = findViewById(R.id.dCommentDetailTextView);

        Intent mIntent = getIntent();
        HashMap<Doctor, ArrayList<String>> commentHashMap = (HashMap<Doctor, ArrayList<String>>) mIntent.getSerializableExtra("hash_map");
        //HashMap<String, ArrayList<String>> commentHashMap = (HashMap<String, ArrayList<String>>) mIntent.getSerializableExtra("hash_map");
        //String doctorName = mIntent.getStringExtra("doctorKey");
        int index = mIntent.getIntExtra("position", -1);

        ArrayList<Doctor> doctorList = new ArrayList<>(commentHashMap.keySet());
        //List<String> doctorList = new ArrayList<>(commentHashMap.keySet());

        ArrayList<String> doctorNameList = new ArrayList<>();
        for(int x = 0; x < doctorList.size(); x++){
            doctorNameList.add(doctorList.get(x).getName());
        }

        //Collections.sort(doctorNameList, String.CASE_INSENSITIVE_ORDER);
        ArrayList<String> values = new ArrayList<>(commentHashMap.get(doctorList.get(index)));


        StringBuilder builder = new StringBuilder();
        for (String comment : values) {
            builder.append("-" + comment + "\n");
        }

        commentDetail.setText(builder.toString());


    }
}
