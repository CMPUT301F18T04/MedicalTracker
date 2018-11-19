package ca.ualberta.t04.medicaltracker;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.t04.medicaltracker.Controller.ElasticSearchController;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void deleteUser(){
        ElasticSearchController.deleteIndex(Util.INDEX_NAME);
    }
}
