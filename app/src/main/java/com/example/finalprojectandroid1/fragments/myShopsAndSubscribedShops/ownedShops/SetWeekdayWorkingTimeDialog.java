package com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShops.ownedShops;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;

import com.example.finalprojectandroid1.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SetWeekdayWorkingTimeDialog {

    String TAG = "SetWeekdayWorkingTimeDialog";
    Button startingTimeButton;
    Button endingTimeButton;
    Button cancelButton;
    Button confirmButton;
    ToggleButton toggleButtonSun;
    ToggleButton toggleButtonMon;
    ToggleButton toggleButtonTue;
    ToggleButton toggleButtonWed;
    ToggleButton toggleButtonThur;

    int startHour;
    int startMinutes;
    int endHour;
    int endMinutes;

    Dialog dialog;

    public SetWeekdayWorkingTimeDialog(Context context, AddOwnedShop addOwnedShop) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.card_set_weekday_working_time_dialog);
        dialog.show();
        startingTimeButton = dialog.findViewById(R.id.setStartOfWorkRangeButton);
        endingTimeButton = dialog.findViewById(R.id.setEndOfWorkRangeButton);

        cancelButton = dialog.findViewById(R.id.cancelButton);
        confirmButton = dialog.findViewById(R.id.confirmButton);

        toggleButtonSun = dialog.findViewById(R.id.toggleButtonSun);
        toggleButtonMon = dialog.findViewById(R.id.toggleButtonMon);
        toggleButtonTue = dialog.findViewById(R.id.toggleButtonTue);
        toggleButtonWed = dialog.findViewById(R.id.toggleButtonWed);
        toggleButtonThur = dialog.findViewById(R.id.toggleButtonThur);
//        sunChip = dialog.findViewById(R.id.chipSun);
//        sunChip = dialog.findViewById(R.id.chipSun);

        ArrayList<String> daysSelected = new ArrayList<>();
//       HashMap<String,List<Integer>> timeSelected = new HashMap<>();

        ArrayList<ToggleButton> chipList = new ArrayList();
        chipList.add(toggleButtonSun);
        chipList.add(toggleButtonMon);
        chipList.add(toggleButtonTue);
        chipList.add(toggleButtonWed);
        chipList.add(toggleButtonThur);

        for(ToggleButton day : chipList){
            day.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(day.isChecked()){
                        Log.d(TAG, "on: " + day.getText().toString());
                        daysSelected.add(day.getText().toString());
                    }else{
                        Log.d(TAG, "off: " + day.getText().toString());
                        daysSelected.remove(day.getText().toString());
                    }
                }
            });

        }
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"days array: " + daysSelected);
                if(startingTimeButton.getText().toString().equals("זמן התחלה") || endingTimeButton.getText().toString().equals("זמן סיום") ){
                    Toast.makeText(context, "נא הכנס שעת החתלה ושעת סיום", Toast.LENGTH_SHORT).show();
                }else if(startHour > endHour || (startHour == endHour && startMinutes > endMinutes) ){
                    Toast.makeText(context, "זמן ההתחלה גדול מזמן הסיום", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "אוקיי", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "startHour: " + startHour + ", startMinutes: " + startMinutes +
                            ", endHour: " + endHour + ", endMinutes: " + endMinutes);
                    addOwnedShop.updateWorkTime(daysSelected,startHour,startMinutes,endHour,endMinutes);
                    dialog.dismiss();
//                    String startTime = String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinutes);
//                    String endTime = String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinutes);

//                    String[] workTime = {startTime,endTime};
//                    for(String selectedDay : daysSelected){
//                        if(workTimeEachDay.containsKey(selectedDay)){
//                            Log.d(TAG, selectedDay + ": " + workTimeEachDay.get(selectedDay));
//                        }else{
//                            ArrayList<String[]> workTimeArray = new ArrayList<>();
//                            workTimeArray.add(workTime);
//                            workTimeEachDay.put(selectedDay,workTimeArray);
//                        }
//                    }

                }

//                weekdayChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
//                    @Override
//                    public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
////                        List<Integer> ids = weekdayChipGroup.getCheckedChipIds();
////                        Log.d(TAG,ids.toString());
//                        Log.d(TAG,"onCheckedChanged");
//
//                    }
//                });
            }
        });

        startingTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "startingTimeButton click");
               showTimePickerDialog(startingTimeButton,v);

            }
        });

        endingTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "endingTimeButton click");
                showTimePickerDialog(endingTimeButton,v);
//                endHour = endTime[0];
//                endMinutes = endTime[1];

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }
    private void showTimePickerDialog(Button timeButton,View v) {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Format the selected time as "hh:mm" and set it to the EditText
                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                timeButton.setText(formattedTime);
                if(timeButton.getId() == R.id.setStartOfWorkRangeButton){
                    startHour = hourOfDay;
                    startMinutes = minute;
                }else if(timeButton.getId() == R.id.setEndOfWorkRangeButton){
                    endHour = hourOfDay;
                    endMinutes = minute;
                }

            }
        };

        // Create and show the TimePickerDialog
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), timeSetListener, hour, minute, true);
        timePickerDialog.show();
    }



}
