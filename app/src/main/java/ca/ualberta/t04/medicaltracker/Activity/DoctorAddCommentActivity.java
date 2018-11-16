package ca.ualberta.t04.medicaltracker.Activity;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Doctor;
import ca.ualberta.t04.medicaltracker.R;

public class DoctorAddCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_add_comment);

        getSupportActionBar().setTitle(R.string.doctor_comment_title);

        EditText addComment = findViewById(R.id.addCommentEditText);
        Button confirmButton = findViewById(R.id.confirmButton);

        String docComment = addComment.getText().toString();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Doctor doctor = DataController.getDoctor();

                /**
                 * need to set comment for a particular record under this doctor
                 */

                Toast.makeText(DoctorAddCommentActivity.this, "New comment added to this record", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
