package com.example.sydney.vacationcalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import static java.lang.Float.parseFloat;

public class MainActivity extends AppCompatActivity {
    TextView showCurrentHoursCollected;
    EditText startingValue;
    EditText dailyAccumulation;
    float collectedHours;
    float accumulation;
    ProgressBar progressBar;
    TextView numberOfDays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showCurrentHoursCollected = (TextView)findViewById(R.id.collectedHours);
        dailyAccumulation = (EditText)findViewById(R.id.dailyAccumulation);
        startingValue = (EditText)findViewById(R.id.startingValue);
        numberOfDays = (TextView)findViewById(R.id.numOfDays);

        //initalize buttons and on-click methods for them
        Button eightHours = (Button)findViewById(R.id.removeEight);
        Button fourHours = (Button)findViewById(R.id.removeFour);
        Button oneHour = (Button)findViewById(R.id.removeOne);
        Button updateButton = (Button)findViewById(R.id.updateButton);

        //remove appropriate number of vacation days and update the current
        eightHours.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                collectedHours -= 8;
                updateValuesAfterUsage();
            }
        });

        fourHours.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                collectedHours -= 4;
                updateValuesAfterUsage();
            }
        });

        oneHour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                collectedHours -= 1;
                updateValuesAfterUsage();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Float startingHoursValue = Float.parseFloat((startingValue).getText().toString());
                Float dailyAccumulatedValue = Float.parseFloat((dailyAccumulation).getText().toString());
                //update values based on user input
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                //update values stroed in shared preferences
                editor.putFloat("dailyAccumulated", dailyAccumulatedValue);
                editor.putFloat("currentValue", startingHoursValue);
                //update collected text
                showCurrentHoursCollected.setText(Float.toString(startingHoursValue));
                //update collected to be starting
                numberOfDays.setText(Math.floor(collectedHours/8) + " days");
                collectedHours = startingHoursValue;
                //update progress bar
                progressBar.setProgress((int)Math.floor(collectedHours));
                editor.commit();
            }
        });

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        //get last login and see how much time has passed so we can update vacation days based on accumulative per day
        long today = new Date().getTime();
        long lastLogin = sharedPref.getLong("lastlogin", 0);
        int workDaysPassed = getWorkingDaysBetweenTwoDates(new Date(lastLogin), new Date(today));
        accumulation = sharedPref.getFloat("dailyAccumulated", 0);
        Float additionalHours = workDaysPassed * accumulation;

        //get last value and input it where appropriate
        collectedHours = sharedPref.getFloat("currentValue", 0) + additionalHours;
        startingValue.setText(Float.toString(collectedHours));
        dailyAccumulation.setText(Float.toString(sharedPref.getFloat("dailyAccumulated", 0)));

        //set progress bar value to stored value
        progressBar = (ProgressBar)findViewById(R.id.countdownTimer);
        progressBar.setProgress((int)Math.floor(collectedHours));

        //set days
        numberOfDays.setText(Math.floor(collectedHours/8) + " days");

        //set current hours collected to stored value
        showCurrentHoursCollected.setText(Float.toString(collectedHours));

        //save today as last login day for future reference
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("lastlogin", today);
        editor.commit();
    }

    public void updateValuesAfterUsage() {
        //store new collected value
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat("currentValue", collectedHours);
        editor.commit();
        //update current accumualation and corresponding progress chart
        showCurrentHoursCollected.setText(Float.toString(collectedHours));
        numberOfDays.setText(Math.floor(collectedHours/8) + " days");
        progressBar.setProgress((int)Math.floor(collectedHours));
    }

    public static int getWorkingDaysBetweenTwoDates(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int workDays = 0;

        //Return 0 if start and end are the same
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 0;
        }
        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }
        do {
            //excluding start date
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workDays;
            }
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

        return workDays;
    }
}
