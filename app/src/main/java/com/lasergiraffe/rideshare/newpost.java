package com.lasergiraffe.rideshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;

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

                note.put("title", note.getTitle());
                note.put("content", note.getContent());
                note.put("userName", note.getName());
                note.put("userPhone", note.getPhone());
                note.saveInBackground();

                Intent main = new Intent(newpost.this, MainActivity.class);
                startActivity(main);
                finish();
                /*Intent main = new Intent();
                setResult(0, main);
                finish();*/

            }
        });


    }
}

