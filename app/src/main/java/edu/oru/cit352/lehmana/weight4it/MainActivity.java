package edu.oru.cit352.lehmana.weight4it;
//Austin Lehman
//4/23/2022
//Weight 4 it
//Main Activity for Weight 4 it
//Starting App page that will give the user the option to create a new workout and log a workout
//for their use. The user will enter a Workout name, Reps, and Weight, Select a Type from a spinner,
//and select if the barbell needs to be use for their workout. The user can then calculate their One
//Rep max and Save their workout. After a valid workout has been saved, the user can select from the
//bottom of the what percent of their one rep max they would like to use for a session. Once the
//percent is selected, the user will see their workout populate, and if they have the barbell option
//checked the plates needed to go up to, but not over their one rep max will be used. The user can
//then logg an instance of their session to be saved for record.

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

import edu.oru.cit352.lehmana.weight4it.log.LogDataSource;
import edu.oru.cit352.lehmana.weight4it.log.LogObject;
import edu.oru.cit352.lehmana.weight4it.log.WorkoutLogActivity;
import edu.oru.cit352.lehmana.weight4it.workout.WorkoutDataSource;
import edu.oru.cit352.lehmana.weight4it.workout.WorkoutListActivity;
import edu.oru.cit352.lehmana.weight4it.workout.WorkoutObject;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, LogDatePicker.SaveDateListener {
    //references to private objects
    private WorkoutObject currentWorkout;
    private LogObject currentLog;
    private Spinner spinner;
    private Spinner repSpinner;
    private String[] holdTypes;
    private Resources resources;
    private int repPercent;

    //https://developer.android.com/guide/topics/ui/controls/spinner#java
    //This is the resource utilized to learn how to properly use spinners for this application 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize methods for use
        initSettingsButton();
        initWeightButton();
        initLogButton();
        initCalculateButton();
        initSaveButton();
        initTextChangedEvents();
        initSwitchEdit();
        initBarBellCheck();
        initLogWorkoutButton();
        //create new logobject for logging workouts
        currentLog = new LogObject();

        //spinner for workout type
        spinner = findViewById(R.id.workoutTypeSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.workout_type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //listen to this page for spinner selection
        spinner.setOnItemSelectedListener(this);


        //spinner for rep%
        repSpinner = findViewById(R.id.oneRepMaxSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterORM = ArrayAdapter.createFromResource(this,
                R.array.workout_reps_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterORM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        repSpinner.setAdapter(adapterORM);
        //listen to this page for spinner selection
        repSpinner.setOnItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();//create reference to current intents bundle extras
        if (extras != null) { //if there are no extras
            initWorkout(extras.getInt("workoutId")); //load workout id
            hideBarbell(currentWorkout.getBarbellToggle() != 1);//check if barbell tool is needs hidden

        } else { //if this is a new workout
            currentWorkout = new WorkoutObject();
            hideBarbell(true); //hide barbell tool by default
        }
        setForEditing(false);//set editing to false

    }

    private void initTextChangedEvents() {
        //static reference to workout name
        final EditText etWorkoutName = findViewById(R.id.editTxtWorkoutName);
        etWorkoutName.addTextChangedListener(new TextWatcher() {//listener for text changed

            public void afterTextChanged(Editable s) {
                //set current workout name
                currentWorkout.setWorkoutName(etWorkoutName.getText().toString());
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });
        //static reference to workout reps
        final EditText etReps = findViewById(R.id.editTxtReps);
        etReps.addTextChangedListener(new TextWatcher() { //listener for text changed
            public void afterTextChanged(Editable s) {

                String hold = etReps.getText().toString().trim(); //set reps to string
                if (hold.equals("")) { //if nothing is entered set ormReps to 0
                    currentWorkout.setOrmReps(0);
                } else { //if there is a character entered
                    //set transfer to value that Hold is and set currentworkout Orm to value
                    //of transfer
                    Integer transfer = Integer.valueOf(hold);
                    currentWorkout.setOrmReps(transfer);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });

        //static reference to weight
        final EditText etWeight = findViewById(R.id.editTextWeight);
        etWeight.addTextChangedListener(new TextWatcher() { //listener for text changed
            public void afterTextChanged(Editable editable) {
                //set value of of weight to hold String
                String hold = etWeight.getText().toString().trim();
                if (hold.equals("")) { //if hold is an empty string set weight to 0
                    currentWorkout.setOrmWeight(0);
                } else { //if there is a character entered
                    //set transfer to value that hold is and set current workout weight to value
                    //of transfer
                    Integer transfer = Integer.valueOf(hold);
                    currentWorkout.setOrmWeight(transfer);
                }
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

        });
    }

    private void initSettingsButton() {
        //reference to settings button
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        //set listener for button press
        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create intent and if button is pressed start activity to settings class
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initWeightButton() {
        //reference to weight button
        ImageButton ibWeight = findViewById(R.id.imageButtonWorkoutList);
        //set listener for button press
        ibWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create intent and if button is pressed start activity for Workout list activity class
                Intent intent = new Intent(MainActivity.this, WorkoutListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent); //start activity
            }
        });
    }

    private void initLogButton() {
        //reference to log button
        ImageButton ibSettings = findViewById(R.id.imageButtonLog);
        //set listener for button press
        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create intent and if button pressed start activity for  workout log activity class
                Intent intent = new Intent(MainActivity.this, WorkoutLogActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent); //start activity
            }
        });
    }

    private void initCalculateButton() {
        Button calcORM = findViewById(R.id.button_Calc_ORM);
        calcORM.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calcORM();
            }
        });
    }

    private void calcORM() {
        //static references to reps, weights, and calculation
        final EditText etReps = findViewById(R.id.editTxtReps);
        final EditText etWeight = findViewById(R.id.editTextWeight);
        final TextView holder = findViewById(R.id.textView_ORM_Calculation_Fill);

        //if reps or weight do not have values entered
        if (!etReps.getText().toString().trim().equals("") && !etReps.getText().toString().isEmpty()
                && !etWeight.getText().toString().trim().equals("") && !etWeight.getText().toString().isEmpty()) {
            //set holdrepstring to etreps and then convert to integer
            String holdRepString = etReps.getText().toString();
            Integer reps = Integer.valueOf(holdRepString);

            //set holdWeightstring to etWeight and then convert to integer
            String holdWeightString = etWeight.getText().toString();
            Integer weight = Integer.valueOf(holdWeightString);

            //create Onerep max variable
            Integer oneRepMax;

            //https://www.calculators.tech/1rm-calculator Gives formula and Reps/Percent Tables used in program
            // The Brzycki formula from Matt Brzycki is tested for accuracy of onerep max calculations
            // as long as reps are under 10 reps

            oneRepMax = (int) (weight / (1.0278 - (0.0278 * reps))); //calculate One Rep Max
            String test = oneRepMax.toString(); //set value of test to value of calculated one rep max
            if (oneRepMax < 1500 && oneRepMax > 1) { //check if weight is realistic(just above current records)
                holder.setText(test); //display one rep max to screen
                currentWorkout.setOrmCalc(oneRepMax); //set current workouts calculated ORM
            } else if (oneRepMax < 1) { //if weight lifted 0 set 0 and send message
                holder.setText("0");
                currentWorkout.setOrmCalc(0);
                Toast.makeText(MainActivity.this, "Lets lift some real weights...", Toast.LENGTH_LONG).show();
            } else {//if weight lifted is unrealistic set to invalid entry and send message
                holder.setText("Invalid\nEntry");
                currentWorkout.setOrmCalc(0);
                Toast.makeText(MainActivity.this, "Ok, Lets be realistic...", Toast.LENGTH_LONG).show();
            }
        } else { //send toast to fill fields
            Toast.makeText(MainActivity.this, "Please Fill Out All Required Fields For ORM Calculation", Toast.LENGTH_LONG).show();
            currentWorkout.setOrmReps(0);
            currentWorkout.setOrmWeight(0);
            currentWorkout.setOrmCalc(0);
        }
    }

    private void initSwitchEdit() {
        //set static reference to Switch
        final Switch editSwitch = findViewById(R.id.switchEdit);
        editSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on click set for editing of screen
                setForEditing(editSwitch.isChecked());
            }
        });
    }

    private void initSaveButton() {
        Button saveButton = findViewById(R.id.saveActivityButton); //reference to saveButton object
        saveButton.setOnClickListener(new View.OnClickListener() { //set listener to wait for button selection

            @Override
            public void onClick(View view) {
                boolean wasSuccessful = false; //create for tracking if save did not fail

                hideKeyboard(); //force hide keyboard after save selected
                WorkoutDataSource ds = new WorkoutDataSource(MainActivity.this); //create new WorkoutDatasource object
                //if current workout has valid name and is not null
                if (currentWorkout.getWorkoutName() != null && !currentWorkout.getWorkoutName().trim().equals("")) {
                    calcORM(); //calculate ORM
                    try {
                        ds.open();  //open database

                        if (currentWorkout.getWorkoutID() == -1) { //check if new contact being added
                            wasSuccessful = ds.insertWorkout(currentWorkout); //attempt to create new workout
                            if (wasSuccessful) { // check if creation was successful
                                int newId = ds.getLastWorkoutId(); //get workouts new id
                                currentWorkout.setWorkoutID(newId); //set workouts new id
                            }
                        } else { // if contact is not new
                            wasSuccessful = ds.updateWorkout(currentWorkout); //try to update the workout
                        }

                        ds.close(); //close the database
                    } catch (Exception e) { //if update/create failed
                        wasSuccessful = false; //set to false
                    }
                } else { //if name is not valid send toast
                    Toast.makeText(MainActivity.this, "Please enter valid workout name", Toast.LENGTH_LONG).show();
                }
                if (wasSuccessful) { //if update/create was successful
                    Switch editSwitch = findViewById(R.id.switchEdit); //reference edit switch
                    editSwitch.toggle();//grab toggle handle and turn off
                    setForEditing(false); //set editing to false deactivate all fields
                }
            }
        });
    }

    private void initBarBellCheck() {
        //reference to Toggle
        CheckBox barBellToggle = findViewById(R.id.barbellCheckBox);
        barBellToggle.setOnClickListener(new View.OnClickListener() { //listener for toggle press
            @Override
            public void onClick(View v) {
                //hide/unhide barbell tool
                hideBarbell(!barBellToggle.isChecked());
            }
        });
    }

    private void hideBarbell(boolean enabled) {
        //textView references for plates in barbell tool
        TextView plate55 = findViewById(R.id.textView_55Pound);
        TextView plate45 = findViewById(R.id.textView_45pound);
        TextView plate35 = findViewById(R.id.textView_35Pound);
        TextView plate25 = findViewById(R.id.textView_25Pound);
        TextView plate10 = findViewById(R.id.textView_10Pound);
        TextView plate5 = findViewById(R.id.textView_5Pound);
        TextView header = findViewById(R.id.textView_PlateTrack);

        if (!enabled) { //if enabled view tool
            plate55.setVisibility(View.VISIBLE);
            plate45.setVisibility(View.VISIBLE);
            plate35.setVisibility(View.VISIBLE);
            plate25.setVisibility(View.VISIBLE);
            plate10.setVisibility(View.VISIBLE);
            plate5.setVisibility(View.VISIBLE);
            header.setVisibility(View.VISIBLE);
            currentWorkout.setBarbellToggle(1); //set current workout using tool
        } else { //if not hide tool
            plate55.setVisibility(View.INVISIBLE);
            plate45.setVisibility(View.INVISIBLE);
            plate35.setVisibility(View.INVISIBLE);
            plate25.setVisibility(View.INVISIBLE);
            plate10.setVisibility(View.INVISIBLE);
            plate5.setVisibility(View.INVISIBLE);
            header.setVisibility(View.INVISIBLE);
            currentWorkout.setBarbellToggle(0);//set current workout not using tool
        }
    }

    private void setForEditing(boolean enabled) {
        //get references to EditTxt and buttons
        EditText editWorkout = findViewById(R.id.editTxtWorkoutName);
        EditText editReps = findViewById(R.id.editTxtReps);
        EditText editWeight = findViewById(R.id.editTextWeight);
        CheckBox checkBarbell = findViewById(R.id.barbellCheckBox);
        Button buttonCalculate = findViewById(R.id.button_Calc_ORM);
        Button buttonSave = findViewById(R.id.saveActivityButton);
        Spinner spinnerType = findViewById(R.id.workoutTypeSpinner);

        //set based on value of enabled
        editWorkout.setEnabled(enabled);
        editReps.setEnabled(enabled);
        editWeight.setEnabled(enabled);
        checkBarbell.setEnabled(enabled);
        buttonCalculate.setEnabled(enabled);
        buttonSave.setEnabled(enabled);
        spinnerType.setEnabled(enabled);

        if (enabled) { //if enabled request focus
            editWorkout.requestFocus();
        } else { //if not enabled focus to top and clear focus
            ScrollView scroll = findViewById(R.id.scrollViewWorkouts);
            scroll.fullScroll(ScrollView.FOCUS_UP);
            scroll.clearFocus();
        }
    }

    private void hideKeyboard() {
        //create input method manager object to get system services
        //get references for all EditText objects, call hideSoftInputFrom window on all
        //to hide keyboard.
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editWorkout = findViewById(R.id.editTxtWorkoutName);
        imm.hideSoftInputFromWindow(editWorkout.getWindowToken(), 0);
        EditText editReps = findViewById(R.id.editTxtReps);
        imm.hideSoftInputFromWindow(editReps.getWindowToken(), 0);
        EditText editWeight = findViewById(R.id.editTextWeight);
        imm.hideSoftInputFromWindow(editWeight.getWindowToken(), 0);
    }

    private void initWorkout(int id) {
        //create datasource object
        WorkoutDataSource ds = new WorkoutDataSource(MainActivity.this);
        try { //try to open database and get it
            ds.open();
            currentWorkout = ds.getSpecificWorkout(id);
            ds.close();
        } catch (Exception e) { //if unable to load send toast
            Toast.makeText(this, "Load Workout Failed", Toast.LENGTH_LONG).show();
        }
        //create handles to references
        EditText editWorkout = findViewById(R.id.editTxtWorkoutName);
        EditText editReps = findViewById(R.id.editTxtReps);
        EditText editWeight = findViewById(R.id.editTextWeight);
        TextView editCalculation = findViewById(R.id.textView_ORM_Calculation_Fill);
        CheckBox checkBarbell = findViewById(R.id.barbellCheckBox);
        Spinner spinnertype = findViewById(R.id.workoutTypeSpinner);


        //set information from workout object
        editWorkout.setText(currentWorkout.getWorkoutName());
        editReps.setText(currentWorkout.getOrmReps().toString());
        editWeight.setText(currentWorkout.getOrmWeight().toString());
        editCalculation.setText(currentWorkout.getOrmCalc().toString());
        spinnertype.setSelection(currentWorkout.getWorkoutType());
        checkBarbell.setChecked(currentWorkout.getBarbellToggle().intValue() > 0);
    }

    private void initLogWorkoutButton() {
        Button changeDate = findViewById(R.id.logworkoutButton); //reference log workout button
        changeDate.setOnClickListener(new View.OnClickListener() { //set listener to wait for log button press
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager(); //create fragment object
                LogDatePicker logDatePicker = new LogDatePicker(); //create datepicker dialog  object
                logDatePicker.show(fm, "DatePick");//show the datepicker as a new fragment
            }
        });
    }

    private String calcPercentWorkout(double percent, int weight) {
        //set calculate percent for workout
        Float calculation = Float.valueOf((float) ((percent / 100) * weight));
        //convert to rounded integer
        Integer round = Integer.valueOf(Math.round(calculation));
        //set rounded result to string
        String conv = round.toString();
        //return string
        return conv;
    }

    private void barBellTrack() {
        //reference all textviews to be changed
        TextView plate55 = findViewById(R.id.textView_55Pound);
        TextView plate45 = findViewById(R.id.textView_45pound);
        TextView plate35 = findViewById(R.id.textView_35Pound);
        TextView plate25 = findViewById(R.id.textView_25Pound);
        TextView plate10 = findViewById(R.id.textView_10Pound);
        TextView plate5 = findViewById(R.id.textView_5Pound);
        TextView weightToComplete = findViewById(R.id.textview_weight_to_complete);

        //set all previous texts to 0
        plate5.setText("5#: 0");
        plate25.setText("25#: 0");
        plate10.setText("10#: 0");
        plate35.setText("35#: 0");
        plate45.setText("45#: 0");
        plate55.setText("55#: 0");


        //set values for calculating
        boolean flag = true;
        int hold;

        Integer requestedWeight = Integer.valueOf(weightToComplete.getText().toString());
        int pTracker55, pTracker45, pTracker35, pTracker25, pTracker10, pTracker5, barWeight;
        barWeight = 45;//weight of standard 45 pound bar
        requestedWeight = requestedWeight - barWeight;//adjust weight requested to account for bar

        //loop through cases to track how many barbells needed while i<7 and the flag is still true
        for (int i = 1; i < 7 && flag; i++) {
            //switch cases for each weight
            //Each switch follows format of first switch, just changes weight value and accounts for
            //lower requested weight if subtraction occurs
            switch (i) {
                case 1:
                    hold = requestedWeight / 55; //set hold to value of requested weight divided by plate weight
                    if (hold % 2 == 0) { //if hold value is even
                        requestedWeight = requestedWeight - (55 * hold); //adjust requested weight for adding plates
                        pTracker55 = hold; //set 55 pound weights to value of hold
                        plate55.setText("55#: " + pTracker55); //set value of ptracker to let user know how much of specific weight to put on bar
                    } else { //if hold is not even
                        requestedWeight = requestedWeight - (55 * (hold - 1));//subtract hold by 1 and calculate if plates need added(if minus 1 is 0 no weight change nothing added to bar)
                        pTracker55 = hold - 1; //set tracker to hold minus 1 to keep even distro of weight
                        plate55.setText("55#: " + pTracker55); //set value to plate weight textview
                    }
                    break; //break from switch go to if check at end to see if value of requested weight is less than 10
                case 2:
                    hold = requestedWeight / 45;
                    if (hold % 2 == 0) {
                        requestedWeight = requestedWeight - (45 * hold);
                        pTracker45 = hold;
                        plate45.setText("45#: " + pTracker45);
                    } else {
                        requestedWeight = requestedWeight - (45 * (hold - 1));
                        pTracker45 = hold - 1;
                        plate45.setText("45#: " + pTracker45);
                    }
                    break;
                case 3:
                    hold = requestedWeight / 35;
                    if (hold % 2 == 0) {
                        requestedWeight = requestedWeight - (35 * hold);
                        pTracker35 = hold;
                        plate35.setText("35#: " + pTracker35);
                    } else {
                        requestedWeight = requestedWeight - (35 * (hold - 1));
                        pTracker35 = hold - 1;
                        plate35.setText("35#: " + pTracker35);
                    }
                    break;
                case 4:
                    hold = requestedWeight / 25;
                    if (hold % 2 == 0) {
                        requestedWeight = requestedWeight - (25 * hold);
                        pTracker25 = hold;
                        plate25.setText("25#: " + pTracker25);
                    } else {
                        requestedWeight = requestedWeight - (25 * (hold - 1));
                        pTracker25 = hold - 1;
                        plate25.setText("25#: " + pTracker25);
                    }
                    break;
                case 5:
                    hold = requestedWeight / 10;
                    if (hold % 2 == 0) {
                        requestedWeight = requestedWeight - (10 * hold);
                        pTracker10 = hold;
                        plate10.setText("10#: " + pTracker10);
                    } else {
                        requestedWeight = requestedWeight - (10 * (hold - 1));
                        pTracker10 = hold - 1;
                        plate10.setText("10#: " + pTracker10);
                    }
                    break;
                case 6:
                    hold = requestedWeight / 5;
                    if (hold % 2 == 0) {
                        requestedWeight = requestedWeight - (5 * hold);
                        pTracker5 = hold;
                        plate5.setText("5#: " + pTracker5);
                    } else {
                        requestedWeight = requestedWeight - (5 * (hold - 1));
                        pTracker5 = hold - 1;
                        plate5.setText("5#: " + pTracker5);
                    }
                    break;
            }

            //check if value is not less than 10 if so flag false to stop as cannot divide evenly
            //past this point with weights given
            if (requestedWeight < 10) {
                flag = false;
            }
        }

    }

    private void saveLog() {
        boolean wasSuccessful; //create for tracking if save did not fail

        hideKeyboard(); //force hide keyboard after save selected
        LogDataSource logDataSource = new LogDataSource(MainActivity.this); //create new WorkoutDatasource object
        try {
            logDataSource.open();  //open database

            if (currentLog.getLogID() == -1) { //check if new contact being added
                wasSuccessful = logDataSource.insertLog(currentLog); //attempt to create new Log
                if (wasSuccessful) { // check if creation was successful
                    int newId = logDataSource.getLastLogId(); //get logs new id
                    currentLog.setLogID(newId); //set logs new id
                }
            } else { // if contact is not new not using in this version could be made to update workouts if needed
                wasSuccessful = logDataSource.updateLog(currentLog); //try to update the workout
            }
            logDataSource.close(); //close the database
        } catch (Exception e) { //if update/create failed
            wasSuccessful = false; //set to false
        }

        if (wasSuccessful) { //if update/create was successful
            //display to user save of item
            Toast.makeText(this, "Workout Saved", Toast.LENGTH_LONG).show();
            //set current log id so it can be overwritten. Do not clear as if orm% is not changed
            //but a second workout is added null will show up in type of workout on db
            currentLog.setLogID(-1);
        }
    }

    @Override
    public void didFinishDatePickerLog(Calendar selectedTime) {
        //for adaptor set name,reps,weight, date, and type
        TextView weightToComplete = findViewById(R.id.textview_weight_to_complete);
        TextView repsComplete = findViewById(R.id.text_view_reps_needed);
        EditText etWorkoutName = findViewById(R.id.editTxtWorkoutName);

        //assign to log weight, name, reps & date
        currentLog.setWorkoutWeight(weightToComplete.getText().toString());
        currentLog.setWorkoutName(etWorkoutName.getText().toString());
        currentLog.setWorkoutReps(repsComplete.getText().toString());
        currentLog.setWorkoutDate(selectedTime);


        //call save log method to save current item to database
        //if the workout % has been selected
        if (currentWorkout.getWorkoutID() != -1 && currentWorkout.getOrmWeight() != null && currentWorkout.getWorkoutName() != null
                && currentWorkout.getWorkoutType() != null && currentWorkout.getOrmReps() != null && currentWorkout.getOrmCalc() != null
                && currentWorkout.getOrmWeight() != 0
                && currentWorkout.getWorkoutType() != 0 && currentWorkout.getOrmReps() != 0 && currentWorkout.getOrmCalc() != 0) {
            //if workout % has been selected
            if (repPercent != 0) {
                saveLog();
            } else {//if workout percent is not selected make toast
                Toast.makeText(this, "Please Select One Rep Max %", Toast.LENGTH_LONG).show();
            }
        } else { //if workout is not saved and values == null or 0 make toast to create and save workout
            Toast.makeText(this, "Please Create and Save Workout", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //compare parent id to current spinners. If parent == specific spinner execute that spinner
        if (parent.getId() == spinner.getId()) {  //execute workouttype spinner on change
            //save spinner selection to current workout and log
            currentWorkout.setWorkoutType(position);
            currentLog.setWorkoutType(position);
        } else if (parent.getId() == repSpinner.getId()) { //check if spinner is repspinner
            repPercent = position;//set to position of spinner
            TextView repsComplete = findViewById(R.id.text_view_reps_needed);//reference to reps needed
            resources = this.getResources(); //get resources from this item
            //set holdtypes array to array from xml file reps count array
            holdTypes = resources.getStringArray(R.array.workout_reps_count_array);
            repsComplete.setText(holdTypes[position]); //set reps to complete from holdtypes array
            //change values of holdtypes array to xml workout reps array
            holdTypes = resources.getStringArray(R.array.workout_reps_array);

            //if the spinner is not default
            if (position != 0) {

                //check if workout is saved and  values not null or 0
                if (currentWorkout.getWorkoutID() != -1 && currentWorkout.getOrmCalc() != null
                        && currentWorkout.getOrmWeight() != null && currentWorkout.getOrmReps() != null
                        && currentWorkout.getOrmCalc() != 0 && currentWorkout.getOrmReps() != 0
                        && currentWorkout.getOrmWeight()!=0)  {
                    calcORM();//calculate OrM
                    //get percent for calculation and remove % and trim leftover space
                    String holdPercent = holdTypes[position].replace("%", " ").trim();
                    Integer convPercent = Integer.valueOf(holdPercent); //convert from string to integer

                    //get references to textviews
                    TextView string = findViewById(R.id.textview_weight_to_complete);
                    TextView oRM = findViewById(R.id.textView_ORM_Calculation_Fill);
                    //get and convect value of orm to integer
                    String oneRepMaxString = oRM.getText().toString();
                    Integer oRMConv = Integer.valueOf(oneRepMaxString);
                    //calculate percent workout  and set text to calculation
                    String weightCalculation = calcPercentWorkout(convPercent, oRMConv);
                    string.setText(weightCalculation);
                    //if barbell calculator being used
                    if (currentWorkout.getBarbellToggle() == 1) {
                        barBellTrack(); //adjust barbell calculations
                    } else {
                        //do nothing
                    }
                } else { //if workout not saved or orm calc is null or 0  set weight/reps to 0
                    Toast.makeText(MainActivity.this, "Please Calculated ORM and Save Workout First", Toast.LENGTH_LONG).show();
                    repSpinner.setSelection(0);
                    TextView weight = findViewById(R.id.textview_weight_to_complete);
                    TextView reps = findViewById(R.id.text_view_reps_needed);
                    weight.setText("0");
                    reps.setText("0");
                }
            } else { //if position is not selected
                TextView weight = findViewById(R.id.textview_weight_to_complete);
                TextView reps = findViewById(R.id.text_view_reps_needed);
                weight.setText("0");
                reps.setText("0");

            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}