package com.lasergiraffe.rideshare;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


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

    /* Calendar for the newPost */
    //final Calendar inputCal = Calendar.getInstance();
    //EditText inputDate = (EditText) findViewById(R.id.date_id);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpost);

        submit = (Button) findViewById(R.id.submit_id);
        //title = (EditText) findViewById(R.id.title_id);
        userName = (EditText) findViewById(R.id.name_id);
        userPhone = (EditText) findViewById(R.id.phone_id);
        description = (EditText) findViewById(R.id.description_id);
        note = new Note();

        EditText price = (EditText) findViewById(R.id.price_id);


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


        /* when Date of Departure EditText is Clicked, pop up a calendar on screen */
/*
        inputDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(newpost.this, new DatePickerDialog(), inputCal
                        .get(Calendar.YEAR), inputCal.get(Calendar.MONTH),
                        inputCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
*/



    }

/*
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            inputCal.set(Calendar.YEAR, year);
            inputCal.set(Calendar.MONTH, monthOfYear);
            inputCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };


    /* update the Date Label once the Date is chosen
    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        inputDate.setText(sdf.format(inputCal.getTime()));
    }
*/
    public void setDriver(View view){

        boolean isOn = ((ToggleButton) view).isChecked();
        EditText price = (EditText) findViewById(R.id.price_id);


        if (isOn) {
            Toast.makeText(getApplicationContext(), "You are a Driver", Toast.LENGTH_SHORT).show();
            price.setHint(R.string.pricePost);

        }
        else {
            Toast.makeText(getApplicationContext(), "You are a Rider", Toast.LENGTH_SHORT).show();
            price.setHint(R.string.priceEstPost);
        }
    }

    public boolean getDriver(View view){

        boolean isOn = ((ToggleButton) view).isChecked();
        return isOn;
    }
}

