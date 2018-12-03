package ca.ualberta.t04.medicaltracker;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import ca.ualberta.t04.medicaltracker.Activity.LoginActivity;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Model.User;

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
        solo.enterText((EditText) solo.getView(R.id.login_username),"intentTest");

        solo.clickOnButton("Login");

        // If the username does not exist
        if (solo.waitForText("exist")){
            solo.clickOnButton("Register");
            solo.enterText((EditText) solo.getView(R.id.register_username),"intentTest");
            solo.clickOnButton("Sign up");
            solo.clickOnButton("Login");
        }

        // Check if the app opens the correct page
        assertTrue(solo.waitForActivity("PatientActivity"));

        // Open slide Bar
        //DrawerLayout drawerLayout = solo.getCurrentActivity().findViewById(R.id.drawer_layout);
        //drawerLayout.openDrawer(Gravity.LEFT);
        solo.clickOnImageButton(0);
        //solo.pressMenuItem(0);
        solo.clickOnMenuItem("Setting");

        // Check if the app opens the correct page
        assertTrue(solo.waitForActivity("SettingActivity"));

        // Click Change language button
        solo.clickOnButton("Change Language");
        assertTrue(solo.waitForActivity("LanguageActivity"));

    }

}
