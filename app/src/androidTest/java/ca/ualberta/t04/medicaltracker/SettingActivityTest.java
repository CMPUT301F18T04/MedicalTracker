package ca.ualberta.t04.medicaltracker;

import android.support.v4.widget.DrawerLayout;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.robotium.solo.Solo;

import ca.ualberta.t04.medicaltracker.Activity.LoginActivity;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class SettingActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    public SettingActivityTest() {
        super(LoginActivity.class);
    }

    @Override
    public void setUp(){
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown(){
        solo.finishOpenedActivities();
    }

    public void testSettingPasswordChange() throws Throwable {

        //Login first
        solo.enterText((EditText) solo.getView(R.id.login_username),"intent");
        solo.enterText((EditText) solo.getView(R.id.login_password),"12345678");
        solo.clickOnButton("Login");

        // IF "match" occurs, means the username and password does not match, just use
        if(solo.waitForText("not")){
            solo.clearEditText(1);
            solo.enterText((EditText) solo.getView(R.id.login_password),"123456");
            solo.clickOnButton("Login");
        }

        // Check if the app opens the correct page
        assertTrue(solo.waitForActivity("PatientActivity"));

        // Open slide Bar
        solo.sleep(2000);
        DrawerLayout drawerLayout = solo.getCurrentActivity().findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(Gravity.LEFT);
        //solo.pressMenuItem(0);
        solo.clickOnMenuItem("Setting");

        // Check if the app opens the correct page
        assertTrue(solo.waitForActivity("SettingActivity"));

        // Click change password button
        solo.clickOnButton("Change Password");

        // Check if the app opens the correct page
        assertTrue(solo.waitForActivity("PasswordActivity"));

        // File in old password,new password, confirm password
        String userName = DataController.getUser().getUserName();
        User user = ElasticSearchController.searchUser(userName);
        String correctPassword = user.getPassword();

        solo.enterText((EditText) solo.getView(R.id.old_password),correctPassword);
        solo.enterText((EditText) solo.getView(R.id.new_password),"123456");
        solo.enterText((EditText) solo.getView(R.id.confirm_password),"123456");

        solo.clickOnButton("Save");

        // IF "same" occurs, means the new password is same with original one
        if(solo.waitForText("same")){
            solo.clearEditText(1);
            solo.clearEditText(2);
            solo.enterText((EditText) solo.getView(R.id.new_password),"12345678");
            solo.enterText((EditText) solo.getView(R.id.confirm_password),"12345678");
            solo.clickOnButton("Save");
        }

        solo.goBack();

    }

}
