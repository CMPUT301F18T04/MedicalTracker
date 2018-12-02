package ca.ualberta.t04.medicaltracker.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import ca.ualberta.t04.medicaltracker.Activity.Doctor.DoctorActivity;
import ca.ualberta.t04.medicaltracker.Activity.Doctor.ScanActivity;
import ca.ualberta.t04.medicaltracker.Activity.Patient.PatientActivity;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Model.Doctor;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Model.Patient;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Model.User;
import ca.ualberta.t04.medicaltracker.Util.CommonUtil;
import ca.ualberta.t04.medicaltracker.Util.NetworkUtil;

/**
 * This activity is for logging in
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */

/*
  This activity is for logging in
 */

public class LoginActivity extends AppCompatActivity {

    private static int REQUEST_CODE = 0;
    private String deviceId;
    private User user = null;

    /**
     * onCreate
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20B2AA")));
        getSupportActionBar().hide();
    }

    /**
     * For logging in
     * @param view View
     */
    public void login(View view) {
        if(!NetworkUtil.isNetworkConnected(this)){
            Toast.makeText(this, getString(R.string.common_string_no_network), Toast.LENGTH_SHORT).show();
            return;
        }

        EditText username_text = findViewById(R.id.login_username);

        if (username_text.getText().toString().equals("")) {
            Toast.makeText(this, R.string.login_toast1, Toast.LENGTH_SHORT).show();
            return;
        }
        String userName = username_text.getText().toString();
        user = ElasticSearchController.searchUser(userName);

        if (user != null) {
            if(isNewDevice(user)){
                AlertDialog ad = new AlertDialog.Builder(this)
                        .setTitle("You logged in a new device, you need to scan a QR code to log in.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(LoginActivity.this, ScanActivity.class);
                                startActivityForResult(intent, REQUEST_CODE);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .create();
                ad.show();
            } else {
                login();
            }

        } else {
            Toast.makeText(this, R.string.login_toast2, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Goes to the register page
     * @param view
     */
    public void register(View view)
    {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Check if it is a new device
     * @param user User
     * @return Boolean
     */
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

    /**
     * Set the language
     * @param lang String
     * @param district String
     */
    public void setLocale(String lang,String district) {
        Locale myLocale = new Locale(lang,district);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode==RESULT_OK){
            if(data!=null){
                String userName = data.getStringExtra("result");
                if(userName!=null && userName.equals(user.getUserName())){
                    login();
                }
                else{
                    Toast.makeText(this, "Username does not match the QR code", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Cannot identify the QR code", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * For logging in
     */
    private void login(){
        user.setDeviceId(deviceId);
        ElasticSearchController.updateUser(user);
        if(user.isDoctor()){
            Doctor doctor = (Doctor) user;
            DataController.setUser(doctor);
            Intent intent = new Intent(LoginActivity.this, DoctorActivity.class);
            startActivity(intent);
        } else {
            Patient patient = (Patient) user;
            DataController.setUser(patient);
            Intent intent = new Intent(LoginActivity.this, PatientActivity.class);
            startActivity(intent);
        }

        String language = user.getLanguage();
        String district = user.getDistrict();

        try{
            setLocale(language,district);
        }catch(Exception e){
            String deviceLanguage = Locale.getDefault().toString();
            String[] separated = deviceLanguage.split("_");
            String lang = separated[0];
            String dist = separated[1];
            setLocale(lang,dist);
        }
        finish();
    }
}
