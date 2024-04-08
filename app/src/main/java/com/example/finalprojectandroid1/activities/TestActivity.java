package com.example.finalprojectandroid1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.finalprojectandroid1.R;

import java.util.Calendar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        editTextTime = findViewById(R.id.editTextTime);
        Button okMulti = findViewById(R.id.okMultiText);
        TextView showTimeText = findViewById(R.id.showText);

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
        radioGroup = findViewById(R.id.radioGroupTest);
        appointmentTimes = new HashMap<>();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "cal click");

            }
        });

//        String s = "רחוב 30 דירה 10 קומה 2, אופקים";
////        String[] ss = s.split(" |,|קומה|דירה");
//        String[] ss = s.split("\\s*(,|\\s|דירה|קומה)\\s*");
//        for(String st : ss){
//            Log.d(TAG,"st: " + st);
//        }

        String street = "רחוב 30";
        String houseNum = "10";
        String floor = "2";
        String city = "אופקים";

        String all = street + " דירה " + houseNum + " קומה " + floor + ", " + city;
        Log.d(TAG, all);




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

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                    RadioButton radioButton = findViewById(selectedRadioButtonId);
                    String appointment = selectedDate + " " + radioButton.getText().toString();
                    text.setText(appointment);

                    // Remove selected appointment time for the selected date
                    String[] appointmentTimesForDate = appointmentTimes.get(selectedDate);
                    if (appointmentTimesForDate != null) {
                        String selectedTime = radioButton.getText().toString();
                        appointmentTimesForDate = removeTime(appointmentTimesForDate, selectedTime);
                        appointmentTimes.put(selectedDate, appointmentTimesForDate);
                    }

                    // Reset radio buttons for the selected date
                    resetRadioGroup(selectedDate);
                } else {
                    // No RadioButton is selected
                    // Handle this case accordingly
                }
            }
        });

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