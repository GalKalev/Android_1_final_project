package com.example.finalprojectandroid1.shop;

import android.widget.RadioGroup;

import java.util.Calendar;
import java.util.HashMap;

public class ShopModel {

    private String shopName;
    private String shopAddress;
    private int shopImage;
    private String shopDes;
    private String shopTags;
    private String shopLinks;
    private HashMap<String,String[]> shopDefaultAvailableTime;
    private HashMap<String,String> shopSetAppointment;
    private int _id;


    public ShopModel(){

    }

    public ShopModel(String shopName, String shopAddress, int shopImage, String shopDes,
                     String shopTags, String shopLinks) {
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopImage = shopImage;
        this.shopDes = shopDes;
        this.shopTags = shopTags;
        this.shopLinks = shopLinks;
    }

    public ShopModel(String shopName, String shopAddress, int shopImage, String shopDes,
                     String shopTags, String shopLinks,
                     HashMap<String, String[]> shopDefaultAvailableTime, HashMap<String,String> shopSetAppointment,
                     int _id) {
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopImage = shopImage;
        this.shopDes = shopDes;
        this.shopTags = shopTags;
        this.shopLinks = shopLinks;
        this.shopDefaultAvailableTime = shopDefaultAvailableTime;
        this.shopSetAppointment = shopSetAppointment;
        this._id = _id;
    }

    public HashMap<String, String[]> getShopDefaultAvailableTime() {
        return shopDefaultAvailableTime;
    }

    public void setShopDefaultAvailableTime(HashMap<String, String[]> shopDefaultAvailableTime) {
        this.shopDefaultAvailableTime = shopDefaultAvailableTime;
    }

    public HashMap<String, String> getShopSetAppointment() {
        return shopSetAppointment;
    }

    public void setShopSetAppointment(HashMap<String, String> shopSetAppointment) {
        this.shopSetAppointment = shopSetAppointment;
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

    public int get_id() {
        return _id;
    }
}
