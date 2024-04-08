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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finalprojectandroid1.GlobalMembers;
import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.ShopInfoActivity;
import com.example.finalprojectandroid1.appointment.AppointmentAdapter;
import com.example.finalprojectandroid1.appointment.AppointmentModel;
import com.example.finalprojectandroid1.shop.TimeRange;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

   SimpleDateFormat sdfForShow;
   SimpleDateFormat sdfForCompareDatabase;
    String chosenReason;
    String formattedStartTime;
    String formattedEndTime;
    int selectedStartDateNum;
    int selectedEndDateNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_owned_shop_appointments_tab, container, false);

        ShopInfoActivity shopInfoActivity = (ShopInfoActivity)getActivity();

        calendarButton = view.findViewById(R.id.calendarButtonAppointTab);
        selectedStartDate = view.findViewById(R.id.startDateText);
        selectedEndDate = view.findViewById(R.id.endDateText);

        TextView selectedStartTime = view.findViewById(R.id.startTimeForDateText);
        TextView selectedEndTime = view.findViewById(R.id.endTimeForDateText);

        Button startTimeButton = view.findViewById(R.id.pickTimeForStartButton);
        Button endTimeButton = view.findViewById(R.id.pickTimeForEndButton);
        Button showAppointsButton = view.findViewById(R.id.showAppointsOnSelectedDatesButton);
        Button blockDatesButton = view.findViewById(R.id.blockDatesButton);
        TextView showBlockPickText = view.findViewById(R.id.showBlockPickText);
        LinearLayout blockDatesLayout = view.findViewById(R.id.blockDatesLayout);
        EditText otherText = view.findViewById(R.id.otherReasonTextEdit);

        Spinner blockReasonSpinner = view.findViewById(R.id.blockReasonsSpinner);
        String[] reasonsList = {"בחר סיבה","מחלה","חופשה","מחלה (משפחה)","שִׁכּוּל","חופש אישי","חופשנ ללא תשלום","אחר"};
        ArrayAdapter<String> reasonSpinnerAdapter = new ArrayAdapter<>(shopInfoActivity,android.R.layout.simple_spinner_item, reasonsList);
        reasonSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blockReasonSpinner.setAdapter(reasonSpinnerAdapter);

        sdfForShow = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdfForCompareDatabase = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        blockReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedReason = (String) parent.getItemAtPosition(position);
                if(selectedReason.equals("אחר")){
                    otherText.setVisibility(View.VISIBLE);
                    chosenReason = selectedReason;
                }else if(!selectedReason.equals("בחר סיבה")){
                    otherText.setVisibility(View.GONE);
                    chosenReason = selectedReason;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        otherText.getOnFocusChangeListener();


        showBlockPickText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(blockDatesLayout.getVisibility() == View.GONE){
                    blockDatesLayout.setVisibility(View.VISIBLE);
                }else{
                    blockDatesLayout.setVisibility(View.GONE);
                }
            }
        });
//
        shopAppointmentsList = new ArrayList<>();
        appointsDateAndTime = new LinkedHashMap<>();
//        selectedDatesAppointmentsList = new ArrayList<>();

