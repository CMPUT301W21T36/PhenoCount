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



public class ScanQRActivity extends AppCompatActivity {
    Experiment experiment;
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        experiment = (Experiment) getIntent().getSerializableExtra("experiment");

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
                i = new Intent(ScanQRActivity.this, BinomialActivity.class);
                i.putExtra("QrSuccess", experiment);
                startActivityForResult(i, 1);
                break;
            case "failure":
                i = new Intent(ScanQRActivity.this, BinomialActivity.class);
                i.putExtra("QrFailure", experiment);
                startActivityForResult(i, 1);
                break;
            default:
                try {
                    int count = Integer.parseInt(result);
                    i = new Intent(ScanQRActivity.this, CountActivity.class);
                    i.putExtra("QrExperiment", experiment);
                    i.putExtra("count", count);
                    startActivityForResult(i, 1);
                    break;
                }
                catch (Exception e) {
                    Toast.makeText(ScanQRActivity.this,"Invalid Entry", Toast.LENGTH_LONG).show();
                    finish();
                    break;
                }
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
            experiment = (Experiment) data.getSerializableExtra("experiment");
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