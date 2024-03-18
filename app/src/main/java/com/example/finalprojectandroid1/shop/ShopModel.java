package com.example.finalprojectandroid1.shop;

import android.widget.RadioGroup;

import java.util.Calendar;

public class ShopModel {

    private String shopName;
    private String shopAddress;
    private int shopImage;
    private String shopDes;
    private String shopTags;
    private String shopLinks;
    private Calendar shopCalendar;//change? see shop adapter
    private RadioGroup shopTime;// change?? see shop adapter
    private int _id;


    public ShopModel(){

    }
    public ShopModel(String shopName, String shopAddress, int shopImage,
                     String shopDes, String shopTags, String shopLinks , int _id ) {
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopImage = shopImage;
        this.shopDes = shopDes;
        this.shopTags = shopTags;
        this.shopLinks = shopLinks;
        this._id = _id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getShopImage() {
        return shopImage;
    }

    public void setShopImage(int shopImage) {
        this.shopImage = shopImage;
    }

    public String getShopDes() {
        return shopDes;
    }

    public void setShopDes(String shopDes) {
        this.shopDes = shopDes;
    }

    public String getShopTags() {
        return shopTags;
    }

    public void setShopTags(String shopTags) {
        this.shopTags = shopTags;
    }

    public String getShopLinks() {
        return shopLinks;
    }

    public void setShopLinks(String shopLinks) {
        this.shopLinks = shopLinks;
    }
    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public Calendar getShopCalendar() {
        return shopCalendar;
    }

    public void setShopCalendar(Calendar shopCalendar) {
        this.shopCalendar = shopCalendar;
    }

    public RadioGroup getShopTime() {
        return shopTime;
    }

    public void setShopTime(RadioGroup shopTime) {
        this.shopTime = shopTime;
    }


    public int get_id() {
        return _id;
    }
}
