// CMPUT301 TAs, LAB7, 2021-03-11, Open Source, https://eclass.srv.ualberta.ca/pluginfile.php/6714096/mod_resource/content/1/Lab7%20Instructions.pdf

package com.cmput301w21t36.phenocount;

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

// Work in Progress
public class UpdateProfileTest {
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
     * Check that we can actually access the Profile page
     */
    @Test
    public void openProfile() {
        // asserts that the current activity is the MainActivity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // click on profile button
        solo.clickOnImageButton(0);
        solo.waitForText("User Profile");
        solo.clickOnText("User Profile");
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
    }

    /**
     * Update both text fields of Username and ContactInfo and ensure
     * they are updated to what was typed
     */
    @Test
    public void editProfile() {
        // Same as openProfile()
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnImageButton(0);
        solo.waitForText("User Profile");
        solo.clickOnText("User Profile");
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);

        // Open dialog box to edit
        solo.clickOnView((Button) solo.getView(R.id.editInfoButton));
        solo.waitForDialogToOpen();

        // Clear the information present and type in a name
        solo.clearEditText((EditText) solo.getView(R.id.editUsername));
        solo.enterText((EditText) solo.getView(R.id.editUsername), "John");

        // Repeat for contact information
        solo.clearEditText((EditText) solo.getView(R.id.editContact));
        solo.enterText((EditText) solo.getView(R.id.editContact), "78012113345");

        // Exit dialog
        solo.clickOnButton("Ok");

        // Check that the correct information is present
        assertTrue(solo.searchText("John"));
        assertTrue(solo.searchText("78012113345"));

        // Also check that we cannot find any wrong information on the profile page
        assertFalse(solo.searchText("Abby"));
        assertFalse(solo.searchText("abgy@gmail.com"));


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
