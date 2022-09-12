package edu.oru.cit352.lehmana.weight4it.log;
//Austin Lehman
//4/23/2022
//weight 4 it
//LogDataSource
//This class is a helper class that will assist with inserting, and updating a Log into the
//database. Allows opening and closing of database
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import edu.oru.cit352.lehmana.weight4it.WorkoutDBHelper;

public class LogDataSource {
    private SQLiteDatabase database; //create reference to SQLitedatabase object
    private final WorkoutDBHelper dbHelper; //create reference to WorkoutdbHelper

    //constructor to create dbhelper
    public LogDataSource(Context context) { //constructor instantiates helper class
        dbHelper = new WorkoutDBHelper(context);
    }

    public void open() throws SQLException { //open the database
        database = dbHelper.getWritableDatabase();
    }

    public void close() { //close the database
        dbHelper.close();
    } //close the database


    public boolean insertLog(LogObject L) { //create new log
        boolean didSucceed = false; //set boolean false assume failed creation
        try { //try to get object and put into database
            ContentValues initialValues = new ContentValues(); //data struct of initial values
            //load values into log object
            initialValues.put("workoutname", L.getWorkoutName());
            initialValues.put("workouttype", L.getWorkoutType());
            initialValues.put("workoutreps", L.getWorkoutReps());
            initialValues.put("workoutweight", L.getWorkoutWeight());
            initialValues.put("workoutdate", String.valueOf(L.getWorkoutDate().getTimeInMillis()));

            didSucceed = database.insert("log", null, initialValues) > 0;//sets didsucceed to 1 if entered
        } catch (Exception e) {
            //Do nothing - will return false if there is an exception
        }
        return didSucceed; //return if successful entry into db
    }

    public boolean updateLog(LogObject L) {
        boolean didSucceed = false; //set to false, assume failure
        try {
            Long rowId = (long) L.getLogID(); //retrieve logid and set to rowid
            ContentValues updateValues = new ContentValues(); //create datastruct of values to update

            //put values into table as exist
            updateValues.put("workoutname", L.getWorkoutName());
            updateValues.put("workouttype", L.getWorkoutType());
            updateValues.put("workoutreps", L.getWorkoutReps());
            updateValues.put("workoutweight", L.getWorkoutWeight());
            updateValues.put("workoutdate", String.valueOf(L.getWorkoutDate().getTimeInMillis()));

            //check if succeded in database update
            didSucceed = database.update("log", updateValues, "_id=" + rowId, null) > 0;
        } catch (Exception e) {
            //do nothing if failed, will stay false
        }
        return didSucceed; //return if updated
    }

    public int getLastLogId() {
        int lastId = -1; //ensure that Id for last workout added
        try { //try query from db
            String query = "Select MAX(_id) from log"; //query to find max id
            Cursor cursor = database.rawQuery(query, null); //create cursor for moving through db
            cursor.moveToFirst(); //move cursor to first position
            lastId = cursor.getInt(0); //set last id to value of id at beginning of db
            cursor.close(); //close cursor
        } catch (Exception e) { //if value not in tables
            lastId = -1; //set value to -1 if not updated
        }
        return lastId; //return value of last log object  added
    }

    public ArrayList<LogObject> getLogs(String sortField, String sortOrder) {
        ArrayList<LogObject> log = new ArrayList<LogObject>(); //create arraylist of log objects
        try { //try accessing the database
            String query = "SELECT * FROM log ORDER BY " + sortField + " " + sortOrder; //select all from log with sort pref
            Cursor cursor = database.rawQuery(query, null); //create cursor to move through database
            LogObject newLog; //create new log object
            cursor.moveToFirst(); //move cursor to first item in db
            while (!cursor.isAfterLast()) { //while there is still data in db
                newLog = new LogObject(); //create new log object
                //add all required information to new log object
                newLog.setLogID(cursor.getInt(0));
                newLog.setWorkoutName(cursor.getString(1));
                newLog.setWorkoutType(cursor.getInt(2));
                newLog.setWorkoutReps(cursor.getString(3));
                newLog.setWorkoutWeight(cursor.getString(4));
                //create calendar object and set time to value date in database
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(5)));
                newLog.setWorkoutDate(calendar); //assign value of date from calendar

                log.add(newLog); //add new workout
                cursor.moveToNext(); //move cursor to next position
            }
            cursor.close();
        } catch (Exception e) {
            log = new ArrayList<LogObject>();
        }
        return log;
    }

    //not used in this version of app, can be used to get specific log in future keeping for reference
    public LogObject getSpecificLog(int logId) { //retrieve Log Id
        LogObject log = new LogObject(); //create new log object
        String query = "SELECT * FROM log WHERE _id =" + logId; //query to select specific log
        Cursor cursor = database.rawQuery(query, null); //create cursor
        if (cursor.moveToFirst()) { //load al workout information if the workout matches a query request
            log.setLogID(cursor.getInt(0));
            log.setWorkoutName(cursor.getString(1));
            log.setWorkoutType(cursor.getInt(2));
            log.setWorkoutReps(cursor.getString(3));
            log.setWorkoutWeight(cursor.getString(4));

            //create calendar object and set time to value date in database
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(cursor.getString(5)));
            log.setWorkoutDate(calendar); //assign value to date from calendar
            cursor.close(); //close cursor
        }
        return log; //return item
    }

    public boolean deleteLog(int logID) {
        boolean didDelete = false; //set variable to false
        try { //try deleting from db
            didDelete = database.delete("log", "_id=" + logID, null) > 0;
        } //if not successful in deletion from db
        catch (Exception e) {
            //do nothing
        }
        return didDelete; //return if item deleted or not in deletion of log
    }
}
