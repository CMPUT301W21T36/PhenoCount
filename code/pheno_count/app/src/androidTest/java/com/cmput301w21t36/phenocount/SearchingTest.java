package com.cmput301w21t36.phenocount;
import com.cmput301w21t36.phenocount.MainActivity;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SearchingTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class,true,true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Check that searching can be opened
     */
    @Test
    public void openSearching() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnImageButton(0);
        solo.waitForText("Search For Experiments");
        solo.clickOnText("Search For Experiments");
        solo.assertCurrentActivity("Wrong Activity", SearchingActivity.class);
    }

    /**
     * Add an experiment and see if it can be searched for and ensure nothing else won't
     */
    @Test
    public void searchExperiment() {
        // Make a new experiment for searching
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnImageButton(0);
        solo.waitForText("Publish an Experiment");
        solo.clickOnText("Publish an Experiment");
        solo.enterText((EditText) solo.getView(R.id.expDesc), "Count how many Blue jays you saw today");
        solo.clickOnView(solo.getView(R.id.radioCount));
        solo.enterText((EditText) solo.getView(R.id.expNum), "10");
        solo.clickOnView(solo.getView(R.id.okButton));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);


        // Now actually search for it
        solo.clickOnImageButton(0);
        solo.waitForText("Search For Experiments");
        solo.clickOnText("Search For Experiments");
        solo.assertCurrentActivity("Wrong Activity", SearchingActivity.class);

        // Type in keyword
        solo.clickOnView(solo.getView(R.id.searchView));
        solo.enterText(0, "blue jay");
        solo.pressSoftKeyboardSearchButton();

        // Show the experiment is there
        assertTrue(solo.searchText("Count how many Blue"));
        // Make sure that something that isn't there won't be there
        assertFalse(solo.searchText("This will not show because no experiment has this"));
    }



    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}

