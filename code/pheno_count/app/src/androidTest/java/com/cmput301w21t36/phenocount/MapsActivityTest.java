package com.cmput301w21t36.phenocount;

import android.app.Activity;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

public class MapsActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class,true,true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkLocation(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        
        solo.clickOnView((Button) solo.getView(R.id.addButton));
        solo.enterText((EditText) solo.getView(R.id.expName), "Maps Test Name");
        solo.enterText((EditText) solo.getView(R.id.expDesc), "Maps Test Description");
        solo.enterText((EditText) solo.getView(R.id.expRegion), "Maps Test Region");
        solo.clickOnView((Button) solo.getView(R.id.radioCount));
        solo.enterText((EditText) solo.getView(R.id.expNum), "20");
        solo.clickOnView((CheckBox) solo.getView(R.id.geoCheckBox));
        solo.clickOnView((Button) solo.getView(R.id.okButton));

        solo.clickOnText("Maps Test Name");
        solo.clickOnMenuItem("Add Trial");

        solo.clickOnView((Button) solo.getView(R.id.locationbtn2));
        solo.sleep(12000);
        solo.clickOnView((Button) solo.getView(R.id.addLocationButton));

        assertFalse(solo.searchText("NOT ADDED"));

    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
