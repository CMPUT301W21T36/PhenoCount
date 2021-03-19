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
        // asserts that the current activity is the MainActivity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // create a new experiment
        solo.clickOnView((Button) solo.getView(R.id.addButton));
        solo.enterText((EditText) solo.getView(R.id.expName), "Coin Flip");
        solo.enterText((EditText) solo.getView(R.id.expDesc), "Flipping a coin");
        solo.enterText((EditText) solo.getView(R.id.expRegion), "Canada");
        solo.clickOnView((Button) solo.getView(R.id.radioBinomial));
        solo.enterText((EditText) solo.getView(R.id.expNum), "2");
        solo.clickOnView((Button) solo.getView(R.id.okButton));

        // add a trial
        solo.clickOnText("Coin Flip");
        solo.clickOnMenuItem("Add Trial");
        solo.clickOnButton("Success");

        // generate qr
        solo.clickOnMenuItem("See Results");
        solo.clickInList(0);
    }
}