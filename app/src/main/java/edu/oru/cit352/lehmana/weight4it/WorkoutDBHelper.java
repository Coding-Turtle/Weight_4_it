package edu.oru.cit352.lehmana.weight4it;
//Austin Lehman
//3/23/2022
//Weight 4 it
//WorkoutDBHelper
// helper class that sets up database for app storage information

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WorkoutDBHelper extends SQLiteOpenHelper {

    private static final String Database_Name = "userworkout.db"; //name of db
    private static final int Database_version = 1; //version of db

    private static final String CREATE_TABLE_WORKOUT = //creation of workout Table sql statement
            "create table workout (_id integer primary key autoincrement, "
                    + "workoutname text not null, workouttype integer ,barbelltoggle integer, "
                    + "ormreps integer, ormweight integer, ormcalc integer);";

    private static final String CREATE_TABLE_LOG = //creation of Log Table sql statement
            "create table log (_id integer primary key autoincrement,"
                    + "workoutname Text not null, workouttype integer, workoutreps text,"
                    + "workoutweight text,workoutdate text);";


    public WorkoutDBHelper(Context context) { //constructor calls for creation
        super(context, Database_Name, null, Database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WORKOUT); //create Workout database
        db.execSQL(CREATE_TABLE_LOG); //create Log database
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //autotrigger on upgrade
        Log.w(WorkoutDBHelper.class.getName(), //create log of database
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS workout");//drop old workout table
        db.execSQL("DROP TABLE IF EXISTS log"); //drop old log table
        onCreate(db); //recreate database
    }
}
