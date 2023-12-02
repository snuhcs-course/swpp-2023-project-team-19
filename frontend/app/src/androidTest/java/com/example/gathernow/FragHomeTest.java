package com.example.gathernow;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
public class FragHomeTest {

    @Rule
    public ActivityScenarioRule<FragHome> activityRule = new ActivityScenarioRule<>(FragHome.class);

    /*
    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), FragHome.class);
        ActivityScenario<FragHome> scenario = ActivityScenario.launch(intent);
    }
     */

    // test if the activity is launched
    @Test
    public void testFragHomeActivity() {
        onView(withId(R.id.flFragment)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void testHome() {
        onView(withId(R.id.menu_search)).perform(click());
        onView(withId(R.id.home_layout)).check(matches(isDisplayed()));

        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.frag_filter_layout)).check(matches(isDisplayed()));
        pressBack();

        onView(withId(R.id.search_button)).perform(click());
        onView(withId(R.id.search_layout)).check(matches(isDisplayed()));
        pressBack();
    }

    @Test
    public void testCreate() {
        onView(withId(R.id.menu_create)).perform(click());
        onView(withId(R.id.event_create_container)).check(matches(isDisplayed()));

        onView(withId(R.id.event_image_upload)).check(matches(isClickable()));
        onView(withId(R.id.event_name)).check(matches(isClickable()));
        onView(withId(R.id.event_type)).check(matches(isClickable()));
        onView(withId(R.id.event_num_participants)).check(matches(isClickable()));
        onView(withId(R.id.event_date)).check(matches(isClickable()));
        onView(withId(R.id.event_time)).check(matches(isClickable()));
        onView(withId(R.id.event_language)).check(matches(isClickable()));
        onView(withId(R.id.event_location)).check(matches(isClickable()));
        onView(withId(R.id.event_description)).check(matches(isClickable()));
        onView(withId(R.id.post)).check(matches(isClickable()));
    }

    @Test
    public void testEvents() {
        onView(withId(R.id.menu_events)).perform(click());
        onView(withId(R.id.event_layout)).check(matches(isDisplayed()));

        onView(withId(R.id.confirmed_text)).perform(click());
        onView(withId(R.id.pending_text)).perform(click());
        onView(withId(R.id.no_event_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void testProfile() {
        onView(withId(R.id.menu_profile)).perform(click());
        onView(withId(R.id.container)).check(matches(isDisplayed()));
        onView(withId(R.id.discover_new)).check(matches(isClickable()));
        onView(withId(R.id.logout_button)).check(matches(isClickable()));
    }

}

