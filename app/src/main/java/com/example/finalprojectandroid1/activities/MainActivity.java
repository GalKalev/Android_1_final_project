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
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.finalprojectandroid1.GlobalMembers;
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
import com.google.firebase.database.DatabaseException;
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

    // The main screen the user sees after loging/signing in

    private String TAG = "mMainActivity";
    ActivityMainBinding binding;
    ProgressBar progressBar;
    String userUid;
    UserInfo user;

    ArrayList<ShopModel> ownedShopList;
    ShopAdapter ownedShopAdapter;

    ArrayList<AppointmentModel> myAppointmentsList;
    AppointmentAdapter myAppointmentsAdapter;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new UserInfo();

        // Getting user information
        Bundle fromLoginSignIn = getIntent().getExtras();
        if(!fromLoginSignIn.isEmpty()){
            userUid = fromLoginSignIn.getString("userUid");
            user = fromLoginSignIn.getParcelable("user");
            if(user == null){
                setUserInfo();
            }
//
        }else{
            Log.e(TAG, "Problem with login or signin");
        }

        // Fetching the user appointments as a customer from database
        myAppointmentsList = new ArrayList<>();
        myAppointmentsAdapter = new AppointmentAdapter(myAppointmentsList,MainActivity.this,0,false, userUid);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressBar = findViewById(R.id.progressBar);

        // When user comes back from creating a shop, they will see the
        // fragment they got to there
        if(fromLoginSignIn.getInt("updateShop") == 1){
            replaceFragment(new MyShopsAndInfo(true));
            binding.bottomNavViewMainActivity.setSelectedItemId(R.id.myShops_icon_bottom_nav);
        }
        else{
            replaceFragment(new MyUpcomingAppointments());
        }

        // Setting the bottom navigation bar
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

        // Exiting the app by clicking the back button twice
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

    private void setUserInfo() {
        FirebaseDatabase.getInstance().getReference("users").child(userUid).child("userAuth").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(UserInfo.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method for changing the fragment in bottom navigation bar
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }

    public ArrayList<AppointmentModel> getMyAppointmentsList() {
        return myAppointmentsList;
    }

    public void setMyAppointmentsList(ArrayList<AppointmentModel> myAppointmentsList) {
        this.myAppointmentsList = myAppointmentsList;
    }

    public AppointmentAdapter getMyAppointmentsAdapter() {
        return myAppointmentsAdapter;
    }

    public void setMyAppointmentsAdapter(AppointmentAdapter myAppointmentsAdapter) {
        this.myAppointmentsAdapter = myAppointmentsAdapter;
    }

    public ArrayList<ShopModel> getOwnedShopList() {
        return ownedShopList;
    }

    public ShopAdapter getOwnedShopAdapter() {
        return ownedShopAdapter;
    }

    public void setOwnedShopAdapter(ShopAdapter ownedShopAdapter) {
        this.ownedShopAdapter = ownedShopAdapter;
    }

    public String getUserUid() {
        return userUid;
    }

    //
    public UserInfo getUser() {
       return user;
    }

    // When clicking on a shop card, this method will navigate to its page.
    // The page functions and appearance depends if the user is the owner of the clicked shop
    @Override
    public void onItemClick(int position, ArrayList<ShopModel> shopList) {
        ShopModel shop = shopList.get(position);

        goToShopPage(shop, false, null,null );

    }

    public void goToShopPage(ShopModel shop, boolean isAppointChange, String date, String startTime){
        Intent clickOnShopFromRes = new Intent(MainActivity.this,ShopInfoActivity.class);
        try{
            if(shop.getShopOwnerId().equals(userUid)){
                clickOnShopFromRes.putExtra("isOwned", true);
            }else{
                clickOnShopFromRes.putExtra("isOwned", false);
                clickOnShopFromRes.putExtra("userUid",userUid);
                clickOnShopFromRes.putExtra("user",user);
                clickOnShopFromRes.putExtra("ownedShopList",ownedShopList);
                clickOnShopFromRes.putExtra("myAppointmentsList",myAppointmentsList);
            }


            ArrayList<List<TimeRange>> timeList = new ArrayList<>();
            ArrayList<String> days = new ArrayList<>();
            for(String day : shop.getShopDefaultAvailableTime().keySet()){
                days.add(day);
                timeList.add(shop.getShopDefaultAvailableTime().get(day));
            }
            HashMap<String, List<TimeRange>> shopDefaultAvailableTime =shop.getShopDefaultAvailableTime();
            if (shopDefaultAvailableTime != null) {
                clickOnShopFromRes.putExtra("shopDefaultAvailableTime", shopDefaultAvailableTime);
            }

            HashMap<String,AppointmentsTimeAndPrice> shopAppointsTypes = shop.getShopSetAppointment();
            if(shopAppointsTypes != null){
                clickOnShopFromRes.putExtra("shopSetAppointment", shopAppointsTypes);
            }

            clickOnShopFromRes.putExtra("daysString", days);
            clickOnShopFromRes.putExtra("timeList", timeList);

            clickOnShopFromRes.putExtra("shop",  shop);
            if(isAppointChange){
                clickOnShopFromRes.putExtra("isAppointChange", true);
                clickOnShopFromRes.putExtra("appointChangeDate", date);
                clickOnShopFromRes.putExtra("appointChangeStartTime", startTime);
            }

            startActivity(clickOnShopFromRes);
        }catch(Exception e){
            Log.e(TAG, "goToShopPage: " + e.getMessage());
        }

    }
}