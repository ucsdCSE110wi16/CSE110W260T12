package com.lasergiraffe.rideshare.util;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lasergiraffe.rideshare.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;

public class SignUp extends AppCompatActivity {
    // boolean started = false;
    EditText username;
    EditText password;
    Button sign_up;
    Button already_have_account;
    TextView errorMessage;
    private void register(final View v){

        if(username.getText().length()==0 || password.getText().length()==0)
            return;
        v.setEnabled(false);
        JSONArray myGroups = new JSONArray();
        ParseUser user = new ParseUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.put("group_key", myGroups);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    finish();
                } else {
                    switch(e.getCode()){
                        case ParseException.USERNAME_TAKEN:
                            errorMessage.setText("Username Already Taken");
                            break;
                        case ParseException.USERNAME_MISSING:
                            errorMessage.setText("Please add a username");
                            break;
                        case ParseException.PASSWORD_MISSING:
                            errorMessage.setText("Please add a password");
                            break;
                        case ParseException.OBJECT_NOT_FOUND:
                            errorMessage.setText("Username or password invalid");
                            break;
                        default:
                            errorMessage.setText(e.getLocalizedMessage());
                            break;}
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
        errorMessage = (TextView) findViewById(R.id.errorMessages);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(v);
            }
        });

        already_have_account.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent i = new Intent(SignUp.this, Login.class);
                //startActivity(i);
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
