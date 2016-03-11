package com.lasergiraffe.rideshare;


import android.app.ListActivity;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.lasergiraffe.rideshare.R.id.test2_text;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by Amit on 3/5/2016.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public final ActivityRule<MainActivity> mActivityRule = new ActivityRule<>(MainActivity.class);

    @Test
    public void shouldLaunchMainScreen() {
        onView(withText("Hello")).check(ViewAssertions.matches(isDisplayed()));
    }

    private ListActivity mActivity;


    public void testPreconditions() {
        assertThat(mActivity, notNullValue());
    }

    public void testEditText_changeInputText() {
        String operandOneHint = mActivity.getString(R.string.test1);
        onView(withId(R.id.test1_text)).check(matches(withHint(operandOneHint)));
    }

    public void testEditText_OperandTwoHint() {
        String operandTwoHint = mActivity.getString(R.string.test2);
        onView(withId(test2_text)).check(matches(withHint(operandTwoHint)));
    }

}


