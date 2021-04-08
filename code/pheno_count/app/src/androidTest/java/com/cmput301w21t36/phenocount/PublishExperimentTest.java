// CMPUT301 TAs, LAB7, 2021-03-11, Open Source, https://eclass.srv.ualberta.ca/pluginfile.php/6714096/mod_resource/content/1/Lab7%20Instructions.pdf

package com.cmput301w21t36.phenocount;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *Test class for PublishExperimentActivity. All the UI tests are written here. Robotium test framework is
 *  used
 * @author  Anisha
 */
public class PublishExperimentTest {
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
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * To check if the experiment gets pushlished successfully
     *From the MainActivity, click on the addButton and enter the required fields
     * for an experiment and hit OK
     */
    @Test
    public void checkAddingExp(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        //solo.clickOnView(solo.getView(R.id.addButton));
        solo.assertCurrentActivity("Wrong Activity", PublishExperimentActivity.class);
        solo.enterText((EditText) solo.getView(R.id.expName), "Red Cars");
        solo.enterText((EditText) solo.getView(R.id.expDesc), "To note the number of red cars observed");
        solo.enterText((EditText) solo.getView(R.id.expRegion), "Edmonton");
        solo.clickOnView(solo.getView(R.id.radioCount));
        solo.enterText((EditText) solo.getView(R.id.expNum), "20");
        solo.clickOnView( solo.getView(R.id.okButton));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * To check the already published experiment in the owner's list
     */
    @Test
    public void checkExpListItem(){
        solo .assertCurrentActivity( "Wrong Activity" , MainActivity. class ) ;
        assertTrue(solo.searchText("Red Cars"));
    }

    /**
     * To check the cancelButton doesn't publish an experiment
     * Check with just pressing cancelButton on opening of PublishExperimentActivity
     * and then with adding data to the fields and pressing cancelButton
     */
    @Test
    public void testCancelButton(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        //solo.clickOnView(solo.getView(R.id.addButton));
        solo.assertCurrentActivity("Wrong Activity", PublishExperimentActivity.class);
        solo.clickOnView(solo.getView(R.id.cancelButton));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        //solo.clickOnView(solo.getView(R.id.addButton));
        solo.assertCurrentActivity("Wrong Activity", PublishExperimentActivity.class);
        solo.enterText((EditText) solo.getView(R.id.expName), "Pink Ballons");
        solo.enterText((EditText) solo.getView(R.id.expDesc), "To note the number of pink ballons observed");
        solo.enterText((EditText) solo.getView(R.id.expRegion), "Edmonton");
        solo.clickOnView(solo.getView(R.id.radioCount));
        solo.enterText((EditText) solo.getView(R.id.expNum), "20");
        solo.clickOnView(solo.getView(R.id.cancelButton));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertFalse(solo.searchText("Pink Ballons"));
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
