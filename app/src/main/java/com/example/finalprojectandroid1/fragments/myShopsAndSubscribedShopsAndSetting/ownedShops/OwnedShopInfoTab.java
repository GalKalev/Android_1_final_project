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
        TextView shopLinks = view.findViewById(R.id.shopLinksInfo);
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

//        shopDes.setText(shop.getShopDes());
//
//        for(String tag : shop.getShopTags()){
//            shopTags.setText(shopTags.getText() + "#" + tag + " ");
//        }
//
//        for(String link : shop.getShopLinks()){
//            shopLinks.setText(link + "\n");
//        }

        shopInfoActivity.setDesLinksTags(shopDes,shopLinks,shopTags);

        for(String day : shopDefaultAvailableTime.keySet()){
            switch (day){
                case "א":
                    setWorkTimeTable(day, sunTimeTable);
                    break;
                case "ב":
                    setWorkTimeTable(day, monTimeTable);
                    break;
                case "ג":
                    setWorkTimeTable(day, tueTimeTable);
                    break;
                case "ד":
                    setWorkTimeTable(day, wedTimeTable);
                    break;
                case "ה":
                    setWorkTimeTable(day, thurTimeTable);
                    break;
                case "ו":
                    setWorkTimeTable(day, friTimeTable);
                    break;
                case "ש":
                    setWorkTimeTable(day, satTimeTable);
                    break;
            }

        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // Width
                LinearLayout.LayoutParams.WRAP_CONTENT  // Height, adjust as needed
        );
        String[] shopAppointsTypesKeys =  shopAppointsTypes.keySet().toArray(new String[0]);

        for(int i = shopAppointsTypesKeys.length - 1 ; i >= 0 ; i--){
            LinearLayout appointmentNameAndLengthLayout = new LinearLayout(getContext());
            appointmentNameAndLengthLayout.setLayoutParams(layoutParams);
            appointmentNameAndLengthLayout.setGravity(Gravity.END);
            layoutParams.weight = 1;

            Log.d(TAG, "appoint: " + shopAppointsTypesKeys[i]);

            LinearLayout.LayoutParams appointLayoutParams =  new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, // Width
                    LinearLayout.LayoutParams.WRAP_CONTENT  // Height, adjust as needed
            );

            TextView ils = new TextView(getContext());
            ils.setText(" ש\"ח");
            ils.setLayoutParams(appointLayoutParams);

            TextView appointmentPrice = new TextView(getContext());
            appointmentPrice.setLayoutParams(appointLayoutParams);
            String price =  String.valueOf(shopAppointsTypes.get(shopAppointsTypesKeys[i]).getPrice()) + " -- " ;
            appointmentPrice.setText(price);



            TextView minutes = new TextView(getContext());
            minutes.setText(" דק ");
            minutes.setLayoutParams(appointLayoutParams);

            TextView appointmentTime = new TextView(getContext());
            appointmentTime.setLayoutParams(appointLayoutParams);
            String time = String.valueOf(shopAppointsTypes.get(shopAppointsTypesKeys[i]).getTime());
            appointmentTime.setText(time);


            TextView hyphen = new TextView(getContext());
            hyphen.setText(" - ");
            hyphen.setLayoutParams(appointLayoutParams);

            TextView appointmentName = new TextView(getContext());
            appointmentName.setInputType(InputType.TYPE_CLASS_TEXT);
            appointmentName.setLayoutParams(appointLayoutParams);
            appointmentName.setText(shopAppointsTypesKeys[i]);


            appointmentNameAndLengthLayout.addView(ils);
            appointmentNameAndLengthLayout.addView(appointmentPrice);
            appointmentNameAndLengthLayout.addView(minutes);
            appointmentNameAndLengthLayout.addView(appointmentTime);
            appointmentNameAndLengthLayout.addView(hyphen);
            appointmentNameAndLengthLayout.addView(appointmentName);


            appointsTypeLayout.addView(appointmentNameAndLengthLayout);

        }


        FirebaseDatabase.getInstance().getReference("shops").child(shop.getShopUid()).child("usersAppearances").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    noCustomers.setVisibility(View.VISIBLE);
                }else{
//
                    noCustomers.setVisibility(View.GONE);
                    for(DataSnapshot usersUidSnap: snapshot.getChildren()){
                        TableRow.LayoutParams textViewParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,1.0f);
                        TableRow customerInfoRow = new TableRow(getContext());

                        ViewGroup.LayoutParams tableRowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
                        customerInfoRow.setLayoutParams(tableRowParams);

                        TextView customerAppearanceCount = new TextView(getContext());
                        customerAppearanceCount.setText(String.valueOf(usersUidSnap.child("appointmentsOrdered").getValue(Integer.class)));
                        customerAppearanceCount.setGravity(Gravity.END);
                        customerAppearanceCount.setLayoutParams(textViewParam);

                        customerInfoRow.addView(customerAppearanceCount);

                        TextView customerName = new TextView(getContext());
                        customerName.setText(usersUidSnap.child("userName").getValue(String.class));
                        customerName.setLayoutParams(textViewParam);
//                        customerName.setGravity(Gravity.END);
                        customerInfoRow.addView(customerName);
                        customerAppearanceListTable.addView(customerInfoRow);



                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    //-------repeated function to set the table as in UpdateShopActivity so fix.-------//
    private void setWorkTimeTable(String day, TableLayout dayLayout) {
        for (TimeRange time : shopDefaultAvailableTime.get(day)) {
            TableRow newWorkTimeRow = new TableRow(getContext());
            TextView showTime = new TextView(getContext());

            String startTimeStr = String.valueOf(time.getStartTime());
            String endTimeStr = String.valueOf(time.getEndTime());

            if (startTimeStr.length() < 4) {
                startTimeStr = "0" + startTimeStr;
            }
            if (endTimeStr.length() < 4) {
                endTimeStr = "0" + endTimeStr;
            }
            String formattedStartTimeStr = startTimeStr.substring(0, 2) + ":" + startTimeStr.substring(2);
            String formattedEndTimeStr = endTimeStr.substring(0, 2) + ":" + endTimeStr.substring(2);
            showTime.setText(formattedStartTimeStr + " - " + formattedEndTimeStr);

            newWorkTimeRow.setGravity(Gravity.END);
            showTime.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
            newWorkTimeRow.addView(showTime);

            dayLayout.addView(newWorkTimeRow);

        }
    }
}