package ca.ualberta.t04.medicaltracker.Activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.ualberta.t04.medicaltracker.Doctor;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
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

        EditText birthday = findViewById(R.id.register_birthday);
        birthday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setBirthday(v);
            }
        });
    }

    public void signUp(View view){
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

        // Check if username is empty
        if(userName.equals("")){
            Toast.makeText(RegisterActivity.this,R.string.register_toast1,Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if username is too short or too long
        if(userName.length()<4 || userName.length() > 15){
            Toast.makeText(RegisterActivity.this,R.string.register_toast2,Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if username contains invalid characters
        if(isValid(userName)){
            Toast.makeText(RegisterActivity.this,R.string.register_toast3,Toast.LENGTH_SHORT).show();
            return;
        }
        // Check if password is empty
        if(password.equals("")){
            Toast.makeText(RegisterActivity.this,R.string.register_toast4,Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if password is too short or too long
        if(password.length() < 6 || password.length() > 20){
            Toast.makeText(RegisterActivity.this,R.string.register_toast5,Toast.LENGTH_SHORT).show();
            return;
        }

        // check if password contains invalid characters
        if(isValid(password)){
            Toast.makeText(RegisterActivity.this,R.string.register_toast6,Toast.LENGTH_SHORT).show();
            return;
        }
        // Check if password is matched confirmed password
        if(!password.equals(editText_confirmed_password.getText().toString())){
            Toast.makeText(RegisterActivity.this, R.string.register_toast7, Toast.LENGTH_SHORT).show();
            return;
        }

        // Get optional information
        String email = editText_email.getText().toString();
        String birthday = editText_birthday.getText().toString();
        String phoneNumber = editText_phone.getText().toString();

        // Get the user's sex
        RadioButton radio_male = findViewById(R.id.register_male);
        RadioButton radio_female = findViewById(R.id.register_female);

        // Check the validation of phone number if it is not empty
        if(!phoneNumber.equals("")){
            if(!PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)){
                Toast.makeText(RegisterActivity.this,R.string.register_toast8,Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Check the validation of email address if it is not empty{
        if(!email.equals("")){
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(RegisterActivity.this,R.string.register_toast9,Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(radio_male.isChecked()){
            isMale = true;
        } else if(radio_female.isChecked()) {
            isMale = false;
        }

        // Check the validation of birthday
        //if(!editText_birthday.equals("")) {
        //    Date current_date = new Date();
        //    SimpleDateFormat format = new SimpleDateFormat(Util.DATE_FORMAT, Locale.getDefault());
        //    Date newBirthday= format.parse(birthday);
        //    if(current_date.before(newBirthday)){
        //        Toast.makeText(RegisterActivity.this,"Your birthday should beyond current date!",Toast.LENGTH_SHORT).show();
        //        return;
        //    }
        //}

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
            Toast.makeText(RegisterActivity.this, R.string.register_toast10, Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(RegisterActivity.this, R.string.register_toast11, Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    public void setBirthday(View view){

        int Year, Month, Day ;

        final Calendar c = Calendar.getInstance();
        Year = c.get(Calendar.YEAR);
        Month = c.get(Calendar.MONTH);
        Day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        EditText birthday = findViewById(R.id.register_birthday);
                        birthday.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    }
                }, Year, Month, Day);
        datePickerDialog.show();
    }

    private boolean isValid(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    // If optional information is input by user, then add them to the user's information
    private void addInformationToUser(User user, String email, String birthday, String phoneNumber, Boolean isMale){
        // Add email to user's information
        if(!email.equals("")){
            user.setEmail(email);
        }

        // Add birthday to user's information
        Date birthdayDate = null;
        SimpleDateFormat format = new SimpleDateFormat(Util.DATE_FORMAT, Locale.getDefault());
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
