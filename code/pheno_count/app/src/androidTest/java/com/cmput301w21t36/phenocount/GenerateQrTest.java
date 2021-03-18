package com.cmput301w21t36.phenocount;

import android.app.Activity;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class GenerateQrTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());

        solo.clickOnView((Button) solo.getView(R.id.addButton));
        solo.enterText((EditText) solo.getView(R.id.expName), "Test Name");
        solo.enterText((EditText) solo.getView(R.id.expDesc), "Test Description");
        solo.enterText((EditText) solo.getView(R.id.expRegion), "Test Region");
        solo.clickOnView((Button) solo.getView(R.id.radioBinomial));
        solo.enterText((EditText) solo.getView(R.id.expNum), "2");
        solo.clickOnView((Button) solo.getView(R.id.okButton));
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
     * Add a city to the listview and check the city name using assertTrue
     * Clear all the cities from the listview and check again with assertFalse
     */
    @Test
    public void testGenerateQr(){
        //Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        // click the first experiment
        solo.clickInList(0);
        solo.clickOnView((Button) solo.getView(R.id.item4));
        solo.clickInList(0);



//        //Click add Button
//        solo.clickOnButton(R.id.addButton);
//        //Get view for EditText and enter a city name
//        solo.enterText((EditText) solo.getView(R.id.editText_name), "Edmonton");
//        solo.clickOnButton("CONFIRM"); //Select CONFIRM Button
//        solo.clearEditText((EditText) solo.getView(R.id.editText_name)); //Clear the EditText
//        /* True if there is a text: Edmonton on the screen, wait at least 2 seconds and
//        find minimum one match. */
//        assertTrue(solo.waitForText("Edmonton", 1, 2000));
//        solo.clickOnButton("ClEAR ALL"); //Select ClEAR ALL
//        //True if there is no text: Edmonton on the screen
//        assertFalse(solo.searchText("Edmonton"));
    }
}