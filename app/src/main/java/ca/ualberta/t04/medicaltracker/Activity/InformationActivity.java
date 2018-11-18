package ca.ualberta.t04.medicaltracker.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.User;

/*
    The activity needs a userName for a specific user.
    UserName is sent by intent. The key of the intent data is "username".
 */
public class InformationActivity extends AppCompatActivity {
    TextView informationEmail;
    RadioGroup radioGroup;
    RadioButton registerMale, registerFemale;
    TextView informationTelNo;
    TextView informationAddress;
    TextView informationDisplayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        informationEmail = (TextView) findViewById(R.id.information_email);
        informationTelNo = (TextView) findViewById(R.id.information_TeleNo);
        informationAddress = (TextView) findViewById(R.id.information_address);
        informationDisplayName = findViewById(R.id.information_displayName);

        String userName = getIntent().getStringExtra("username");

        User user = ElasticSearchController.searchUser(userName);
        getSupportActionBar().setTitle(user.getName());
        initPage(user);
    }

    private void initPage(User user){
        TextView userName = findViewById(R.id.information_username);
        userName.setText(user.getUserName());

        if(user.getName()!=null){
            informationDisplayName.setText(user.getName());
        } else {
            informationDisplayName.setText("");
        }
        if(user.getEmail() != null) {
            informationEmail.setText(user.getEmail());
        } else {
            informationEmail.setText("");
        }
        if(user.getAddress() != null) {
            informationAddress.setText(user.getAddress());
        } else {
            informationAddress.setText("");
        }
        if(user.getPhoneNumber() != null) {
            informationTelNo.setText(user.getPhoneNumber());
        } else {
            informationTelNo.setText("");
        }
        if(user.getMale()!= null){
            registerMale = (RadioButton) findViewById(R.id.register_male);
            registerFemale = (RadioButton) findViewById(R.id.register_female);
            if(user.getMale())
                registerMale.setChecked(true);
            else if(!user.getMale())
                registerFemale.setChecked(true);
        }

    }

}
