package com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectandroid1.GlobalMembers;
import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.ShopInfoActivity;
import com.example.finalprojectandroid1.shop.AppointmentsTimeAndPrice;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.example.finalprojectandroid1.shop.TimeRange;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OwnedShopInfoTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OwnedShopInfoTab extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OwnedShopInfoTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OwnedShopInfoTab.
     */
    // TODO: Rename and change types and number of parameters
    public static OwnedShopInfoTab newInstance(String param1, String param2) {
        OwnedShopInfoTab fragment = new OwnedShopInfoTab();
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

    // The owner can see the all the information of the shop including
    // different customers and the number of time they appeared in the shop
    String TAG = "OwnedShopInfoTab";
    ShopModel shop;
    HashMap<String, List<TimeRange>> shopDefaultAvailableTime = new HashMap<>();
    HashMap<String, AppointmentsTimeAndPrice> shopAppointsTypes = new HashMap<>();

    TableLayout sunTimeTable;
    TableLayout monTimeTable;
    TableLayout tueTimeTable;
    TableLayout wedTimeTable;
    TableLayout thurTimeTable;
    TableLayout friTimeTable;
    TableLayout satTimeTable;

    LinearLayout appointsTypeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_owned_shop_info_tab, container, false);
        ShopInfoActivity shopInfoActivity = (ShopInfoActivity) getActivity();
        shop = shopInfoActivity.getShop();
        shopDefaultAvailableTime = shopInfoActivity.getShopDefaultAvailableTime();
        shopAppointsTypes = shopInfoActivity.getShopAppointsTypes();

        TextView shopTags = view.findViewById(R.id.shopTagsInfo);
        TextView shopDes = view.findViewById(R.id.shopDesInfo);
        TextView noCustomers = view.findViewById(R.id.noCustomersText);

        sunTimeTable = view.findViewById(R.id.sunTimeTable);
        monTimeTable = view.findViewById(R.id.monTimeTable);
        tueTimeTable = view.findViewById(R.id.tueTimeTable);
        wedTimeTable = view.findViewById(R.id.wedTimeTable);
        thurTimeTable = view.findViewById(R.id.thurTimeTable);
        friTimeTable = view.findViewById(R.id.friTimeTable);
        satTimeTable = view.findViewById(R.id.satTimeTable);

        TableLayout customerAppearanceListTable = view.findViewById(R.id.customerAppearanceListTable);

        appointsTypeLayout = view.findViewById(R.id.appointsTypeLayout);
        LinearLayout linksLayout = view.findViewById(R.id.linksButtonsLayouOwnedTab);

        shopInfoActivity.setDesLinksTags(shopDes,linksLayout,shopTags);

        ArrayList<String> daysList = new ArrayList<>();
        daysList.add("א");
        daysList.add("ב");
        daysList.add("ג");
        daysList.add("ד");
        daysList.add("ה");
        daysList.add("ו");
        daysList.add("ש");


        // Setting the default shop activity in the week
        for(String day : daysList){
            if(shopDefaultAvailableTime.containsKey(day) && !shopDefaultAvailableTime.get(day).isEmpty() ){
                switch (day){
                    case "א":
                    sunTimeTable.removeAllViews();
                    shopInfoActivity.setWorkTimeTable(day, sunTimeTable);
                    break;
                case "ב":
                    monTimeTable.removeAllViews();
                    shopInfoActivity.setWorkTimeTable(day, monTimeTable);
                    break;
                case "ג":
                    tueTimeTable.removeAllViews();
                    shopInfoActivity.setWorkTimeTable(day, tueTimeTable);
                    break;
                case "ד":
                    wedTimeTable.removeAllViews();
                    shopInfoActivity.setWorkTimeTable(day, wedTimeTable);
                    break;
                case "ה":
                    thurTimeTable.removeAllViews();
                    shopInfoActivity.setWorkTimeTable(day, thurTimeTable);
                    break;
                case "ו":
                    friTimeTable.removeAllViews();
                    shopInfoActivity.setWorkTimeTable(day, friTimeTable);
                    break;
                case "ש":
                    satTimeTable.removeAllViews();
                    shopInfoActivity.setWorkTimeTable(day, satTimeTable);
                    break;
                }
            }else{
                TableRow noTimeRow = new TableRow(getContext());
                TextView noTimeText = new TextView(getContext());
                noTimeText.setTextColor(Color.BLACK);
                noTimeText.setText("לא הוזנו שעות פעילות ביום זה");
                noTimeRow.addView(noTimeText);
                switch (day){
                    case "א":
                        sunTimeTable.addView(noTimeRow);

                        break;
                    case "ב":
                        monTimeTable.addView(noTimeRow);
                        break;
                    case "ג":
                        tueTimeTable.addView(noTimeRow);
                        break;
                    case "ד":
                        wedTimeTable.addView(noTimeRow);
                        break;
                    case "ה":
                        thurTimeTable.addView(noTimeRow);
                        break;
                    case "ו":
                        friTimeTable.addView(noTimeRow);
                        break;
                    case "ש":
                        satTimeTable.addView(noTimeRow);
                        break;
                }
            }

        }


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Setting the different appointment type the owner has set
        for(String typeName : shopAppointsTypes.keySet()){
            LinearLayout appointmentNameAndLengthLayout = new LinearLayout(getContext());
            appointmentNameAndLengthLayout.setLayoutParams(layoutParams);
            appointmentNameAndLengthLayout.setGravity(Gravity.END);
            layoutParams.weight = 1;

            LinearLayout.LayoutParams appointLayoutParams =  new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, // Width
                    LinearLayout.LayoutParams.WRAP_CONTENT  // Height, adjust as needed
            );

            TextView ils = new TextView(getContext());
            ils.setText(" ש\"ח");
            ils.setLayoutParams(appointLayoutParams);
            ils.setTextColor(Color.BLACK);

            TextView appointmentPrice = new TextView(getContext());
            appointmentPrice.setLayoutParams(appointLayoutParams);
            String price =  String.valueOf(shopAppointsTypes.get(typeName).getPrice()) + " -- " ;
            appointmentPrice.setText(price);
            appointmentPrice.setTextColor(Color.BLACK);



            TextView minutes = new TextView(getContext());
            minutes.setText(" דק ");
            minutes.setLayoutParams(appointLayoutParams);
            minutes.setTextColor(Color.BLACK);

            TextView appointmentTime = new TextView(getContext());
            appointmentTime.setLayoutParams(appointLayoutParams);
            String time = String.valueOf(shopAppointsTypes.get(typeName).getTime());
            appointmentTime.setText(time);
            appointmentTime.setTextColor(Color.BLACK);


            TextView hyphen = new TextView(getContext());
            hyphen.setText(" - ");
            hyphen.setLayoutParams(appointLayoutParams);
            hyphen.setTextColor(Color.BLACK);

            TextView appointmentName = new TextView(getContext());
            appointmentName.setInputType(InputType.TYPE_CLASS_TEXT);
            appointmentName.setLayoutParams(appointLayoutParams);
            appointmentName.setText(typeName);
            appointmentName.setTextColor(Color.BLACK);


            appointmentNameAndLengthLayout.addView(ils);
            appointmentNameAndLengthLayout.addView(appointmentPrice);
            appointmentNameAndLengthLayout.addView(minutes);
            appointmentNameAndLengthLayout.addView(appointmentTime);
            appointmentNameAndLengthLayout.addView(hyphen);
            appointmentNameAndLengthLayout.addView(appointmentName);


            appointsTypeLayout.addView(appointmentNameAndLengthLayout);
        }

        // Setting the customer appearances table; with their name and overall
        // number of time they had an appointment
        FirebaseDatabase.getInstance().getReference("shops").child(shop.getShopUid()).child("usersAppearances").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    noCustomers.setVisibility(View.VISIBLE);
                    customerAppearanceListTable.setVisibility(View.GONE);
                }else{
                    noCustomers.setVisibility(View.GONE);
                    for(DataSnapshot usersUidSnap: snapshot.getChildren()){
                        TableRow.LayoutParams textViewParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,1.0f);
                        TableRow customerInfoRow = new TableRow(getContext());
                        customerInfoRow.setBackgroundResource(R.drawable.top_black_line);

                        ViewGroup.LayoutParams tableRowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
                        customerInfoRow.setLayoutParams(tableRowParams);

                        TextView customerAppearanceCount = new TextView(getContext());
                        customerAppearanceCount.setText(String.valueOf(usersUidSnap.child("appointmentsOrdered").getValue(Integer.class)));
                        customerAppearanceCount.setGravity(Gravity.END);
                        customerAppearanceCount.setTextColor(Color.BLACK);
                        customerAppearanceCount.setLayoutParams(textViewParam);

                        customerInfoRow.addView(customerAppearanceCount);

                        TextView customerName = new TextView(getContext());
                        customerName.setText(usersUidSnap.child("userName").getValue(String.class));
                        customerName.setLayoutParams(textViewParam);
                        customerName.setTextColor(Color.BLACK);
                        customerInfoRow.addView(customerName);
                        customerAppearanceListTable.addView(customerInfoRow);



                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(shopInfoActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

}