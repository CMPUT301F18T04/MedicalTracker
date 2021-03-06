package ca.ualberta.t04.medicaltracker.Activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.Util.CommonUtil;

/**
 * This activity is for displaying the profile detail of a general user
 *
 * @author CMPUT301F18T04 Team 04
 * @version Project part 05 1.0
 * @since 1.0
 */

/*
  This activity is for displaying the profile detail of a general user
 */

public class ProfileActivity extends AppCompatActivity {


    /**
     * onCreate
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle(R.string.profile_title);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20B2AA")));

        EditText birthday = findViewById(R.id.profile_birthday);
        birthday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setBirthday(v);
            }
        });

        initPage();
    }

    /**
     * Initialize the page
     */
    private void initPage(){
        // get username
        TextView username = findViewById(R.id.profile_username);
        username.setText(DataController.getUser().getUserName());

        // get Nickname
        TextView nickname = findViewById(R.id.nick_name);
        nickname.setText(DataController.getUser().getName());

        // get birthday
        EditText birthday = findViewById(R.id.profile_birthday);
        if(DataController.getUser().getBirthday()!=null){
            String temp = DataController.getUser().getBirthdayString();
            String[] arrOfTemp = temp.split("T");
            birthday.setText(arrOfTemp[0]);
        }

        // get sex
        if(DataController.getUser().getMale()!=null){
            RadioButton male = findViewById(R.id.profile_male);
            RadioButton female = findViewById(R.id.profile_female);
            if(DataController.getUser().getMale())
                male.setChecked(true);
            else if(!DataController.getUser().getMale())
                female.setChecked(true);
        }

        // get phone number
        if(DataController.getUser().getPhoneNumber()!=null){
            EditText phone = findViewById(R.id.profile_phone);
            phone.setText(DataController.getUser().getPhoneNumber());
        }

        // get email
        if(DataController.getUser().getEmail()!=null){
            EditText email = findViewById(R.id.profile_email);
            email.setText(DataController.getUser().getEmail());
        }

        // get address
        if(DataController.getUser().getAddress()!=null){
            EditText address = findViewById(R.id.profile_address);
            address.setText(DataController.getUser().getAddress());
        }

        birthday.requestFocus();
    }

    /**
     * For editing the profile of the current user
     * @param view View
     */
    public void edit(View view){

        // get new nickname
        TextView nickname = findViewById(R.id.nick_name);
        String newNickName = nickname.getText().toString();

        // get new birthday
        EditText birthday = findViewById(R.id.profile_birthday);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date newBirthday = null;
        try {
            newBirthday = format.parse(birthday.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Check birthday validation
        if(newBirthday != null && !newBirthday.equals("")) {
            Date current_date = new Date();
            if(current_date.before(newBirthday)){
                Toast.makeText(ProfileActivity.this,R.string.profile_toast1,Toast.LENGTH_SHORT).show();
                return;
            }
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
        String phoneNumber = phone.getText().toString();

        // Check phone number validation
        if(!phoneNumber.equals("")){
            if(!PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)){
                Toast.makeText(ProfileActivity.this,R.string.profile_toast2,Toast.LENGTH_SHORT).show();
                return;
            }

            if(phoneNumber.length() < 5 || phoneNumber.length() > 15){
                Toast.makeText(ProfileActivity.this,"Phone number should between 5-15 digits!",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // get new email
        EditText email = findViewById(R.id.profile_email);

        // Check email validation
        String Email=email.getText().toString();
        if(!Email.equals("")){
            if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                Toast.makeText(ProfileActivity.this,R.string.profile_toast3,Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // get new address
        EditText address = findViewById(R.id.profile_address);
        String Address = address.getText().toString();

        DataController.updateProfile(newNickName,newBirthday, isMale, phoneNumber, Email, Address);

        Toast.makeText(ProfileActivity.this, R.string.profile_toast4, Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Sets the birthday of the current user
     * @param view View
     */
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
                        EditText birthday = findViewById(R.id.profile_birthday);

//                        birthday.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth + "T00:00");
                        birthday.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    }
                }, Year, Month, Day);
        datePickerDialog.show();
    }
}
