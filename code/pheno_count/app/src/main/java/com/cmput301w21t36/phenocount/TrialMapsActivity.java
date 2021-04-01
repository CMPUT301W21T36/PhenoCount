package com.cmput301w21t36.phenocount;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TrialMapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_maps);
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.trialsMap);
        mapFrag.getMapAsync(this);
        getSupportActionBar().setTitle("Trial Locations");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));

        LatLng delhi = new LatLng(28.704,77.102 );
        googleMap.addMarker(new MarkerOptions()
                .position(delhi)
                .title("Marker in Delhi"));
    }
}