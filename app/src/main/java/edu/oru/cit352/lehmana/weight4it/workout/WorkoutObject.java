package edu.oru.cit352.lehmana.weight4it.workout;
//Austin Lehman
//Mobile APP CIT 352
//DR. Osborne
//4/23/2022
//weight 4 it
//WorkoutObject Class
//This class creates a workout object that can store all information requested from a user for the
//creation of a Workout. Has gettors and settors for adjusting workout information

public class WorkoutObject {
    //ref variables for workout
    private int workoutID;
    private String workoutName;
    private Integer workoutType;
    private Integer barbellToggle;
    private Integer ormReps;
    private Integer ormWeight;
    private Integer ormCalc;


    public WorkoutObject() {
        workoutID = -1;
    } //constructor for Workout Object

    public int getWorkoutID() {
        return workoutID;
    } //getter for workout Id

    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }//Setter for workout id

    public String getWorkoutName() {
        return workoutName;
    } //getter for workout name

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }//Setter for workout name

    public Integer getWorkoutType() {
        return workoutType;
    } //getter for workout type

    public void setWorkoutType(Integer workoutType) {
        this.workoutType = workoutType;
    }//setter for workout type

    public Integer getBarbellToggle() {
        return barbellToggle;
    }//getter for barbell toggle

    public void setBarbellToggle(Integer barbellToggle) {
        this.barbellToggle = barbellToggle;
    } //setter for barbell toggle

    public Integer getOrmReps() {
        return ormReps;
    } //getter for orm reps

    public void setOrmReps(Integer ormReps) {
        this.ormReps = ormReps;
    } //setter for orm reps

    public Integer getOrmWeight() {
        return ormWeight;
    }//gettor for orm weight

    public void setOrmWeight(Integer ormWeight) {
        this.ormWeight = ormWeight;
    }//setter for orm weight

    public Integer getOrmCalc() {
        return ormCalc;
    }//getter for orm calculation

    public void setOrmCalc(Integer ormCalc) {
        this.ormCalc = ormCalc;
    } //setter for orm calculation
}
