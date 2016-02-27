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
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MyGroups extends ListActivity {
    public static List<Note> groups;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        /* sets up adapter */
        groups = new ArrayList<Note>();
        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(this, R.layout.list_item_layout, groups);
        setListAdapter(adapter);
        refreshGroupList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_groups, menu);
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

    private void refreshGroupList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("notes");
        query.orderByDescending("createdAt");
        List<String> myGroups = (ArrayList<String>) ParseUser.getCurrentUser().get("group_key");
        query.whereContainedIn("objectId", myGroups);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> postList, ParseException e) {
                if (e == null) {
                    // If there are results, update the list of posts
                    // and notify the adapter
                    groups.clear();
                    for (ParseObject post : postList) {
                        Note note = new Note(post.getObjectId(), post.getString("title"),
                                post.getString("userName"), post.getString("phone"), post.getString("content"),
                                post.getInt("capacity"), post.getInt("currNumRiders"));
                        System.out.println("capacity is " + post.getInt("capacity"));
                        note.getObjectId();
                        groups.add(note);
                    }
                    ((ArrayAdapter<Note>) getListAdapter()).notifyDataSetChanged();
                } else {
                    Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                }
            }
        });

        // OpenPost
        ListView list = getListView();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MyGroups.this, ChatActivity.class);
                String note_key = ((Note) parent.getItemAtPosition(position)).getId();
                i.putExtra("note_key", note_key);
                startActivity(i);
                //finish();
            }

        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ParseUser user = ParseUser.getCurrentUser();
                List<String> myGroups = (ArrayList<String>) user.get("group_key");
                String note_key = ((Note) parent.getItemAtPosition(position)).getId();
                myGroups.remove(note_key);
                ParseQuery<Note> query = ParseQuery.getQuery("notes");
                Note note = null;
                try {
                    note = (Note)query.get(note_key);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                note.increment("currNumRiders", -1); //currNumRiders--
                note.saveInBackground();
                user.put("group_key", myGroups);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            //successful save
                            refreshGroupList();
                        } else {
                            Log.v("System.out", e.getMessage());
                        }
                    }
                });
                return false;
            }
        });
    }
}
