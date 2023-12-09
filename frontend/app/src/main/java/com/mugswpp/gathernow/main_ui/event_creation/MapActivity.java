package com.mugswpp.gathernow.main_ui.event_creation;

import static com.mugswpp.gathernow.main_ui.event_creation.TranslateAPI.translate_address;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import com.mugswpp.gathernow.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.LocationTrackingMode;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private NaverMap naverMap;
    private Marker selectedMarker;
    private LatLng selectedLocation;
    private String locationName;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private String apiKeyId = ""; // Replace with your Naver API client ID
    private String apiKey = ""; // Replace with your Naver API client secret

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

                    // Check and request location permissions
                    if (isLocationPermissionGranted()) {
                        // Enable location tracking
                        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
                        // Get the last known location
                        LatLng lastKnownLocation = getLastKnownLocation();
                        if (lastKnownLocation != null) {
                            // Create a CameraPosition with target location and zoom level
                            CameraPosition cameraPosition = new CameraPosition(lastKnownLocation, 13f);
                            // Move the camera to the specified position
                            naverMap.moveCamera(CameraUpdate.toCameraPosition(cameraPosition).animate(CameraAnimation.Easing));
                        } else {
                            // Default location is Seoul
                            LatLng seoulLocation = new LatLng(37.5665, 126.9780);
                            CameraPosition defaultCameraPosition = new CameraPosition(seoulLocation, 13f);
                            naverMap.moveCamera(CameraUpdate.toCameraPosition(defaultCameraPosition).animate(CameraAnimation.Easing));
                            // Alternatively, you can show a message to the user
                            Toast.makeText(MapActivity.this, "Last known location not available. Showing default location.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        requestLocationPermission();
                    }

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

                        // Call the method to obtain and set the location name
                        parseLocationName(selectedLocation);
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

            // Create an intent to pass the selected location and location name back to EventCreateActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedLocation", selectedLocation);
            resultIntent.putExtra("locationName", locationName);

            // Show an AlertDialog to get additional information for locationName
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter a name for this location");
            builder.setMessage("e.g Building 302, Software Lab");
            // Set up the input field in the dialog
            final EditText input = new EditText(this);
            builder.setView(input);
            // Set up the buttons
            builder.setPositiveButton("OK", (dialog, which) -> {
                String additionalInfo = input.getText().toString().trim();
                if (additionalInfo.isEmpty()) {
                    resultIntent.putExtra("locationName", locationName);
                }
                else{
                    resultIntent.putExtra("locationName", additionalInfo + ", " + locationName);
                }
                // Set the result to be sent back
                setResult(Activity.RESULT_OK, resultIntent);
                // Finish MapActivity
                finish();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            // Show the AlertDialog
            builder.show();
        }
    }

    private void parseLocationName(LatLng coordinates) {
        // Use OkHttpClient to make an HTTP request to Naver Reverse Geocoding API
        OkHttpClient client = new OkHttpClient();

        // Build the URL with parameters
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc").newBuilder();
        urlBuilder.addQueryParameter("coords", coordinates.longitude + "," + coordinates.latitude);
        urlBuilder.addQueryParameter("output", "json");

        // Build the request with headers
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header("X-NCP-APIGW-API-KEY-ID", apiKeyId)
                .header("X-NCP-APIGW-API-KEY", apiKey)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Parse the JSON response using Gson
                    String responseBody = response.body().string();
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);

                    // Extract the location name from the JSON response
                    locationName = extractLocationName(jsonObject);
                } else {
                    // Handle unsuccessful response
                    Log.e("MapActivity", "Unsuccessful response");
                    locationName = "Link to Naver map";
                }
            }

            // Helper method to extract location name from the response
            private String extractLocationName(JsonObject jsonObject) {
                if (jsonObject.has("results")) {
                    JsonArray resultsArray = jsonObject.getAsJsonArray("results");
                    if (resultsArray.size() > 0) {
                        JsonObject firstResult = resultsArray.get(0).getAsJsonObject();
                        JsonObject region = firstResult.getAsJsonObject("region");

                        String area1 = extractNameFromRegion(region, "area1");
                        String area2 = extractNameFromRegion(region, "area2");
                        String area3 = extractNameFromRegion(region, "area3");
                        String area4 = extractNameFromRegion(region, "area4");

                        String koreanAddress = area1 + area2 + area3 + area4;
                        String englishAddress = translate_address(koreanAddress);
                        String finalAddress = "";
                        if(koreanAddress.isEmpty()){
                            finalAddress = "Link to Naver map";
                        }
                        else if (englishAddress == null) {
                            finalAddress = koreanAddress;
                        }
                        else{
                            finalAddress = englishAddress;
                        }
                        Log.d("MapActivity", "Address: " + koreanAddress);
                        Log.d("MapActivity", "Address: " + englishAddress);

                        // Concatenate the area names
                        return finalAddress;
                    }
                }
                return null;
            }


            private String extractNameFromRegion(JsonObject region, String areaKey) {
                if (region.has(areaKey)) {
                    JsonObject area = region.getAsJsonObject(areaKey);
                    if (area.has("name")) {
                        return area.get("name").getAsString() + " ";
                    }
                }
                return "";
            }
        });
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
            // Check and move the camera if location permissions are already granted
            if (naverMap != null && isLocationPermissionGranted()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
                LatLng lastKnownLocation = getLastKnownLocation();
                if (lastKnownLocation != null) {
                    CameraPosition cameraPosition = new CameraPosition(lastKnownLocation, 13f);
                    naverMap.moveCamera(CameraUpdate.toCameraPosition(cameraPosition).animate(CameraAnimation.Easing));
                }
            }
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

    private boolean isLocationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (naverMap != null) {
                    // Enable location tracking after permission is granted
                    naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
                }
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Nullable
    private LatLng getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            // Use the more accurate location if available, else use the other one
            Location lastKnownLocation = (gpsLocation != null) ? gpsLocation : networkLocation;
            if (lastKnownLocation != null) {
                return new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            }
        }
        return null;
    }
}
