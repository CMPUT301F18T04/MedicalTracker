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
        solo.enterText((EditText) solo.getView(R.id.login_username),"patient");
        solo.enterText((EditText) solo.getView(R.id.login_password),"123456");
        solo.clickOnButton("Login");

        // Open slide Bar
        DrawerLayout drawerLayout = solo.getCurrentActivity().findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(Gravity.LEFT);
        solo.sleep(2000);
        solo.clickOnMenuItem("Setting");

    }

}
