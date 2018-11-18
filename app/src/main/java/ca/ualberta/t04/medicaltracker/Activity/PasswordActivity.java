package ca.ualberta.t04.medicaltracker.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.R;
import ca.ualberta.t04.medicaltracker.User;

public class PasswordActivity extends AppCompatActivity {

    private EditText oldPassword;
    private EditText newPassword;
    private EditText confirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        oldPassword = findViewById(R.id.old_password);
        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.confirm_password);

    }

    public void Save(View view){
        String passwordBefore = oldPassword.getText().toString();
        String passwordNew = newPassword.getText().toString();
        String passwordConfirm = confirmPassword.getText().toString();

        String userName = DataController.getUser().getUserName();
        User user = ElasticSearchController.searchUser(userName);

        // Check if old password is correct
        if(!passwordBefore.equals(user.getPassword())){
            Toast.makeText(PasswordActivity.this,"Old password is not correct!",Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if password is empty
        if(passwordNew.equals("")){
            Toast.makeText(PasswordActivity.this,"Password can not be empty!",Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if password is too short or too long
        if(passwordNew.length() < 6 || passwordNew.length() > 20){
            Toast.makeText(PasswordActivity.this,"Password should between 6-20 characters!",Toast.LENGTH_SHORT).show();
            return;
        }

        // check if password contains invalid characters
        if(isValid(passwordNew)){
            Toast.makeText(PasswordActivity.this,"Password contains invalid characters!",Toast.LENGTH_SHORT).show();
            return;
        }
        // Check if password is matched confirmed password
        if(!passwordNew.equals(passwordConfirm)){
            Toast.makeText(PasswordActivity.this, "Password does not match confirmed password!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if new password is same with old password
        if(passwordNew.equals(passwordBefore)){
            Toast.makeText(PasswordActivity.this, "New password cannot be same ith old one!", Toast.LENGTH_SHORT).show();
            return;
        }
        DataController.updatePassword(passwordNew);
        Toast.makeText(PasswordActivity.this, "Your password has been updated!", Toast.LENGTH_SHORT).show();
        finish();

    }

    private boolean isValid(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

}