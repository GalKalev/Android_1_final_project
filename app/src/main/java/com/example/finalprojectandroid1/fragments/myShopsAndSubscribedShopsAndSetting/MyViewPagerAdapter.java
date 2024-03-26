package com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting.ownedShops.MyOwnedShops;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new SubscibedPersonalShops();
            case 1:
                return new MyOwnedShops();
            case 2:
                return new AccountSettings();
            default:
                return new SubscibedPersonalShops();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
