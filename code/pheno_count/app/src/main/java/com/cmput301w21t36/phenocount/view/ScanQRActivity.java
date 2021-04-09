package com.cmput301w21t36.phenocount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

/**
 * Scans a Qr code and creates the appropriate trial
 *
 * Uses the code-scanner library
 * REFERENCES
 * Yuriy Budiyev, 06-12-18,  MIT License, https://github.com/yuriy-budiyev/code-scanner
 */

public class ScanQRActivity extends AppCompatActivity {
    private com.cmput301w21t36.phenocount.Experiment experiment;
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.activity_qr_scan);

        experiment = (com.cmput301w21t36.phenocount.Experiment) getIntent().getSerializableExtra("experiment");

        // ensure that the app has permissions to use the camera
        if (ContextCompat.checkSelfPermission(ScanQRActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(ScanQRActivity.this, new String[] {Manifest.permission.CAMERA}, 123);
        } else {
            startScanning();
        }
    }

    /**
     * Initiates the scan
     * Gets the scanned data and passes it back to the experiment
     */
    private void startScanning() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(ScanQRActivity.this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createTrial(result.getText());
                    }
                });
            }
        });
    }

    /**
     * Called when the uses allows the permission to use the camera
     * Starts the scanning
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
                startScanning();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Start the camera preview when resumed
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(mCodeScanner != null) {
            mCodeScanner.startPreview();
        }
    }

    /**
     * Stop the camera when not active to save resources
     */
    @Override
    protected void onPause() {
        if(mCodeScanner != null) {
            mCodeScanner.releaseResources();
        }
        super.onPause();
    }

    /**
     * Based on the scanned data, perform the appropriate actions
     * @param result
     */
    public void createTrial(String result){
        Intent i;
        switch (result) {
            case "success":
                i = new Intent(ScanQRActivity.this, com.cmput301w21t36.phenocount.BinomialActivity.class);
                i.putExtra("QrSuccess", experiment);
                startActivityForResult(i, 1);
                break;
            case "failure":
                i = new Intent(ScanQRActivity.this, com.cmput301w21t36.phenocount.BinomialActivity.class);
                i.putExtra("QrFailure", experiment);
                startActivityForResult(i, 1);
                break;
            case "count":
                i = new Intent(ScanQRActivity.this, com.cmput301w21t36.phenocount.CountActivity.class);
                i.putExtra("QrExperiment", experiment);
                startActivityForResult(i, 1);
                break;
        }
    }

    /**
     * Sends the experiment object and retrieves the updated object
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            experiment = (com.cmput301w21t36.phenocount.Experiment) data.getSerializableExtra("experiment");
            Intent i = new Intent();
            i.putExtra("experiment", experiment);
            setResult(Activity.RESULT_OK,i);
            finish();
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            System.out.println("No Data");
        }
    }
}