package com.cmput301w21t36.phenocount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/** REFERENCES
Ralf Kistner, 27-09-19,  Apache-2.0 License, https://github.com/journeyapps/zxing-android-embedded */

/*
 * Opens the camera in order to scan a barcode and then returns
 * the scanned data to be entered into the input field
 */

public class ScanBarcodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scan);

        // open the camera to start the scan
        new IntentIntegrator(ScanBarcodeActivity.this).initiateScan();
    }
    /**
     * Get the data from the scan
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) { // check that something got returned
            if(result.getContents() == null) { // check that data was scanned successfully
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                finish(); // go back to previous activity if nothing is scanned
            } else {
                // pass the scanned text back to the experiment
                Intent i = new Intent();
                i.putExtra("scannedText", result.getContents());
                setResult(RESULT_OK, i);
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}