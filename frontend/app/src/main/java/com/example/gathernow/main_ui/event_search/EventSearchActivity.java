package com.example.gathernow.main_ui.event_search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gathernow.FragHome;
import com.example.gathernow.R;
import com.google.android.material.textfield.TextInputEditText;

public class EventSearchActivity extends AppCompatActivity {

    private TextInputEditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_search);

        // If want to implement filter in search activity,
        // the confirm button should not lead to filter activity but stay at search activity
        // just skip this feature for now...

        /*
        ImageButton filterButton = findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment bottomSheetDialogFragment = new FilterFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });*/

        searchBar = findViewById(R.id.search_bar);
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // User pressed "Done" on the keyboard or clicked "Enter"
                    String searchText = searchBar.getText().toString();
                    Log.d("EventSearch", "searchText: " + searchText);
                    // Do something with the search text
                    performSearch(searchText);
                    hideKeyboard();
                    return true; // Consume the event
                }
                return false; // Continue processing the event
            }
        });


        // TODO: if no event for selected keywords & filters, show sad frog; else show related events
        RelativeLayout no_event_layout = findViewById(R.id.no_event_layout);
        TextView go_home_text = (TextView) no_event_layout.findViewById(R.id.go_home_text);
        go_home_text.setPaintFlags(go_home_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        go_home_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FragHome.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void performSearch(String searchText) {
        // TODO: Implement search logic here
        // This method will be called when the user presses "Enter" on the keyboard
        // Use the searchText parameter to perform the search
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
    }
}