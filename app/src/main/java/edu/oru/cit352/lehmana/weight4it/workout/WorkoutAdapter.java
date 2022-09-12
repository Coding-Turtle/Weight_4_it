package edu.oru.cit352.lehmana.weight4it.workout;
//Austin Lehman
//Mobile APP CIT 352
//DR. Osborne
//3/21/2022
//Weight 4 it
//WorkoutAdapter
//This class is a helper that
//manages individual line items and formats data for display view, and
//manages each line item view. Allows users to select to view and toggle to delete workouts. Allows
//users to add new workouts from page

import android.content.Context;
import android.content.res.Resources;
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

public class WorkoutAdapter extends RecyclerView.Adapter {
    private final ArrayList<WorkoutObject> workoutData; //create array of workout objects
    private String[] holdTypes; //createString array
    private Resources resources; //create resources object
    private View.OnClickListener mOnItemClickListener; //create listener
    private boolean isDeleting; //create boolean for is deleting
    private final Context parentContext; //create context object

    public class WorkoutViewHolder extends RecyclerView.ViewHolder {
        //add ref variables for widgets
        public TextView textViewWorkout;
        public TextView textViewType;
        public TextView textViewMax;
        public Button deleteButton;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            //assign objects to reference variables
            textViewWorkout = itemView.findViewById(R.id.textWorkoutName);
            textViewType = itemView.findViewById(R.id.textWorkoutType);
            textViewMax = itemView.findViewById(R.id.textOneRepMax);
            deleteButton = itemView.findViewById(R.id.buttonDeleteWorkout);
            itemView.setTag(this); //set tag to know what was clicked
            itemView.setOnClickListener(mOnItemClickListener); //set listene to item view
        }

        //getters for Workout,Type,Maxweight and delete button
        public TextView getTextViewWorkout() {
            return textViewWorkout;
        }

        public TextView getTextViewType() {
            return textViewType;
        }

        public TextView getTextViewMax() {
            return textViewMax;
        }

        public Button getDeleteButton() {
            return deleteButton;
        }

    }


    public WorkoutAdapter(ArrayList<WorkoutObject> arraylist, Context context) { //constructor for Workoutadapter
        //assign arraylist and context to variables of what is passed by constructor
        workoutData = arraylist;
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        //set listener to what was called by activity
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //view holder created
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_list_item, parent, false);
        return new WorkoutViewHolder(v); //return inflated view holder
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) { //hold recycler view
        WorkoutViewHolder whv = (WorkoutViewHolder) holder; //workoutview holder object

        resources = parentContext.getResources(); //get resources from parent context for xml arrays

        holdTypes = resources.getStringArray(R.array.workout_type_array); //assign xml array for workout type to holdtype array
        whv.getTextViewWorkout().setText(workoutData.get(position).getWorkoutName()); //get and set  values for workout
        whv.getTextViewType().setText("Workout Type: " + holdTypes[workoutData.get(position).getWorkoutType()]); //get and set values for  workout type
        whv.getTextViewMax().setText("One Rep Max: " + workoutData.get(position).getOrmCalc().toString() + " Pounds"); //set values for one rep max

        //check if is deleting
        if (isDeleting) {
            //if is deleting set button visible
            whv.getDeleteButton().setVisibility(View.VISIBLE);
            whv.getDeleteButton().setOnClickListener(new View.OnClickListener() { //set listener to wait for click
                @Override
                public void onClick(View v) {
                    deleteItem(position);
                }//on click delete item
            });
        } else { //if not deleting button is invisible
            whv.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return workoutData.size();//return the size af workoutData array for seeing how many items in view
    }

    private void deleteItem(int position) {
        WorkoutObject workout = workoutData.get(position); //get position of workout object to be deletid
        WorkoutDataSource ds = new WorkoutDataSource(parentContext); //create database helper
        try { //try to delete
            ds.open(); //open db
            boolean didDelete = ds.deleteWorkout(workout.getWorkoutID()); //try to delete and set result to boolean
            ds.close(); //close db
            if (didDelete) { //if item was deleted
                workoutData.remove(position); //remove from dataset
                notifyDataSetChanged(); //refresh screen to remove item from screen
            } else { //if item not deleted toast failure
                Toast.makeText(parentContext, "Delete Failure!", Toast.LENGTH_LONG).show();
            }
        }//catch exception
        catch (Exception a) {
            //do nothing
        }
    }

    public void setDelete(boolean b) {
        isDeleting = b;
    } //set deleting

}
