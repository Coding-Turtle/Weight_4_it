package edu.oru.cit352.lehmana.weight4it.workout;
//Austin Lehman
//Mobile APP CIT 352
//DR. Osborne
//4/23/2022
//weight 4 it
//WorkoutDataSource
//This class is a helper class that will assist with inserting, and updating a workout into the
//database. Allows opening and closing of database

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

import edu.oru.cit352.lehmana.weight4it.WorkoutDBHelper;

public class WorkoutDataSource {
    private SQLiteDatabase database; //create reference to SQLitedatabase object
    private final WorkoutDBHelper dbHelper; //create reference to Workout DBHelper object

    //constructor to create dbHelper
    public WorkoutDataSource(Context context) { //constructor instantiates helper class
        dbHelper = new WorkoutDBHelper(context);
    }

    public void open() throws SQLException { //open database
        database = dbHelper.getWritableDatabase();
    }

    public void close() { //close the database
        dbHelper.close();
    } //close database


    public boolean insertWorkout(WorkoutObject W) { //create new workout
        boolean didSucceed = false; //assume failure of creation set boolean false
        try { //try to get object and put into database
            ContentValues initialValues = new ContentValues(); //data struct of Initial values
            //load values into workoutObject
            initialValues.put("workoutname", W.getWorkoutName());
            initialValues.put("workouttype", W.getWorkoutType());
            initialValues.put("barbelltoggle", W.getBarbellToggle());
            initialValues.put("ormreps", W.getOrmReps());
            initialValues.put("ormweight", W.getOrmWeight());
            initialValues.put("ormcalc", W.getOrmCalc());

            didSucceed = database.insert("workout", null, initialValues) > 0; //sets did succeed if insert succesfull
        } catch (Exception e) {
            //Do nothing - will return false if there is an exception
        }
        return didSucceed; //return if successful entry into db
    }

    public boolean updateWorkout(WorkoutObject W) {
        boolean didSucceed = false; //set to false, assume failure

        try {
            Long rowId = (long) W.getWorkoutID(); //retrieve workoutId and set to rowid
            ContentValues updateValues = new ContentValues(); //data struct of values

            //put values into the table as the exist
            updateValues.put("workoutname", W.getWorkoutName());
            updateValues.put("workouttype", W.getWorkoutType());
            updateValues.put("barbelltoggle", W.getBarbellToggle());
            updateValues.put("ormreps", W.getOrmReps());
            updateValues.put("ormweight", W.getOrmWeight());
            updateValues.put("ormcalc", W.getOrmCalc());

            //check if succeeded in database update
            didSucceed = database.update("workout", updateValues, "_id=" + rowId, null) > 0;
        } catch (Exception e) {
            //do nothing
        }
        return didSucceed; //return if updated
    }

    public int getLastWorkoutId() {
        int lastId = -1; //ensure that dataID for last workout added
        try {
            String query = "Select MAX(_id) from workout"; //query to find maxid
            Cursor cursor = database.rawQuery(query, null);//cursor creation
            cursor.moveToFirst(); //move cursor to first row
            lastId = cursor.getInt(0); //set value of cursor at position to last id
            cursor.close(); //close cursor
        } catch (Exception e) {// if value not in table
            lastId = -1;  //set to -1 if not updated
        }
        return lastId; //return value of last workout added
    }

    public ArrayList<WorkoutObject> getWorkouts(String sortField, String sortOrder) {
        ArrayList<WorkoutObject> workouts = new ArrayList<WorkoutObject>(); //create arraylist of workout objects
        try {//try accessing the database
            String query = "SELECT * FROM workout ORDER BY " + sortField + " " + sortOrder; //select all from workouts with sort pref
            Cursor cursor = database.rawQuery(query, null); //create cursor to go through data
            WorkoutObject newWorkout; //create new workout object
            cursor.moveToFirst(); //move cursor to first db location
            while (!cursor.isAfterLast()) { //while data still in db
                newWorkout = new WorkoutObject(); //create new workout object
                // and add all required information to new workout
                newWorkout.setWorkoutID(cursor.getInt(0));
                newWorkout.setWorkoutName(cursor.getString(1));
                newWorkout.setWorkoutType(cursor.getInt(2));
                newWorkout.setBarbellToggle(cursor.getInt(3));
                newWorkout.setOrmReps(cursor.getInt(4));
                newWorkout.setOrmWeight(cursor.getInt(5));
                newWorkout.setOrmCalc(cursor.getInt(6));

                workouts.add(newWorkout); //add new workout
                cursor.moveToNext(); //move cursor to next position
            }
            cursor.close(); //close cursor
        } catch (Exception e) { //return empty list of workout objects  if cant access
            workouts = new ArrayList<WorkoutObject>();
        }
        return workouts; //return arraylist of workout objects
    }

    public WorkoutObject getSpecificWorkout(int workoutID) { //retrieve Workout ID
        WorkoutObject workout = new WorkoutObject(); //create new workout Object
        String query = "SELECT * FROM workout WHERE _id =" + workoutID; //query to select specific workoutID
        Cursor cursor = database.rawQuery(query, null); //create cursor
        if (cursor.moveToFirst()) {//load all workout information for workout that matches query request
            workout = new WorkoutObject();
            workout.setWorkoutID(cursor.getInt(0));
            workout.setWorkoutName(cursor.getString(1));
            workout.setWorkoutType(cursor.getInt(2));
            workout.setBarbellToggle(cursor.getInt(3));
            workout.setOrmReps(cursor.getInt(4));
            workout.setOrmWeight(cursor.getInt(5));
            workout.setOrmCalc(cursor.getInt(6));
            cursor.close(); //close cursor
        }
        return workout; //return specific workout requested
    }

    public boolean deleteWorkout(int workoutID) {
        boolean didDelete = false; //set variable to false
        try { //try deleting from db
            didDelete = database.delete("workout", "_id=" + workoutID, null) > 0;
        } //if not successful in deleting from db
        catch (Exception e) {
            //do nothing
        }
        return didDelete; //return if successful or not in deletion of workout
    }

}
