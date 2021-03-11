package com.cmput301w21t36.phenocount;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;

public class ScanQRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open Camera to scan QR
                new IntentIntegrator(ScanQRActivity.this).initiateScan();
            }
        });
    }
}