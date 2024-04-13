package com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finalprojectandroid1.GlobalMembers;
import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.ShopInfoActivity;
import com.example.finalprojectandroid1.appointment.AppointmentAdapter;
import com.example.finalprojectandroid1.appointment.AppointmentModel;
import com.example.finalprojectandroid1.shop.TimeRange;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

    ArrayList<BlockDatesAttributes> blockDatesAttributesList;

    DatabaseReference blockRef;

    TableLayout blockedDatesTable;

    TableLayout vacationTable;
    TableLayout sickLeaveTable;
    TableLayout familySickLeaveTable;
    TableLayout bereavementTable;
    TableLayout ptoDatesTable;
    TableLayout unpaidLeaveDatesTable;
    TableLayout otherDatesTable;
    int vacationSum = 0;
    int sickLeaveSum = 0;
    int familySickLeaveSum = 0;
    int unpaidLeaveSum = 0;
    int bereavementSum = 0;
    int ptoSum = 0;
    int otherSum = 0;



    TextView vacationSumText;
    TextView sickLeaveSumText;
    TextView familySickLeaveSumText;
    TextView unpaidLeaveSumText;
    TextView bereavementSumText;
    TextView ptoSumText;
    TextView otherSumText;

    LinearLayout progressBarLayout;
    ShopInfoActivity shopInfoActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_owned_shop_appointments_tab, container, false);

        shopInfoActivity = (ShopInfoActivity)getActivity();

        progressBarLayout = view.findViewById(R.id.progressBarShopAppointsTab);

        calendarButton = view.findViewById(R.id.calendarButtonAppointTab);
        selectedStartDate = view.findViewById(R.id.startDateText);
        selectedEndDate = view.findViewById(R.id.endDateText);

        TextView selectedStartTime = view.findViewById(R.id.startTimeForDateText);
        TextView selectedEndTime = view.findViewById(R.id.endTimeForDateText);
        TextView noAppoints = view.findViewById(R.id.noShopAppoitnsTextTab);


        Button startTimeButton = view.findViewById(R.id.pickTimeForStartButton);
        Button endTimeButton = view.findViewById(R.id.pickTimeForEndButton);
        Button showAppointsButton = view.findViewById(R.id.showAppointsOnSelectedDatesButton);
        Button blockDatesButton = view.findViewById(R.id.blockDatesButton);
        Button showBlockPickButton = view.findViewById(R.id.showBlockPickButton);
        LinearLayout blockDatesLayout = view.findViewById(R.id.blockDatesLayout);
        EditText otherText = view.findViewById(R.id.otherReasonTextEdit);

        blockedDatesTable = view.findViewById(R.id.blockedDatesTable);
        blockDatesAttributesList = new ArrayList<>();

        vacationTable = view.findViewById(R.id.vacationTable);
        sickLeaveTable = view.findViewById(R.id.sickLeaveTable);
        familySickLeaveTable = view.findViewById(R.id.sickFamilyLeaveTable);
        bereavementTable = view.findViewById(R.id.bereavementTable);
        ptoDatesTable = view.findViewById(R.id.ptoDatesTable);
        unpaidLeaveDatesTable = view.findViewById(R.id.unpaidLeaveDatesTable);
        otherDatesTable = view.findViewById(R.id.otherDatesTable);

        vacationSumText = view.findViewById(R.id.vacationSum);
        sickLeaveSumText = view.findViewById(R.id.sickLeaveSum);
        familySickLeaveSumText = view.findViewById(R.id.sickFamilyLeaveSum);
        unpaidLeaveSumText = view.findViewById(R.id.unpaidLeaveSum);
        bereavementSumText = view.findViewById(R.id.bereavementSum);
        ptoSumText = view.findViewById(R.id.ptoSum);
        otherSumText = view.findViewById(R.id.otherSum);

        Spinner blockReasonSpinner = view.findViewById(R.id.blockReasonsSpinner);
        String[] reasonsList = {"בחר סיבה","מחלה","חופשה","מחלה (משפחה)","שִׁכּוּל","חופש אישי","חופשה ללא תשלום","אחר"};
        ArrayAdapter<String> reasonSpinnerAdapter = new ArrayAdapter<>(shopInfoActivity,R.layout.spinner_text, reasonsList);
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

