package com.example.gathernow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gathernow.main_ui.event_creation.EventCreateActivity;
import com.example.gathernow.main_ui.events.EventsActivity;
import com.example.gathernow.main_ui.home.HomeActivity;
import com.example.gathernow.main_ui.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class FragHome extends AppCompatActivity implements BottomNavigationView
                   .OnNavigationItemSelectedListener {
        BottomNavigationView bottomNavigationView;
        // prevent user from going back to previous screens
        private boolean isBackPressed = false;

        @Override
        public void onBackPressed() {
            if (isBackPressed) {
                finishAffinity();
                System.exit(0);
            }
            this.isBackPressed = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            // reset back pressed after 2 seconds
            new android.os.Handler().postDelayed(
                    () -> isBackPressed = false, 2000);
        }
        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_frag_home);

            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setOnItemSelectedListener(this);
            bottomNavigationView.setSelectedItemId(R.id.menu_search);

            Intent intent = getIntent();
            String targetFragment = intent.getStringExtra("targetFragment");

            Log.e ("BackButton", "targetFragment: " + targetFragment);

            if (targetFragment != null){
                if (targetFragment.equals("event")) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flFragment, new EventsActivity())
                            .commit();
                    bottomNavigationView.setSelectedItemId(R.id.menu_events);
                } else if (targetFragment.equals("profile")) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flFragment, new ProfileActivity())
                            .commit();
                    bottomNavigationView.setSelectedItemId(R.id.menu_profile);
                }
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new HomeActivity())
                        .commit();
            }


        }
        EventsActivity eventsActivity = new EventsActivity();
        HomeActivity homeActivity = new HomeActivity();
        EventCreateActivity eventCreateActivity = new EventCreateActivity();
        ProfileActivity profileActivity = new ProfileActivity();

        @Override
        public boolean onNavigationItemSelected (@NonNull MenuItem item){
            Bundle bundle = new Bundle();
            if (item.getItemId() == R.id.menu_search) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, homeActivity)
                        .commit();
                return true;
            } else if (item.getItemId() == R.id.menu_create) {
                bundle.putBoolean("fromFragHome", true);
                eventCreateActivity.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, eventCreateActivity)
                        .commit();
                return true;
            } else if (item.getItemId() == R.id.menu_events) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, eventsActivity)
                        .commit();
                return true;
            } else if (item.getItemId() == R.id.menu_profile) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, profileActivity)
                        .commit();
                return true;
            }
            return false;
        }
}

