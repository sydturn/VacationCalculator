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
import android.widget.TextView;

import static java.lang.Float.parseFloat;

public class MainActivity extends AppCompatActivity {
    TextView showCurrentHoursCollected;
    EditText startingValue;
    EditText dailyAccumulation;
    float collectedHours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showCurrentHoursCollected = (TextView)findViewById(R.id.collectedHours);
        dailyAccumulation = (EditText)findViewById(R.id.dailyAccumulation);
        startingValue = (EditText)findViewById(R.id.startingValue);

        //initalize buttons and on-click methods for them
        Button eightHours = (Button)findViewById(R.id.removeEight);
        Button fourHours = (Button)findViewById(R.id.removeFour);
        Button oneHour = (Button)findViewById(R.id.removeOne);
        Button updateButton = (Button)findViewById(R.id.updateButton);

        //remove appropriate number of vacation days and update the current
        eightHours.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                collectedHours -= 8;
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putFloat("currentValue", collectedHours);
                editor.commit();
                showCurrentHoursCollected.setText(Float.toString(collectedHours));
            }
        });

        fourHours.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                collectedHours -= 4;
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putFloat("currentValue", collectedHours);
                editor.commit();
                showCurrentHoursCollected.setText(Float.toString(collectedHours));
            }
        });

        oneHour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                collectedHours -= 1;
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putFloat("currentValue", collectedHours);
                editor.commit();
                showCurrentHoursCollected.setText(Float.toString(collectedHours));
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Float startingHoursValue = Float.parseFloat((startingValue).getText().toString());
                Float dailyAccumulatedValue = Float.parseFloat((dailyAccumulation).getText().toString());
                //update values based on user input
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putFloat("dailyAccumulated", dailyAccumulatedValue);
                editor.putFloat("currentValue", startingHoursValue);
                showCurrentHoursCollected.setText(Float.toString(startingHoursValue));
                collectedHours = startingHoursValue;
                editor.commit();
            }
        });

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        float currentValue = sharedPref.getFloat("currentValue", 0);
        startingValue.setText(Float.toString(sharedPref.getFloat("currentValue", 0)));
        dailyAccumulation.setText(Float.toString(sharedPref.getFloat("dailyAccumulated", 0)));

        showCurrentHoursCollected.setText(Float.toString(currentValue));
    }
}
