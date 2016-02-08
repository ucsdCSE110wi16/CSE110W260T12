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

import java.util.ArrayList;
import java.util.List;

import static com.parse.Parse.initialize;


public class MainActivity extends ListActivity {

    public static List<Note> posts;
    static boolean started = false; //don't reinitialize parse if already done once
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [Optional] Power your app with Local Datastore. For more info, go to
        // https://parse.com/docs/android/guide#local-datastore
        //enableLocalDatastore(this);

        //already done once check (VERY INELEGANT SOLUTION - work on later)
        if(!started) {
            ParseObject.registerSubclass(Note.class);

            initialize(this);
            ParseInstallation.getCurrentInstallation().saveInBackground();
            started = true;
        }
        // Declare post as an arraylist
        posts = new ArrayList<Note>();
        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(this, R.layout.list_item_layout, posts);
        setListAdapter(adapter);
        refreshPostList();


        Button switchtonewpage = (Button) findViewById(R.id.newpost_button);
        switchtonewpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPost = new Intent(v.getContext(), newpost.class);
                startActivityForResult(newPost, 1);
            }
        });

        ListView list = getListView();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, OpenedPostActivity.class);
                String noteTitle = parent.getItemAtPosition(position).toString();
                String noteContent = ((Note)parent.getItemAtPosition(position)).getContent();
                i.putExtra(getString(R.string.theNote), noteTitle);
                i.putExtra(getString(R.string.theContent), noteContent);
                System.out.println("Note title is " + noteTitle);
                startActivity(i);
            }
        });

        Button clear = (Button) findViewById(R.id.clear_button);
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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshPostList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("notes");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> postList, ParseException e) {
                if (e == null) {
                    // If there are results, update the list of posts
                    // and notify the adapter
                    posts.clear();
                    for (ParseObject post : postList) {
                        Note note = new Note(post.getObjectId(), post.getString("title"), post.getString("content"));
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


}
