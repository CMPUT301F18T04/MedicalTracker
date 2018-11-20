package ca.ualberta.t04.medicaltracker;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

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

        //Login first
        solo.enterText((EditText) solo.getView(R.id.login_username),"intent");
        solo.enterText((EditText) solo.getView(R.id.login_password),"123456");
        solo.clickOnButton("Login");

        if(solo.waitForText("match")){
            solo.clearEditText((EditText) solo.getView(R.id.login_password));
            solo.enterText((EditText) solo.getView(R.id.login_password),"12345678");
            solo.clickOnButton("Login");
        }

        // Check if the app opens the correct page
        assertTrue(solo.waitForActivity("PatientActivity"));

        // Open slide Bar
        //DrawerLayout drawerLayout = solo.getCurrentActivity().findViewById(R.id.drawer_layout);
        //drawerLayout.openDrawer(Gravity.LEFT);
        solo.clickOnImageButton(0);
        //solo.pressMenuItem(0);
        solo.clickOnMenuItem("About");

        // Check if the app opens the correct page
        assertTrue(solo.waitForActivity("AboutActivity"));

        solo.goBackToActivity("PatientActivity");

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
