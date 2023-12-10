package com.mugswpp.gathernow;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.mugswpp.gathernow.main_ui.event_creation.MapActivity;

import org.junit.Rule;
import org.junit.Test;

public class MapActivityTest {
    @Rule
    public ActivityScenarioRule<MapActivity> activityRule = new ActivityScenarioRule<>(MapActivity.class);

    @Test
    public void testLayout() {
        onView(withId(R.id.map_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void testMapView() {
        onView(withId(R.id.mapView)).check(matches(isDisplayed()));
    }

    @Test
    public void testConfirmButton() {
        onView(withId(R.id.confirmButton)).perform(click());
    }
}


