package com.example.gathernow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import android.os.Bundle;
import android.view.MenuItem;

public class fragHome extends AppCompatActivity implements BottomNavigationView
                   .OnNavigationItemSelectedListener {
        BottomNavigationView bottomNavigationView;

        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_frag_home);

            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setOnItemSelectedListener(this);
            bottomNavigationView.setSelectedItemId(R.id.menu_search);
        }
        eventHome eventHome = new eventHome();
        eventSearch eventSearch = new eventSearch();
        eventCreate eventCreate = new eventCreate();
        profileHome profileHome = new profileHome();
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
                        .replace(R.id.flFragment, eventCreate)
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
                        .replace(R.id.flFragment, profileHome)
                        .commit();
                return true;
            }
            return false;
        }
}

