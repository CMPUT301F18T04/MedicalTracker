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
        solo.enterText((EditText) solo.getView(R.id.register_username),"doctor000");

        RadioButton rb = (RadioButton) solo.getView(R.id.register_doctor);
        solo.clickOnView(rb);

        //Click button to sign up
        solo.clickOnView(solo.getView(R.id.register_button_signup));

        //If the text "Duplicated" occurs, then it means the account is already existed, then
        //the robot will use the account of doctor to log in
        if (solo.waitForText("Duplicated")){
            solo.getCurrentActivity().finish();
        }

        //Log in
        solo.enterText((EditText) solo.getView(R.id.login_username), "doctor000");
        solo.clickOnButton("Login");
        assertTrue(solo.waitForActivity("DoctorActivity"));

        //Click on the first patient, click on the first problem,Click on the first record  to view record detail
        solo.clickInList(0);
        solo.clickInList(0);
        solo.clickInList(0);
        assertTrue(solo.waitForActivity("DoctorRecordDetailActivity"));

       //Click on Map ImageView to checkout location
        solo.clickOnView(solo.getView(R.id.doctor_record_detail_view_location));
        assertTrue(solo.waitForActivity("MapViewActivity"));

        //View all Locations of a patient.
        solo.clickOnView(solo.getView(R.id.all_locations));
        assertTrue(solo.waitForText("Move around"));

        //In AddPatientActivity, test search patients function.
        //solo.enterText((EditText) solo.getView(R.id.add_patient_username), "test5");
        //solo.clickOnButton("Search");


        //View the device location.
        solo.clickOnView(solo.getView(R.id.my_location));
        assertTrue(solo.waitForText("You are here"));

        //Go back to doctor record detail activity.
        solo.goBack();

        //Click comment button in the bottom.
        solo.clickOnView(solo.getView(R.id.doctorCommentButton));
        solo.enterText((EditText) solo.getView(R.id.addCommentEditText), "This looks not good.");
        solo.clickOnButton("OK");
        assertTrue(solo.waitForText("Successfully"));

        //Click the image showing in the upper right to checkout slide show activity.
        solo.clickOnView(solo.getView(R.id.recordImageView));
        assertTrue(solo.waitForActivity("SlideShowActivity"));
        solo.scrollToSide(Solo.LEFT);
        solo.scrollToSide(Solo.RIGHT);

        solo.goBack();
        solo.goBack();
        solo.goBack();
        solo.goBack();

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

        // Check teh refresh function.
        solo.goBackToActivity("DoctorActivity");
        solo.clickOnView(solo.getView(R.id.fab));
        assertTrue(solo.waitForText("Refresh"));
    }
}
