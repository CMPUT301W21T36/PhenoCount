package com.cmput301w21t36.phenocount;

import android.app.Activity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TrialTest {
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
    public void checkTrial(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnImageButton(0);
        solo.clickOnText("Publish an Experiment");
        solo.enterText((EditText) solo.getView(R.id.expName), "Sheep Count");
        solo.enterText((EditText) solo.getView(R.id.expDesc), "How many sheeps can you see?");
        solo.enterText((EditText) solo.getView(R.id.expRegion), "Middle East");
        solo.clickOnView((Button) solo.getView(R.id.radioCount));
        solo.enterText((EditText) solo.getView(R.id.expNum), "20");
        solo.clickOnView((Button) solo.getView(R.id.okButton));


        solo.clickOnText("Sheep Count");
        solo.clickOnMenuItem("See Results");
        assertFalse(solo.searchText("1"));
        solo.goBack();

        solo.clickOnMenuItem("Add Trial");
        solo.clickOnButton("Record");

        solo.clickOnMenuItem("See Results");
        assertTrue(solo.searchText("1"));

    }

}
