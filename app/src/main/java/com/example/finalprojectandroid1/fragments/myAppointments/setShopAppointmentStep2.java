package com.example.finalprojectandroid1.fragments.myAppointments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.ShopInfoActivity;
import com.example.finalprojectandroid1.shop.TimeRange;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link setShopAppointmentStep2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class setShopAppointmentStep2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public setShopAppointmentStep2() {
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
    public static setShopAppointmentStep2 newInstance(String param1, String param2) {
        setShopAppointmentStep2 fragment = new setShopAppointmentStep2();
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

    HashMap<String, TimeRange> shopUnavailableTime;
    HashMap<String, HashMap<TimeRange,String>> userUnavailableAppoints;
    // enter unavailable times

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_shop_appointment_step2, container, false);

        shopInfoActivity = (ShopInfoActivity)getActivity();
        defaultWeekWork = shopInfoActivity.getShopDefaultAvailableTime();

        CalendarView chooseDateCalendar = view.findViewById(R.id.chooseDateCalendar);

        availableTimesRadioGroup = view.findViewById(R.id.availableTimesRadioGroup);
        availableTimesRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
        unavailableAppoints = view.findViewById(R.id.unavailableAppointsText);
        Button nextStep = view.findViewById(R.id.nextStep2Button);
        Button back = view.findViewById(R.id.backToStep1Button);

        calendar = Calendar.getInstance();


        Bundle fromStep1 = getArguments();
        timeSum = fromStep1.getInt("timeSum");
        shopUnavailableTime = (HashMap<String, TimeRange>) fromStep1.getSerializable("shopUnavailableTime");
        userUnavailableAppoints = (HashMap<String, HashMap<TimeRange,String>>) fromStep1.getSerializable("userUnavailableAppoints");
//        int priceSum = fromStep1.getInt("priceSum");
        Log.d(TAG, "timeSum: " + timeSum);

        Date currDate = Calendar.getInstance().getTime();

        Log.d(TAG,"currDate: " + currDate);

        chooseDateCalendar.setMinDate(currDate.getTime());

        Log.d(TAG,"min date: " + chooseDateCalendar.getMinDate());
        Log.d(TAG,"min date: " + chooseDateCalendar.getMinDate());
        chooseDateCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                Log.d(TAG, "year: " + year + " month: " + month + " dayOfMonth: " + dayOfMonth);
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year ;

//                chosenDate = dayOfMonth + "/" + (month + 1) + "/" + year ;
                calendar.set(year, month, dayOfMonth);

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
                    toStep3.putStringArrayList("chosenAppointName",fromStep1.getStringArrayList("chosenAppointName"));
                    toStep3.putInt("priceSum",fromStep1.getInt("priceSum"));
                    toStep3.putInt("timeSum",timeSum);
                    toStep3.putSerializable("userUnavailableAppoints",getArguments().getSerializable("shopUnavailableTime"));
                    toStep3.putBoolean("chosenTakenUserAppoint",chosenTakenUserAppoint);
                    toStep3.putInt("userUnavailableStartTime",userUnavailableStartTime);
                    toStep3.putString("userUnavailableShopUid",userUnavailableShopUid);
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

    private void getAvailableTime(int dayNum, String selectedDate){
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
                Toast.makeText(getActivity(), "שגיאה בבחירת תאריך", Toast.LENGTH_SHORT).show();
                return;
        }
        availableTimesRadioGroup.removeAllViews();
        if(defaultWeekWork.containsKey(day)){
            unavailableAppoints.setVisibility(View.GONE);
            for(int i = 0; i < defaultWeekWork.get(day).size(); i++){
                Log.d(TAG, "day: " + day);
                int startDayTime = defaultWeekWork.get(day).get(i).getStartTime();
                int endDayTime = defaultWeekWork.get(day).get(i).getEndTime();
                Log.d(TAG, "i: " + i + " start: " + startDayTime + " end: " + endDayTime);
                int currEndTime = endDayTime;
                int currStartTime = startDayTime;
                while(true){
                    String currStartTimeText = String.valueOf(currStartTime);

                    if(currStartTimeText.length() < 4){
                        currStartTimeText = "0" + currStartTimeText;
                    }

                    String startTimeText = currStartTimeText.substring(0, 2) + ":" + currStartTimeText.substring(2);
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    try{

                        Date date = sdf.parse(startTimeText);
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTime(date);
                        calendar1.add(Calendar.MINUTE,timeSum);
                        String endTime = sdf.format(calendar1.getTime());

                        boolean shopTimeTaken = false;

//                        chosenDate = selectedDate;

                        currEndTime = Integer.parseInt(endTime.replace(":",""));
                        if(shopUnavailableTime != null){
                            for(String shopDate : shopUnavailableTime.keySet()){
                                if( shopDate != null && shopDate.equals(selectedDate) ){
                                    int shopUnavailableStartTime = shopUnavailableTime.get(shopDate).getStartTime();
                                    int shopUnavailableEndTime= shopUnavailableTime.get(shopDate).getEndTime();
                                    Log.d(TAG, "shopDate: " + shopDate + " unavailableStartTime: " + shopUnavailableStartTime + " unavailableEndTime: " + shopUnavailableEndTime);

                                    if(currStartTime >= shopUnavailableStartTime && currStartTime < shopUnavailableEndTime || currEndTime > shopUnavailableStartTime && currEndTime <= shopUnavailableEndTime){
                                        shopTimeTaken = true;
                                        break;
                                    }
                                }else{
                                    Log.d(TAG, "shopDate: " + shopDate);
                                }
                            }
                        }else{
                            Log.d(TAG,"shopUnavailableTime EMPTY");
                        }
                        Log.d(TAG, "currStartTime: " + currStartTime + " currEndTime: " + currEndTime);
                        Log.d(TAG, "timeTaken: " + shopTimeTaken);

                        if(currEndTime > endDayTime){
                            break;
                        }
                        boolean userTimeTaken = false;

                        if(!shopTimeTaken){
                            if(userUnavailableAppoints != null){
                                for(String userDate : userUnavailableAppoints.keySet()){
                                    if( userDate != null && userDate.equals(selectedDate) ){
                                        HashMap<TimeRange,String> timeAndName = userUnavailableAppoints.get(userDate);
                                        for(Map.Entry<TimeRange,String> entry : timeAndName.entrySet() ){
                                            userUnavailableStartTime = entry.getKey().getStartTime();
                                            userUnavailableEndTime = entry.getKey().getEndTime();
                                            userUnavailableShopUid = entry.getValue();
                                        }


                                        Log.d(TAG, "userDate: " + userDate + " userUnavailableStartTime: " + userUnavailableStartTime + " userUnavailableEndTime: " + userUnavailableEndTime);

                                        if(currStartTime >= userUnavailableStartTime && currStartTime < userUnavailableEndTime || currEndTime > userUnavailableStartTime && currEndTime <= userUnavailableEndTime){
                                            userTimeTaken = true;
                                            break;
                                        }
                                    }else{
                                        Log.d(TAG, "shopDate: " + userDate);
                                    }
                                }
                            }else{
                                Log.d(TAG, "userUnavailableAppoints null");
                            }



                            RadioButton radioTime = new RadioButton(getContext());
                            radioTime.setText(startTimeText);
                            if(userTimeTaken){
                                radioTime.setTextColor(Color.parseColor("#FF0000"));
                            }
                            radioTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    chosenStartTime = startTimeText;
                                    chosenEndTime = endTime;
                                    chosenDate = selectedDate;
                                    if(radioTime.getCurrentTextColor() == Color.parseColor("#FF0000")){
                                        chosenTakenUserAppointTime = userUnavailableStartTime;
                                        chosenTakenUserAppoint = true;

                                    }
                                }
                            });
                            availableTimesRadioGroup.addView(radioTime);

//                        Log.date(TAG, "start: " + startTimeText + " end: " + endTime);

                        }
                        currStartTime = currEndTime;
                    }catch(Exception e){
                        Log.e(TAG, "cant parse start time: " + e.getMessage());
                        break;
                    }
                }

            }
        }else{
            Log.d(TAG,"not contains key");
            unavailableAppoints.setText("אין תורים פנויים");
            unavailableAppoints.setVisibility(View.VISIBLE);
        }


    }

    private void goBack(View v){
        Bundle toStep1 = new Bundle();
        toStep1.putSerializable("userUnavailableAppoints",getArguments().getSerializable("userUnavailableAppoints"));
        toStep1.putSerializable("shopUnavailableTime",getArguments().getSerializable("shopUnavailableTime"));
        Navigation.findNavController(v).navigate(R.id.action_setShopAppointmentStep2_to_setShopAppointmentStep1,toStep1);
    }



}