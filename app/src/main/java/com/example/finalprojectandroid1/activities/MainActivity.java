package com.example.finalprojectandroid1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.databinding.ActivityMainBinding;
import com.example.finalprojectandroid1.fragments.myAppointments.MyUpcomingAppointments;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.MyShopsAndInfo;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops.NavFromOwnedListToAdd;
import com.example.finalprojectandroid1.fragments.searchShops.SearchShops;
import com.example.finalprojectandroid1.shop.ShopAdapter;
import com.example.finalprojectandroid1.shop.ShopData;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = "mMainActivity";
    ActivityMainBinding binding;
    DatabaseReference myRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference imageRef ;
    ProgressBar progressBar;
    String userUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
        Bundle fromLoginSignIn = getIntent().getExtras();
        if(!fromLoginSignIn.isEmpty()){
             userUid = fromLoginSignIn.getString("curUserUid");
        }else{
            Log.e(TAG, "Problem with login or signin");
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressBar = findViewById(R.id.progressBar);
//
        Log.d(TAG,"inside");
//
        replaceFragment(new MyUpcomingAppointments());

//
        binding.bottomNavViewMainActivity.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.calendar_icon_bottom_nav) {
                replaceFragment(new MyUpcomingAppointments());
            } else if (item.getItemId() == R.id.shops_icon_bottom_nav) {
                replaceFragment(new SearchShops());
            } else if (item.getItemId() == R.id.myShops_icon_bottom_nav) {
                replaceFragment(new NavFromOwnedListToAdd());
            }
            return true;
        });

    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }

    public void addOwnedShop(String shopName, String shopAddress,  byte[] imageData,
                             String shopDes, ArrayList<String[]> links, ArrayList<String> tags,
                             HashMap<String,Integer> appointmentType,HashMap<String, List<Integer[]>> defaultWorkTimeEachDay){

        Log.d(TAG,"add shop");
        myRef = database.getReference("shops");

        // Generate a unique key for the new shop
        String uid = myRef.push().getKey();

        // Create a reference to the new shop under "stores" node
        DatabaseReference newShopRef = myRef.child(uid);



        // Upload shop image
        imageRef = storageRef.child("shops/images/" + uid + ".jpg");
        progressBar.setVisibility(View.VISIBLE);

        UploadTask uploadTask = imageRef.putBytes(imageData);
        uploadTask.addOnFailureListener(exception -> {
            progressBar.setVisibility(View.GONE);
            // Handle unsuccessful uploads
            Log.d(TAG, "Image upload failed: " + exception.getMessage());
            Toast.makeText(MainActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {

            // Get the download URL of the uploaded image
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                // Create a new ShopModel object with the shop data
//
//                ShopModel newShop = new ShopModel(userUid,shopName, shopAddress, imageUrl, shopDes,
//                        tags, links,
//                        defaultWorkTimeEachDay, appointmentType,
//                        ShopData._id.size()+1);
                ShopModel newShop = new ShopModel(userUid,shopName, shopAddress, imageUrl, shopDes,
                        tags, null,
                        null, appointmentType,
                        ShopData._id.size()+1);



                // Save the new shop data to the database
                try {
                    newShopRef.setValue(newShop).addOnSuccessListener(aVoid -> {
                        DatabaseReference workTimeRef = newShopRef.child("shopDefaultWorkTimeWeekday");
                        for (String day : defaultWorkTimeEachDay.keySet()) {
                            DatabaseReference eachDayWorkTimeRef = workTimeRef.child(day);
                            for (Integer[] i : defaultWorkTimeEachDay.get(day)) {
                                DatabaseReference timeSlotRef = eachDayWorkTimeRef.push(); // Push a new unique key
                                timeSlotRef.child("startTime").setValue(i[0]); // Save start time
                                timeSlotRef.child("endTime").setValue(i[1]); // Save end time
                            }
                        }


                        DatabaseReference linksRef = newShopRef.child("shopLinks");
                        for(String[] link : links){
                            DatabaseReference linkSlot = linksRef.push();
                            linkSlot.child("linkText").setValue(link[0]);
                            linkSlot.child("media").setValue(link[1]);
                        }
                        progressBar.setVisibility(View.GONE);
                        newShop.setShopDefaultAvailableTime(defaultWorkTimeEachDay);
                        newShop.setShopLinks(links);
                    }).addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "Error setValue: " + e.getMessage());
                    });
                    Log.d(TAG,"string: " + newShop.toString());
                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "Error setValue: " + e.getMessage());
                }

            });
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

    public void removeSubscription(){

    }

}