//        shopAppointmentAdapter = new AppointmentAdapter(selectedDatesAppointmentsList, getContext(), true);

        calendarButton.setOnClickListener(v -> DatePickerDialog());
        sdfForShow = new SimpleDateFormat("dd/MM/yyyy");

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
                        int dateNum = Integer.parseInt(dateSnap.getKey());
                        String dateKey = dateSnap.getKey();
                        Log.d(TAG, "snapshot.getKey(): " + dateSnap.getKey());

                        for(DataSnapshot appointSnap: dateSnap.getChildren()){
                            String timeKey = appointSnap.getKey();
                            Log.d(TAG, "appointSnap.getKey(): " + appointSnap.getKey());
                            if(dateNum < GlobalMembers.todayDate() ||
                                    (dateNum == GlobalMembers.todayDate() && Integer.parseInt(appointSnap.child("time").child("startTime").getValue(String.class)) <= GlobalMembers.timeRightNow())) {
                                appointSnap.getRef().removeValue();
                                continue;
                            }
//
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

                                shopAppointsAdapter = new AppointmentAdapter(shopAppointmentsList,shopInfoActivity,1,true, shopInfoActivity.getShop().getShopUid());
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
                        formattedStartTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        selectedStartTime.setText(formattedStartTime);
                        selectedStartTimeNum = Integer.parseInt(formattedStartTime.replace(":",""));
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
                        formattedEndTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        selectedEndTime.setText(formattedEndTime);
                        selectedEndTimeNum = Integer.parseInt(formattedEndTime.replace(":",""));
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
                    Toast.makeText(shopInfoActivity, "נא לבחור תאריכים ושעות להצגת התורים", Toast.LENGTH_SHORT).show();
                }else{
//
                    ArrayList<AppointmentModel> selectedDatesAppointmentsList = showDateInRecyclerView();
                    selectedAppointmentsAdapter = new AppointmentAdapter(selectedDatesAppointmentsList,shopInfoActivity,1,true, shopInfoActivity.getShop().getShopUid());

//                appointsRes.setLayoutManager(layoutManager);
                    appointsRes.setAdapter(selectedAppointmentsAdapter);
                    selectedAppointmentsAdapter.notifyDataSetChanged();
                }


            }
        });


        blockDatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedStartDateText == null || selectedEndDateText == null || selectedStartTimeNum == 0 || selectedEndTimeNum == 0
                || chosenReason == null || (chosenReason.equals("אחר")  && otherText.getText().toString().equals(""))){
                    Toast.makeText(shopInfoActivity, "נא למלא את כל השדות לחסימת התאריכים", Toast.LENGTH_SHORT).show();
                }else{

                    Log.d(TAG,"selectedStartDateText: " + selectedStartDateText + " selectedEndDateText: " +
                            selectedEndDateText + " chosenReason: " + chosenReason + " formattedStartTime: " + formattedStartTime + " formattedEndTime: " + formattedEndTime);

                    ArrayList<AppointmentModel> selectedDatesAppointmentsList = showDateInRecyclerView();
                    if(selectedDatesAppointmentsList.size() > 0){
                        Toast.makeText(shopInfoActivity, "יש לבטל תורים בתאריכים אלה לפני חסימתם", Toast.LENGTH_SHORT).show();
                    }else {


                        try{
                            DatabaseReference blockRef =   FirebaseDatabase.getInstance().getReference("shops").child(shopInfoActivity.getShop().getShopUid())
                                    .child("blockedDates");
                            blockRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(!snapshot.exists()){
                                        Log.d(TAG, "!snapshot.exists()");
                                        BlockDatesAttributes blockDatesAttributes;

                                        blockDatesAttributes = new BlockDatesAttributes(new TimeRange(String.valueOf(selectedStartTimeNum),String.valueOf(selectedEndTimeNum)),
                                                Integer.parseInt(selectedEndDateText),chosenReason,otherText.getText().toString());


                                        blockRef.child(selectedStartDateText).setValue(blockDatesAttributes);

                                    }else{
                                        boolean isBlockedAlready = false;
                                        Log.d(TAG, "snapshot.exists()");
                                        for (DataSnapshot blockDateSnap : snapshot.getChildren()){
                                            TimeRange time = blockDateSnap.child("time").getValue(TimeRange.class);
                                            int startDateSnap = Integer.parseInt(blockDateSnap.getKey());
                                            int endDateSnap = blockDateSnap.child("endDate").getValue(Integer.class);
                                            int startTimeSnap = Integer.parseInt(time.getStartTime());
                                            int endTimeSnap = Integer.parseInt(time.getEndTime());
                                            if(startDateSnap > selectedEndDateNum || (startDateSnap == selectedEndDateNum && startTimeSnap > selectedEndTimeNum)
                                             || endDateSnap < selectedStartDateNum || (endDateSnap == selectedStartDateNum && endTimeSnap < selectedStartTimeNum)){
                                                Log.d(TAG, "aproved");


                                            }else{
                                                Log.d(TAG, "denined");
                                                isBlockedAlready = true;
                                                Toast.makeText(shopInfoActivity, "תאריכים אלו או חלק מהם רשומים כחסומים", Toast.LENGTH_SHORT).show();
                                                break;

                                            }
                                            if(!isBlockedAlready){


                                                BlockDatesAttributes blockDatesAttributes;

                                                blockDatesAttributes = new BlockDatesAttributes(new TimeRange(String.valueOf(selectedStartTimeNum),String.valueOf(selectedEndTimeNum)),
                                                        selectedEndDateNum,chosenReason,null);

                                                blockRef.child(selectedStartDateText).setValue(blockDatesAttributes);

                                            }
                                            Log.d(TAG, "isBlockedAlready: " + isBlockedAlready) ;

                                        }
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
//
                        }catch(Exception e){
                            Log.e(TAG, "error fetching bloch days: " + e.getMessage());
                        }




//                        String startDate, TimeRange time,String endDate, String reason, String otherText

                    }



//                    SimpleDateFormat sdfCompare = new SimpleDateFormat("yyyyMMdd");

//                    String timeToRemove = formattingTime(appointmentModel.getTime().getStartTime());

//                    BlockDatesAttributes blockDates = new BlockDatesAttributes()
//                    Log.d(TAG, "selectedStartDateText: " + selectedStartDateText + " selectedEndDateText: " + selectedEndDateText
//                    + " selectedStartTimeNum: " + selectedStartTimeNum + " selectedEndTimeNum: " + selectedEndTimeNum + " chosenReason: " + chosenReason);
//                    DatabaseReference blockRef =   FirebaseDatabase.getInstance().getReference("shops").child(shopInfoActivity.getShop().getShopUid())
//                            .child("blockedDates");
//                    if(chosenReason.equals("אחר")){
//                        String otherReasonInput = otherText.getText().toString();
//                        Log.d(TAG, "otherReasonInput: " + otherReasonInput);
//                        blockRef.child(chosenReason).push().setValue(otherReasonInput);
//                    }
//                    blockRef.child(chosenReason).setValue(ServerValue.increment(1));



                    // reset all the settings after updating database

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

            String formattedStartDateShow =  sdfForShow.format(new Date(startDate));
            String formattedEndDateShow= sdfForShow.format(new Date(endDate));

            selectedStartDateText = sdfForCompareDatabase.format(new Date(startDate));
            selectedEndDateText = sdfForCompareDatabase.format(new Date(endDate));

            selectedStartDate.setText(formattedStartDateShow);
            selectedStartDate.setGravity(Gravity.END);
            selectedEndDate.setText(formattedEndDateShow);
            selectedEndDate.setGravity(Gravity.END);
        });

        datePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
    }

    private ArrayList<AppointmentModel> showDateInRecyclerView(){
        ArrayList<AppointmentModel> selectedDatesAppointmentsList = new ArrayList<>();
        for(String[] timeAndDate : appointsDateAndTime.keySet()){
            int date = Integer.parseInt(timeAndDate[0]);
            int startTime = Integer.parseInt(timeAndDate[1].replace(":",""));
            selectedStartDateNum = Integer.parseInt(selectedStartDateText);
            selectedEndDateNum = Integer.parseInt(selectedEndDateText);

            if((date == selectedStartDateNum && selectedStartTimeNum <= startTime ) || (date == selectedEndDateNum && selectedEndTimeNum >= startTime)
                    || date > selectedStartDateNum && date < selectedEndDateNum){

                selectedDatesAppointmentsList.add(appointsDateAndTime.get(timeAndDate));
                Log.d(TAG,appointsDateAndTime.get(timeAndDate).shopToString() );
            }
        }
        Log.d(TAG, "selectedDatesAppointmentsList size: " + selectedDatesAppointmentsList.size());
        return selectedDatesAppointmentsList;
    }

    private class BlockDatesAttributes{
//        String startDate;
        int endDate;
        TimeRange time;
        String reason;
        String otherText;
        public BlockDatesAttributes() {
        }

        public BlockDatesAttributes( TimeRange time,int endDate, String reason, String otherText) {
//            this.startDate = startDate;
            this.endDate = endDate;
            this.time = time;
            this.reason = reason;
            this.otherText = otherText;
        }

//        public String getStartDate() {
//            return startDate;
//        }
//
//        public void setStartDate(String startDate) {
//            this.startDate = startDate;
//        }


        public int getEndDate() {
            return endDate;
        }

        public void setEndDate(int endDate) {
            this.endDate = endDate;
        }

        public TimeRange getTime() {
            return time;
        }

        public void setTime(TimeRange time) {
            this.time = time;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getOtherText() {
            return otherText;
        }

        public void setOtherText(String otherText) {
            this.otherText = otherText;
        }

        @Override
        public String toString() {
            return "BlockDatesAttributes{" +
//                    "startDate='" + startDate + '\'' +
                    ", endDate='" + endDate + '\'' +
                    ", time=" + time.toString() +
                    ", reason='" + reason + '\'' +
                    ", otherText='" + otherText + '\'' +
                    '}';
        }
    }




}