package com.wgu.pa;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.wgu.pa.UI.VacationList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class VacationSearchUITest {
    @Rule
    public ActivityScenarioRule<VacationList> activityRule =
            new ActivityScenarioRule<>(VacationList.class);

    @Test
    public void whenSearchingAvailableVacation_thenMatchingSearchVacationResultsAreReturned() {
        // Perform a search for a existing entry
        onView(withId(R.id.search)).check(matches(isDisplayed())).perform(click());
        onView(withId(androidx.appcompat.R.id.search_src_text))
                .check(matches(isDisplayed()))
                .perform(typeText("China"));

        closeSoftKeyboard();

        onView(withText("China")).check(matches(isDisplayed()));
    }

    @Test
    public void whenSearchingNonExistentVacation_thenSearchReturnsNoResults() {
        // Perform a search for a non-existent entry
        onView(withId(R.id.search)).check(matches(isDisplayed())).perform(click());
        onView(withId(androidx.appcompat.R.id.search_src_text)).check(matches(isDisplayed()))
                .perform(typeText("Nonexistent"));

        closeSoftKeyboard();

        onView(withText("No results found")).check(matches(isDisplayed()));
    }
}

