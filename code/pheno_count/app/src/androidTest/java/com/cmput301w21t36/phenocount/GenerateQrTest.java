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
    public ActivityTestRule<com.cmput301w21t36.phenocount.MainActivity> rule =
            new ActivityTestRule<>(com.cmput301w21t36.phenocount.MainActivity.class, true, true);
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
     * Add an experiment to the list and check the name using assertTrue
     */
    @Test
    public void testGenerateQr(){
        // asserts that the current activity is the MainActivity
        solo.assertCurrentActivity("Wrong Activity", com.cmput301w21t36.phenocount.MainActivity.class);

        // create a new experiment
        solo.assertCurrentActivity("Wrong Activity", com.cmput301w21t36.phenocount.MainActivity.class);
        solo.clickOnImageButton(0);
        solo.clickOnText("Publish an Experiment");
        solo.enterText((EditText) solo.getView(R.id.expName), "Coin Flip");
        solo.enterText((EditText) solo.getView(R.id.expDesc), "Flipping a coin");
        solo.enterText((EditText) solo.getView(R.id.expRegion), "Canada");
        solo.clickOnView(solo.getView(R.id.radioBinomial));
        solo.enterText((EditText) solo.getView(R.id.expNum), "20");
        solo.clickOnView( solo.getView(R.id.okButton));

        // generate a QR for the experiment
        solo.clickOnText("Coin Flip");
        solo.sleep(4000);
        solo.clickOnView(solo.getView(R.id.qrButton));
        assertTrue(solo.getView(R.id.qrButton).isShown());

        // add a trial
        solo.clickOnMenuItem("Add Trial");
        solo.clickOnButton("Success");

        // generate a QR for an individual trial
        solo.clickOnMenuItem("See Results");
        solo.clickInList(0);
        solo.sleep(4000);
        assertTrue(solo.getView(R.id.qrView).isShown());
    }
}