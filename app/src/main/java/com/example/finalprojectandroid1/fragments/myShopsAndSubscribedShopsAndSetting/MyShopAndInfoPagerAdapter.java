package com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops.MyOwnedShops;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops.NavFromOwnedListToAdd;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops.OwnedShopAppointmentsTab;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops.OwnedShopInfoTab;

import java.util.Locale;

public class MyShopAndInfoPagerAdapter extends FragmentStateAdapter {
    String TAG = "MyShopAndInfoPagerAdapter";

    int fragmentNum;
    int tabsCount;

    public MyShopAndInfoPagerAdapter(@NonNull FragmentActivity fragmentActivity, int fragmentNum, int tabsCount) {
        super(fragmentActivity);
        this.fragmentNum = fragmentNum;
        this.tabsCount = tabsCount;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(fragmentNum == 0){
            switch (position){
                case 0:
                    Log.d(TAG, "case 0");
                    return new SubscibedPersonalShops();
                case 1:
                    Log.d(TAG, "case 1");
                    return new NavFromOwnedListToAdd();
                case 2:
                    Log.d(TAG, "case 2");
                    return new AccountSettings();
                default:
                    Log.d(TAG, "case default");
                    return new SubscibedPersonalShops();
            }

        }else{
            switch (position){
                case 0:
                    return new OwnedShopAppointmentsTab();
                case 1:
                    return new OwnedShopInfoTab();

                default:
                    return new OwnedShopInfoTab();
            }
        }

    }

    @Override
    public int getItemCount() {
        return tabsCount;
    }
}
