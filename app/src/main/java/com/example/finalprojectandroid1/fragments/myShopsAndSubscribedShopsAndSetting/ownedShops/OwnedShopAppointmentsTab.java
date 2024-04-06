package com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.ShopInfoActivity;
import com.example.finalprojectandroid1.appointment.AppointmentAdapter;
import com.example.finalprojectandroid1.appointment.AppointmentModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OwnedShopAppointmentsTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OwnedShopAppointmentsTab extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OwnedShopAppointmentsTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OwnedShopAppointmentsTab.
     */
    // TODO: Rename and change types and number of parameters
    public static OwnedShopAppointmentsTab newInstance(String param1, String param2) {
        OwnedShopAppointmentsTab fragment = new OwnedShopAppointmentsTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    String TAG = "OwnedShopAppointmentsTab";
    Button calendarButton;
    TextView selectedStartDate;
    String selectedStartDateText;
    TextView selectedEndDate;
    String selectedEndDateText;

    int selectedStartTimeNum;
    int selectedEndTimeNum;
    AppointmentAdapter shopAppointsAdapter;
    LinkedHashMap<String[], AppointmentModel> appointsDateAndTime;
    ArrayList<AppointmentModel> shopAppointmentsList;
//    ArrayList<AppointmentModel> selectedDatesAppointmentsList;
    AppointmentAdapter selectedAppointmentsAdapter;
    SimpleDateFormat simpleDateFormat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_owned_shop_appointments_tab, container, false);
        calendarButton = view.findViewById(R.id.calendarButtonAppointTab);
        selectedStartDate = view.findViewById(R.id.startDateText);
        selectedEndDate = view.findViewById(R.id.endDateText);

        TextView selectedStartTime = view.findViewById(R.id.startTimeForDateText);
        TextView selectedEndTime = view.findViewById(R.id.endTimeForDateText);

        Button startTimeButton = view.findViewById(R.id.pickTimeForStartButton);
        Button endTimeButton = view.findViewById(R.id.pickTimeForEndButton);
        Button showAppointsButton = view.findViewById(R.id.showAppointsOnSelectedDatesButton);
//
        ShopInfoActivity shopInfoActivity = (ShopInfoActivity) getActivity();
        shopAppointmentsList = new ArrayList<>();
        appointsDateAndTime = new LinkedHashMap<>();
//        selectedDatesAppointmentsList = new ArrayList<>();

