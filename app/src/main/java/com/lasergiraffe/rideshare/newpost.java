package com.lasergiraffe.rideshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lamki on 2/1/2016.
 */
public class newpost extends Activity {
    private Note note;
    EditText title;
    EditText description; //content
    EditText userName; //name
    EditText userPhone; //phone
    Button submit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpost);

        submit = (Button) findViewById(R.id.submit_id);
        title = (EditText) findViewById(R.id.title_id);
        userName = (EditText) findViewById(R.id.name_id);
        userPhone = (EditText) findViewById(R.id.phone_id);
        description = (EditText) findViewById(R.id.description_id);
        note = new Note();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                note.setId(note.getObjectId());
                note.setTitle(title.getText().toString());
                note.setContent(description.getText().toString());
                // Kenny 2/22 changed from: note.setName(userName.getText().toString());
                note.setName(ParseUser.getCurrentUser().getUsername().toString());
                note.setPhone(userPhone.getText().toString());

                //SETTING CAPACITY TO 5 FOR ALL FOR NOW
                note.setCapacity(5);
                note.setCurrNumRiders(1);

                note.put("title", note.getTitle());
                note.put("content", note.getContent());
                note.put("userName", note.getName());
                note.put("userPhone", note.getPhone());
                note.put("capacity", note.getCapacity());
                note.put("currNumRiders", note.getCurrNumRiders());
                note.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            ParseUser user = ParseUser.getCurrentUser();
                            List<String> myGroups = (ArrayList<String>) user.get("group_key");
                            myGroups.add(note.getObjectId());
                            user.put("group_key", myGroups);
                            user.saveInBackground();
                            //Intent main = new Intent(newpost.this, MainActivity.class);
                            //startActivity(main);
                            finish();
                        } else {
                            Log.v("System.out", e.getMessage());
                        }
                    }
                });

            }
        });


    }
}