//        otherText.getOnFocusChangeListener();



        showBlockPickButton.setOnClickListener(new View.OnClickListener() {
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

        try {
            calendarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        DatePickerDialog();
                    }catch (Exception e){
                        Log.e(TAG,"click: " + e.getMessage());
                    }

                }
            });
        }catch (Exception e){
            Log.e(TAG, "date dialog v->: " + e.getMessage());
        }

        sdfForShow = new SimpleDateFormat("dd/MM/yyyy");

        RecyclerView appointsRes = view.findViewById(R.id.appointResInShop);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        appointsRes.setLayoutManager(layoutManager);

        DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("shops")
                .child(shopInfoActivity.getShop().getShopUid());
        DatabaseReference getShopAppoints = shopRef.child("shopAppointments");

        blockRef =   FirebaseDatabase.getInstance().getReference("shops").child(shopInfoActivity.getShop().getShopUid())
                .child("blockedDates");



        try{
            getShopAppoints.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int count = 0;
                    for(DataSnapshot dateSnap: snapshot.getChildren()){
                        if(snapshot.exists()){
                            int dateNum = Integer.parseInt(dateSnap.getKey());
                            String dateKey = dateSnap.getKey();
                            Log.d(TAG, "snapshot.getKey(): " + dateSnap.getKey());

                            for(DataSnapshot appointSnap: dateSnap.getChildren()){
                                String timeKey = appointSnap.getKey();
                                Log.d(TAG, "appointSnap.getKey(): " + appointSnap.getKey());
                                if(dateNum < GlobalMembers.todayDate() ||
                                        (dateNum == GlobalMembers.todayDate() && Integer.parseInt(appointSnap.child("time").child("startTime").getValue(String.class)) <= GlobalMembers.timeRightNowInt())) {
                                    shopRef.child("usersAppearances").child(appointSnap.child("userUid").getValue(String.class)).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (!snapshot.exists()) {
                                                snapshot.child("userName").getRef().setValue(appointSnap.child("userName").getValue(String.class));
                                            }
                                            snapshot.child("appointmentsOrdered").getRef().setValue(ServerValue.increment(1));

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


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

                        }else{
                            noAppoints.setVisibility(View.VISIBLE);
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

        getBlockedDates();
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
                    ArrayList<AppointmentModel> selectedDatesAppointmentsList = showChosenDatesInRecyclerView();
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

                    ArrayList<AppointmentModel> selectedDatesAppointmentsList = showChosenDatesInRecyclerView();
                    if(selectedDatesAppointmentsList.size() > 0){
                        Toast.makeText(shopInfoActivity, "יש לבטל תורים בתאריכים אלה לפני חסימתם", Toast.LENGTH_SHORT).show();
                    }else {
                        progressBarLayout.setVisibility(View.VISIBLE);
                        boolean isBlockedAlready = false;
                        for(BlockDatesAttributes bda : blockDatesAttributesList){
                            try{
                                int savedStartDate = bda.getStartDate();
                                int savedEndDate = bda.getEndDate();
                                int savedStartTime = Integer.parseInt(bda.getTime().getStartTime());
                                int savedEndTime = Integer.parseInt(bda.getTime().getEndTime());

                                if(savedStartDate > selectedEndDateNum || (savedStartDate == selectedEndDateNum && savedStartTime > selectedEndTimeNum)
                                        || savedEndDate < selectedStartDateNum || (savedEndDate == selectedStartDateNum && savedEndTime < selectedStartTimeNum)){

                                }else{
                                    isBlockedAlready = true;
                                    progressBarLayout.setVisibility(View.GONE);
                                    Toast.makeText(shopInfoActivity, "תאריכים אלו או חלק מהם רשומים כחסומים", Toast.LENGTH_SHORT).show();
                                    break;

                                }

                            }catch(Exception e){
                                Log.e(TAG, "block: " + e.getMessage());
                            }





                        }
                        if(!isBlockedAlready) {


                            BlockDatesAttributes blockDatesAttributes;
                            blockDatesAttributes = new BlockDatesAttributes(new TimeRange(formattedStartTime.replace(":", ""), formattedEndTime.replace(":", "")),
                                    Integer.parseInt(selectedEndDateText), Integer.parseInt(selectedStartDateText), chosenReason, otherText.getText().toString());

                            blockDatesAttributesList.add(blockDatesAttributes);

                            blockRef.child(selectedStartDateText).setValue(blockDatesAttributes);
                            Query addBlockedDatesQuery = blockRef.orderByChild("reason").equalTo(chosenReason);
                            switch (chosenReason){
                                case "חופשה":
                                    vacationTable.removeAllViews();

                                    break;
                                case "מחלה":

                                    sickLeaveTable.removeAllViews();
                                    break;
                                case "מחלה (משפחה)":

                                    familySickLeaveTable.removeAllViews();
                                    break;
                                case "חופש אישי":

                                    ptoDatesTable.removeAllViews();
                                    break;
                                case "חופשה ללא תשלום":

                                    unpaidLeaveDatesTable.removeAllViews();
                                    break;

                                case "שִׁכּוּל":

                                    bereavementTable.removeAllViews();
                                    break;
                                case "אחר":

                                    otherDatesTable.removeAllViews();
                                    break;


                            }
                            addBlockedDatesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot blockSnap : snapshot.getChildren()){
                                        Log.d(TAG, "add new to table snapshot.getKey(): " + blockSnap.getKey());
                                        updateBlockedTableByReason(chosenReason,blockSnap.getValue(BlockDatesAttributes.class));
                                    }


                                    blockDatesLayout.setVisibility(View.GONE);

                                    selectedStartTime.setText("שעת התחלה");
                                    selectedEndTimeNum = 0;
                                    formattedStartTime = null;

                                    selectedEndTime.setText("שעת סיום");
                                    selectedEndTimeNum = 0;
                                    formattedEndTime = null;

                                    selectedStartDateText = null;
                                    selectedEndDateText = null;
                                    selectedStartDate.setText("תאריך התחלה");
                                    selectedEndDate.setText("תאריך סיום");

                                    blockReasonSpinner.setSelection(0);
                                    chosenReason = null;
                                    otherText.setText("");

                                    progressBarLayout.setVisibility(View.GONE);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                        }



                    }


                }
            }
        });



        return view;
    }

    private void DatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog startDatePickerDialog = new DatePickerDialog(getContext(),R.style.MyDatePickerStyle, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String startMonth;
                if(monthOfYear < 10){
                    startMonth = "0" + (monthOfYear + 1);
                }else{
                    startMonth = String.valueOf(monthOfYear + 1);
                }

                String startDay;
                if(dayOfMonth < 10){
                    startDay = "0" + dayOfMonth;
                }else{
                    startDay = String.valueOf(dayOfMonth);
                }
                selectedStartDateText = year + startMonth + startDay;
                selectedStartDateNum = Integer.parseInt(selectedStartDateText);
                selectedStartDate.setText(GlobalMembers.convertDateFromCompareToShow(selectedStartDateText));
                selectedStartDate.setGravity(Gravity.START);
                Log.d(TAG, "startDate: " + selectedStartDateNum);
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                    Date minDate = sdf.parse(selectedStartDateText);
                    long minDateInMills = minDate.getTime();
                    DatePickerDialog endDatePickerDialog = new DatePickerDialog(getContext(), R.style.MyDatePickerStyle,new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String endMonth;
                            if(month < 10){
                                endMonth = "0" + (month + 1);
                            }else{
                                endMonth = String.valueOf(month + 1);
                            }

                            String endDay;
                            if(dayOfMonth < 10){
                                endDay = "0" + dayOfMonth;
                            }else{
                                endDay = String.valueOf(dayOfMonth);
                            }
                            selectedEndDateText = year + endMonth + endDay;
                            selectedEndDateNum = Integer.parseInt(selectedEndDateText);
                            selectedEndDate.setText(GlobalMembers.convertDateFromCompareToShow(selectedEndDateText));
//                            selectedEndDate.setGravity(Gravity.START);
                            Log.d(TAG, "endDate: " + selectedEndDateNum);
                            Log.d(TAG, selectedEndDate.getText().toString() + " and " + selectedStartDate.getText().toString());
                        }
                    }, nowYear, nowMonth, nowDay);
                    endDatePickerDialog.getDatePicker().setMinDate(minDateInMills);
                    endDatePickerDialog.setMessage("תאריך סיום");
                    endDatePickerDialog.show();


                }catch(Exception e) {
                    Log.e(TAG, "error parsing: " + e.getMessage());
                }




            }
        }, nowYear, nowMonth, nowDay);
        startDatePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        startDatePickerDialog.setMessage("תאריך התחלה");
        startDatePickerDialog.show();

    }

    private ArrayList<AppointmentModel> showChosenDatesInRecyclerView(){
        ArrayList<AppointmentModel> selectedDatesAppointmentsList = new ArrayList<>();
        for(String[] timeAndDate : appointsDateAndTime.keySet()){
            int date = Integer.parseInt(timeAndDate[0]);
            int startTime = Integer.parseInt(timeAndDate[1].replace(":",""));
            selectedStartDateNum = Integer.parseInt(selectedStartDateText);
//            selectedEndDateNum = Integer.parseInt(selectedEndDateText);

            if((date == selectedStartDateNum && selectedStartTimeNum <= startTime ) || (date == selectedEndDateNum && selectedEndTimeNum >= startTime)
                    || date > selectedStartDateNum && date < selectedEndDateNum){

                selectedDatesAppointmentsList.add(appointsDateAndTime.get(timeAndDate));
                Log.d(TAG,appointsDateAndTime.get(timeAndDate).shopToString() );
            }
        }
        Log.d(TAG, "selectedDatesAppointmentsList size: " + selectedDatesAppointmentsList.size());
        return selectedDatesAppointmentsList;
    }

    private void getBlockedDates(){
        blockDatesAttributesList.clear();


        blockRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBarLayout.setVisibility(View.VISIBLE);
                if (snapshot.exists()){

                    for(DataSnapshot blockDateSnap : snapshot.getChildren()){
                        try{


                            BlockDatesAttributes blockDatesAttributes = blockDateSnap.getValue(BlockDatesAttributes.class);
                            blockDatesAttributesList.add(blockDatesAttributes);

                            String reason = blockDatesAttributes.getReason();

                            updateBlockedTableByReason(reason,blockDatesAttributes);

//                            TableRow tableRowTime = new TableRow(getContext());
//                            tableRowTime.setGravity(Gravity.END);
//
//                            LinearLayout linearLayoutTime = new LinearLayout(getContext());
//                            linearLayoutTime.setOrientation(LinearLayout.HORIZONTAL);
//
//                            LinearLayout linearLayoutText = new LinearLayout(getContext());
//                            linearLayoutText.setOrientation(LinearLayout.VERTICAL);
//                            int blockedStartDate =  blockDatesAttributes.getStartDate();
//                            int blockedEndDate =  blockDatesAttributes.getEndDate();
//                            final int sum;
//
//                            try{
//                                Date startDate = sdfForCompareDatabase.parse(String.valueOf(blockDatesAttributes.getStartDate()));
//                                Date endDate = sdfForCompareDatabase.parse(String.valueOf(blockDatesAttributes.getEndDate()));
//
//                                long difference = Math.abs(startDate.getTime() - endDate.getTime());
//                                sum = (int)difference / (24 * 60 * 60 * 1000) + 1;
////                                changedSum = sum;
//
//                                TextView datesRangeText = new TextView(getContext());
//
//                                String datesRangeStr = GlobalMembers.convertDateFromCompareToShow(String.valueOf(blockedStartDate)) + " - "
//                                        + GlobalMembers.convertDateFromCompareToShow(String.valueOf(blockedEndDate));
//                                datesRangeText.setText(datesRangeStr);
//
//                                TextView otherTextTable = new TextView(getContext());
//                                otherTextTable.setText(blockDatesAttributes.getOtherText());
//
//
//                                linearLayoutText.addView(datesRangeText);
//                                linearLayoutText.addView(otherTextTable);
//
//
////                                Log.d(TAG, "changedSum: " + changedSum);
//
//
//
////                            blockDatesAttributes.set
//
//
//
//                                switch (reason){
//                                    case "חופשה":
//
//                                        if(GlobalMembers.todayDate() <= blockDatesAttributes.getEndDate()){
//                                            Button deleteBlockedDates = new Button(getContext());
//                                            deleteBlockedDates.setText("מחיקה");
//                                            deleteBlockedDates.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    blockRef.child(String.valueOf(blockDatesAttributes.getStartDate())).removeValue();
//                                                    vacationSum -= sum;
//                                                    vacationTable.removeView(tableRowTime);
//                                                    vacationSumText.setText(String.valueOf(vacationSum));
//                                                    Log.d(TAG, "after delete vacationSum: " + vacationSum);
//                                                }
//                                            });
//
//                                            linearLayoutTime.addView(deleteBlockedDates);
//                                        }
//                                        linearLayoutTime.addView(linearLayoutText);
//                                        tableRowTime.addView(linearLayoutTime);
//                                        vacationTable.addView(tableRowTime);
//                                        vacationSum += sum;
//                                        Log.d(TAG, "vacationSum: " + vacationSum);
//    //
//                                        vacationSumText.setText(String.valueOf(vacationSum));
//                                        break;
//                                    case "מחלה":
//                                        if(GlobalMembers.todayDate() <= blockDatesAttributes.getEndDate()){
//                                            Button deleteBlockedDates = new Button(getContext());
//                                            deleteBlockedDates.setText("מחיקה");
//                                            deleteBlockedDates.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    blockRef.child(String.valueOf(blockDatesAttributes.getStartDate())).removeValue();
//                                                    sickLeaveSum -= sum;
//                                                    sickLeaveTable.removeView(tableRowTime);
//                                                    sickLeaveSumText.setText(String.valueOf(sickLeaveSum));
//                                                    Log.d(TAG, "after delete sickLeaveSum: " + sickLeaveSum);
//                                                }
//                                            });
//
//                                            linearLayoutTime.addView(deleteBlockedDates);
//                                        }
//                                        linearLayoutTime.addView(linearLayoutText);
//                                        tableRowTime.addView(linearLayoutTime);
//                                        sickLeaveTable.addView(tableRowTime);
//                                        sickLeaveSum += sum;
//                                        Log.d(TAG, "sickLeaveSum: " + sickLeaveSum);
//                                        //
//                                        vacationSumText.setText(String.valueOf(sickLeaveSum));
//                                        break;
//                                    case "מחלה (משפחה)":
//                                        updateBlockedTableByReason(sickFamilyLeaveTable,blockDatesAttributes);
//                                        sickFamilyLeaveSum += sum;
//                                        sickFamilyLeaveSumText.setText(String.valueOf(sickFamilyLeaveSum));
//                                        break;
//                                    case "שִׁכּוּל":
//                                        updateBlockedTableByReason(bereavementTable,blockDatesAttributes);
//                                        bereavementSum += sum;
//                                        bereavementSumText.setText(String.valueOf(bereavementSum));
//                                        break;
//                                    case "חופש אישי":
//                                        updateBlockedTableByReason(ptoDatesTable,blockDatesAttributes);
//                                        ptoSum += sum;
//                                        ptoSumText.setText(String.valueOf(ptoSum));
//                                        break;
//                                    case "חופשה ללא תשלום":
//                                        updateBlockedTableByReason(unpaidLeaveDatesTable,blockDatesAttributes);
//                                        unpaidLeaveSum += sum;
//                                        unpaidLeaveSumText.setText(String.valueOf(unpaidLeaveSum));
//                                        break;
//                                    case "אחר":
//                                        updateBlockedTableByReason(otherDatesTable,blockDatesAttributes);
//                                        otherSum += sum;
//                                        otherSumText.setText(String.valueOf(otherSum));
//                                        break;
//
//
//                                }
//                            }catch(Exception e){
//                                Log.e(TAG, "Error calculating difference between dates: " + e.getMessage());
//                            }

                        }catch(Exception e){
                            Log.e(TAG,"error fetching blocked dates: " + e.getMessage());
                        }



                    }
                    progressBarLayout.setVisibility(View.GONE);
                }else{

                    progressBarLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




    private void updateBlockedTableByReason(String reason, BlockDatesAttributes bda){
        TableRow tableRowTime = new TableRow(getContext());
        tableRowTime.setGravity(Gravity.END);
        tableRowTime.setBackgroundResource(R.drawable.bottom_black_line);

        LinearLayout linearLayoutTime = new LinearLayout(getContext());
        linearLayoutTime.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutTime.setGravity(Gravity.CENTER|Gravity.END);

        LinearLayout linearLayoutText = new LinearLayout(getContext());
        linearLayoutText.setOrientation(LinearLayout.VERTICAL);
        int blockedStartDate =  bda.getStartDate();
        int blockedEndDate =  bda.getEndDate();
        Log.d(TAG,"blockedStartDate: " + blockedStartDate + " blockedEndDate: " + blockedEndDate );
        Log.d(TAG,"bda: " + bda.toString() );
        final int sum;

        try{
            Date startDate = sdfForCompareDatabase.parse(String.valueOf(bda.getStartDate()));
            Date endDate = sdfForCompareDatabase.parse(String.valueOf(bda.getEndDate()));

            long difference = Math.abs(startDate.getTime() - endDate.getTime());
            sum = (int)difference / (24 * 60 * 60 * 1000) + 1;
//                                changedSum = sum;

            TextView datesRangeText = new TextView(getContext());;

            String datesRangeStr = GlobalMembers.convertDateFromCompareToShow(String.valueOf(blockedStartDate)) + " - "
                    + GlobalMembers.convertDateFromCompareToShow(String.valueOf(blockedEndDate));
            datesRangeText.setText(datesRangeStr);
            datesRangeText.setTextColor(Color.BLACK);
            datesRangeText.setTextSize(15);
//            datesRangeText.setBackgroundColor(Color.TRANSPARENT);

            linearLayoutText.addView(datesRangeText);
            if(!bda.getOtherText().toString().equals("")){
                TextView otherTextTable = new TextView(getContext());
                otherTextTable.setText(bda.getOtherText().toString());
                otherTextTable.setTextColor(Color.BLACK);
                linearLayoutText.addView(otherTextTable);
            }



            Log.d(TAG, "reason: " + reason);

            ImageButton deleteBlockedDates = new ImageButton(getContext());
            deleteBlockedDates.setImageResource(R.drawable.round_cancel_24);
            deleteBlockedDates.setBackgroundColor(Color.TRANSPARENT);


            switch (reason){
                case "חופשה":

                    if(GlobalMembers.todayDate() <= bda.getEndDate()){
//                        Button deleteBlockedDates = new Button(getContext());
//                        deleteBlockedDates.setText("ביטול");
//                        deleteBlockedDates.setBackgroundResource(R.drawable.update_activity_input_delete_button);
                        deleteBlockedDates.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                blockRef.child(String.valueOf(bda.getStartDate())).removeValue();
                                vacationSum -= sum;
                                vacationTable.removeView(tableRowTime);
                                vacationSumText.setText(String.valueOf(vacationSum));
                                Log.d(TAG, "after delete vacationSum: " + vacationSum);
                            }
                        });

                        linearLayoutTime.addView(deleteBlockedDates);
                    }
                    linearLayoutTime.addView(linearLayoutText);
                    tableRowTime.addView(linearLayoutTime);
                    vacationTable.addView(tableRowTime);
                    vacationSum += sum;
                    Log.d(TAG, "vacationSum: " + vacationSum);
                    //
                    vacationSumText.setText(String.valueOf(vacationSum));
                    break;
                case "מחלה":
                    if(GlobalMembers.todayDate() <= bda.getEndDate()){
//                        Button deleteBlockedDates = new Button(getContext());
//                        deleteBlockedDates.setText("מחיקה");
//                        deleteBlockedDates.setBackgroundResource(R.drawable.update_activity_input_delete_button);
                        deleteBlockedDates.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                blockRef.child(String.valueOf(bda.getStartDate())).removeValue();
                                sickLeaveSum -= sum;
                                sickLeaveTable.removeView(tableRowTime);
                                sickLeaveSumText.setText(String.valueOf(sickLeaveSum));
                                Log.d(TAG, "after delete sickLeaveSum: " + sickLeaveSum);
                            }
                        });

                        linearLayoutTime.addView(deleteBlockedDates);
                    }
                    linearLayoutTime.addView(linearLayoutText);
                    tableRowTime.addView(linearLayoutTime);
                    sickLeaveTable.addView(tableRowTime);
                    sickLeaveSum += sum;
                    Log.d(TAG, "sickLeaveSum: " + sickLeaveSum);
                    //
                    sickLeaveSumText.setText(String.valueOf(sickLeaveSum));
                    break;
                case "מחלה (משפחה)":
                    if(GlobalMembers.todayDate() <= bda.getEndDate()){
//                        Button deleteBlockedDates = new Button(getContext());
//                        deleteBlockedDates.setText("מחיקה");
//                        deleteBlockedDates.setBackgroundResource(R.drawable.update_activity_input_delete_button);
                        deleteBlockedDates.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                blockRef.child(String.valueOf(bda.getStartDate())).removeValue();
                                familySickLeaveSum -= sum;
                                familySickLeaveTable.removeView(tableRowTime);
                                familySickLeaveSumText.setText(String.valueOf(familySickLeaveSum));
                                Log.d(TAG, "after delete sickFamilyLeaveSum: " + familySickLeaveSum);
                            }
                        });

                        linearLayoutTime.addView(deleteBlockedDates);
                    }
                    linearLayoutTime.addView(linearLayoutText);
                    tableRowTime.addView(linearLayoutTime);
                    familySickLeaveTable.addView(tableRowTime);
                    familySickLeaveSum += sum;
                    Log.d(TAG, "sickFamilyLeaveSum: " + familySickLeaveSum);

                    familySickLeaveSumText.setText(String.valueOf(familySickLeaveSum));
                    break;
                case "שִׁכּוּל":
                    if(GlobalMembers.todayDate() <= bda.getEndDate()){
//                        Button deleteBlockedDates = new Button(getContext());
//                        deleteBlockedDates.setText("מחיקה");
//                        deleteBlockedDates.setBackgroundResource(R.drawable.update_activity_input_delete_button);
                        deleteBlockedDates.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                blockRef.child(String.valueOf(bda.getStartDate())).removeValue();
                                bereavementSum -= sum;
                                bereavementTable.removeView(tableRowTime);
                                bereavementSumText.setText(String.valueOf(bereavementSum));
                                Log.d(TAG, "after delete bereavementSum: " + bereavementSum);
                            }
                        });

                        linearLayoutTime.addView(deleteBlockedDates);
                    }
                    linearLayoutTime.addView(linearLayoutText);
                    tableRowTime.addView(linearLayoutTime);
                    bereavementTable.addView(tableRowTime);
                    bereavementSum += sum;
                    Log.d(TAG, "bereavementSum: " + bereavementSum);

                    bereavementSumText.setText(String.valueOf(bereavementSum));

                    break;
                case "חופש אישי":
                    if(GlobalMembers.todayDate() <= bda.getEndDate()){
//                        Button deleteBlockedDates = new Button(getContext());
//                        deleteBlockedDates.setText("מחיקה");
//                        deleteBlockedDates.setBackgroundResource(R.drawable.update_activity_input_delete_button);
                        deleteBlockedDates.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                blockRef.child(String.valueOf(bda.getStartDate())).removeValue();
                                ptoSum -= sum;
                                ptoDatesTable.removeView(tableRowTime);
                                ptoSumText.setText(String.valueOf(ptoSum));
                                Log.d(TAG, "after delete ptoSum: " + ptoSum);
                            }
                        });

                        linearLayoutTime.addView(deleteBlockedDates);
                    }
                    linearLayoutTime.addView(linearLayoutText);
                    tableRowTime.addView(linearLayoutTime);
                    ptoDatesTable.addView(tableRowTime);
                    ptoSum += sum;
                    Log.d(TAG, "ptoSum: " + ptoSum);
                    //
                    ptoSumText.setText(String.valueOf(ptoSum));

                    break;
                case "חופשה ללא תשלום":

                    if(GlobalMembers.todayDate() <= bda.getEndDate()){
//                        Button deleteBlockedDates = new Button(getContext());
//                        deleteBlockedDates.setText("מחיקה");
//                        deleteBlockedDates.setBackgroundResource(R.drawable.update_activity_input_delete_button);
                        deleteBlockedDates.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                blockRef.child(String.valueOf(bda.getStartDate())).removeValue();
                                unpaidLeaveSum -= sum;
                                unpaidLeaveDatesTable.removeView(tableRowTime);
                                unpaidLeaveSumText.setText(String.valueOf(unpaidLeaveSum));
                                Log.d(TAG, "after delete unpaidLeaveSum: " + unpaidLeaveSum);
                            }
                        });

                        linearLayoutTime.addView(deleteBlockedDates);
                    }
                    linearLayoutTime.addView(linearLayoutText);
                    tableRowTime.addView(linearLayoutTime);
                    unpaidLeaveDatesTable.addView(tableRowTime);
                    unpaidLeaveSum += sum;
                    Log.d(TAG, "unpaidLeaveSum: " + unpaidLeaveSum);
                    //
                    unpaidLeaveSumText.setText(String.valueOf(unpaidLeaveSum));

                    break;
                case "אחר":

                    if(GlobalMembers.todayDate() <= bda.getEndDate()){
//                        Button deleteBlockedDates = new Button(getContext());
//                        deleteBlockedDates.setText("מחיקה");
//                        deleteBlockedDates.setBackgroundResource(R.drawable.update_activity_input_delete_button);
                        deleteBlockedDates.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                blockRef.child(String.valueOf(bda.getStartDate())).removeValue();
                                otherSum -= sum;
                                otherDatesTable.removeView(tableRowTime);
                                otherSumText.setText(String.valueOf(otherSum));
                                Log.d(TAG, "after delete otherSumText: " + otherSum);
                            }
                        });

                        linearLayoutTime.addView(deleteBlockedDates);
                    }
                    linearLayoutTime.addView(linearLayoutText);
                    tableRowTime.addView(linearLayoutTime);
                    otherDatesTable.addView(tableRowTime);
                    otherSum += sum;
                    Log.d(TAG, "otherSumText: " + otherSum);
                    //
                    otherSumText.setText(String.valueOf(otherSum));
                    break;


            }
        }catch(Exception e){
            Log.e(TAG, "Error calculating difference between dates: " + e.getMessage());
            Toast.makeText(shopInfoActivity, "שגיאת מערכת. יש לנסות שוב", Toast.LENGTH_SHORT).show();
        }


    }

    public static class BlockDatesAttributes{
//        String startDate;
        int endDate;
        int startDate;
        TimeRange time;
        String reason;
        String otherText;

        public BlockDatesAttributes() {
        }

        public BlockDatesAttributes(TimeRange time, int endDate, int startDate, String reason, String otherText) {
//            this.startDate = startDate;
            this.endDate = endDate;
            this.time = time;
            this.startDate = startDate;
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


        public int getStartDate() {
            return startDate;
        }

        public void setStartDate(int startDate) {
            this.startDate = startDate;
        }

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
                    "endDate=" + endDate +
                    ", startDate=" + startDate +
                    ", time=" + time.toString() +
                    ", reason='" + reason + '\'' +
                    ", otherText='" + otherText + '\'' +
                    '}';
        }
    }




}