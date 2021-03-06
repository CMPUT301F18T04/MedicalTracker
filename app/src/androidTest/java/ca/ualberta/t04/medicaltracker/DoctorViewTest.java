package ca.ualberta.t04.medicaltracker;

import android.support.v4.widget.DrawerLayout;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.robotium.solo.Solo;

import ca.ualberta.t04.medicaltracker.Activity.LoginActivity;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class DoctorViewTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    public DoctorViewTest() {
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

    public void testDoctorView() throws Throwable {

        //Click the button register to sign up first
        solo.clickOnButton("Register");

        // Check if the app opens the correct page
        assertTrue(solo.waitForActivity("RegisterActivity"));


        // Enter the test account's information
        solo.enterText((EditText) solo.getView(R.id.register_username),"patient000");

        //Click button to sign up
        solo.clickOnView(solo.getView(R.id.register_button_signup));

        //If the text "Duplicated" occurs, then it means the account is already existed, then
        //the robot will use the account of doctor to log in
        if (solo.waitForText("Duplicated")){
            solo.getCurrentActivity().finish();
        }

        //Login first
        solo.enterText((EditText) solo.getView(R.id.login_username),"patient000");

        solo.clickOnButton("Login");

        if(solo.waitForText("match")){
            solo.clickOnButton("Login");
        }
        // Check if the app opens the correct page
        assertTrue(solo.waitForActivity("PatientActivity"));

        // Open slide Bar
        //DrawerLayout drawerLayout = solo.getCurrentActivity().findViewById(R.id.drawer_layout);
        //drawerLayout.openDrawer(Gravity.LEFT);
        solo.clickOnImageButton(0);
        //solo.pressMenuItem(0);
        solo.clickOnMenuItem("Doctor");

        // Check if the app opens the correct page
        assertTrue(solo.waitForActivity("DoctorViewActivity"));

        solo.goBack();

    }

}