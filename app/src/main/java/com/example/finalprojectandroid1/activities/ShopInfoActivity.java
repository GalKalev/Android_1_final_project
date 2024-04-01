package com.example.finalprojectandroid1.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.example.finalprojectandroid1.shop.WeekdayWorkTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopInfoActivity extends AppCompatActivity {


    String TAG = "sShopInfoActivity";
    ShopModel shop;
    HashMap<String, List<WeekdayWorkTime>> shopDefaultAvailableTime = new HashMap<>();
    HashMap<String, Integer> shopAppointsTypes = new HashMap<>();
    int shopPosition;
    ArrayList<List<WeekdayWorkTime>> timeList = new ArrayList<>();
    ArrayList<String> days = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);

        TextView shopName = findViewById(R.id.shopNameShopActivity);
        TextView shopAddress = findViewById(R.id.shopAddressShopActivity);
        ImageView shopImage = findViewById(R.id.shopImageShopActivity);

        Bundle fromMainActivity = getIntent().getExtras();
        if(!fromMainActivity.isEmpty()){
            int isOwner = fromMainActivity.getInt("isOwner");

            shop = getIntent().getParcelableExtra("shop");
            shopDefaultAvailableTime = (HashMap<String, List<WeekdayWorkTime>>) getIntent().getSerializableExtra("shopDefaultAvailableTime");
            shopAppointsTypes = (HashMap<String, Integer>) getIntent().getSerializableExtra("shopSetAppointment");
            shopPosition = fromMainActivity.getInt("shopPosition");

            shopName.setText(shop.getShopName().toString());
            shopAddress.setText(shop.getShopAddress().toString());
            String imageUrl = shop.getShopImage().toString();
            Glide.with(this).load(imageUrl).into(shopImage);
            if(isOwner != 1){
                Log.d(TAG,"not owner of shop");
//                NavController navController = Navigation.findNavController(this, R.id.);
            }
        }else{
            Log.e(TAG, "Problem with getting from main activity");

        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(ShopInfoActivity.this, MainActivity.class);
                i.putExtra("updateShop",1);
                i.putExtra("curUserUid", shop.getShopOwnerId());

                startActivity(i);
            }
        });

    }

    public ShopModel getShop() {
        return shop;
    }

    public void setShop(ShopModel shop) {
        this.shop = shop;
    }

    public HashMap<String, List<WeekdayWorkTime>> getShopDefaultAvailableTime() {
        return shopDefaultAvailableTime;
    }

    public void setShopDefaultAvailableTime(HashMap<String, List<WeekdayWorkTime>> shopDefaultAvailableTime) {
        this.shopDefaultAvailableTime = shopDefaultAvailableTime;
    }

    public HashMap<String, Integer> getShopAppointsTypes() {
        return shopAppointsTypes;
    }

    public int getShopPosition() {
        return shopPosition;
    }


}