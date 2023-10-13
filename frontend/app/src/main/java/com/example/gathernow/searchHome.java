package com.example.gathernow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class searchHome extends AppCompatActivity{
        private BottomNavigationView bottomNavigationView;

        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search_home);

            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.menu_search) {
                        //Intent intent = new Intent(v.getContext(), signUp.class);
                        //startActivity(intent);
                        return true;
                    } else if (item.getItemId() == R.id.menu_create) {
                        //Intent intent = new Intent(v.getContext(), signUp.class);
                        //startActivity(intent);
                        return true;
                    } else if (item.getItemId() == R.id.menu_events) {
                        //Intent intent = new Intent(v.getContext(), signUp.class);
                        //startActivity(intent);
                        return true;
                    } else if (item.getItemId() == R.id.menu_profile) {
                        //Intent intent = new Intent(v.getContext(), signUp.class);
                        //startActivity(intent);
                        return true;
                    }return false;
                }
            });

        }

}
