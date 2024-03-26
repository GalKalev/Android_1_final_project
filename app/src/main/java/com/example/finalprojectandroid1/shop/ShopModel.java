package com.example.finalprojectandroid1.shop;

import android.widget.RadioGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ShopModel {

    private String shopOwnerId;
    private String shopName;
    private String shopAddress;
    private String shopImage;
    private String shopDes;
    private List<String> shopTags;
    private  List<String[]> shopLinks;
    private HashMap<String, List<Integer[]>> shopDefaultAvailableTime;
    private HashMap<String,Integer> shopSetAppointment;
    private HashMap<String,Integer> shopUserTracking;
    private int _id;


    public ShopModel(){

    }

//    public ShopModel(String shopName, String shopAddress, String shopImage, String shopDes,
//                     String shopTags, String shopLinks) {
//        this.shopName = shopName;
//        this.shopAddress = shopAddress;
//        this.shopImage = shopImage;
//        this.shopDes = shopDes;
//        this.shopTags = shopTags;
//        this.shopLinks = shopLinks;
//    }

    public ShopModel(String shopOwnerId,String shopName, String shopAddress, String shopImage, String shopDes,
                     ArrayList<String> shopTags, ArrayList<String[]> shopLinks,
                     HashMap<String, List<Integer[]>> shopDefaultAvailableTime, HashMap<String,Integer> shopSetAppointment,
                     int _id) {
        this.shopOwnerId = shopOwnerId;
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

    public HashMap<String, List<Integer[]>> getShopDefaultAvailableTime() {
        return shopDefaultAvailableTime;
    }

    public void setShopDefaultAvailableTime(HashMap<String, List<Integer[]>> shopDefaultAvailableTime) {
        this.shopDefaultAvailableTime = shopDefaultAvailableTime;
    }

    public HashMap<String, Integer> getShopSetAppointment() {
        return shopSetAppointment;
    }

    public void setShopSetAppointment(HashMap<String, Integer> shopSetAppointment) {
        this.shopSetAppointment = shopSetAppointment;
    }


    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getShopDes() {
        return shopDes;
    }

    public void setShopDes(String shopDes) {
        this.shopDes = shopDes;
    }

    public List<String> getShopTags() {
        return shopTags;
    }

    public void setShopTags(List<String> shopTags) {
        this.shopTags = shopTags;
    }

    public List<String[]> getShopLinks() {
        return shopLinks;
    }

    public void setShopLinks(List<String[]> shopLinks) {
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

    public String getShopOwnerId() {
        return shopOwnerId;
    }

    @Override
    public String toString() {
        return "ShopModel{" +
                "shopOwnerId='" + shopOwnerId + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopAddress='" + shopAddress + '\'' +
                ", shopImage='" + shopImage + '\'' +
                ", shopDes='" + shopDes + '\'' +
                ", shopTags=" + shopTags +
                ", shopLinks=" + shopLinks +
                ", shopDefaultAvailableTime=" + shopDefaultAvailableTime +
                ", shopUserTracking=" + shopUserTracking +
                ", _id=" + _id +
                '}';
    }
}
