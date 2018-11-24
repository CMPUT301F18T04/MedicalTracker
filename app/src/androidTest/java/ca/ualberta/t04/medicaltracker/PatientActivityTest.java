package ca.ualberta.t04.medicaltracker;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.robotium.solo.Solo;

import ca.ualberta.t04.medicaltracker.Activity.LoginActivity;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class PatientActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    public PatientActivityTest() {
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

    public void testPatientPage() throws Throwable {
        // Click the button register to sign up first
        solo.clickOnButton("Register");

        // Check if the app opens the correct page
        assertTrue(solo.waitForActivity("RegisterActivity"));


        // Enter the test account's information
        solo.enterText((EditText) solo.getView(R.id.register_username), "patient");
        solo.enterText((EditText) solo.getView(R.id.register_password), "123456");
        solo.enterText((EditText) solo.getView(R.id.register_confirmed_password), "123456");

        // Click button to sign up
        solo.clickOnView(solo.getView(R.id.register_button_signup));

        // If the text "Duplicated" occurs, then it means the account is already existed, then
        // the robot will use the account of patient to log in
        if(solo.waitForText("Duplicated")){
            solo.getCurrentActivity().finish();
        }

        solo.enterText((EditText) solo.getView(R.id.login_username), "patient");
        solo.enterText((EditText) solo.getView(R.id.login_password), "123456");
        solo.clickOnButton("Login");

        assertTrue(solo.waitForActivity("PatientActivity"));

        // Let the robot press the second button in the menu.
        // The first one is the search button
        solo.clickOnView(solo.getView(R.id.action_add));

        //solo.pressMenuItem(2);

        assertTrue(solo.waitForActivity("AddProblemActivity"));

        // Test to add a new problem
        solo.enterText((EditText) solo.getView(R.id.add_problem_title), "testProblem");
        solo.enterText((EditText) solo.getView(R.id.add_problem_description), "test");

        solo.clickOnView(solo.getView(R.id.add_problem_button_add));

        assertTrue(solo.waitForText("testProblem"));

        // Click to view all the records of the problem
        ListView problem_list = (ListView) solo.getView(R.id.main_page_list_view);
        View problem = problem_list.getChildAt(0);
        solo.clickOnView(problem);

        assertTrue(solo.waitForActivity("RecordHistoryActivity"));

        // Press the button add to add a new record
        solo.clickOnView(solo.getView(R.id.action_add));

        assertTrue(solo.waitForActivity("AddRecordActivity"));

        solo.enterText((EditText) solo.getView(R.id.add_record_title), "testRecord");
        solo.enterText((EditText) solo.getView(R.id.add_record_description), "test");

        solo.clickOnButton("Add");

        assertTrue(solo.waitForText("testRecord"));

        // Click the record to view the detail of the record
        ListView record_list = (ListView) solo.getView(R.id.record_history_list_view);
        View record = record_list.getChildAt(0);
        solo.clickOnView(record);

        assertTrue(solo.waitForActivity("RecordDetailActivity"));

        solo.clearEditText((EditText) solo.getView(R.id.addCommentEditText));
        solo.enterText((EditText) solo.getView(R.id.addCommentEditText), "testRecord2");
        solo.clearEditText((EditText) solo.getView(R.id.descriptionEditText));
        solo.enterText((EditText) solo.getView(R.id.descriptionEditText), "test2");

        solo.clickOnButton("Save");

        assertTrue(solo.waitForText("testRecord2"));

        solo.goBackToActivity("PatientActivity");

        // Click the button search
        solo.clickOnView(solo.getView(R.id.action_search));

        // Choose the search type
        View spinner = solo.getView(Spinner.class, 0);
        solo.clickOnView(spinner);
        solo.clickOnView(solo.getView(TextView.class, 0));

        solo.clickOnView(solo.getView(R.id.search_searchView));

        // Search by the query "test"
        final SearchView searchView = (SearchView) solo.getView(R.id.search_searchView);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                searchView.setQuery("test", true);
            }
        });

        // Check if the search result is correct
        assertTrue(solo.waitForText("testProblem"));

        // Click the first problem and view the detail of the first record
        solo.clickInList(0);
        solo.clickInList(0);

        assertTrue(solo.waitForActivity("RecordDetailActivity"));

        solo.goBack();
        solo.goBack();

        assertTrue(solo.waitForText("testProblem"));

        solo.goBackToActivity("PatientActivity");

        solo.clickInList(0);

    }

}
