package com.example.finalprojectandroid1.fragments.myAppointments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.ShopInfoActivity;
import com.example.finalprojectandroid1.shop.AppointmentsTimeAndPrice;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.example.finalprojectandroid1.shop.TimeRange;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetShopAppointmentStep1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetShopAppointmentStep1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SetShopAppointmentStep1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetShopAppointment.
     */
    // TODO: Rename and change types and number of parameters
    public static SetShopAppointmentStep1 newInstance(String param1, String param2) {
        SetShopAppointmentStep1 fragment = new SetShopAppointmentStep1();
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

    String TAG = "SetShopAppointmentStep1";
    int priceSum = 0;
    int timeSum = 0;
    ArrayList<String> chosenAppointsName;
    ShopModel shop;


    HashMap<String, AppointmentsTimeAndPrice> shopSetAppointment;
    HashMap<String,TimeRange> shopUnavailableAppoints;
    ShopInfoActivity shopInfoActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_shop_appointment_step1, container, false);

        shopInfoActivity = (ShopInfoActivity)getActivity();

        LinearLayout appointTypeListLayout = view.findViewById(R.id.appointTypesListLayout);
        TextView priceSumText = view.findViewById(R.id.appointmentSumPrice);
        Button nextBtn = view.findViewById(R.id.toStep2SetAppointmentsButton);
        Button backBtn = view.findViewById(R.id.backToNotOwnedButton);
        chosenAppointsName = new ArrayList<>();
        shopSetAppointment = shopInfoActivity.getShopAppointsTypes();
        String[] appointKeys = shopSetAppointment.keySet().toArray(new String[0]);

        shop = shopInfoActivity.getShop();
        shopUnavailableAppoints = new HashMap<>();

        for(int i = appointKeys.length - 1; i >= 0; i--){

            final String appointName = appointKeys[i];

            Log.d(TAG, "appointName: " + appointName);

            LinearLayout eachAppointLayout = new LinearLayout(getContext());
            eachAppointLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.gravity = Gravity.END;
            eachAppointLayout.setLayoutParams(layoutParams);

            TextView typePrice = new TextView(getContext());
            typePrice.setText(" -> " + shopSetAppointment.get(appointName).getPrice() + "  ש\"ח");

            TextView typeName = new TextView(getContext());
            typeName.setText(appointName);

            CheckBox appointCheckBox = new CheckBox(getContext());
            eachAppointLayout.addView(typePrice);
            eachAppointLayout.addView(typeName);
            eachAppointLayout.addView(appointCheckBox);

            appointCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int appointPriceNum = shopSetAppointment.get(appointName).getPrice();
                    int appointTimeNum = shopSetAppointment.get(appointName).getTime();
                    if(isChecked){
                        Log.d(TAG, "checked");
                        priceSum += appointPriceNum;
                        timeSum += appointTimeNum;
                        chosenAppointsName.add(appointName);
                    }else{
                        Log.d(TAG, "not checked");
                        priceSum -= appointPriceNum;
                        timeSum -= appointTimeNum;
                        chosenAppointsName.remove(appointName);
                    }
                    priceSumText.setText("סהכ: " + priceSum + " ש\"ח");
                }
            });

            appointTypeListLayout.addView(eachAppointLayout);
        }

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle toStep2 = new Bundle();
                toStep2.putInt("timeSum", timeSum);
                toStep2.putInt("priceSum", priceSum);
                toStep2.putSerializable("userUnavailableAppoints",getArguments().getSerializable("userUnavailableAppoints"));
                toStep2.putSerializable("shopUnavailableTime",getArguments().getSerializable("shopUnavailableTime"));
                toStep2.putBoolean("isAppointChange",getArguments().getBoolean("isAppointChange"));
                toStep2.putString("appointChangeDate",getArguments().getString("appointChangeDate"));
                toStep2.putString("appointChangeStartTime",getArguments().getString("appointChangeStartTime"));
                toStep2.putStringArrayList("chosenAppointsName", chosenAppointsName);

                Log.d(TAG, "timeSum: " + timeSum);
                if(chosenAppointsName.isEmpty()){
                    Toast.makeText(shopInfoActivity, "נא לבחור תור/ים", Toast.LENGTH_SHORT).show();
                }else{
                    Navigation.findNavController(view).navigate(R.id.action_setShopAppointmentStep1_to_setShopAppointmentStep2,toStep2);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
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

    public void getShopUnavailableAppointments(){
        try{
            DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("shops").child("shopAppointments");
            shopRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot appoint : snapshot.getChildren()){


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Log.d(TAG, "no shop appoints yet");
        }

    }
    public void getUserUnavailableAppointments(){
        try{
            DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("users").child("userAppointments");
            shopRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot appoint : snapshot.getChildren()){


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Log.d(TAG, "no user appoints yet");
        }

    }

    public void goBack(View v){

        Bundle backToNotOwned = new Bundle();
        if(getArguments().getBoolean("isAppointChange")){
           shopInfoActivity.finish();
        }

        // date
        // time
        Navigation.findNavController(v).navigate(R.id.action_setShopAppointmentStep1_to_notOwnedShopStats,backToNotOwned);
    }
}