package edu.temple.dmhelper.bluetooth;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.DataInteraction;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;

import edu.temple.dmhelper.R;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DiscoveryActivityTest {
    private ActivityScenario<DiscoveryActivity> scenario;

    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(DiscoveryActivity.class);
    }

    @Test
    public void deviceReturns_onSelect() {
        scenario.moveToState(Lifecycle.State.RESUMED);

        DataInteraction constraintLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.deviceList),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                3)))
                .atPosition(0);
        constraintLayout.perform(click());

        Instrumentation.ActivityResult result = scenario.getResult();
        assertEquals(Activity.RESULT_OK, result.getResultCode());
        assertNotNull(result.getResultData());
        assertEquals(DiscoveryActivity.ACTION_DEVICE_SELECTED, result.getResultData().getAction());
        assertNotNull(result.getResultData().getParcelableExtra(DiscoveryActivity.EXTRA_DEVICE));
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
