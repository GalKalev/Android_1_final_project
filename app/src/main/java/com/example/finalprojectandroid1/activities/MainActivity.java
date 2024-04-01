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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.databinding.ActivityMainBinding;
import com.example.finalprojectandroid1.fragments.myAppointments.MyUpcomingAppointments;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.MyShopsAndInfo;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops.MyOwnedShops;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops.NavFromOwnedListToAdd;
import com.example.finalprojectandroid1.fragments.searchShops.SearchShops;
import com.example.finalprojectandroid1.shop.ShopAdapter;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.example.finalprojectandroid1.shop.ShopResInterface;
import com.example.finalprojectandroid1.shop.WeekdayWorkTime;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

    ArrayList<ShopModel> ownedShopList;
    ShopAdapter ownedShopAdapter;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        Bundle fromLoginSignIn = getIntent().getExtras();
        if(!fromLoginSignIn.isEmpty()){
            userUid = fromLoginSignIn.getString("curUserUid");
            Log.d(TAG, "user uid: " + userUid);
        }else{
            Log.e(TAG, "Problem with login or signin");
        }


        ownedShopList = new ArrayList<>();
        ownedShopAdapter = new ShopAdapter(MainActivity.this, ownedShopList,this );
        setOwnedShopList();

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


    public ArrayList<ShopModel> getOwnedShopList() {
        return ownedShopList;
    }

    public void setOwnedShopList(ArrayList<ShopModel> ownedShopList) {
        this.ownedShopList = ownedShopList;
    }

    public ShopAdapter getOwnedShopAdapter() {
        return ownedShopAdapter;
    }

    public void setOwnedShopAdapter(ShopAdapter ownedShopAdapter) {
        this.ownedShopAdapter = ownedShopAdapter;
    }

    public void addOwnedShop(String shopName, String shopAddress, byte[] imageData,
                             String shopDes, ArrayList<String> links, ArrayList<String> tags,
                             HashMap<String,Integer> appointmentType, HashMap<String, List<WeekdayWorkTime>> defaultWorkTimeEachDay){

//        Log.d(TAG,"add shop");
//        myRef = database.getReference("shops");
//
//        // Generate a unique key for the new shop
//        String shopUid = myRef.push().getKey();
//
//        // Create a reference to the new shop under "stores" node
//        DatabaseReference newShopRef = myRef.child(shopUid);
//
//        // Upload shop image
//        imageRef = storageRef.child("shops/images/" + shopUid + ".jpg");
//        progressBar.setVisibility(View.VISIBLE);
//
//        UploadTask uploadTask = imageRef.putBytes(imageData);
//        uploadTask.addOnFailureListener(exception -> {
//            progressBar.setVisibility(View.GONE);
//            // Handle unsuccessful uploads
//            Log.d(TAG, "Image upload failed: " + exception.getMessage());
//            Toast.makeText(MainActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
//        }).addOnSuccessListener(taskSnapshot -> {
//
//            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                String imageUrl = uri.toString();
//
//                ShopModel newShop = new ShopModel(shopUid,shopName, shopAddress, imageUrl, shopDes,
//                        userUid, appointmentType, tags, links,defaultWorkTimeEachDay);
//
//                // Save the new shop data to the database
//                try {
//                    newShopRef.setValue(newShop).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            ownedShopList.add(newShop);
//                            progressBar.setVisibility(View.GONE);
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            progressBar.setVisibility(View.GONE);
//                            Toast.makeText(MainActivity.this, "העלאת החנות נכשלה. נסה שוב", Toast.LENGTH_SHORT).show();
//                        }
//                    });
////
//
//                   Log.d(TAG,newShop.toString()) ;
//
//                } catch (Exception e) {
//                    progressBar.setVisibility(View.GONE);
//                    Log.d(TAG, "Error setValue: " + e.getMessage());
//                }
//
//            });
//        });
    }

    private void setOwnedShopList(){
        DatabaseReference ownedShopsRef = FirebaseDatabase.getInstance().getReference("shops");
        Query getOwnedShopsQuery = ownedShopsRef.orderByChild("shopOwnerId").equalTo(userUid);

        getOwnedShopsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG,"sanpshot count: " + snapshot.getChildrenCount());
                for (DataSnapshot shopSnapshot : snapshot.getChildren()) {
                    try{
                        String shopAddress = shopSnapshot.child("shopAddress").getValue(String.class);
                        String shopDes = shopSnapshot.child("shopDes").getValue(String.class);
                        String shopImage = shopSnapshot.child("shopImage").getValue(String.class);
                        String shopName = shopSnapshot.child("shopName").getValue(String.class);
                        String shopOwnerId = shopSnapshot.child("shopOwnerId").getValue(String.class);
                        String shopUid = shopSnapshot.child("shopUid").getValue(String.class);

                        HashMap<String,Integer> shopSetAppointment = new HashMap<>();
                        for(DataSnapshot appoints : shopSnapshot.child("shopSetAppointment").getChildren()){
                            shopSetAppointment.put(appoints.getKey(),appoints.getValue(Integer.class));
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


                        HashMap<String, List<WeekdayWorkTime>> defaultWorkTimeEachDay = new HashMap<>();
                        for(DataSnapshot day : shopSnapshot.child("shopDefaultAvailableTime").getChildren()){
                            List<WeekdayWorkTime> timesList = new ArrayList<>();
                            for(DataSnapshot times : day.getChildren()){
                                int startTime = times.child("startTime").getValue(Integer.class);
                                int endTime = times.child("endTime").getValue(Integer.class);
                                // Create a WeekdayWorkTime object with the extracted startTime and endTime
                                WeekdayWorkTime workTime = new WeekdayWorkTime(startTime, endTime);
                                // Add the workTime object to the timesList
                                timesList.add(workTime);
                            }
                            // Add the timesList to the defaultWorkTimeEachDay HashMap with the day key
                            defaultWorkTimeEachDay.put(day.getKey(), timesList);
                        }

                        ownedShopList.add(new ShopModel(shopUid,shopName,shopAddress,
                                shopImage,shopDes,shopOwnerId,shopSetAppointment,shopTags,
                                shopLinks,defaultWorkTimeEachDay));
                        Log.d(TAG,"ownedShopList: " + ownedShopList.toString());
                        ownedShopAdapter.notifyDataSetChanged();


                    }catch(Exception e){
                        Log.d(TAG, "Error fetching value: " + e.getMessage());
                    }
//                    ownedShopList.add(shopSnapshot.getValue(ShopModel.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addSubscription(DatabaseReference rRef, ArrayList<ShopModel> dataset,
                                ShopAdapter adapter, String uid){
        myRef = database.getReference("stores").child(uid).child("storeId");

        rRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataset.clear(); // Clear the existing data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {




//                    ShopModel shopModel = new ShopModel(shopName, shopAddress,shopImage,shopDes
//                            ,shopTags, shopLinks,);
//                    dataset.add(shopModel);
                }
                // Notify the adapter that the data set has changed
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

    }

    public String getUserUid() {
        return userUid;
    }

    public void removeSubscription(){

    }

    @Override
    public void onItemClick(int position, ArrayList<ShopModel> shopList) {
        Intent clickOnShopFromRes = new Intent(MainActivity.this,ShopInfoActivity.class);
        if(shopList.get(position).getShopOwnerId().equals(userUid)){
            clickOnShopFromRes.putExtra("isOwned", 1);
        }else{
            clickOnShopFromRes.putExtra("isOwned", 0);
        }


//            HashMap<String,List<WeekdayWorkTime>> temp = new HashMap<>();
//            temp = ownedShopList.get(position).getShopDefaultAvailableTime();
        ArrayList<List<WeekdayWorkTime>> timeList = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();
         for(String day : shopList.get(position).getShopDefaultAvailableTime().keySet()){
             days.add(day);
             timeList.add(shopList.get(position).getShopDefaultAvailableTime().get(day));
         }
        HashMap<String, List<WeekdayWorkTime>> shopDefaultAvailableTime = shopList.get(position).getShopDefaultAvailableTime();
        if (shopDefaultAvailableTime != null) {
            clickOnShopFromRes.putExtra("shopDefaultAvailableTime", shopDefaultAvailableTime);
        }

        HashMap<String,Integer> shopAppointsTypes = shopList.get(position).getShopSetAppointment();
        if(shopAppointsTypes != null){
            clickOnShopFromRes.putExtra("shopSetAppointment", shopAppointsTypes);
        }

        clickOnShopFromRes.putExtra("daysString", days);
        clickOnShopFromRes.putExtra("timeList", timeList);

        clickOnShopFromRes.putExtra("shop",  shopList.get(position));

        startActivity(clickOnShopFromRes);
    }
}