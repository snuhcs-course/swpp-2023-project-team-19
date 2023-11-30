package com.example.gathernow;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.action.ViewActions.click;

import com.example.gathernow.main_ui.event_application_form.ApplicationFormActivity;

@RunWith(AndroidJUnit4.class)
public class ApplicationFormActivityTest {

    @Rule
    public ActivityScenarioRule<ApplicationFormActivity> activityRule = new ActivityScenarioRule<>(ApplicationFormActivity.class);

    @Test
    public void testApplicationFormActivity() {
        onView(withId(R.id.application_form_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void testEditText() {
        onView(withId(R.id.applicant_contact)).perform(typeText("1234567890\n"));
        onView(withId(R.id.applicant_message)).perform(typeText("Hello!"));
        onView(withId(R.id.send_application_button)).perform(click());
    }

}
