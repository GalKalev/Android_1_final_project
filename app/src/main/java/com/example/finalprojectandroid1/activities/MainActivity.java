package com.example.finalprojectandroid1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.databinding.ActivityMainBinding;
import com.example.finalprojectandroid1.fragments.myAppointments.MyUpcomingAppointments;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShops.MyShopsAndInfo;
import com.example.finalprojectandroid1.fragments.searchShops.SearchShops;
import com.example.finalprojectandroid1.shop.ShopAdapter;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String TAG = "mMain Activity";
    ActivityMainBinding binding;
    DatabaseReference myRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//
        Log.d(TAG,"inside");
//
        replaceFragment(new MyShopsAndInfo());
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

    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
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