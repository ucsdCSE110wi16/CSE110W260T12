package com.lasergiraffe.rideshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lasergiraffe.rideshare.util.SignUp;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class Login extends AppCompatActivity {

    TextView errorMessage;
    EditText username;
    EditText password;
    Button login;
    Button need_acc;

    //Overriding the back button, such that we disable the back button on the login page
    @Override
    public void onBackPressed() {
    }
    private void login(final View v){
        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(),
                new LogInCallback() {

                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user!=null) {
                            Intent i = new Intent(Login.this, MainActivity.class);
                            //startActivity(i);
                            finish();
                        }
                        else{
                            // Signup failed. Look at the ParseException to see what happened.
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
                                    break;
                            }
                        }
                    }
                });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.signin_username);
        password = (EditText) findViewById(R.id.signin_password);
        login = (Button) findViewById(R.id.login);
        need_acc = (Button) findViewById(R.id.need_acc);
        errorMessage = (TextView) findViewById(R.id.errorMessages);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
        need_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, SignUp.class);
                startActivity(i);
                finish();
            }


        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
