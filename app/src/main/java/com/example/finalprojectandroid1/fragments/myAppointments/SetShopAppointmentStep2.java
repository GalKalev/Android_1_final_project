package com.example.finalprojectandroid1.fragments.myAppointments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectandroid1.GlobalMembers;
import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.ShopInfoActivity;
import com.example.finalprojectandroid1.shop.TimeRange;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetShopAppointmentStep2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetShopAppointmentStep2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SetShopAppointmentStep2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment setShopAppointmentStep2.
     */
    // TODO: Rename and change types and number of parameters
    public static SetShopAppointmentStep2 newInstance(String param1, String param2) {
        SetShopAppointmentStep2 fragment = new SetShopAppointmentStep2();
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

    // Choosing the appointment date and time

    String TAG = "setShopAppointmentStep2";
    ShopInfoActivity shopInfoActivity;
    RadioGroup availableTimesRadioGroup;
    HashMap<String, List<TimeRange>> defaultWeekWork;
    int timeSum;
    TextView unavailableAppoints;
    Calendar calendar;
    String chosenStartTime;
    String chosenEndTime;
    String chosenDate;
    boolean chosenTakenUserAppoint = false;
    int chosenTakenUserAppointTime;
    private int userUnavailableStartTime;
    int userUnavailableEndTime;
    String userUnavailableShopUid;

    ArrayList<String[]> shopBlockedDates;
    HashMap<String,ArrayList<String[]>>  shopUnavailableAppointments;
    //    ArrayList<int[]> shopBlockedTime;
    HashMap<String, ArrayList<String[]>> userUnavailableAppoints;
    // enter unavailable times
    int maxRadioButtons = 4;
    int radioButtonsCounter = 0;

    ProgressBar progressBar;
    SimpleDateFormat sdf;

    LinearLayout radioGroupsLayout;
    ArrayList<RadioButton> radioButtonsList;
    int checkedTimeRadio;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_shop_appointment_step2, container, false);

        shopInfoActivity = (ShopInfoActivity)getActivity();
        defaultWeekWork = shopInfoActivity.getShopDefaultAvailableTime();

        CalendarView chooseDateCalendar = view.findViewById(R.id.chooseDateCalendar);
        TextView setDateAndTimeText = view.findViewById(R.id.setDateAndTimeText);

        unavailableAppoints = view.findViewById(R.id.unavailableAppointsText);
        Button nextStep = view.findViewById(R.id.nextStep2Button);
        Button back = view.findViewById(R.id.backToStep1Button);
        progressBar = view.findViewById(R.id.progressBarStep2);
        radioGroupsLayout = view.findViewById(R.id.radioGroupLayout);

        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("HHmm");


        Bundle fromStep1 = getArguments();
        timeSum = fromStep1.getInt("timeSum");
        shopBlockedDates = (ArrayList<String[]>) fromStep1.getSerializable("shopBlockedDates");
        shopUnavailableAppointments = (HashMap<String, ArrayList<String[]>>) fromStep1.getSerializable("shopUnavailableAppointments");
        userUnavailableAppoints = (HashMap<String, ArrayList<String[]>>) fromStep1.getSerializable("userUnavailableAppoints");
        Log.d(TAG, "timeSum: " + timeSum);
        radioButtonsList = new ArrayList<>();

        Date currDate = Calendar.getInstance().getTime();

        chooseDateCalendar.setMinDate(currDate.getTime());
        chooseDateCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Picking the date of the appointment
        chooseDateCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                String dayOfMonthText;
                if(dayOfMonth < 10){
                    dayOfMonthText = "0" + dayOfMonth;
                }else{
                    dayOfMonthText = String.valueOf(dayOfMonth);
                }
                String monthText;
                if(month + 1 < 10){
                    monthText = "0" + (month + 1);
                }else{
                    monthText = String.valueOf(month + 1);
                }
                String selectedDate = year +  monthText + dayOfMonthText ;

                calendar.set(year, month, dayOfMonth);

                setDateAndTimeText.setVisibility(View.GONE);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                getAvailableTime(dayOfWeek,selectedDate);

            }
        });

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(chosenStartTime == null || chosenDate == null){
                    Toast.makeText(shopInfoActivity, "נא לבחור תאריך ושעה", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d(TAG, "date: " + chosenDate + " time: " + chosenStartTime);
                    Bundle toStep3 = new Bundle();
                    toStep3.putString("chosenStartTime", chosenStartTime);
                    toStep3.putString("chosenEndTime", chosenEndTime);
                    toStep3.putString("chosenDate",chosenDate);
                    toStep3.putStringArrayList("chosenAppointsName",fromStep1.getStringArrayList("chosenAppointsName"));
                    toStep3.putInt("priceSum",fromStep1.getInt("priceSum"));
                    toStep3.putInt("timeSum",timeSum);
                    toStep3.putSerializable("userUnavailableAppoints",getArguments().getSerializable("userUnavailableAppoints"));
                    toStep3.putSerializable("shopBlockedDates",getArguments().getSerializable("shopBlockedDates"));
                    toStep3.putSerializable("shopUnavailableAppointments",getArguments().getSerializable("shopUnavailableAppointments"));
                    toStep3.putBoolean("chosenTakenUserAppoint",chosenTakenUserAppoint);
                    toStep3.putInt("userUnavailableStartTime",userUnavailableStartTime);
                    toStep3.putString("userUnavailableShopUid",userUnavailableShopUid);
                    toStep3.putSerializable("userUnavailableAppoints", userUnavailableAppoints);
                    toStep3.putBoolean("isAppointChange",fromStep1.getBoolean("isAppointChange"));
                    toStep3.putString("appointChangeDate",fromStep1.getString("appointChangeDate"));
                    toStep3.putString("appointChangeStartTime",fromStep1.getString("appointChangeStartTime"));
                    Navigation.findNavController(view).navigate(R.id.action_setShopAppointmentStep2_to_setShopAppointmentStep3,toStep3);
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                goBack(view);
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "back pressed");
                goBack(view);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return view;
    }




    // Finding the available appointment time and adding buttons to choose from
    private void getAvailableTime(int dayNum, String selectedDate ){
        progressBar.setVisibility(View.VISIBLE);
        radioGroupsLayout.removeAllViews();

        String startDayTime = null;
        String endDayTime = null;

        // Checking date availability in blockedDates
        int selectedDateInt = Integer.parseInt(selectedDate);
        for (String[] blockedDates : shopBlockedDates){
            int blockedStartDate = Integer.parseInt(blockedDates[0]);
            int blockedEndDate = Integer.parseInt(blockedDates[1]);
            String blockedStartTime = blockedDates[2];
            String blockedEndTime = blockedDates[3];
            if(blockedStartDate < selectedDateInt && selectedDateInt < blockedEndDate){
                unavailableAppoints.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                return;
            }
            if (blockedStartDate == selectedDateInt) {
                endDayTime = blockedStartTime;
            }if (blockedEndDate == selectedDateInt) {
                startDayTime = blockedEndTime;
            }
        }
        // If date available, checking it's day for to set
        // start and end time of that day
        String day;
        switch(dayNum){
            case 1:
                day = "א";
                break;
            case 2:
                day = "ב";
                break;
            case 3:
                day = "ג";
                break;
            case 4:
                day = "ד";
                break;
            case 5:
                day = "ה";
                break;
            case 6:
                day = "ו";
                break;
            case 7:
                day = "ש";
                break;
            default:
                Toast.makeText(getActivity(), GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
                return;
        }


        if(defaultWeekWork.containsKey(day)){
            radioButtonsCounter = 0;

            RadioGroup radioGroup = createNewRadioGroup();
            radioGroupsLayout.addView(radioGroup);
            int radioId = 1;
            radioButtonsList.clear();
            checkedTimeRadio = 0;

            for(TimeRange time : defaultWeekWork.get(day)){
                if(startDayTime == null || Integer.parseInt(time.getStartTime()) > Integer.parseInt(startDayTime)){

                    startDayTime = time.getStartTime();

                }

                if(selectedDateInt == GlobalMembers.todayDate()){

                    if(GlobalMembers.timeRightNowInt() > Integer.parseInt(startDayTime)){
                        startDayTime = GlobalMembers.timeRightNowString();
                    }
                }
                Calendar calendar = Calendar.getInstance();

                // Setting the first time of the day
                try{
                    Date date = sdf.parse(startDayTime);
                    calendar.setTime(date);
                }catch(Exception e){
                    Log.e(TAG, "error formatting time right now: " + e.getMessage());
                }
                int minutes = calendar.get(Calendar.MINUTE);
                int roundedMinutes = 5 * ((minutes + 4) / 5);

                calendar.set(Calendar.MINUTE, roundedMinutes);
                calendar.set(Calendar.SECOND,0);

                startDayTime = sdf.format(calendar.getTime());
                Log.d(TAG, "startDayTime: " + startDayTime);

                if(endDayTime == null || Integer.parseInt(time.getEndTime()) < Integer.parseInt(endDayTime)){
                    endDayTime = time.getEndTime();
                }

                String startAppointTime = startDayTime;
                int startAppointTimeInt = Integer.parseInt(startDayTime);

                int startDayTimeInt = Integer.parseInt(startDayTime);
                int endDayTimeInt = Integer.parseInt(endDayTime);

                String endAppointTime = setAppointTime(startAppointTime);
                if(endAppointTime == null){
                    Toast.makeText(shopInfoActivity, "שגיאה במערכת. נא לנסות שוב במועד אחר", Toast.LENGTH_SHORT).show();
                    break;
                }

                int endAppointTimeInt = Integer.parseInt(endAppointTime);

                // Setting available time radio buttons
                while(endDayTimeInt > endAppointTimeInt) {
                    try{
                        // Setting start and end to appointment depending on
                        // the appointmetn time
                        Date date = sdf.parse(startAppointTime);
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTime(date);
                        calendar1.add(Calendar.MINUTE, timeSum);
                        endAppointTime = sdf.format(calendar1.getTime());

                    }catch(Exception e){
                        Log.e(TAG, "cant parse appointment end time: " + e.getMessage());
                        Toast.makeText(shopInfoActivity, "שגיאה במערכת. נא לנסות שוב במועד אחר", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    startAppointTimeInt = Integer.parseInt(startAppointTime);
                    endAppointTimeInt = Integer.parseInt(endAppointTime);


                    boolean shopTakenTime = false;
                    // Checking if time in not occupied by a different appointment
                    if(shopUnavailableAppointments.containsKey(selectedDate)){
                        for(String[] takenShopAppointTime : shopUnavailableAppointments.get(selectedDate)){
                            int takenAppointStartTime = Integer.parseInt(takenShopAppointTime[0]);
                            int takenAppointEndTime = Integer.parseInt(takenShopAppointTime[1]);

                            if((takenAppointStartTime < endAppointTimeInt && endAppointTimeInt < takenAppointEndTime) || (takenAppointStartTime <= startAppointTimeInt && startAppointTimeInt < takenAppointEndTime)){
                                startAppointTime = takenShopAppointTime[1];
                                startAppointTimeInt = Integer.parseInt(takenShopAppointTime[1]);
                                shopTakenTime = true;
                                break;
                            }
                        }
                    }

                    if(shopTakenTime){
                        continue;
                    }


                    boolean userTimeTaken = false;

                    // Setting the time for overlapping hours for the costumer
                    if(userUnavailableAppoints.containsKey(selectedDate)){
                        for(String[] takenUserAppointTimeAndShopUid : userUnavailableAppoints.get(selectedDate)){
                            int takenAppointStartTime = Integer.parseInt(takenUserAppointTimeAndShopUid[0]);
                            int takenAppointEndTime = Integer.parseInt(takenUserAppointTimeAndShopUid[1]);

                            if((takenAppointStartTime < endAppointTimeInt && endAppointTimeInt < takenAppointEndTime) || (takenAppointStartTime <= startAppointTimeInt && startAppointTimeInt < takenAppointEndTime)){
                                userTimeTaken = true;
                                userUnavailableStartTime = takenAppointStartTime;
                                userUnavailableEndTime = takenAppointEndTime;
                                userUnavailableShopUid = takenUserAppointTimeAndShopUid[2];
                                break;
                            }
                        }
                    }
                    if(!shopTakenTime){
                        unavailableAppoints.setVisibility(View.GONE);

                        // If time is overlapping with customer another appointment
                        // the button will look different
                        RadioButton radioTime = new RadioButton(getContext());
                        if(userTimeTaken){
                            radioTime.setBackgroundResource(R.drawable.radio_taken_selector);
                            radioTime.setTextColor(Color.WHITE);

                        }else{
                            radioTime.setBackgroundResource(R.drawable.radio_selector);
                            radioTime.setTextColor(Color.BLACK);
                        }

                        radioTime.setTextSize(18);
                        radioTime.setPadding(30,30,30,30);
                        LinearLayout.LayoutParams layoutParamsButton = new LinearLayout.LayoutParams(
                          ViewGroup.LayoutParams.WRAP_CONTENT,
                          ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        layoutParamsButton.setMargins(10,10,10,10);

                        radioTime.setGravity(Gravity.CENTER);
                        radioTime.setButtonDrawable(android.R.color.transparent);
                        radioTime.setLayoutParams(layoutParamsButton);

                        String formattedAppointTime;
                        try{
                            formattedAppointTime = GlobalMembers.formattingTimeToString(startAppointTime);
                            if(formattedAppointTime == null){
                                Toast.makeText(shopInfoActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
                                throw new NullPointerException("formattedTime is null") ;
                            }
                        }catch (Exception e){
                            Log.e(TAG,"formatted time is null: " + e.getMessage());
                            break;
                        }
                        if(radioButtonsCounter == maxRadioButtons){
                            radioGroup = createNewRadioGroup();
                            radioGroupsLayout.addView(radioGroup);
                            radioButtonsCounter = 0;

                        }

                        radioTime.setText(formattedAppointTime);
                        radioTime.setId(radioId);
                        radioButtonsList.add(radioTime);

                        final String finalStartTime = formattedAppointTime;
                        final String finalEndTime = endAppointTime;

                        radioTime.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int id = radioTime.getId();
                                if(checkedTimeRadio != 0 && checkedTimeRadio != id){
                                    radioButtonsList.get(checkedTimeRadio - 1).setChecked(false);
                                    RadioGroup parentGroup = (RadioGroup) radioTime.getParent();
                                    parentGroup.clearCheck();

                                }
                                radioTime.setChecked(true);
                                checkedTimeRadio = radioTime.getId();
                                chosenStartTime = finalStartTime;
                                chosenEndTime = finalEndTime;
                                chosenDate = selectedDate;
                                // Saving the time of the other appointment for future cancellation
                                if(radioTime.getCurrentTextColor() == Color.WHITE){
                                    chosenTakenUserAppointTime = userUnavailableStartTime;
                                    chosenTakenUserAppoint = true;

                                }
                            }
                        });
                        radioButtonsCounter++;
                        radioId ++;

                        radioGroup.addView(radioTime);

                    }

                    // Setting the next appointment time and repeating the process
                    int newStartHours = Integer.parseInt(startAppointTime.substring(0,2));
                    int newStartMinutes = Integer.parseInt(startAppointTime.substring(2));

                    newStartMinutes += 5;
                    if(newStartMinutes >= 60){
                        newStartHours += 1;
                        newStartMinutes -= 60;
                    }

                    startAppointTime = String.format(Locale.getDefault(),"%02d%02d", newStartHours,newStartMinutes);
                }

                progressBar.setVisibility(View.GONE);


            }

        }else {
            progressBar.setVisibility(View.GONE);
            unavailableAppoints.setVisibility(View.VISIBLE);
        }


    }

    private RadioGroup createNewRadioGroup(){
        RadioGroup radioGroup = new RadioGroup(getContext());
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        radioGroup.setBackgroundResource(R.color.appBackground);
        radioGroup.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams layoutParamsGroup = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParamsGroup.setMargins(10,10,10,10);
        radioGroup.setLayoutParams(layoutParamsGroup);

        return radioGroup;

    }


    private String setAppointTime(String startAppointTime){
        try{
            Date date = sdf.parse(startAppointTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date);
            calendar1.add(Calendar.MINUTE, timeSum);
            String endAppointTime = sdf.format(calendar1.getTime());
            return endAppointTime;
        }catch(Exception e){
            Log.e(TAG, "cant parse appointment end time: " + e.getMessage());
            return null;
        }

    }


    private void goBack(View v){
        Bundle toStep1 = new Bundle();
        toStep1.putSerializable("userUnavailableAppoints",getArguments().getSerializable("userUnavailableAppoints"));
        toStep1.putSerializable("shopBlockedDates",getArguments().getSerializable("shopBlockedDates"));
        toStep1.putBoolean("isAppointChange",getArguments().getBoolean("isAppointChange"));
        toStep1.putString("appointChangeDate",getArguments().getString("appointChangeDate"));
        toStep1.putString("appointChangeStartTime",getArguments().getString("appointChangeStartTime"));
        toStep1.putSerializable("shopUnavailableAppointments",getArguments().getSerializable("shopUnavailableAppointments"));
        Navigation.findNavController(v).navigate(R.id.action_setShopAppointmentStep2_to_setShopAppointmentStep1,toStep1);
    }



}