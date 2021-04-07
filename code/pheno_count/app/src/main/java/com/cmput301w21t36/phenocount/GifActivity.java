package com.cmput301w21t36.phenocount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class GifActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setTheme(R.style.Theme_PhenoCount);
            setContentView(R.layout.activity_gif);

            final GifImageView explore = findViewById(R.id.explore_text);
            explore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<Trial> trials = (ArrayList<Trial>) getIntent().getSerializableExtra("gifTrials");

                    Intent i = new Intent(GifActivity.this, TrialMapsActivity.class);
                    i.putExtra("trials",trials);
                    startActivity(i);

                }
            });



        }
    }
