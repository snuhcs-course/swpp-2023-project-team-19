package com.mugswpp.gathernow;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FilterFragmentTest {

    @Rule
    public ActivityScenarioRule<FragHome> activityRule = new ActivityScenarioRule<>(FragHome.class);

    @Test
    public void testFilterFragmentUI() {
        // Trigger the filter fragment to appear
        onView(withId(R.id.filter_button)).perform(click());

        // Verify that the filter options are displayed correctly
        onView(withId(R.id.frag_filter_layout)).check(matches(isDisplayed()));

        // Verify that the filter chips are displayed
        onView(withId(R.id.language_filter)).check(matches(isDisplayed()));

        // Test selecting/deselecting chips
        onView(withId(R.id.l_Korean)).perform(ViewActions.click());
        onView(withId(R.id.l_Korean)).perform(ViewActions.click());
        onView(withId(R.id.l_English)).perform(ViewActions.click());
        onView(withId(R.id.l_English)).perform(ViewActions.click());

        onView(withId(R.id.et_sports)).perform(ViewActions.click());
        onView(withId(R.id.et_sports)).perform(ViewActions.click());
        onView(withId(R.id.et_leisure)).perform(ViewActions.click());
        onView(withId(R.id.et_leisure)).perform(ViewActions.click());

        onView(withId(R.id.reset_button)).check(matches(isDisplayed()));
        onView(withId(R.id.reset_button)).perform(ViewActions.click());

        onView(withId(R.id.bottomSheetCloseButton)).check(matches(isDisplayed()));
        onView(withId(R.id.bottomSheetCloseButton)).perform(ViewActions.click());
        onView(withId(R.id.home_layout)).check(matches(isDisplayed()));
    }
}
