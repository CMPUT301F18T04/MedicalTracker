package ca.ualberta.t04.medicaltracker;

import android.support.v4.widget.DrawerLayout;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.RadioButton;

import com.robotium.solo.Solo;

import ca.ualberta.t04.medicaltracker.Activity.LoginActivity;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class AboutActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    public AboutActivityTest() {
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

    public void testAboutUs() throws Throwable {

        ElasticSearchController.deleteUser("doctor000");

        //Click the button register to sign up first
        solo.clickOnButton("Register");

        // Check if the app opens the correct page
        assertTrue(solo.waitForActivity("RegisterActivity"));


        // Enter the test account's information
        solo.enterText((EditText) solo.getView(R.id.register_username),"doctor000");

        RadioButton rb = (RadioButton) solo.getView(R.id.register_doctor);
        solo.clickOnView(rb);

        //Click button to sign up
        solo.clickOnView(solo.getView(R.id.register_button_signup));

        //If the text "Duplicated" occurs, then it means the account is already existed, then
        //the robot will use the account of doctor to log in
        if (solo.waitForText("Duplicated",0,1000)){
            solo.getCurrentActivity().finish();
        }

        //Login first
        solo.enterText((EditText) solo.getView(R.id.login_username),"doctor000");
        solo.clickOnButton("Login");

        // Check if the app opens the correct page
        assertTrue(solo.waitForActivity("DoctorActivity"));

        // Open slide Bar
        //DrawerLayout drawerLayout = solo.getCurrentActivity().findViewById(R.id.drawer_layout);
        //drawerLayout.openDrawer(Gravity.LEFT);
        solo.clickOnImageButton(0);

        //solo.pressMenuItem(0);
        solo.clickOnMenuItem("About");

        // Check if the app opens the correct page
        assertTrue(solo.waitForActivity("AboutActivity"));

        solo.goBackToActivity("DoctorActivity");

        solo.clickOnImageButton(0);

        solo.clickOnMenuItem("Profile");

        solo.clearEditText((EditText) solo.getView(R.id.nick_name));
        solo.enterText((EditText) solo.getView(R.id.nick_name), "test");

        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.profile_button_save));

        solo.clickOnImageButton(0);
        assertTrue(solo.waitForText("test"));

        solo.clickOnImageButton(0);

        solo.clickOnMenuItem("Profile");

        solo.clearEditText((EditText) solo.getView(R.id.nick_name));
        solo.enterText((EditText) solo.getView(R.id.nick_name), "intent");

        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.profile_button_save));

        solo.clickOnImageButton(0);
        assertTrue(solo.waitForText("intent"));
    }

}
