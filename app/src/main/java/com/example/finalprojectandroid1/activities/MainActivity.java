package com.example.finalprojectandroid1.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.appointment.AppointmentAdapter;
import com.example.finalprojectandroid1.appointment.AppointmentModel;
import com.example.finalprojectandroid1.databinding.ActivityMainBinding;
import com.example.finalprojectandroid1.fragments.myAppointments.MyUpcomingAppointments;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.MyShopsAndInfo;
import com.example.finalprojectandroid1.fragments.searchShops.SearchShops;
import com.example.finalprojectandroid1.shop.AppointmentsTimeAndPrice;
import com.example.finalprojectandroid1.shop.ShopAdapter;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.example.finalprojectandroid1.shop.ShopResInterface;
import com.example.finalprojectandroid1.shop.TimeRange;
import com.example.finalprojectandroid1.shop.shopFragments.Address;
import com.example.finalprojectandroid1.user.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements ShopResInterface {

    private String TAG = "mMainActivity";
    ActivityMainBinding binding;
    DatabaseReference myRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference imageRef ;
    ProgressBar progressBar;
    String userUid;
    UserInfo user;

    ArrayList<ShopModel> ownedShopList;
    ShopAdapter ownedShopAdapter;

    ArrayList<ShopModel> subShopList;
    ShopAdapter subShopAdapter;

    ArrayList<AppointmentModel> myAppointmentsList;
    AppointmentAdapter myAppointmentsAdapter;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        user = new UserInfo();

        Bundle fromLoginSignIn = getIntent().getExtras();
        if(!fromLoginSignIn.isEmpty()){

            userUid = fromLoginSignIn.getString("userUid");
            getUser();
            Log.d(TAG, "user uid: " + userUid);
//            Log.d(TAG, "user: " + user.toString());
        }else{
            Log.e(TAG, "Problem with login or signin");
        }


        ownedShopList = new ArrayList<>();
        ownedShopAdapter = new ShopAdapter(MainActivity.this, ownedShopList,this );
        setOwnedShopList();

//        subShopList = new ArrayList<>();
//        subShopAdapter = new ShopAdapter(MainActivity.this, subShopList,this);
//        setSubShopList();

        myAppointmentsList = new ArrayList<>();
        myAppointmentsAdapter = new AppointmentAdapter(myAppointmentsList,MainActivity.this,false, userUid);
//        setMyAppointmentsList();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressBar = findViewById(R.id.progressBar);
//
        if(fromLoginSignIn.getInt("updateShop") == 1){
            replaceFragment(new MyShopsAndInfo(true));
            binding.bottomNavViewMainActivity.setSelectedItemId(R.id.myShops_icon_bottom_nav);
            Log.d(TAG, "update shop 1");


        } else{
            replaceFragment(new MyUpcomingAppointments());
            Log.d(TAG, "update shop not 1");
//            Log.d(TAG, "user uid: " + userUid);
        }
//


//
        binding.bottomNavViewMainActivity.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.calendar_icon_bottom_nav) {
                replaceFragment(new MyUpcomingAppointments());
            } else if (item.getItemId() == R.id.shops_icon_bottom_nav) {
                replaceFragment(new SearchShops());
            } else if (item.getItemId() == R.id.myShops_icon_bottom_nav) {
                replaceFragment(new MyShopsAndInfo());
            }
            return true;
        });


        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(doubleBackToExitPressedOnce){
                    finishAffinity();

                }else{
                    Toast.makeText(MainActivity.this, "ליציאה יש ללחוץ חזרה פעם נוספת", Toast.LENGTH_SHORT).show();
                    doubleBackToExitPressedOnce = true;
                    new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);

                }
            }
        });

    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }

    public ArrayList<AppointmentModel> getMyAppointmentsList() {
        return myAppointmentsList;
    }

