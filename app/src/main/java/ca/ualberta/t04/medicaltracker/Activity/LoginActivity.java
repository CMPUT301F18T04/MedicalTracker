package ca.ualberta.t04.medicaltracker.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ca.ualberta.t04.medicaltracker.Activity.Doctor.DoctorActivity;
import ca.ualberta.t04.medicaltracker.Activity.Patient.PatientActivity;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Model.Doctor;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Model.Patient;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Model.User;
import ca.ualberta.t04.medicaltracker.Util.CommonUtil;

/*
  This activity is for logging in
 */

public class LoginActivity extends AppCompatActivity {

    private String deviceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
    }

    public void login(View view) {
        EditText username_text = findViewById(R.id.login_username);

        if (username_text.getText().toString().equals("")) {
            Toast.makeText(this, R.string.login_toast1, Toast.LENGTH_SHORT).show();
            return;
        }
        String userName = username_text.getText().toString();
        User user = ElasticSearchController.searchUser(userName);

        if (user != null) {
            if (user.isDoctor()) {
                Doctor doctor = (Doctor) user;
                DataController.setUser(doctor);

                if(isNewDevice(user)){
                    Toast.makeText(this, "You logged in a new device", Toast.LENGTH_SHORT).show();
                    user.setDeviceId(deviceId);
                    ElasticSearchController.updateUser(user);
                }

                Intent intent = new Intent(LoginActivity.this, DoctorActivity.class);
                startActivity(intent);

            }
            else {
                Patient patient = (Patient) user;
                DataController.setUser(patient);

                if(isNewDevice(user)){
                    Toast.makeText(this, "You logged in a new device", Toast.LENGTH_SHORT).show();
                    user.setDeviceId(deviceId);
                    ElasticSearchController.updateUser(user);
                }

                Intent intent = new Intent(LoginActivity.this, PatientActivity.class);
                startActivity(intent);
            }
            finish();
        } else {
            Toast.makeText(this, R.string.login_toast2, Toast.LENGTH_SHORT).show();
        }
    }

    public void register(View view)
    {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private Boolean isNewDevice(User user){
        deviceId = CommonUtil.getIMEI();
        if(user.getDeviceId().equals("")){
            return false;
        }
        if(!user.getDeviceId().equals(deviceId)){
            return true;
        }
        return false;
    }
}
