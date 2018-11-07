package ca.ualberta.t04.medicaltracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/*
    The activity needs a userName for a specific user.
    UserName is sent by intent. The key of the intent data is "username".
 */
public class InformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation);

        String userName = getIntent().getStringExtra("username");

        User user = ElasticSearchController.searchUser(userName);
        initPage(user);
    }

    private void initPage(User user){
        TextView userName = findViewById(R.id.information_username);
        userName.setText(user.getUserName());
    }
}
