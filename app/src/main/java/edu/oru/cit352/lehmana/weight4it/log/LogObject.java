package edu.oru.cit352.lehmana.weight4it.log;
//Austin Lehman
//4/23/2022
//weight 4 it
//LogObject Class
//This class creates a workout object that can store all information requested from a user for the
//creation of a Log. Has getters and setters for adjusting log information
import java.util.Calendar;

public class LogObject {
    //ref variables for log object
    private int logID;
    private String workoutName;
    private Integer workoutType;
    private String workoutReps;
    private String workoutWeight;
    private Calendar workoutDate;

    public LogObject() { //constructor for log object
        logID = -1;
        workoutDate = Calendar.getInstance();
    }

    public int getLogID() {
        return logID;
    } //getter to return logid

    public void setLogID(int logID) {
        this.logID = logID;
    } //setter to set logID

    public String getWorkoutName() {
        return workoutName;
    } //getter to return workoutName

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName; //setter to Set workout name
    }

    public Integer getWorkoutType() {
        return workoutType;
    } //getter to return workoutType

    public void setWorkoutType(Integer workoutType) {
        this.workoutType = workoutType; //setter to set workout type
    }

    public String getWorkoutReps() {
        return workoutReps;
    } //getter to return  workout reps

    public void setWorkoutReps(String workoutReps) {
        this.workoutReps = workoutReps; //setter to set workout reps
    }

    public String getWorkoutWeight() {
        return workoutWeight;
    } //getter to return workoutWeight

    public void setWorkoutWeight(String workoutWeight) {
        this.workoutWeight = workoutWeight;//setter to set workout weight
    }

    public Calendar getWorkoutDate() {
        return workoutDate;
    } //getter to return workout date

    public void setWorkoutDate(Calendar workoutDate) {
        this.workoutDate = workoutDate; //setter to set workoutDate
    }
}
