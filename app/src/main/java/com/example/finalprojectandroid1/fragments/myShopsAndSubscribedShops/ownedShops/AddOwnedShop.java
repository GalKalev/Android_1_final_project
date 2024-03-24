package com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShops.ownedShops;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectandroid1.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddOwnedShop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddOwnedShop extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddOwnedShop() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddOwnedShop.
     */
    // TODO: Rename and change types and number of parameters
    public static AddOwnedShop newInstance(String param1, String param2) {
        AddOwnedShop fragment = new AddOwnedShop();
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

    String TAG = "AddOwnedShop";

     TableLayout sunRowLayout;
     TableLayout monRowLayout;
     TableLayout tueRowLayout;
     TableLayout wedRowLayout;
     TableLayout thurRowLayout;
     TableLayout friRowLayout;
     TableLayout satRowLayout;

    HashMap<String,ArrayList<Integer[]>> defaultWorkTimeEachDay = new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_owned_shop, container, false);

        Log.d(TAG,"inside");

        EditText shopName = view.findViewById(R.id.addShopName);

        EditText shopAddress = view.findViewById(R.id.addShopAddress);

        Button addShopImageButton = view.findViewById(R.id.addShopImageButton);

        EditText shopDes = view.findViewById(R.id.addShopDes);

        EditText shopLink = view.findViewById(R.id.addShopLink);
        Button addShopLinkButton = view.findViewById(R.id.addShopLinkButton);

        Spinner addTagsSpinner = view.findViewById(R.id.addShopTagsSpinner);

        Button addWorkingTimeButton = view.findViewById(R.id.addWorkingTimeRangeButton);

        sunRowLayout = view.findViewById(R.id.sunLayout);
        monRowLayout = view.findViewById(R.id.monLayout);
        tueRowLayout = view.findViewById(R.id.tueLayout);
        monRowLayout = view.findViewById(R.id.monLayout);
        wedRowLayout = view.findViewById(R.id.wedLayout);
        thurRowLayout = view.findViewById(R.id.thurLayout);
        friRowLayout = view.findViewById(R.id.friLayout);
        satRowLayout = view.findViewById(R.id.satLayout);

        addWorkingTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetWeekdayWorkingTimeDialog(v.getContext(),AddOwnedShop.this);
//                Log.d(TAG,daysSelected + " , " + timeSelected );
            }
        });


        return view;
    }

    public void updateWorkTime(ArrayList<String> days,int startHour, int startMinutes, int endHour, int endMinutes){
        int newStartTime;
        int newEndTime;
        if(startMinutes < 10){
            newStartTime = Integer.valueOf(startHour + "0" + startMinutes);
        }else{
            newStartTime = Integer.valueOf(startHour + "" + startMinutes);
        }
        if(endMinutes < 10){
            newEndTime = Integer.valueOf(endHour + "0" + endMinutes);
        }else{
            newEndTime = Integer.valueOf(endHour + "" + endMinutes);
        }
        Log.d(TAG, "newStartTime: " + newStartTime + ", newEndTime: " + newEndTime);
        for(String day : days){

            boolean timeInDay = false;
            if(defaultWorkTimeEachDay.containsKey(day)){
                for(Integer[] time : defaultWorkTimeEachDay.get(day)){
                    int startInTime = time[0];
                    int endInTime = time[1];
                    if ((startInTime <= newStartTime && newStartTime < endInTime) ||
                            (startInTime < newEndTime && newEndTime <= endInTime) ||
                            (newStartTime <= startInTime && endInTime <= newEndTime)) {
                        timeInDay = true;
                        Toast.makeText(getContext(), "הזמן שהוזן חופף עם זמן קיים.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (!timeInDay) {
                    Integer[] newTime = {newStartTime,newEndTime};
                    defaultWorkTimeEachDay.get(day).add(newTime);
                }
            }else{
                Integer[] timeArray = {newStartTime, newEndTime};
                ArrayList<Integer[]> allTimeArray = new ArrayList<>();
                allTimeArray.add(timeArray);

                defaultWorkTimeEachDay.put(day,allTimeArray);
            }
        }
        updateDefaultDaysHash();
    }


    private void updateDefaultDaysHash(){
        for(String day : defaultWorkTimeEachDay.keySet()){

            Log.d(TAG, "print day: " + day);
            ArrayList<Integer[]> timeRanges = defaultWorkTimeEachDay.get(day);
            timeRanges.sort(new Comparator<Integer[]>() {
                @Override
                public int compare(Integer[] time1, Integer[] time2) {
                    return Integer.compare(time1[0], time2[0]);
                }
            });

            updateDaysTable(day,timeRanges);

            for(Integer[] time : timeRanges){
                Log.d(TAG, "print time: " + time[0] + ", " + time[1]);

            }
        }
    }

    private void updateDaysTable(String day, ArrayList<Integer[]> timeArray){
        TableLayout updateTable = new TableLayout(getContext());
        for (Integer[] time : timeArray) {
            TableRow newWorkTimeRow = new TableRow(getContext());
            TextView showTime = new TextView(getContext());

            Button deleteNewTime = new Button(getContext());

            String startTimeStr = String.valueOf(time[0]);
            String endTimeStr = String.valueOf(time[1]);

            if (startTimeStr.length() < 4) {
                startTimeStr = "0" + startTimeStr;
            }
            if (endTimeStr.length() < 4) {
                endTimeStr = "0" + endTimeStr;
            }
            String formattedStartTimeStr = startTimeStr.substring(0, 2) + ":" + startTimeStr.substring(2);
            String formattedEndTimeStr = endTimeStr.substring(0, 2) + ":" + endTimeStr.substring(2);

            showTime.setText(formattedStartTimeStr + " - " + formattedEndTimeStr);

            deleteNewTime.setText("מחק שעה");
            deleteNewTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newWorkTimeRow.removeView(showTime);
                    newWorkTimeRow.removeView(deleteNewTime);
                    defaultWorkTimeEachDay.get(day).remove(time);
                    updateDefaultDaysHash();
                }
            });
            newWorkTimeRow.addView(deleteNewTime);
            newWorkTimeRow.addView(showTime);
            updateTable.addView(newWorkTimeRow);
        }

        switch (day) {
            case "א":
                sunRowLayout.removeAllViews(); // Clear existing views
                sunRowLayout.addView(updateTable); // Add the new table
                break;
            case "ב":
                monRowLayout.removeAllViews(); // Clear existing views
                monRowLayout.addView(updateTable); // Add the new table
                break;
            case "ג":
                tueRowLayout.removeAllViews(); // Clear existing views
                tueRowLayout.addView(updateTable); // Add the new table
                break;
            case "ד":
                wedRowLayout.removeAllViews(); // Clear existing views
                wedRowLayout.addView(updateTable); // Add the new table
                break;
            case "ה":
                thurRowLayout.removeAllViews(); // Clear existing views
                thurRowLayout.addView(updateTable); // Add the new table
                break;
            case "ו":
                friRowLayout.removeAllViews(); // Clear existing views
                friRowLayout.addView(updateTable); // Add the new table
                break;
            case "ש":
                satRowLayout.removeAllViews(); // Clear existing views
                satRowLayout.addView(updateTable); // Add the new table
                break;
            default:
        }

    }
}