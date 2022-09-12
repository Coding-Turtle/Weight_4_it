package edu.oru.cit352.lehmana.weight4it;
//Austin Lehman
//4/23/2022
//Weight 4 it
//Settings activity
//App intent that allows users to change the Workout list and Log list order
//Also has buttons that allow users to switch between pages

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import edu.oru.cit352.lehmana.weight4it.log.WorkoutLogActivity;
import edu.oru.cit352.lehmana.weight4it.workout.WorkoutListActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_settings);
        //initialize buttons for usage
        initSettingsButton();
        initWeightButton();
        initLogButton();
        initSettings();
        initSortByClick();
        initSortOrderClick();
    }

    private void initSettingsButton() {
        //reference and hold settings button
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setEnabled(false);//disable use of settings button
    }

    private void initWeightButton() {
        //reference and hold Weight button
        ImageButton ibWorkoutList = findViewById(R.id.imageButtonWorkoutList);
        ibWorkoutList.setOnClickListener(new View.OnClickListener() {//set listener to wait for selection
            @Override
            public void onClick(View v) {
                //create intent to go from this activity to the workoutList activity
                Intent intent = new Intent(SettingsActivity.this, WorkoutListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //avoid duplicate activities
                startActivity(intent); //start new activity
            }
        });
    }

    private void initLogButton() {
        //reference and hold log button
        ImageButton ibSettings = findViewById(R.id.imageButtonLog);
        ibSettings.setOnClickListener(new View.OnClickListener() {//set listener to wait for selection
            @Override
            public void onClick(View v) {
                //create intent to go from this activity to workoutLog activity
                Intent intent = new Intent(SettingsActivity.this, WorkoutLogActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//aviod duplicate activities
                startActivity(intent); //start new activity
            }
        });
    }

    private void initSettings() {
        //create and store shared preferences as a string for workout sortby and order
        String sortBy = getSharedPreferences("WorkoutSortPreferences",
                Context.MODE_PRIVATE).getString("workoutsortby", "workoutname");//defoult values for sort by
        String sortOrder = getSharedPreferences("WorkoutSortPreferences",
                Context.MODE_PRIVATE).getString("workoutsortorder", "ASC"); //default values for order

        //radio buttons for lift name and weight  on workout list
        RadioButton rbLiftName = findViewById(R.id.radioLiftName);
        RadioButton rbMaxWeight = findViewById(R.id.radioMaxWeight);

        if (sortBy.equalsIgnoreCase("workoutname")) {//if sortby is set to workout name
            rbLiftName.setChecked(true); //set to true lift name
        } else { //set  max weight to true if lift is not selected
            rbMaxWeight.setChecked(true);
        }

        //radio buttons for ascending/descending
        RadioButton rbAsc = findViewById(R.id.radioAscending);
        RadioButton rbDes = findViewById(R.id.radioDescending);

        if (sortOrder.equalsIgnoreCase("ASC")) { //if sort order is set to asc
            rbAsc.setChecked(true); //set asc button to true
        } else { //if not ascending set descend
            rbDes.setChecked(true);
        }

        //create ond store shared preferences as a string for Log sortby and order
        String logSortBy = getSharedPreferences("LogSortPreferences",
                Context.MODE_PRIVATE).getString("logsortby", "workoutname"); //defaults for sortby
        String logSortOrder = getSharedPreferences("LogSortPreferences",
                Context.MODE_PRIVATE).getString("logsortorder", "ASC"); //defaults for sortorder

        //radio buttons for lift name and log date
        RadioButton rbLogLiftName = findViewById(R.id.radioLogLiftName);
        RadioButton rbLogDate = findViewById(R.id.radioLogDate);

        if (logSortBy.equalsIgnoreCase("workoutname")) {//if sortby is set to workout name
            rbLogLiftName.setChecked(true); //set lift name to checked
        } else { //if it is not workout name set logDate to checked
            rbLogDate.setChecked(true);
        }
        //radio buttons for ascending/descending
        RadioButton rbLogAsc = findViewById(R.id.radioLogAscending);
        RadioButton rbLogDes = findViewById(R.id.radioLogDescending);

        if (logSortOrder.equalsIgnoreCase("ASC")) { //if sort order is set to asc
            rbLogAsc.setChecked(true); //set asc button to true
        } else { //if no ascending set to desc
            rbLogDes.setChecked(true);
        }
    }

    private void initSortByClick() {
        //create handle to workout list radio group
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {//set listener to wait for change

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) { //check if change in buttons
                RadioButton rbLiftName = findViewById(R.id.radioLiftName);//create reference to liftname button
                if (rbLiftName.isChecked()) { //if liftname is checked set preferences to workout name
                    getSharedPreferences("WorkoutSortPreferences",
                            Context.MODE_PRIVATE).edit().putString("workoutsortby", "workoutname").apply();
                } else { //if lift name is not checked set preferences to ormCalc
                    getSharedPreferences("WorkoutSortPreferences",
                            Context.MODE_PRIVATE).edit().putString("workoutsortby", "ormcalc").apply();
                }
            }
        });
        //create references to Log radio group
        RadioGroup rgLogSortBy = findViewById(R.id.radioGroupLogSortBy);
        rgLogSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {//set listener to wait for change

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) { //check if change in buttons
                RadioButton rbLogLiftName = findViewById(R.id.radioLogLiftName);//create reference to LogLift name
                if (rbLogLiftName.isChecked()) { //if loglift name is checked set as preferences
                    getSharedPreferences("LogSortPreferences",
                            Context.MODE_PRIVATE).edit().putString("logsortby", "workoutname").apply();
                } else { //if lift name is not checked set preference to workoutDate
                    getSharedPreferences("LogSortPreferences",
                            Context.MODE_PRIVATE).edit().putString("logsortby", "workoutdate").apply();
                }
            }
        });
    }

    private void initSortOrderClick() {
        RadioGroup rgSortOrder = findViewById(R.id.radioGroupSortOrder); //create reference to radio groupup for sort order
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { //set listener to wait for button press

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) { //check if change with radio button
                RadioButton rbAscending = findViewById(R.id.radioAscending); //get handle on ascending radiobutton ascending object
                if (rbAscending.isChecked()) { //if the ascending button is checked access shared pref and edit preferences with ascending
                    getSharedPreferences("WorkoutSortPreferences",
                            Context.MODE_PRIVATE).edit().putString("workoutsortorder", "ASC").apply();
                } else { //if the ascending button is not checked set preferences to descending
                    getSharedPreferences("WorkoutSortPreferences",
                            Context.MODE_PRIVATE).edit().putString("workoutsortorder", "DESC").apply();
                }
            }
        });

        RadioGroup rgLogSortOrder = findViewById(R.id.radioGroupLogSortOrder); //create reference to radio group Log order
        rgLogSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { //set listener to woit for button press

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) { //check if changed button
                RadioButton rbLogAscending = findViewById(R.id.radioLogAscending); //get handle on ascending radiobutton object
                if (rbLogAscending.isChecked()) { //if the ascending button is checked access shared pref and edit preferences with ascending for Log SortOrder
                    getSharedPreferences("LogSortPreferences",
                            Context.MODE_PRIVATE).edit().putString("logsortorder", "ASC").apply();
                } else {
                    getSharedPreferences("LogSortPreferences", //if the ascending button is not checked set preferences to descending for LogSortOrder
                            Context.MODE_PRIVATE).edit().putString("logsortorder", "DESC").apply();
                }
            }
        });
    }

}