package com.example.gathernow;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.action.ViewActions.click;

import com.example.gathernow.authenticate.login.LogInActivity;

@RunWith(AndroidJUnit4.class)
public class LogInActivityTest {

    @Rule
    public ActivityScenarioRule<LogInActivity> activityRule = new ActivityScenarioRule<>(LogInActivity.class);

    @Test
    public void testLogInActivity() {
        onView(withId(R.id.loginRootLayout)).check(matches(isDisplayed()));
        onView(withId(R.id.email)).perform(typeText("test@yahoo.com"));
        onView(withId(R.id.password)).perform(typeText("12345678"));

        onView(withId(R.id.login)).check(matches(isDisplayed())).check(matches(isEnabled()));
        onView(withId(R.id.login)).perform(click());
        onView(withId(R.id.login)).check(matches(isClickable()));
    }

}

