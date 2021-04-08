/** This TrialMapsActivity deals with the location functionality of viewing an experiment's
 * trials' locations in a map.
 * Context: Comes from Experiment info page.
 */
package com.cmput301w21t36.phenocount;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class TrialMapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mapFrag;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.activity_trial_maps);
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.trialsMap);
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        ArrayList<Trial> trials = (ArrayList<Trial>) getIntent().getSerializableExtra("trials");
        //checking if any locations were added to trial at all
        boolean locations_exist = false;
        for(Trial trial: trials){
            if(trial.getLongitude()!= 200 && trial.getLatitude() != 200){
                locations_exist = true;
                break;
            }
        }
        //conditions when no data to show on map
        if(trials.size() == 0 || !locations_exist){
            Toast.makeText(
                    TrialMapsActivity.this,
                    "No Locations to show.",
                    Toast.LENGTH_LONG).show();
            finish();
        }
        int i = 1;
        ArrayList<Marker> markers = new ArrayList<>();
        for(Trial trial : trials){
            if(trial.getLatitude() != 200.0 && trial.getLongitude() != 200.0){
            LatLng location = new LatLng(trial.getLatitude(),trial.getLongitude());
            Marker marker = googleMap.addMarker(new MarkerOptions().position(location).title("Trial "+ i));
            markers.add(marker);
            i++;
            }
        }
        //https://stackoverflow.com/a/14828739
        //to position map around markers
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        System.out.println("MARKERS "+ markers.size());
        if (!markers.isEmpty()) {
            LatLngBounds bounds = builder.build();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.12);
            CameraUpdate cu;
            if (markers.size() == 1)
                cu = CameraUpdateFactory.newLatLng(markers.get(0).getPosition());
            else
                cu = CameraUpdateFactory.newLatLngBounds(bounds,width,height,padding);

            googleMap.moveCamera(cu);
        }


    }
}