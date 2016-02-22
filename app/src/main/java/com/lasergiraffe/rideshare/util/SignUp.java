package com.lasergiraffe.rideshare.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lasergiraffe.rideshare.Login;
import com.lasergiraffe.rideshare.MainActivity;
import com.lasergiraffe.rideshare.Note;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

import com.lasergiraffe.rideshare.R;
import com.parse.SignUpCallback;

import static com.parse.Parse.initialize;

public class SignUp extends AppCompatActivity {
   // boolean started = false;
    EditText username;
    EditText password;
    Button sign_up;
    Button already_have_account;

    private void register(final View v){

        if(username.getText().length()==0 || password.getText().length()==0)
            return;
        v.setEnabled(false);
        ParseUser user = new ParseUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Intent i = new Intent(SignUp.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    v.setEnabled(true);
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*
        if(!started) {
            ParseObject.registerSubclass(Note.class);

            initialize(this);
            ParseInstallation.getCurrentInstallation().saveInBackground();
            started = true;
        }*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = (EditText) findViewById(R.id.register_username);
        password = (EditText) findViewById(R.id.register_password);
        sign_up = (Button) findViewById(R.id.sign_up);
        already_have_account = (Button) findViewById(R.id.already_have_acc);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(v);
            }
        });

        already_have_account.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this, Login.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
}
