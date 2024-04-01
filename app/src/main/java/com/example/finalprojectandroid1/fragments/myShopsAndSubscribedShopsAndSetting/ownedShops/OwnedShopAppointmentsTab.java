package com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops;

import android.os.Bundle;

import androidx.fragment.app.Fragment;


import androidx.core.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalprojectandroid1.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    TextView selectedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_owned_shop_appointments_tab, container, false);
        calendarButton = view.findViewById(R.id.calendarButtonAppointTab);
        selectedDate = view.findViewById(R.id.showDatesText);

        calendarButton.setOnClickListener(v -> DatePickerDialog());

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

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String startDateString = sdf.format(new Date(startDate));
            String endDateString = sdf.format(new Date(endDate));

            String selectedDateRange = startDateString + " - " + endDateString;
            selectedDate.setText(selectedDateRange);
        });

        datePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
    }
}