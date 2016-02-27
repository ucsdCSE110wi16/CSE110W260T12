package com.lasergiraffe.rideshare;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.parse.Parse.initialize;


public class MainActivity extends ListActivity {

    public static List<Note> posts;
    static boolean started = false; //don't reinitialize parse if already done once

    @Override
    protected void onResume() {
        super.onResume();
        refreshPostList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* sets up parse */
        initializeParse();

        /* prevent from this activity if not logged on */
        checkLoggedOn();

        /* sets up layout */
        setupPosts();

        //BUTTONS :D
        Button switchtonewpage = (Button) findViewById(R.id.newpost_button);
        Button clear = (Button) findViewById(R.id.clear_button);
        Button logout = (Button) findViewById(R.id.logout_button);
        Button myGroups = (Button) findViewById(R.id.myGroups);

        //listview is the layout for this page
        ListView list = getListView();

        //Click the "New Post" button to make a new post
        switchtonewpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPost = new Intent(v.getContext(), newpost.class);
                startActivityForResult(newPost, 1);
                refreshPostList();
                //finish();
            }
        });

        // Click an item on the action bar to show the description
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, OpenedPostActivity.class);
                String noteTitle = parent.getItemAtPosition(position).toString();
                String noteContent = ((Note)parent.getItemAtPosition(position)).getContent();
                String note_key = ((Note)parent.getItemAtPosition(position)).getId();
                String username = ((Note)parent.getItemAtPosition(position)).getName();
                int capacity = ((Note)parent.getItemAtPosition(position)).getCapacity();
                int currNumRiders = ((Note)parent.getItemAtPosition(position)).getCurrNumRiders();
                i.putExtra(getString(R.string.theNote), noteTitle);
                i.putExtra(getString(R.string.theContent), noteContent);
                i.putExtra("note_key", note_key);
                i.putExtra("note_username", username);
                i.putExtra("capacity", capacity);
                i.putExtra("currNumRiders", currNumRiders);
                startActivity(i);
                refreshPostList();
                //finish();
            }
        });

        // "Clear" Button (We will get rid of this button)
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ParseQuery<ParseObject> query = ParseQuery.getQuery("notes");
                query.findInBackground(new FindCallback<ParseObject>() {


                    @Override
                    public void done(List<ParseObject> postList, ParseException e) {
                        if (e == null) {
                            // If there are results, update the list of posts
                            // and notify the adapter
                            posts.clear();
                            ParseObject.deleteAllInBackground(postList);
                            ((ArrayAdapter<Note>) getListAdapter()).notifyDataSetChanged();
                        } else {
                            Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                        }
                    }
                });
                refreshPostList();
            }
        });

        // "Log out" Button for users to log out
        logout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                //finish();
            }
        });

        //"My Group" Button to show users' own groups
        myGroups.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this, MyGroups.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //System.out.println("Created?");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //System.out.println("Created options");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_logout:
                ParseUser.logOut();
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                //finish();
                return true;
        }
        return false;
    }

    // Method that refreshes the action bar from Parse.
    private void refreshPostList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("notes");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> postList, ParseException e) {
                if (e == null) {
                    // If there are results, update the list of posts
                    // and notify the adapter
                    posts.clear();
                    for (ParseObject post : postList) {
                        Note note = new Note(post.getObjectId(), post.getString("title"),
                                post.getString("userName"), post.getString("phone"), post.getString("content"), post.getInt("capacity"),
                                post.getInt("currNumRiders"));
                        note.getObjectId();
                        posts.add(note);
                    }
                    ((ArrayAdapter<Note>) getListAdapter()).notifyDataSetChanged();
                } else {
                    Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                }
            }
        });
    }

    private void checkLoggedOn(){
        final ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser == null) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            //finish();
        }
    }

    private void setupPosts(){
        posts = new ArrayList<Note>();
        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(this, R.layout.list_item_layout, posts);
        setListAdapter(adapter);
        refreshPostList();
    }

    private void initializeParse(){
        ParseObject.registerSubclass(Note.class);
        ParseObject.registerSubclass(Message.class);


        initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        //started = true;
    }
}
