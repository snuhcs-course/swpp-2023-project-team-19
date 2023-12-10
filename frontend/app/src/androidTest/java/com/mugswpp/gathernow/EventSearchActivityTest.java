package com.mugswpp.gathernow;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.content.Intent;

import com.mugswpp.gathernow.main_ui.event_search.EventSearchActivity;

@RunWith(AndroidJUnit4.class)
public class EventSearchActivityTest {

    @Rule
    public ActivityScenarioRule<EventSearchActivity> activityRule = new ActivityScenarioRule<>(EventSearchActivity.class);

    @Before
    public void setUp() throws Exception {

        // Create an intent that mimics the one from your snippet
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), EventSearchActivity.class);

        // Launch the activity with the intent
        ActivityScenario<EventSearchActivity> scenario = ActivityScenario.launch(intent);
    }


    // test if the activity is launched
    @Test
    public void testEventFilterActivity() {
        // Now the activity is started with the required state, proceed to test the UI
        onView(withId(R.id.search_layout)).check(matches(isDisplayed()));
    }

    // test if the search bar is displayed
    @Test
    public void testEnterKeyPress() {
        // Simulate user entering a search query
        onView(withId(R.id.search_bar))
                .perform(ViewActions.typeText("test query")) // Type the text into the search bar
                .perform(ViewActions.pressImeActionButton()); // Press the IME action button (Enter key)

        // assertEquals("test query", activityRule.getScenario().getResult().getResultData().getStringExtra("query"));
        // the test of user input query is implemented under unit test
    }

}
