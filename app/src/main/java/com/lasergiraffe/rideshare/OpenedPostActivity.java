package com.lasergiraffe.rideshare;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lasergiraffe.rideshare.util.SystemUiHider;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class OpenedPostActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String thisUser = ParseUser.getCurrentUser().getUsername();

        Bundle extra = getIntent().getExtras();

        String noteTitle = extra.getString(getString(R.string.noteTitle));
        //final String noteToDest = extra.getString(getString(R.string.noteToDest));
        //final String noteFromDest = extra.getString(getString(R.string.noteFromDest));
        String noteDetails = extra.getString(getString(R.string.noteDetails));
        String noteName = extra.getString(getString(R.string.noteName));
        final String thispostuser = extra.getString(getString(R.string.noteUsername));
        final String noteKey = extra.getString(getString(R.string.noteKey));
        String notePrice = extra.getString(getString(R.string.notePrice));
        String notePhone = extra.getString(getString(R.string.notePhone));
        String noteTimeDate = extra.getString(getString(R.string.noteTimeDate));
        int noteCurrNumRiders = Integer.parseInt(extra.getString(getString(R.string.noteCurrNumRiders)));
        int noteCapacity = Integer.parseInt(extra.getString(getString(R.string.noteCapacity)));
        String totalSeatsAvailable = noteCurrNumRiders + "/" + noteCapacity;

        final int finalCurrNumRiders = noteCurrNumRiders;
        final int finalCapacity = noteCapacity;

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_opened_post);

        final View controlsView = findViewById(R.id.openedpostbg_id);
        final View contentView = findViewById(R.id.titleText_id);

        TextView titleText = (TextView) findViewById(R.id.titleText_id);
        TextView nameText = (TextView) findViewById(R.id.nameText_id);
        TextView phoneText = (TextView) findViewById(R.id.phoneText_id);
        TextView timeDateText = (TextView) findViewById(R.id.timeDateText_id);
        TextView numSeatsText = (TextView) findViewById(R.id.numSeats_id);
        TextView priceText = (TextView) findViewById(R.id.priceText_id);
        TextView detailsText = (TextView) findViewById(R.id.detailsText_id);


        // ??????????? final TextView currNumRidersOverCapacity = (TextView) findViewById(R.id.currNumRidersOverCapacity);
        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.join_group).setOnTouchListener(mDelayHideTouchListener);

        Button joinButton = (Button) findViewById(R.id.join_group);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = ParseUser.getCurrentUser();
                List<String> myGroups = (ArrayList<String>) user.get("group_key");
                if(myGroups.contains(noteKey)){
                    Toast.makeText(OpenedPostActivity.this, "Already in group!",
                            Toast.LENGTH_SHORT).show();
                }
                else if(finalCurrNumRiders >= finalCapacity){
                    Toast.makeText(OpenedPostActivity.this, "Group full!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    myGroups.add(noteKey);
                    user.put("group_key", myGroups);
                    ParseQuery<Note> query = ParseQuery.getQuery("notes");
                    Note note = null;
                    try {
                        note = (Note)query.get(noteKey);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    note.increment(getString(R.string.noteCurrNumRiders));
                    note.saveInBackground();
                    //??????????? currNumRidersOverCapacity.setText(currNumRiders+1+"/"+capacity);
                    user.saveInBackground();

                    Toast.makeText(OpenedPostActivity.this, "Successfully joined the group!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //TextView name = (TextView) findViewById(R.id.fullscreen_name);

        Bundle extras = null;
        if(savedInstanceState==null){
            extras = getIntent().getExtras();
            if(extras==null){
                 noteTitle = null;
                 noteDetails = null;
                 noteName= null;
                 notePhone= null;
                 noteTimeDate = null;
                 notePrice = null;
                 noteCurrNumRiders = 0;
                 noteCapacity = 0;
                 totalSeatsAvailable = null;
            }
            else{
                noteTitle = extras.getString(getString(R.string.noteTitle));
                noteDetails = extras.getString(getString(R.string.noteDetails));
                noteName = extras.getString(getString(R.string.noteName));
                notePhone = extras.getString(getString(R.string.notePhone));
                notePrice = extras.getString(getString(R.string.notePrice));
                noteCurrNumRiders = Integer.parseInt(extra.getString(getString(R.string.noteCurrNumRiders)));
                noteCapacity = Integer.parseInt(extra.getString(getString(R.string.noteCapacity)));
                noteTimeDate = extras.getString(getString(R.string.noteTimeDate));
                totalSeatsAvailable = (noteCurrNumRiders + "/" + noteCapacity);

            }
        }
        titleText.setText(noteTitle);
        detailsText.setText(noteDetails);
        nameText.setText(noteName);
        phoneText.setText(notePhone);
        priceText.setText(notePrice);
        numSeatsText.setText(totalSeatsAvailable);
        timeDateText.setText(noteTimeDate);


        //currNumRidersOverCapacity.setText(s);


        //MOVED VARIABLES TO TOP
        final Button deletepost = (Button)findViewById(R.id.deletePost_id);
        if (thisUser.equals(thispostuser)) {
            deletepost.getBackground().setColorFilter(null);
        }
        else {
            deletepost.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            deletepost.setEnabled(false);
        }
        //Delete the post (Only person who opens the post has the privilege)
        deletepost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ParseQuery<ParseObject> queryForNote = ParseQuery.getQuery("notes");
                ParseQuery<ParseObject> queryForMsg = ParseQuery.getQuery("Message");
                //Delete the post based on the note_id
                queryForNote.getInBackground(noteKey,new GetCallback<ParseObject>(){
                    public void done(ParseObject object, ParseException e) {
                        if (e == null)
                            object.deleteInBackground();
                        else
                            System.out.println("Error in deleting");
                    }
                });
                //Delete the messages related to the post
                queryForMsg.whereEqualTo("noteKey", noteKey);
                queryForMsg.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> invites, ParseException e) {
                        if (e == null) {
                            // iterate over all messages and delete them
                            for (ParseObject invite : invites) {
                                invite.deleteInBackground();
                            }
                        } else {
                            //Handle condition here
                        }
                    }
                });
                finish();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
