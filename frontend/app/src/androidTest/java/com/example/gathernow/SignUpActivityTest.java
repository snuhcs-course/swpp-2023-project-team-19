package com.example.gathernow;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.intent.rule.IntentsTestRule;
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

import com.example.gathernow.authenticate.register.SignUpActivity;

@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {

    @Rule
    public ActivityScenarioRule<SignUpActivity> activityRule = new ActivityScenarioRule<>(SignUpActivity.class);

    @Test
    public void testSignUpActivity() {
        onView(withId(R.id.signupRootLayout)).check(matches(isDisplayed()));
        onView(withId(R.id.name)).perform(typeText("test"));
        onView(withId(R.id.email)).perform(typeText("test@yahoo.com"));
        onView(withId(R.id.password)).perform(typeText("12345678"));
        onView(withId(R.id.password_confirm)).perform(typeText("12345678"));

        onView(withId(R.id.signup)).check(matches(isDisplayed())).check(matches(isEnabled()));
        onView(withId(R.id.signup)).perform(click());
        onView(withId(R.id.signup)).check(matches(isClickable()));
    }

}
