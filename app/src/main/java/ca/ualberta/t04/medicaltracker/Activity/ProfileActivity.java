package ca.ualberta.t04.medicaltracker.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ca.ualberta.t04.medicaltracker.DataController;
import ca.ualberta.t04.medicaltracker.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initPage();
    }

    private void initPage(){
        TextView username = findViewById(R.id.profile_username);
        username.setText(DataController.getUser().getUserName());

        EditText birthday = findViewById(R.id.profile_birthday);
        if(DataController.getUser().getBirthday()!=null){
            birthday.setText(DataController.getUser().getBirthdayString());
        }

        if(DataController.getUser().getMale()!=null){
            RadioButton male = findViewById(R.id.profile_male);
            RadioButton female = findViewById(R.id.profile_female);
            if(DataController.getUser().getMale())
                male.setChecked(true);
            else
                female.setChecked(true);
        }

        if(DataController.getUser().getPhoneNumber()!=null){
            EditText phone = findViewById(R.id.profile_phone);
            phone.setText(DataController.getUser().getPhoneNumber());
        }

        if(DataController.getUser().getEmail()!=null){
            EditText email = findViewById(R.id.profile_email);
            email.setText(DataController.getUser().getEmail());
        }

        if(DataController.getUser().getAddress()!=null){
            EditText address = findViewById(R.id.profile_address);
            address.setText(DataController.getUser().getAddress());
        }

        birthday.requestFocus();
    }

    public void edit(View view){
        // get new birthday
        EditText birthday = findViewById(R.id.profile_birthday);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date newBirthday = null;
        try {
            newBirthday = format.parse(birthday.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // get new sex
        RadioButton male = findViewById(R.id.profile_male);
        RadioButton female = findViewById(R.id.profile_female);
        Boolean isMale = null;
        if(male.isChecked())
            isMale = true;
        else if(female.isChecked())
            isMale = false;

        // get new phone
        EditText phone = findViewById(R.id.profile_phone);

        // get new email
        EditText email = findViewById(R.id.profile_email);

        // get new address
        EditText address = findViewById(R.id.profile_address);

        DataController.updateProfile(newBirthday, isMale, phone.getText().toString(), email.getText().toString(), address.getText().toString());

        Toast.makeText(ProfileActivity.this, "Your profile has been updated!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
