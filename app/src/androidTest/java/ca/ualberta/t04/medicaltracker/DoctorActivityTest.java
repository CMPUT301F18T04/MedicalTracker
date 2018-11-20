package ca.ualberta.t04.medicaltracker;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.robotium.solo.Solo;

import ca.ualberta.t04.medicaltracker.Activity.LoginActivity;
import ca.ualberta.t04.medicaltracker.Activity.PatientActivity;

public class DoctorActivityTest extends ActivityInstrumentationTestCase2<LoginActivity>{
    private Solo solo;

    public DoctorActivityTest(){
        super(LoginActivity.class);
    }

    @Override
    public void setUp(){
        solo = new Solo(getInstrumentation(),getActivity());
    }

    @Override
    public void tearDown(){
        solo.finishOpenedActivities();
    }

    public void testDoctorPage() throws Throwable{
        //Click the button register to sign up first
        solo.clickOnButton("Register");

        // Check if the app opens the correct page
        assertTrue(solo.waitForActivity("RegisterActivity"));


        // Enter the test account's information
        solo.enterText((EditText) solo.getView(R.id.register_username),"doctor1");
        solo.enterText((EditText) solo.getView(R.id.register_password),"123456");
        solo.enterText((EditText) solo.getView(R.id.register_confirmed_password),"123456");
        RadioButton rb = (RadioButton) solo.getView(R.id.register_doctor);
        solo.clickOnView(rb);

        //Click button to sign up
        solo.clickOnView(solo.getView(R.id.register_button_signup));

        //If the text "Duplicated" occurs, then it means the account is already existed, then
        //the robot will use the account of doctor to log in
        if (solo.waitForText("Duplicated")){
            solo.getCurrentActivity().finish();
        }

        solo.enterText((EditText) solo.getView(R.id.login_username), "doctor1");
        solo.enterText((EditText) solo.getView(R.id.login_password), "123456");
        solo.clickOnButton("Login");

        assertTrue(solo.waitForActivity("DoctorActivity"));


        //Let the robot press the second button in the menu to test addPatientActivity.
        //The first one is the search button

        solo.clickOnView(solo.getView(R.id.action_add));
        assertTrue(solo.waitForActivity("AddPatientActivity"));

        //In AddPatientActivity, test search patients function.
        solo.enterText((EditText) solo.getView(R.id.add_patient_username), "test5");
        solo.clickOnButton("Search");

        //Check if the search result is correct
        if (!(solo.waitForText("Cannot"))){
            solo.clickOnButton("Add");
        };

        solo.goBackToActivity("DoctorActivity");

        //Click on the first patient, click on the first problem,Click on the first record  to view record detail
        solo.clickInList(0);
        solo.clickInList(0);
        solo.clickInList(0);

        assertTrue(solo.waitForActivity("DoctorRecordDetailActivity"));

        solo.goBackToActivity("DoctorActivity");

        // Click the button search
        solo.clickOnView(solo.getView(R.id.action_search));

        // Choose the search type
        View spinner = solo.getView(Spinner.class, 0);
        solo.clickOnView(spinner);
        solo.clickOnView(solo.getView(TextView.class, 0));

        solo.clickOnView(solo.getView(R.id.search_searchView));

        // Search by the query "p"
        final SearchView searchView = (SearchView) solo.getView(R.id.search_searchView);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                searchView.setQuery("p", true);
            }
        });

        // Check if the search result is correct
        assertTrue(solo.waitForText("p1"));

        solo.goBackToActivity("DoctorActivity");
        solo.goBack();
        solo.goBack();

        solo.clickLongInList(4);

        solo.clickOnMenuItem("Delete");

        assertTrue(solo.waitForText("Succeed"));

        solo.clickOnView(solo.getView(R.id.fab));

        assertTrue(solo.waitForText("Refresh"));
    }
}
