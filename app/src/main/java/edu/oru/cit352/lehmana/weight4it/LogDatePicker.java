package edu.oru.cit352.lehmana.weight4it;
//Austin Lehman
//4/23/2022
//Weight 4 it
//LogDatePicker DatePickerDialog
//Fragment that brings up a dialog box that allows users to select the Date for use. Saves
//Year/month/day/format

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class LogDatePicker extends DialogFragment {
    Calendar selectedDate; //class variable to save selected date

    public interface SaveDateListener {
        void didFinishDatePickerLog(Calendar selectedTime);
    }

    public LogDatePicker() {
        // Empty constructor required for DialogFragment
    }

    @Override
    //creates view of resource associated with inflater
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.select_date, container);
        //gets references to widgets
        getDialog().setTitle("Select Date");
        selectedDate = Calendar.getInstance();//instantiates object and sets to current date

        final CalendarView cv = view.findViewById(R.id.calendarView); //creates final calendarview object
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() { //listener to wait for date change by user
            @Override
            //when user selects date, set selected date as chosen date
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                selectedDate.set(year, month, day);
            }
        });

        Button saveButton = view.findViewById(R.id.buttonLog); //instantiate save button object
        saveButton.setOnClickListener(new View.OnClickListener() { //set listener to wait for save button selection
            @Override
            public void onClick(View view) {
                saveItem(selectedDate);// when save is selected save currently chosen date to contact list
            }
        });
        Button cancelButton = view.findViewById(R.id.buttonCancel); //instantiate cancel button object
        cancelButton.setOnClickListener(new View.OnClickListener() {//wait for user to select button to cancel dialog
            @Override
            public void onClick(View v) {
                getDialog().dismiss(); //close datepicker dialog
            }
        });
        return view; //close dialog box
    }

    private void saveItem(Calendar selectedTime) {
        LogDatePicker.SaveDateListener activity = (LogDatePicker.SaveDateListener) getActivity(); //create reference to listener
        activity.didFinishDatePickerLog(selectedTime); //gets & shows the results of dialog
        getDialog().dismiss();//dismisses fragment
    }
}
