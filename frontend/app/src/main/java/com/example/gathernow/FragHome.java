package com.example.gathernow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gathernow.main_ui.event_creation.EventCreateActivity;
import com.example.gathernow.main_ui.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import android.os.Bundle;
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
        }
        EventHome eventHome = new EventHome();
        EventSearch eventSearch = new EventSearch();
        EventCreateActivity eventCreateActivity = new EventCreateActivity();
        ProfileActivity profileActivity = new ProfileActivity();
        @Override
        public boolean onNavigationItemSelected (@NonNull MenuItem item){
            if (item.getItemId() == R.id.menu_search) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, eventSearch)
                        .commit();
                return true;
            } else if (item.getItemId() == R.id.menu_create) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, eventCreateActivity)
                        .commit();
                return true;
            } else if (item.getItemId() == R.id.menu_events) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, eventHome)
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

