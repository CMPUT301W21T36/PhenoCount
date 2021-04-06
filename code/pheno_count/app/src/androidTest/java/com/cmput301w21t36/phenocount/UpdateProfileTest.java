// CMPUT301 TAs, LAB7, 2021-03-11, Open Source, https://eclass.srv.ualberta.ca/pluginfile.php/6714096/mod_resource/content/1/Lab7%20Instructions.pdf
package com.cmput301w21t36.phenocount;

import android.app.Activity;
import android.widget.Button;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// Work in Progress
public class UpdateProfileTest {
    private Solo solo;

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

    @Test
    public void openProfile() {
        // asserts that the current activity is the MainActivity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // click on profile button
        solo.clickOnView((Button) solo.getView(R.id.profileButton));
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
    }

    @Test
    public void editProfile() {
        // Work in progress
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView((Button) solo.getView(R.id.profileButton));
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);

        solo.clickOnView((Button) solo.getView(R.id.profileButton));
        solo.enterText((EditText) solo.getView(R.id.expName), "Red Cars");

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
