package edu.temple.dmhelper.warhorn;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiObjectNotFoundException;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import edu.temple.dmhelper.MainActivity;
import edu.temple.dmhelper.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ChangeEventTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void changeEventTest() throws UiObjectNotFoundException, InterruptedException {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.warhornButton), withText("Warhorn"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                withId(R.id.frameLayout),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        ChromeLogIn.login();

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.AddButton), withText("+ Add Event"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.Event_Info),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton2.perform(click());

        Thread.sleep(2000);

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.slug),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("siege-of-agrad"), closeSoftKeyboard());

        Thread.sleep(2000);

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton3.perform(scrollTo(), click());

        Thread.sleep(2000);

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.AddButton), withText("+ Add Event"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.Event_Info),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton4.perform(click());

        Thread.sleep(2000);

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.slug),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("indy-dnd"), closeSoftKeyboard());

        Thread.sleep(2000);

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton5.perform(scrollTo(), click());

        Thread.sleep(2000);

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.CurrentEvent),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.Event_Info),
                                        0),
                                1),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        Thread.sleep(2000);

        DataInteraction textView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        textView.perform(click());

        Thread.sleep(2000);
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
