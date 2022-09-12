package edu.oru.cit352.lehmana.weight4it.log;
//Austin Lehman
//4/23/2022
//weight 4 it
//WorkoutLogActivity
//App intent that allows users to see list of saved Logged  workouts and switch between pages
//users can also add new Logs and delete saved logs
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.oru.cit352.lehmana.weight4it.MainActivity;
import edu.oru.cit352.lehmana.weight4it.R;
import edu.oru.cit352.lehmana.weight4it.SettingsActivity;
import edu.oru.cit352.lehmana.weight4it.workout.WorkoutListActivity;

public class WorkoutLogActivity extends AppCompatActivity {

    ArrayList<LogObject> logs; //create reference variable for log objects
    LogAdapter logAdapter; //create reference variable for LogAdapter
    RecyclerView workoutLog; //create recyclerview for workoutlog

    private final View.OnClickListener onItemClickListener = new View.OnClickListener() { //wait for user to click on item on list
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag(); //create recycler view object
            int position = viewHolder.getAdapterPosition(); //get position of adapter that has been clicked
            int logID = logs.get(position).getLogID(); //get logid of item selected
            Intent intent = new Intent(WorkoutLogActivity.this, MainActivity.class); //take from Log list to main activity
            intent.putExtra("logId", logID); //keyvalue for logid to take to main
            startActivity(intent); //start main activity
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) { //receive when passed
        super.onCreate(savedInstanceState); //create based on previous saved info
        setContentView(R.layout.activity_workout_log); //set content view
        //initialization of all buttons and switches
        initSettingsButton();
        initWeightButton();
        initLogButton();
        initLogWorkoutButton();
        initDeleteSwitch();
    }

    public void onResume() {
        super.onResume();//call parent

        String sortBy = getSharedPreferences("LogSortPreferences",
                Context.MODE_PRIVATE).getString("logsortby", "workoutname"); //default value if no value present of contact list preferences
        String sortOrder = getSharedPreferences("LogSortPreferences",
                Context.MODE_PRIVATE).getString("logsortorder", "ASC"); //default value if no value present for order
        LogDataSource logDataSource = new LogDataSource(this);//create object

        try { //try to access file
            logDataSource.open(); //open database
            logs = logDataSource.getLogs(sortBy, sortOrder);//set all workout database saves to arraylist
            logDataSource.close(); //close database
            if (logs.size() > 0) {
                workoutLog = findViewById(R.id.rvLifts); //reference variable and handle on rvLifts object
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager((this)); //creating a layoutmanager
                workoutLog.setLayoutManager(layoutManager); //associate layoutmanager with recycler view
                logAdapter = new LogAdapter(logs, this); //adapter receives contacts to set, and sets reference to self
                logAdapter.setOnItemClickListener(onItemClickListener);
                workoutLog.setAdapter(logAdapter); // set adaptor for the recycler view
            } else { //if size of log list is 0 start intent and go to main
                Intent intent = new Intent(WorkoutLogActivity.this, MainActivity.class);
                startActivity(intent); //go to main
            }
        } catch (Exception e) {
            //popup message if error in data retrieval
            Toast.makeText(this, "Workout Retrieval Error", Toast.LENGTH_LONG).show();
        }
    }

    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings); //create object for settings button
        ibSettings.setOnClickListener(new View.OnClickListener() { //set listener to wait for settings button press
            @Override
            public void onClick(View v) {
                //create new intent go to settings activity from this activity
                Intent intent = new Intent(WorkoutLogActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //set flag for no duplicate activities
                startActivity(intent); //start settings activity
            }
        });
    }

    private void initWeightButton() {
        ImageButton ibWeight  = findViewById(R.id.imageButtonWorkoutList);//create object for weight button
        ibWeight.setOnClickListener(new View.OnClickListener() { //set listener to wait for weight button press
            @Override
            public void onClick(View v) {
                //create new intent to go to workout list activity from this activity
                Intent intent = new Intent(WorkoutLogActivity.this, WorkoutListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //set flag no duplicate activities
                startActivity(intent); //start workout list activity
            }
        });
    }

    private void initLogButton() {
        ImageButton ibSettings = findViewById(R.id.imageButtonLog); //create object for log button
        ibSettings.setEnabled(false); //disable log button
    }

    private void initLogWorkoutButton() {
        Button logWorkoutBtn = findViewById(R.id.buttonLogWorkoutFromLog); //create object for logworkout button
        logWorkoutBtn.setOnClickListener(new View.OnClickListener() { //set listener to wait for button press

            @Override
            public void onClick(View v) {
                //create new intent to go from this log activity to workout list activity
                //does same action as weight button, but guides user if they dont know how to log
                Intent intent = new Intent(WorkoutLogActivity.this, WorkoutListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //avoid duplicate activities
                startActivity(intent); //start workoutlist activity
            }
        });
    }

    private void initDeleteSwitch() {
        //reference delete switch
        Switch delete = findViewById(R.id.switchDelete);
        delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //set listener for delete switch
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean status = buttonView.isChecked(); //check if switch is active
                logAdapter.setDelete(status); //set to delete if checked
                logAdapter.notifyDataSetChanged(); //refresh page on delete

            }
        });
    }
}