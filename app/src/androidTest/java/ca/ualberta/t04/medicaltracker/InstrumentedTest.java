package ca.ualberta.t04.medicaltracker;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.searchbox.indices.DeleteIndex;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ca.ualberta.t04.medicaltracker", appContext.getPackageName());
    }

    /*
        !!!  Use Debug Mode to test this part  !!!
     */
    @Test
    public void signUpTest()
    {
        String testName = "testSignUp";

        Patient patient = new Patient(testName, "123");
        try {
            new ElasticSearchController.DeleteUserTask().execute(testName);
            Boolean done = new ElasticSearchController.SignUpTask().execute(patient).get();

            if(done){
                User user = new ElasticSearchController.SearchUserTask().execute(testName).get();
                assertTrue("User should be same as patient!", user.getUserName().equals(testName));
                return;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        assertTrue("Did not pass the test!",false);
    }
}
