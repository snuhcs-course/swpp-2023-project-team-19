package com.mugswpp.gathernow;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import android.content.Intent;

import com.mugswpp.gathernow.main_ui.event_info.EventInfoActivity;


@RunWith(AndroidJUnit4.class)
public class EventInfoActivityTest {

    @Before
    public void setUp() {
        // Create an intent that mimics the intent passed from the previous activity
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), EventInfoActivity.class);
        intent.putExtra("userId", 4); // Example, set as needed
        intent.putExtra("eventId", 3); // Example values
        intent.putExtra("sourceFrag", "home");

        // Launch the activity with the intent
        ActivityScenario<EventInfoActivity> scenario = ActivityScenario.launch(intent);
    }

    @Test
    public void testEventInfoActivity() {
        onView(withId(R.id.event_info_layout)).check(matches(isDisplayed()));
    }

}

