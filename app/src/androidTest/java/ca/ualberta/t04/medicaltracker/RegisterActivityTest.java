package ca.ualberta.t04.medicaltracker;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;
import ca.ualberta.t04.medicaltracker.Activity.LoginActivity;
/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class RegisterActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    public RegisterActivityTest() {
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

    public void testRegister() throws Throwable {

        // Click register button first
        solo.clickOnButton("Register");

        //fill in all the information of the user
        solo.enterText((EditText) solo.getView(R.id.register_username),"testRegister");
        solo.enterText((EditText) solo.getView(R.id.register_email),"test@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.register_phone),"12345678");

        // finish register
        solo.clickOnButton("Sign up");

        // check if the username is exist
        if (solo.waitForText("Duplicated")){
            solo.getCurrentActivity().finish();
        }

        assertTrue(solo.waitForActivity("LoginActivity"));

        // Check register by logging this account
        //Login first
        solo.enterText((EditText) solo.getView(R.id.login_username),"testRegister");

        solo.clickOnButton("Login");

        // Check if the app opens the correct page
        assertTrue(solo.waitForActivity("PatientActivity"));

    }
}
