package ca.ualberta.t04.medicaltracker.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ca.ualberta.t04.medicaltracker.Doctor;
import ca.ualberta.t04.medicaltracker.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Patient;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.User;
import ca.ualberta.t04.medicaltracker.Util;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText editText_username = findViewById(R.id.register_username);
        editText_username.requestFocus();
    }

    public void signUp(View view)
    {
        Boolean isDoctor = false;
        Boolean isMale = null;

        // Get all edit texts
        EditText editText_username = findViewById(R.id.register_username);
        EditText editText_password = findViewById(R.id.register_password);
        EditText editText_confirmed_password = findViewById(R.id.register_confirmed_password);
        EditText editText_email = findViewById(R.id.register_email);
        EditText editText_birthday = findViewById(R.id.register_birthday);
        EditText editText_phone = findViewById(R.id.register_phone);

        // Get userName and password
        String userName = editText_username.getText().toString();
        String password = editText_password.getText().toString();

        // Check if password is matched confirmed password
        if(!password.equals(editText_confirmed_password.getText().toString())){
            Toast.makeText(RegisterActivity.this, "Password does not match confirmed password!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get optional information
        String email = editText_email.getText().toString();
        String birthday = editText_birthday.getText().toString();
        String phoneNumber = editText_phone.getText().toString();

        // Get the user's sex
        RadioButton radio_male = findViewById(R.id.register_male);
        RadioButton radio_female = findViewById(R.id.register_female);

        if(radio_male.isChecked()){
            isMale = true;
        } else if(radio_female.isChecked()) {
            isMale = false;
        }

        // Check the user's role
        RadioButton radio_doctor = findViewById(R.id.register_doctor);

        if(radio_doctor.isChecked()){
            isDoctor = true;
        }

        // Try to sign up by using elastic search
        Boolean done;
        if(isDoctor) {
            Doctor doctor = new Doctor(userName, password);
            doctor.setName(userName);
            addInformationToUser(doctor, email, birthday, phoneNumber, isMale);
            // If succeed to sign up, then return true
            done = ElasticSearchController.signUp(doctor);
        } else {
            Patient patient = new Patient(userName, password);
            patient.setName(userName);
            addInformationToUser(patient, email, birthday, phoneNumber, isMale);
            done = ElasticSearchController.signUp(patient);
        }

        if(!done)
            Toast.makeText(RegisterActivity.this, "Duplicated UserName", Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(RegisterActivity.this, "Succeed to Sign Up", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    // If optional information is input by user, then add them to the user's information
    private void addInformationToUser(User user, String email, String birthday, String phoneNumber, Boolean isMale){
        // Add email to user's information
        if(!email.equals("")){
            user.setEmail(email);
        }

        // Add birthday to user's information
        Date birthdayDate = null;
        SimpleDateFormat format = new SimpleDateFormat(Util.DATE_FORMATE, Locale.getDefault());
        try {
            birthdayDate = format.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(birthdayDate != null){
            user.setBirthday(birthdayDate);
        }

        // Add phoneNumber to user's information
        if(!phoneNumber.equals("")){
            user.setPhoneNumber(phoneNumber);
        }

        // Add sex to user's information
        if(isMale!=null){
            user.setMale(isMale);
        }
    }
}
