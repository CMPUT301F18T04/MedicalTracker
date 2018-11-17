package ca.ualberta.t04.medicaltracker.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.User;

/*
    The activity needs a userName for a specific user.
    UserName is sent by intent. The key of the intent data is "username".
 */
public class InformationActivity extends AppCompatActivity {
    TextView information_email;
    RadioGroup radioGroup;
    RadioButton register_male, register_female;
    TextView information_TeleNo;
    TextView information_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        information_email = (TextView) findViewById(R.id.information_email);
        information_TeleNo = (TextView) findViewById(R.id.information_TeleNo);
        information_address = (TextView) findViewById(R.id.information_address);


        String userName = getIntent().getStringExtra("username");

        User user = ElasticSearchController.searchUser(userName);
        initPage(user);
    }




    private void initPage(User user){
        TextView userName = findViewById(R.id.information_username);
        userName.setText(user.getUserName());
        user = DataController.getUser();
        if(user.getEmail() != null) {
            information_email.setText(user.getEmail());
        }
        if(user.getAddress() != null) {
            information_address.setText(user.getAddress());
        }
        if(user.getPhoneNumber() != null) {
            information_TeleNo.setText(user.getPhoneNumber());
        }
        if(user.getMale()!= null){
            register_male = (RadioButton) findViewById(R.id.register_male);
            register_female = (RadioButton) findViewById(R.id.register_female);
            if(user.getMale())
                register_male.setChecked(true);
            else if(!user.getMale())
                register_female.setChecked(true);
        }

    }

}
