package ca.ualberta.t04.medicaltracker.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ca.ualberta.t04.medicaltracker.DataController;
import ca.ualberta.t04.medicaltracker.Doctor;
import ca.ualberta.t04.medicaltracker.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Patient;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.User;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
    }

    public void login(View view)
    {
        new ElasticSearchController.DeleteUserTask().execute("mjy");
        EditText username_text = findViewById(R.id.login_username);
        EditText password_text = findViewById(R.id.login_password);
        if(username_text.getText().toString().equals("") || password_text.getText().toString().equals("")){
            Toast.makeText(this, "UserName or Password cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        String userName = username_text.getText().toString();
        User user = ElasticSearchController.searchUser(userName);

        if(password_text.getText().toString().equals(user.getPassword())){
            if(user.isDoctor()){
                Doctor doctor = (Doctor) user;
                DataController.setUser(doctor);
                Intent intent = new Intent(LoginActivity.this, DoctorActivity.class);
                startActivity(intent);
            }
            else {
                Patient patient = (Patient) user;
                DataController.setUser(patient);
                Intent intent = new Intent(LoginActivity.this, PatientActivity.class);
                startActivity(intent);
            }
            finish();
        } else {
            Toast.makeText(this, "UserName does not match the password!", Toast.LENGTH_SHORT).show();
        }

    }

    public void register(View view)
    {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
