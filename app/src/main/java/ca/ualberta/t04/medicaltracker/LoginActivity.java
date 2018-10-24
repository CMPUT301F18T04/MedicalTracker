package ca.ualberta.t04.medicaltracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
    }

    public void login(View view)
    {
        EditText username_text = findViewById(R.id.editText);
        User user = new User(username_text.getText().toString(), false);
        user.setName(username_text.getText().toString());
        DataController.setUser(user);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
