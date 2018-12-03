package ca.ualberta.t04.medicaltracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.robotium.solo.Solo;

import java.util.Date;
import java.util.HashMap;

import ca.ualberta.t04.medicaltracker.Activity.LoginActivity;
import ca.ualberta.t04.medicaltracker.Controller.DataController;
import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;
import ca.ualberta.t04.medicaltracker.Model.Record;

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
        ElasticSearchController.deleteUser("patient001");
        // Click the button register to sign up first
        solo.clickOnButton("Register");

        // Check if the app opens the correct page
        assertTrue(solo.waitForActivity("RegisterActivity"));


        // Enter the test account's information
        solo.enterText((EditText) solo.getView(R.id.register_username), "patient001");

        // Click button to sign up
        solo.clickOnView(solo.getView(R.id.register_button_signup));

        solo.enterText((EditText) solo.getView(R.id.login_username), "patient001");

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

        if(solo.waitForText("Google"))
            solo.clickOnButton("OK");
        solo.enterText((EditText) solo.getView(R.id.add_record_title), "testRecord");
        solo.enterText((EditText) solo.getView(R.id.add_record_description), "test");

        solo.clickOnView(solo.getView(R.id.add_record_body_location));
        solo.clickOnView(solo.getView(R.id.body_location_head));

        solo.clickOnView(solo.getView(R.id.record_add_location));

        solo.goBackToActivity("AddRecordActivity");

        solo.clickOnButton("Add");

        HashMap<Bitmap, String> bitmapStringHashMap = new HashMap<>();

        Bitmap bitmap1 = BitmapFactory.decodeResource(InstrumentationRegistry.getTargetContext().getResources(), R.drawable.ic_menu_camera);
        Bitmap bitmap2 = BitmapFactory.decodeResource(InstrumentationRegistry.getTargetContext().getResources(), R.drawable.ic_menu_send);
        bitmapStringHashMap.put(bitmap1, "a");
        bitmapStringHashMap.put(bitmap2, "b");
        HashMap<Bitmap, Boolean> frontBack = new HashMap<>();
        frontBack.put(bitmap1, true);
        frontBack.put(bitmap2, false);
        final Record testRecord = new Record("testRecord", new Date(), "test", bitmapStringHashMap, frontBack, null, BodyLocation.Head);
        // Here, since Robotium cannot take a photo, so adding a record by code
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                DataController.getRecordList().get("problem1").addRecord(testRecord);
            }
        });


        solo.goBackToActivity("RecordHistoryActivity");

        assertTrue(solo.waitForText("testRecord"));

        // Click the record to view the detail of the record
        ListView record_list = (ListView) solo.getView(R.id.record_history_list_view);
        View record = record_list.getChildAt(0);
        solo.clickOnView(record);

        assertTrue(solo.waitForActivity("RecordDetailActivity"));

        if(solo.waitForText("Google"))
            solo.clickOnButton("OK");

        solo.clearEditText((EditText) solo.getView(R.id.addCommentEditText));
        solo.enterText((EditText) solo.getView(R.id.addCommentEditText), "testRecord2");
        solo.clearEditText((EditText) solo.getView(R.id.descriptionEditText));
        solo.enterText((EditText) solo.getView(R.id.descriptionEditText), "test2");

        solo.clickOnView(solo.getView(R.id.recordImageView));

        solo.waitForActivity("SlideShowActivity");

        solo.goBackToActivity("RecordDetailActivity");

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

        if(solo.waitForText("Google"))
            solo.clickOnButton("OK");

        solo.goBack();
        solo.goBack();

        assertTrue(solo.waitForText("testProblem"));

        solo.goBackToActivity("PatientActivity");

    }

}
