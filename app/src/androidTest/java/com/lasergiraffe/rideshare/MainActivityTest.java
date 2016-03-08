package com.lasergiraffe.rideshare;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Amit on 3/5/2016.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public final ActivityRule<MainActivity> mActivityRule = new ActivityRule<>(MainActivity.class);

    @Test
    public void listGoesOverTheFold() {
    }
}