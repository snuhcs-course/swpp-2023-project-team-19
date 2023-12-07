package com.mugswpp.gathernow;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.action.ViewActions.click;

import android.content.Intent;

import com.mugswpp.gathernow.main_ui.event_filter.EventFilterActivity;

import java.util.ArrayList;
import java.util.Arrays;

@RunWith(AndroidJUnit4.class)
public class EventFilterActivityTest {

    @Rule
    public ActivityScenarioRule<EventFilterActivity> activityRule = new ActivityScenarioRule<>(EventFilterActivity.class);

    @Before
    public void setUp() throws Exception {

        // Create an intent that mimics the intent passed from the previous activity
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), EventFilterActivity.class);
        intent.putExtra("isFreeEvent", true); // Example, set as needed
        intent.putStringArrayListExtra("selectedLanguageChips", new ArrayList<>(Arrays.asList("English", "Spanish"))); // Example values
        intent.putStringArrayListExtra("selectedEventTypeChips", new ArrayList<>(Arrays.asList("Sports", "Culture"))); // Example values
        intent.putStringArrayListExtra("selectedDateChips", new ArrayList<>(Arrays.asList("This week", "Next week"))); // Example values
        intent.putStringArrayListExtra("selectedTimeChips", new ArrayList<>(Arrays.asList("Morning", "Afternoon"))); // Example values

        // Launch the activity with the intent
        ActivityScenario<EventFilterActivity> scenario = ActivityScenario.launch(intent);
    }

    @Test
    public void testEventFilterActivity() {
        onView(withId(R.id.filter_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void testButtonClick() {
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.frag_filter_layout)).check(matches(isDisplayed()));
    }

}
