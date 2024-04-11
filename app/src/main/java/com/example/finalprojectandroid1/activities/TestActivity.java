package com.example.finalprojectandroid1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.finalprojectandroid1.GlobalMembers;
import com.example.finalprojectandroid1.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class TestActivity extends AppCompatActivity {

    String TAG = "tTestActivity";
    String selectedDate;
    TextView text;
    RadioGroup radioGroup;
    String[] setTime = {"10:00", "10:30", "12:00", "14:30"};
    HashMap<String, String[]> appointmentTimes;
    EditText editTextTime;
    String date;
    RadioGroup radioGroup1;
    RadioGroup radioGroup2;
    int nowYear,nowMonth,nowDay;
    int maxRadio = 4;
    int checkedRadio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        editTextTime = findViewById(R.id.editTextTime);
        Button okMulti = findViewById(R.id.okMultiText);
        TextView showTimeText = findViewById(R.id.showText);
        Button btn = findViewById(R.id.button);
        Button dateBtn = findViewById(R.id.dateButton);


        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);



        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MaterialDatePicker.Builder<Pair<Long,Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
//                builder.setTitleText("יש לבחור תקופת זמן");
//
//                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
//
//                // Get yesterday's date
//                Calendar calendar = Calendar.getInstance();
//                calendar.add(Calendar.DAY_OF_MONTH, -1); // Subtract one day
//
//                // Set the time to midnight to get the start of the day
//                calendar.set(Calendar.HOUR_OF_DAY, 0);
//                calendar.set(Calendar.MINUTE, 0);
//                calendar.set(Calendar.SECOND, 0);
//                calendar.set(Calendar.MILLISECOND, 0);
//
//                long minStartDate = calendar.getTimeInMillis();
//                constraintsBuilder.setStart(minStartDate);
//
//                // Set a validator to restrict dates before yesterday
//                constraintsBuilder.setValidator(DateValidatorPointForward.now());
//
//                builder.setCalendarConstraints(constraintsBuilder.build());
//
//                MaterialDatePicker<Pair<Long,Long>> datePicker = builder.build();
//
//                datePicker.addOnPositiveButtonClickListener(selection -> {
//                    Long startDate = selection.first;
//                    Long endDate = selection.second;
//
////                    String formattedStartDateShow =  sdfForShow.format(new Date(startDate));
////                    String formattedEndDateShow= sdfForShow.format(new Date(endDate));
////
////                    selectedStartDateText = sdfForCompareDatabase.format(new Date(startDate));
////                    selectedEndDateText = sdfForCompareDatabase.format(new Date(endDate));
////
////                    selectedStartDate.setText(formattedStartDateShow);
////                    selectedStartDate.setGravity(Gravity.END);
////                    selectedEndDate.setText(formattedEndDateShow);
////                    selectedEndDate.setGravity(Gravity.END);
//                });
//
//                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date = "" + year + (monthOfYear + 1) + dayOfMonth;

                        Log.d(TAG, "date: " + date);
                    }
                }, nowYear, nowMonth, nowDay);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog timeDialog = new Dialog(v.getContext());
//                timeDialog.setContentView(R.layout.card_time_picker_dialog);
                timeDialog.show();

//                TimePicker timePicker = timeDialog.findViewById(R.id.timePicker);
                Button backButton = timeDialog.findViewById(R.id.backButton);
                Button confirmButton = timeDialog.findViewById(R.id.confirmButton);



                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    }
                };
//
//                Calendar calendar = Calendar.getInstance();
//                int hour = calendar.get(Calendar.HOUR_OF_DAY);
//                int minute = calendar.get(Calendar.MINUTE);
//                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), timeSetListener, hour, minute, true);
////
//                timePickerDialog.show();
            }
        });


        // Set OnClickListener for the EditText to show the time picker dialog
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        okMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the time from the EditText and display it
                showTimeText.setText(editTextTime.getText().toString());
            }
        });

// Function to show the TimePickerDialog


        CalendarView calendarView = findViewById(R.id.calendarTest);
        text = findViewById(R.id.textTest);
        Button ok = findViewById(R.id.okButtonTest);
//        radioGroup1 = findViewById(R.id.radioGroup1);
//        radioGroup2 = findViewById(R.id.radioGroup2);
        appointmentTimes = new HashMap<>();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "cal click");

            }
        });

        ArrayList<RadioButton> radioButtonsList = new ArrayList<>();