//    public void setMyAppointmentsList() {
//        try{
//            DatabaseReference userAppoints = FirebaseDatabase.getInstance().getReference("users").child(userUid).child("userAppointments");
//
//            userAppoints.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    Log.d(TAG, "setMyAppointmentsList snapshot count: " + snapshot.getChildrenCount());
//                    Log.d(TAG, "setMyAppointmentsList snapshot value: " + snapshot.getValue());
//                    myAppointmentsList.add(snapshot.getValue(AppointmentModel.class));
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }catch(Exception e){
//            Log.e(TAG, "Error fetching user appointments: " + e.getMessage());
//        }
//
//    }

    public AppointmentAdapter getMyAppointmentsAdapter() {
        return myAppointmentsAdapter;
    }

    public void setMyAppointmentsAdapter(AppointmentAdapter myAppointmentsAdapter) {
        this.myAppointmentsAdapter = myAppointmentsAdapter;
    }

    public ArrayList<ShopModel> getOwnedShopList() {
        return ownedShopList;
    }

//    public void setOwnedShopList(ArrayList<ShopModel> ownedShopList) {
//        this.ownedShopList = ownedShopList;
//    }

    public ShopAdapter getOwnedShopAdapter() {
        return ownedShopAdapter;
    }

    public void setOwnedShopAdapter(ShopAdapter ownedShopAdapter) {
        this.ownedShopAdapter = ownedShopAdapter;
    }

    private void setOwnedShopList(){

        DatabaseReference ownedShopsRef = FirebaseDatabase.getInstance().getReference("shops");
        Query getOwnedShopsQuery = ownedShopsRef.orderByChild("shopOwnerId").equalTo(userUid);

        getOwnedShopsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG,"sanpshot count: " + snapshot.getChildrenCount());
                for (DataSnapshot shopSnapshot : snapshot.getChildren()) {
//                    try{
//                        setShopListData(ownedShopList,ownedShopAdapter,shopSnapshot);
//
//                    }catch(Exception e){
//                        Log.d(TAG, "Error fetching value: " + e.getMessage());
//                    }
                    ownedShopList.add(shopSnapshot.getValue(ShopModel.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setShopListData(ArrayList<ShopModel> list, ShopAdapter adapter, DataSnapshot shopSnapshot){
        Log.d(TAG, "list.size(): " + list.size() );
        Address shopAddress = shopSnapshot.child("shopAddress").getValue(Address.class);
        String shopDes = shopSnapshot.child("shopDes").getValue(String.class);
        String shopImage = shopSnapshot.child("shopImage").getValue(String.class);
        String shopName = shopSnapshot.child("shopName").getValue(String.class);
        String shopOwnerId = shopSnapshot.child("shopOwnerId").getValue(String.class);
        String shopUid = shopSnapshot.child("shopUid").getValue(String.class);

        HashMap<String,AppointmentsTimeAndPrice> shopSetAppointment = new HashMap<>();
        for(DataSnapshot appoint : shopSnapshot.child("shopSetAppointment").getChildren()){
            AppointmentsTimeAndPrice timeAndPrice = new AppointmentsTimeAndPrice();
            timeAndPrice.setTime(Integer.parseInt(appoint.child("time").getValue().toString()));
            timeAndPrice.setPrice(Integer.parseInt(appoint.child("price").getValue().toString()));
            shopSetAppointment.put(appoint.getKey(),timeAndPrice);
        }

        ArrayList<String> shopTags = new ArrayList<>();
        for(DataSnapshot tagList : shopSnapshot.child("shopTags").getChildren()){
            String tag = tagList.getValue(String.class);
            shopTags.add(tag);
        }

        ArrayList<String> shopLinks = new ArrayList<>();
        for(DataSnapshot links : shopSnapshot.child("shopLinks").getChildren()){
            String link = links.getValue(String.class);
            shopLinks.add(link);
        }


        HashMap<String, List<TimeRange>> defaultWorkTimeEachDay = new HashMap<>();
        for(DataSnapshot day : shopSnapshot.child("shopDefaultAvailableTime").getChildren()){
            List<TimeRange> timesList = new ArrayList<>();
            for(DataSnapshot times : day.getChildren()){
                int startTime = times.child("startTime").getValue(Integer.class);
                int endTime = times.child("endTime").getValue(Integer.class);
                // Create a WeekdayWorkTime object with the extracted startTime and endTime
                TimeRange workTime = new TimeRange(startTime, endTime);
                // Add the workTime object to the timesList
                timesList.add(workTime);
            }
            // Add the timesList to the defaultWorkTimeEachDay HashMap with the day key
            defaultWorkTimeEachDay.put(day.getKey(), timesList);
        }

        list.add(new ShopModel(shopUid,shopName,shopAddress,
                shopImage,shopDes,shopOwnerId,shopSetAppointment,shopTags,
                shopLinks,defaultWorkTimeEachDay));
        Log.d(TAG,"list: " + list.toString());
        adapter.notifyDataSetChanged();

    }

    public ArrayList<ShopModel> getSubShopList() {
        return subShopList;
    }

//    public ArrayList<ShopModel> setSubShopList() {
//        subShopList.clear();
//        Log.d(TAG, "subShopList.size(): " + subShopList.size());
//        try{
//            DatabaseReference subShopsRef = FirebaseDatabase.getInstance().getReference("users")
//                    .child(userUid).child("subscribedShops");
//            subShopsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    ArrayList<String> subKeyList = new ArrayList<>();
//                    for(DataSnapshot shopSnapshot : snapshot.getChildren()){
//                        String shopKey = shopSnapshot.getKey();
//
//                        subKeyList.add(shopKey);
//                    }
//
//                    DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("shops");
//                    for(String shopKey : subKeyList){
//
//                        Log.d(TAG, "shopKey: " + shopKey);
//                        shopRef.child(shopKey).addListenerForSingleValueEvent(new ValueEventListener() {
//                            int count = 0;
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                subShopList.add(snapshot.getValue(ShopModel.class));
//                                count++;
//                                if(count == subKeyList.size()){
//                                    subShopAdapter = new ShopAdapter(MainActivity.this, subShopList,MainActivity.this); ;
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//        }catch(Exception e){
//            Log.e(TAG, "setSubShopList: " + e.getMessage());
//        }
//
//        return
//
//    }

    public ShopAdapter getSubShopAdapter() {
        return subShopAdapter;
    }

    public void setSubShopAdapter(ShopAdapter subShopAdapter) {
        this.subShopAdapter = subShopAdapter;
    }

    public String getUserUid() {
        return userUid;
    }

    public void removeSubscription(){

    }


    public void getUser() {
        FirebaseDatabase.getInstance().getReference("users").child(getUserUid()).child("userAuth").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(UserInfo.class);

                Log.d(TAG, user.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onItemClick(int position, ArrayList<ShopModel> shopList) {
        Intent clickOnShopFromRes = new Intent(MainActivity.this,ShopInfoActivity.class);
        if(shopList.get(position).getShopOwnerId().equals(userUid)){
            Log.d(TAG, "owner id: " + shopList.get(position).getShopOwnerId());
            clickOnShopFromRes.putExtra("isOwned", true);
        }else{
            clickOnShopFromRes.putExtra("isOwned", false);
            clickOnShopFromRes.putExtra("userUid",userUid);
            clickOnShopFromRes.putExtra("user",user);
            clickOnShopFromRes.putExtra("ownedShopList",ownedShopList);
        }


//            HashMap<String,List<WeekdayWorkTime>> temp = new HashMap<>();
//            temp = ownedShopList.get(position).getShopDefaultAvailableTime();
        ArrayList<List<TimeRange>> timeList = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();
         for(String day : shopList.get(position).getShopDefaultAvailableTime().keySet()){
             days.add(day);
             timeList.add(shopList.get(position).getShopDefaultAvailableTime().get(day));
         }
        HashMap<String, List<TimeRange>> shopDefaultAvailableTime = shopList.get(position).getShopDefaultAvailableTime();
        if (shopDefaultAvailableTime != null) {
            clickOnShopFromRes.putExtra("shopDefaultAvailableTime", shopDefaultAvailableTime);
        }

        HashMap<String,AppointmentsTimeAndPrice> shopAppointsTypes = shopList.get(position).getShopSetAppointment();
        if(shopAppointsTypes != null){
            clickOnShopFromRes.putExtra("shopSetAppointment", shopAppointsTypes);
        }

        clickOnShopFromRes.putExtra("daysString", days);
        clickOnShopFromRes.putExtra("timeList", timeList);

        clickOnShopFromRes.putExtra("shop",  shopList.get(position));

        startActivity(clickOnShopFromRes);
    }
}