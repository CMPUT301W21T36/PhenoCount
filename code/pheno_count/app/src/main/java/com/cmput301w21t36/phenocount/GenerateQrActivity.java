package com.cmput301w21t36.phenocount;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

// Use this to get here
//Intent i = new Intent(ExperimentActivity.this, GenerateQRActivity.class);
//i.putExtra("info", info);
//startActivityForResult(i, 1);

public class GenerateQrActivity extends AppCompatActivity {
    ImageView qrImage;
    Button generateButton;
    EditText inputText;
    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generate);

        info = getIntent().getSerializableExtra("info").toString(); // get information to be put into QR code

        qrImage = findViewById(R.id.imageView);

        //Just for testing
        generateButton = findViewById(R.id.generateButton);
        inputText = findViewById(R.id.inputText);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create QR Encoder with value to be encoded
                QRGEncoder qrgEncoder = new QRGEncoder(info, null, QRGContents.Type.TEXT, 500);
                try {
                    // Getting QR as Bitmap
                    Bitmap bitmap = qrgEncoder.getBitmap();
                    // Set QR to ImageView
                    qrImage.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Log.v("Exception", e.toString());
                }
            }
        });

    }
}