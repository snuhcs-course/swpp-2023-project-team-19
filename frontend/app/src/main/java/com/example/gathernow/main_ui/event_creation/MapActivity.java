package com.example.gathernow.main_ui.event_creation;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gathernow.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private NaverMap naverMap;
    private Marker selectedMarker;
    private LatLng selectedLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.mapView);

        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(NaverMap naverMap) {
                    MapActivity.this.naverMap = naverMap;

                    // ... (Previous code)

                    naverMap.setOnMapClickListener((point, coord) -> {
                        if (selectedMarker != null) {
                            selectedMarker.setMap(null);
                        }

                        // Update the selected location
                        selectedLocation = coord;

                        // Add a marker at the clicked location
                        selectedMarker = new Marker();
                        selectedMarker.setPosition(coord);
                        selectedMarker.setMap(naverMap);

                        // You can now use 'selectedLocation' as the selected location (LatLng)
                        // ...

                        // For example, you can return the selected location to the calling activity
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("selectedLocation", selectedLocation);
                        setResult(Activity.RESULT_OK, resultIntent);
                    });
                }
            });
        }
    }

    public void onConfirmClick(View view) {
        // Check if the NaverMap instance is available
        if (naverMap != null) {
            // Get the current camera position (selected location)
            CameraPosition cameraPosition = naverMap.getCameraPosition();
            LatLng selectedLocation = cameraPosition.target;

            // Create an intent to pass the selected location back to EventCreateActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedLocation", selectedLocation);

            // Set the result to be sent back
            setResult(Activity.RESULT_OK, resultIntent);

            // Finish MapActivity
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mapView != null) {
            mapView.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mapView != null) {
            mapView.onStop();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }
}
