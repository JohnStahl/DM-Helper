package edu.temple.dmhelper.bluetooth;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;

import edu.temple.dmhelper.Character;
import edu.temple.dmhelper.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JoinGameActivityTest {
    public ActivityScenario<JoinGameActivity> scenario;

    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(JoinGameActivity.class);
    }

    @Test
    public void deviceReturns_onSelect() {
        scenario.moveToState(Lifecycle.State.RESUMED);

        String testName = "test";
        int testInitiative = 5;

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.characterName),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(replaceText(testName), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.characterInitiative),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText(String.valueOf(testInitiative)), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.joinButton), withText("Join Game"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton2.perform(click());

        Instrumentation.ActivityResult result = scenario.getResult();
        assertEquals(Activity.RESULT_OK, result.getResultCode());

        Intent resultData = result.getResultData();
        assertNotNull(resultData);
        assertEquals(JoinGameActivity.ACTION_CHARACTER_CREATED, resultData.getAction());

        Character character = (Character) resultData.getSerializableExtra(JoinGameActivity.EXTRA_CHARACTER);
        assertNotNull("JoinGameActivity didn't return character", character);
        assertEquals(testName, character.getName());
        assertEquals(testInitiative, character.getInitiative());
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
