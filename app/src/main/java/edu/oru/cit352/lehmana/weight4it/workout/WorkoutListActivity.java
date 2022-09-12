package edu.oru.cit352.lehmana.weight4it.workout;
//Austin Lehman
//Mobile APP CIT 352
//DR. Osborne
//4/23/2022
//weight 4 it
//WorkoutListActivity
//App intent that allows users to see list of saved workouts and switch between pages
//users can also add new workouts and delete saved workouts

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
import edu.oru.cit352.lehmana.weight4it.log.WorkoutLogActivity;

public class WorkoutListActivity extends AppCompatActivity {

    ArrayList<WorkoutObject> workouts; //create reference variable for WorkoutObject arraylist
    WorkoutAdapter workoutAdapter; //create reference variable for WorkoutAdapter
    RecyclerView workoutList; //create recyclerview for workoutList


    private final View.OnClickListener onItemClickListener = new View.OnClickListener() { //wait for user to click on item on list
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag(); //create recycler view object
            int position = viewHolder.getAdapterPosition(); //get position of adapter that has been clicked
            int workoutID = workouts.get(position).getWorkoutID(); //get workoutId of item selected
            Intent intent = new Intent(WorkoutListActivity.this, MainActivity.class); //Take from Workout List to Main activity
            intent.putExtra("workoutId", workoutID); //keyvalue for workoutId to take to Main
            startActivity(intent); //start main activity
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) { //receive when passed
        super.onCreate(savedInstanceState); //create based on previous saved info
        setContentView(R.layout.activity_workout_list); //set content view
        //initialization of all buttons and switches
        initSettingsButton();
        initWeightButton();
        initLogButton();
        initAddWorkoutButton();
        initDeleteSwitch();
    }

    public void onResume() { //called an start/resume
        super.onResume();//call parent
        String sortBy = getSharedPreferences("WorkoutSortPreferences",
                Context.MODE_PRIVATE).getString("workoutsortby", "workoutname"); //default value if no value present of contact list preferences
        String sortOrder = getSharedPreferences("WorkoutSortPreferences",
                Context.MODE_PRIVATE).getString("workoutsortorder", "ASC"); //default value if no value present for order
        WorkoutDataSource ds = new WorkoutDataSource(this); //create object

        try { //try to access file
            ds.open(); //open database
            workouts = ds.getWorkouts(sortBy, sortOrder);//set all workout database saves to arraylist
            ds.close(); //close database
            if (workouts.size() > 0) {
                workoutList = findViewById(R.id.rvLifts); //reference variable and handle on rvLifts object
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager((this)); //creating a layoutmanager
                workoutList.setLayoutManager(layoutManager); //associate layoutmanager with recycler view
                workoutAdapter = new WorkoutAdapter(workouts, this); //adapter receives contacts to set, and sets reference to self
                workoutAdapter.setOnItemClickListener(onItemClickListener);
                workoutList.setAdapter(workoutAdapter); // set adaptor for the recycler view
            } else { //if size of workout list is 0 start intent to go to main
                Intent intent = new Intent(WorkoutListActivity.this, MainActivity.class);
                startActivity(intent);
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
                //create new intent to go to settings activity from this activity
                Intent intent = new Intent(WorkoutListActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //set flag for no duplicate activities
                startActivity(intent); //start settings activity
            }
        });
    }

    private void initWeightButton() {
        ImageButton ibWeight = findViewById(R.id.imageButtonWorkoutList);//create object for weight button
        ibWeight.setEnabled(false); //set button to false
    }

    private void initLogButton() {
        ImageButton ibSettings = findViewById(R.id.imageButtonLog); //create object for Log button
        ibSettings.setOnClickListener(new View.OnClickListener() { //set listener to wait for settings button press
            @Override
            public void onClick(View v) {
                //create new intent to go to Log activity from this activity
                Intent intent = new Intent(WorkoutListActivity.this, WorkoutLogActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // set flag fo no duplicate activities
                startActivity(intent); //start WorkoutLog activity
            }
        });
    }

    private void initAddWorkoutButton() {
        //rceate object for Add workout button
        Button newWorkout = findViewById(R.id.buttonLogWorkoutFromLog);
        newWorkout.setOnClickListener(new View.OnClickListener() {//set listener to wait for NewWorkout button press
            @Override
            public void onClick(View v) {
                //create new intent to go to main activity to create new workout from this activity
                Intent intent = new Intent(WorkoutListActivity.this, MainActivity.class);
                startActivity(intent); //go to main activity
            }
        });
    }

    private void initDeleteSwitch() {
        //reference delete switch
        Switch delete = findViewById(R.id.switchDelete);
        delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //set listener for delete switch
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean status = buttonView.isChecked();//check if switch active
                workoutAdapter.setDelete(status); //set to delete if checked
                workoutAdapter.notifyDataSetChanged();//refresh page on delete

            }
        });
    }

}