//        shopAppointmentAdapter = new AppointmentAdapter(selectedDatesAppointmentsList, getContext(), true);

        calendarButton.setOnClickListener(v -> DatePickerDialog());
        simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");

        RecyclerView appointsRes = view.findViewById(R.id.appointResInShop);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        appointsRes.setLayoutManager(layoutManager);


        DatabaseReference getShopAppoints = FirebaseDatabase.getInstance().getReference("shops")
                .child(shopInfoActivity.getShop().getShopUid()).child("shopAppointments");



        try{
            getShopAppoints.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int count = 0;
                    for(DataSnapshot dateSnap: snapshot.getChildren()){
                        String dateKey = dateSnap.getKey();
                        Log.d(TAG, "snapshot.getKey(): " + dateSnap.getKey());

                        for(DataSnapshot appointSnap: dateSnap.getChildren()){
                            String timeKey = appointSnap.getKey();
                            Log.d(TAG, "appointSnap.getKey(): " + appointSnap.getKey());
//                            Log.d(TAG, "setMyAppointmentsList appointSnap count: " + appointSnap.getChildrenCount());
//                        Log.d(TAG, "setMyAppointmentsList appointSnap value: " + appointSnap.getValue());
                            AppointmentModel newAppoint = appointSnap.getValue(AppointmentModel.class);
                            String[] timeAndDateKey = {dateKey,timeKey};
                            Log.d(TAG, timeAndDateKey[0] + " " + timeAndDateKey[1]);
                            Log.d(TAG, newAppoint.shopToString());
                            try{
                                appointsDateAndTime.put(timeAndDateKey,newAppoint);
                            }catch(Exception e){
                                Log.e(TAG, "appointsDateAndTime error: " + e.getMessage());
                            }

//                            Log.d(TAG, "newAppoint: " + newAppoint.getDate());

                            shopAppointmentsList.add(newAppoint);
                            count++;
                            if(count == snapshot.getChildrenCount()){
//                                Log.d(TAG, "shopAppointmentsList.size(): " + shopAppointmentsList.size());
//
//                                Log.d(TAG, "size: " + shopAppointmentsList.size());
                                for(int i = 0; i < shopAppointmentsList.size() - 1; i++){
                                    Log.d(TAG, "date: " + shopAppointmentsList.get(i).getDate());
                                    Log.d(TAG, "start time: " + shopAppointmentsList.get(i).getTime().getStartTime());
                                }
                                for(String[] check : appointsDateAndTime.keySet()){
                                    Log.d(TAG, "hash date: " + appointsDateAndTime.get(check).getDate());
                                    Log.d(TAG, "hash start time: " + appointsDateAndTime.get(check).getTime().getStartTime());
                                }
                                shopAppointsAdapter = new AppointmentAdapter(shopAppointmentsList,shopInfoActivity,true, shopInfoActivity.getShop().getShopUid());
//                                Log.d(TAG, "shopAppointmentsList.get(0): " + shopAppointmentsList.get(0).getClass());
                                appointsRes.setAdapter(shopAppointsAdapter);

                            }
                        }
//
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch(Exception e){
            Log.e(TAG, "Error fetching user appointments: " + e.getMessage());
        }

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Format the selected time as "hh:mm" and set it to the EditText
                        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        selectedStartTime.setText(formattedTime);
                        selectedStartTimeNum = Integer.parseInt(formattedTime.replace(":",""));
                        selectedStartTime.setGravity(Gravity.END);


                    }
                };

                // Create and show the TimePickerDialog
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), timeSetListener, hour, minute, true);
                timePickerDialog.show();
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Format the selected time as "hh:mm" and set it to the EditText
                        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        selectedEndTime.setText(formattedTime);
                        selectedEndTimeNum = Integer.parseInt(formattedTime.replace(":",""));
                        selectedEndTime.setGravity(Gravity.END);


                    }
                };

                // Create and show the TimePickerDialog
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), timeSetListener, hour, minute, true);
                timePickerDialog.show();
            }





        });

        showAppointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedStartDateText == null || selectedEndDateText == null || selectedStartTimeNum == 0 || selectedEndTimeNum == 0 ){
                    Toast.makeText(shopInfoActivity, "נא לבחור תאריכים ושעות", Toast.LENGTH_SHORT).show();
                }else{
                    ArrayList<AppointmentModel> selectedDatesAppointmentsList = new ArrayList<>();
                    for(String[] timeAndDate : appointsDateAndTime.keySet()){
                        int date = Integer.parseInt(timeAndDate[0]);
                        int startTime = Integer.parseInt(timeAndDate[1].replace(":",""));
                        int startDate = Integer.parseInt(selectedStartDateText);
                        int endDate = Integer.parseInt(selectedEndDateText);

                        Log.d(TAG,"date: " + date + " startDate: " + startDate + " endDate: " + endDate);
                        Log.d(TAG,"startTime: " + startTime + " selectedStartTimeNum: " + selectedStartTimeNum + " selectedEndTimeNum: " + selectedEndTimeNum);
                        if((date == startDate && selectedStartTimeNum <= startTime ) || (date == endDate && selectedEndTimeNum >= startTime)
                                || date > startDate && date < endDate){

                            selectedDatesAppointmentsList.add(appointsDateAndTime.get(timeAndDate));
                            Log.d(TAG,appointsDateAndTime.get(timeAndDate).shopToString() );
                        }
//                        if((date == startDate && selectedStartTimeNum <= startTime )){
//                            selectedDatesAppointmentsList.add(appointsDateAndTime.get(timeAndDate));
//                            check = 1;
//                            Log.d(TAG,appointsDateAndTime.get(timeAndDate).shopToString() );
//                            Log.d(TAG,"check: " + check );
//                        }else if((date == endDate && selectedEndTimeNum >= startTime)){
//                            selectedDatesAppointmentsList.add(appointsDateAndTime.get(timeAndDate));
//                            check = 2;
//                            Log.d(TAG,appointsDateAndTime.get(timeAndDate).shopToString() );
//                            Log.d(TAG,"check: " + check );
//                        }else if(date > startDate && date < endDate){
//                            selectedDatesAppointmentsList.add(appointsDateAndTime.get(timeAndDate));
//                            check = 3;
//                            Log.d(TAG,appointsDateAndTime.get(timeAndDate).shopToString() );
//                            Log.d(TAG,"check: " + check );
//                        }
                        Log.d(TAG,"___________________________________________________________________________________");
                    }
                    Log.d(TAG, "selectedDatesAppointmentsList size: " + selectedDatesAppointmentsList.size());
//                for(AppointmentModel appointmentModel : selectedDatesAppointmentsList){
//                    Log.d(TAG, appointmentModel.shopToString());
//                }
                    selectedAppointmentsAdapter = new AppointmentAdapter(selectedDatesAppointmentsList,shopInfoActivity,true, shopInfoActivity.getShop().getShopUid());

//                appointsRes.setLayoutManager(layoutManager);
                    appointsRes.setAdapter(selectedAppointmentsAdapter);
                    selectedAppointmentsAdapter.notifyDataSetChanged();
                }


            }
        });



        return view;
    }

    private void DatePickerDialog() {
        MaterialDatePicker.Builder<Pair<Long,Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("יש לבחור תקופת זמן");

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

        // Get yesterday's date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1); // Subtract one day

        // Set the time to midnight to get the start of the day
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long minStartDate = calendar.getTimeInMillis();
        constraintsBuilder.setStart(minStartDate);

        // Set a validator to restrict dates before yesterday
        constraintsBuilder.setValidator(DateValidatorPointForward.now());

        builder.setCalendarConstraints(constraintsBuilder.build());

        MaterialDatePicker<Pair<Long,Long>> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Long startDate = selection.first;
            Long endDate = selection.second;

            SimpleDateFormat sdfForShow = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat sdfForCompareDatabase = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

            String formattedStartDateShow =  sdfForShow.format(new Date(startDate));
            String formattedEndDateShow= sdfForShow.format(new Date(endDate));

            selectedStartDateText = sdfForCompareDatabase.format(new Date(startDate));
            selectedEndDateText = sdfForCompareDatabase.format(new Date(endDate));

//            Log.d(TAG, "formattedStartDateShow: " + formattedStartDateShow + " formattedStartDateCompare: " + formattedStartDateCompare);

//            try{
//                selectedStartDateText = sdfForCompareDatabase.parse(formattedStartDateCompare);
//                selectedEndDateText = sdfForCompareDatabase.parse(formattedEndDateCompare);
//
//            }catch(Exception e){
//                Log.e(TAG, e.getMessage());
//            }

            //             SimpleDateFormat sdfOriginalFormat = new SimpleDateFormat("yyyyMMdd");
//        SimpleDateFormat sdfTargetFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//        dateKeyNum = Integer.valueOf(chosenDate);
//
//        try{
//            Date dateKey = sdfOriginalFormat.parse(chosenDate);
//            savedDateText = sdfTargetFormat.format(dateKey);
//        }catch(Exception e){
//            Log.e(TAG, e.getMessage());
//        }



            selectedStartDate.setText(formattedStartDateShow);
            selectedStartDate.setGravity(Gravity.END);
            selectedEndDate.setText(formattedEndDateShow);
            selectedEndDate.setGravity(Gravity.END);
        });

        datePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
    }
}