//        radioGroupList.add(radioGroup1);
//        radioGroupList.add(radioGroup2);
//        radioGroup1.

        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        int radioCount = 0;
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(radioGroup);
        for(int i = 1; i < 21; i++){
            if(radioCount == maxRadio){
                radioCount = 0;
                radioGroup = new RadioGroup(this);
                radioGroup.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.addView(radioGroup);
            }
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText("10:00");
            radioButton.setTextColor(Color.BLACK);
            radioButton.setId(i);
            Log.d(TAG, "i: " + i);
            radioButtonsList.add(radioButton);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkedRadio != 0 ){
                        radioButtonsList.get(checkedRadio -1).setChecked(false);
                    }
                    checkedRadio = radioButton.getId();
                    Log.d(TAG, "checkedRadio: " + checkedRadio);
                }
            });
            radioGroup.addView(radioButton);
            radioCount++;


        }

//        String s = "רחוב 30 דירה 10 קומה 2, אופקים";
////        String[] ss = s.split(" |,|קומה|דירה");
//        String[] ss = s.split("\\s*(,|\\s|דירה|קומה)\\s*");
//        for(String st : ss){
//            Log.d(TAG,"st: " + st);
//        }
//
//        String street = "רחוב 30";
//        String houseNum = "10";
//        String floor = "2";
//        String city = "אופקים";
//
//        String all = street + " דירה " + houseNum + " קומה " + floor + ", " + city;
//        Log.d(TAG, all);




        // Get today's date
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);

        // Set initial appointment times for all dates starting from today
        for (int i = dayOfMonth; i <= 31; i++) {
            appointmentTimes.put(getFormattedDate(year, month + 1, i), setTime);
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = getFormattedDate(year, month + 1, dayOfMonth);
                resetRadioGroup(selectedDate);
            }
        });

//        ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
//                if (selectedRadioButtonId != -1) {
//                    RadioButton radioButton = findViewById(selectedRadioButtonId);
//                    String appointment = selectedDate + " " + radioButton.getText().toString();
//                    text.setText(appointment);
//
//                    // Remove selected appointment time for the selected date
//                    String[] appointmentTimesForDate = appointmentTimes.get(selectedDate);
//                    if (appointmentTimesForDate != null) {
//                        String selectedTime = radioButton.getText().toString();
//                        appointmentTimesForDate = removeTime(appointmentTimesForDate, selectedTime);
//                        appointmentTimes.put(selectedDate, appointmentTimesForDate);
//                    }
//
//                    // Reset radio buttons for the selected date
//                    resetRadioGroup(selectedDate);
//                } else {
//                    // No RadioButton is selected
//                    // Handle this case accordingly
//                }
//            }
//        });

    }

    private void showTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Format the selected time as "hh:mm" and set it to the EditText
                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                editTextTime.setText(formattedTime);
            }
        };

        // Create and show the TimePickerDialog
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, timeSetListener, hour, minute, true);
        timePickerDialog.show();
    }



    // Method to remove a specific appointment time from the appointment times array
    private String[] removeTime(String[] appointmentTimesArray, String timeToRemove) {
        // Calculate the new length of the updated array
        int newSize = appointmentTimesArray.length - 1;
        // Create a new array with the updated size
        String[] updatedTimes = new String[newSize];
        // Initialize an index for the updated array
        int newIndex = 0;
        // Iterate over the original array
        for (String time : appointmentTimesArray) {
            // Check if the current time is not equal to the time to remove
            if (!time.equals(timeToRemove)) {
                // If not equal, add it to the updated array
                updatedTimes[newIndex++] = time;
            }
        }
        return updatedTimes;
    }

    // Method to reset the radio group with appointment times for the selected date
    private void resetRadioGroup(String selectedDate) {
        radioGroup.removeAllViews();
        String[] appointmentTimesForDate = appointmentTimes.get(selectedDate);
        if (appointmentTimesForDate == null) {
            // Initialize appointmentTimesForDate with default appointment times
            appointmentTimesForDate = setTime;
        }
        for (String time : appointmentTimesForDate) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(time);
            radioGroup.addView(radioButton);
        }
    }


    // Method to get formatted date string (yyyyMMdd) from year, month, and day
    private String getFormattedDate(int year, int month, int dayOfMonth) {
        return String.format("%04d%02d%02d", year, month, dayOfMonth);
    }
}