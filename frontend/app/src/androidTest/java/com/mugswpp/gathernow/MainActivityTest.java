package com.mugswpp.gathernow;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testEventFilterActivity() {
        onView(withId(R.id.main_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void testSignUpButton() {
        onView(withId(R.id.signupButton)).perform(click());
        onView(withId(R.id.signupRootLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginButton() {
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.loginRootLayout)).check(matches(isDisplayed()));
    }

}
