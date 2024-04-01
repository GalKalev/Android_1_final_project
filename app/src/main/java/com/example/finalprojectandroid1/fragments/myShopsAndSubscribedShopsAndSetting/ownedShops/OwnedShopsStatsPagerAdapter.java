package com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.AccountSettings;
import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.SubscibedPersonalShops;

public class OwnedShopsStatsPagerAdapter extends FragmentStateAdapter {

    public OwnedShopsStatsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new OwnedShopInfoTab();
            case 1:
                return new OwnedShopAppointmentsTab();
            default:
                return new OwnedShopInfoTab();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}