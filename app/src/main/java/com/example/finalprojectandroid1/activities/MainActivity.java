package com.example.finalprojectandroid1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.util.Log;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.databinding.ActivityMainBinding;
import com.example.finalprojectandroid1.fragments.myCalendar.PersonalCalendar;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShops.MyShopsAndInfo;
import com.example.finalprojectandroid1.fragments.searchShops.SearchShops;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    private String TAG = "mMain Activity";
    ActivityMainBinding binding;
    DatabaseReference myRef;

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
        replaceFragment(new PersonalCalendar());
//
        binding.bottomNavViewMainActivity.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.calendar_icon_bottom_nav) {
                replaceFragment(new PersonalCalendar());
            } else if (item.getItemId() == R.id.shops_icon_bottom_nav) {
                replaceFragment(new SearchShops());
            } else if (item.getItemId() == R.id.myShops_icon_bottom_nav) {
                replaceFragment(new MyShopsAndInfo());
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragnentTransaction = fragmentManager.beginTransaction();
        fragnentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragnentTransaction.commit();
    }
}