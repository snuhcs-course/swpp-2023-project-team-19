package com.mugswpp.gathernow;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.mugswpp.gathernow.main_ui.event_creation.CreateSuccessfulActivity;

import org.junit.Rule;
import org.junit.Test;

public class CreateSuccessfulTest {
    @Rule
    public ActivityScenarioRule<CreateSuccessfulActivity> activityRule = new ActivityScenarioRule<>(CreateSuccessfulActivity.class);

    @Test
    public void testLayout() {
        onView(withId(R.id.create_successful_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void testCloseButton() {
        onView(withId(R.id.close)).perform(click());
        onView(withId(R.id.home_layout)).check(matches(isDisplayed()));
    }
}

