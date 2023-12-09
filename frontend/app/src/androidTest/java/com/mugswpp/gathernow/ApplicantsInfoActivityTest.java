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

import com.mugswpp.gathernow.main_ui.event_applicant_info.ApplicantsInfoActivity;

@RunWith(AndroidJUnit4.class)
public class ApplicantsInfoActivityTest {

    @Rule
    public ActivityScenarioRule<ApplicantsInfoActivity> activityRule = new ActivityScenarioRule<>(ApplicantsInfoActivity.class);

    @Test
    public void testApplicationFormActivity() {
        onView(withId(R.id.container1)).check(matches(isDisplayed()));
    }

}

