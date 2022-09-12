package edu.oru.cit352.lehmana.weight4it.log;
//Austin Lehman
//3/21/2022
//Weight 4 it
//LogAdapter
//This class is a helper that
//manages individual line items and formats data for display view for the Log, and
//manages each line item view. Allows users to view and toggle to delete workouts. Allows
//users to add new Log by taking them to their workout list. The view does not allow users to select
//a workout, only delete and scroll the page to view history
import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.oru.cit352.lehmana.weight4it.R;

public class LogAdapter extends RecyclerView.Adapter {
    private final ArrayList<LogObject> logData; //create array of log objects
    private String[] holdTypes; //create string array to hold types of workouts from xml
    private Resources resources; //create resources object
    private View.OnClickListener mOnItemClickListener; //create listener
    private boolean isDeleting; //create boolean for is deleting
    private final Context parentContext; //create context object


    public class LogViewHolder extends RecyclerView.ViewHolder {
        //holders for variables of textviews and button widgets
        public TextView textViewWorkout;
        public TextView textViewType;
        public TextView textViewReps;
        public TextView textViewWeight;
        public TextView textViewDate;
        public Button deleteButton;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            //assign objects to reference variables
            textViewWorkout = itemView.findViewById(R.id.textWorkoutName);
            textViewType = itemView.findViewById(R.id.textWorkoutType);
            textViewReps = itemView.findViewById(R.id.textWorkoutReps);
            textViewWeight = itemView.findViewById(R.id.textWorkoutWeight);
            textViewDate = itemView.findViewById(R.id.textWorkoutDate);
            deleteButton = itemView.findViewById(R.id.buttonDeleteLog);

            //set tag to thiS for item view and set onclick listener
            itemView.setTag(this);
            //disabled setOnclick listener this page is for view & delete only. May add ability to edit
            //or bring historical page in  different versions so keeping this call
            //itemView.setOnClickListener(mOnItemClickListener);
        }

        //return values of holder objects workouts, type,reps,weight,date, and delete button
        public TextView getTextViewWorkout() {
            return textViewWorkout;
        }

        public TextView getTextViewType() {
            return textViewType;
        }

        public TextView getTextViewReps() {
            return textViewReps;
        }

        public TextView getTextViewWeight() {
            return textViewWeight;
        }

        public TextView getTextViewDate() {
            return textViewDate;
        }

        public Button getDeleteButton() {
            return deleteButton;
        }

    }


    public LogAdapter(ArrayList<LogObject> arraylist, Context context) { //constructor for logadapter
        //assign logData and parent context to what is passed by logAdapter
        logData = arraylist;
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        //set listener to what was called by activity
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //viewholder creation
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_log_item, parent, false);
        return new LogAdapter.LogViewHolder(v); //return inflated viewholder
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) { //hold recycler view
        LogViewHolder wlv = (LogViewHolder) holder; //logview holder object
        //create reference to hold formated date for item
        CharSequence dateHold = DateFormat.format("MM/dd/yyyy", logData.get(position).getWorkoutDate());
        resources = parentContext.getResources();//get resources from parent context for xml array

        holdTypes = resources.getStringArray(R.array.workout_type_array); //assign xml array for workout type to hold type array
        wlv.getTextViewWorkout().setText(logData.get(position).getWorkoutName()); //get and set values for workout
        wlv.getTextViewType().setText("Workout Type: " + holdTypes[logData.get(position).getWorkoutType()]); //get and  set  values for workout type
        wlv.getTextViewReps().setText("Reps: " + logData.get(position).getWorkoutReps());//get and set values for reps
        wlv.getTextViewWeight().setText("Weight: " + logData.get(position).getWorkoutWeight()); //get and set values for weight
        wlv.getTextViewDate().setText(dateHold.toString()); //get ant set values for date

        //check if is deleting
        if (isDeleting) {
            //if is deleting set button to visible and enable on click listener
            wlv.getDeleteButton().setVisibility(View.VISIBLE);
            wlv.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position); //on click delete  item at position clicked
                }
            });
        } else { //if deleting is not enabled make delete button invisible
            wlv.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return logData.size();//return size of logData array for seeing items in view
    }

    private void deleteItem(int position) {
        LogObject loggedItem = logData.get(position); //get pasition of log object to be deleted
        LogDataSource logDataSource = new LogDataSource(parentContext); //create database helper
        try { //try to delete instance
            logDataSource.open(); //open db
            boolean didDelete = logDataSource.deleteLog(loggedItem.getLogID()); //delete item and set to boolean result
            logDataSource.close(); //close db
            if (didDelete) { //if item was deleted
                logData.remove(position); //remove the item from the dataset
                notifyDataSetChanged(); //refresh screen to remove item from screen
            } else { //if item not deleted toast failure
                Toast.makeText(parentContext, "Delete Failure!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception a) {
        //do nothing if error
        }
    }

    public void setDelete(boolean b) {
        isDeleting = b;
    } //set deleting values

}
