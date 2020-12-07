package edu.temple.dmhelper.warhorn;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiObjectNotFoundException;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.temple.dmhelper.MainActivity;
import edu.temple.dmhelper.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class WarhornLoginTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void warhornLoginTest() throws UiObjectNotFoundException, InterruptedException {
        ViewInteraction appCompatButton = onView(
                allOf(ViewMatchers.withId(R.id.warhornButton), withText("Warhorn"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                withId(R.id.frameLayout),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        ChromeLogIn.login();

        ViewInteraction textView = onView(
                allOf(withId(R.id.Profile_Info),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction spinner = onView(
                allOf(withId(R.id.CurrentEvent),
                        withParent(withParent(withId(R.id.Event_Info))),
                        isDisplayed()));
        spinner.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.AddButton), withText("+ ADD EVENT"),
                        withParent(withParent(withId(R.id.Event_Info))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.RemoveEvent), withText("- REMOVE EVENT"),
                        withParent(withParent(withId(R.id.Event_Info))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction scrollView = onView(
                allOf(withId(R.id.Sessions),
                        withParent(withParent(withId(R.id.Event_Info))),
                        isDisplayed()));
        scrollView.check(matches(isDisplayed()));

        Thread.sleep(3000);
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
