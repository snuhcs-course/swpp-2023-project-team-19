package com.example.gathernow;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

// import androidx.test.rule.ActivityTestRule;

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

import com.example.gathernow.main_ui.event_filter.EventFilterActivity;

import java.util.ArrayList;
import java.util.Arrays;

@RunWith(AndroidJUnit4.class)
public class EventFilterActivityTest {

    @Rule
    public ActivityScenarioRule<EventFilterActivity> activityRule = new ActivityScenarioRule<>(EventFilterActivity.class);

    @Before
    public void setUp() throws Exception {

        // Create an intent that mimics the one from your snippet
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), EventFilterActivity.class);
        intent.putExtra("isFreeEvent", true); // Example, set as needed
        intent.putStringArrayListExtra("selectedLanguageChips", new ArrayList<>(Arrays.asList("English", "Spanish"))); // Example values
        intent.putStringArrayListExtra("selectedEventTypeChips", new ArrayList<>(Arrays.asList("Sports", "Culture"))); // Example values
        intent.putStringArrayListExtra("selectedDateChips", new ArrayList<>(Arrays.asList("This week", "Next week"))); // Example values
        intent.putStringArrayListExtra("selectedTimeChips", new ArrayList<>(Arrays.asList("Morning", "Afternoon"))); // Example values

        // Launch the activity with the intent
        ActivityScenario<EventFilterActivity> scenario = ActivityScenario.launch(intent);
    }

    // test if the activity is launched
    @Test
    public void testEventFilterActivity() {
        // Now the activity is started with the required state, proceed to test the UI
        onView(withId(R.id.filter_layout)).check(matches(isDisplayed()));
    }

    // test button click
    @Test
    public void testButtonClick() {
        // Assuming you have a button with an ID `button_id` in EventFilterActivity
        onView(withId(R.id.filter_button)).perform(click());
        // assert that the button click does what you expect: in this case, it should launch a fragment
        onView(withId(R.id.frag_filter_layout)).check(matches(isDisplayed()));
    }

}
