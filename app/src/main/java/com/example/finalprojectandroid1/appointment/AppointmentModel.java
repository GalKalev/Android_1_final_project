package com.example.finalprojectandroid1.appointment;

import com.example.finalprojectandroid1.shop.TimeRange;

import java.util.ArrayList;

public class AppointmentModel {
    String shopName;
    String shopUid;
    String shopAddress;
    String userUid;
    String userName;

    TimeRange time;
    String date;
    ArrayList<String> appointmentTypes;
    String price;

    public AppointmentModel() {
    }

    //for user database
    public AppointmentModel(String shopName, String shopAddress,String shopUid, TimeRange time, String date, ArrayList<String> appointmentTypes, String price) {
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopUid = shopUid;
        this.time = time;
        this.date = date;
        this.appointmentTypes = appointmentTypes;
        this.price = price;
    }

    //for shop database
    public AppointmentModel(String userUid,String userName, TimeRange time, String date, ArrayList<String> appointmentTypes) {
        this.userUid = userUid;
        this.userName = userName;
        this.time = time;
        this.date = date;
        this.appointmentTypes = appointmentTypes;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public TimeRange getTime() {
        return time;
    }

    public void setTime(TimeRange time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getAppointmentTypes() {
        return appointmentTypes;
    }

    public void setAppointmentTypes(ArrayList<String> appointmentTypes) {
        this.appointmentTypes = appointmentTypes;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShopUid() {
        return shopUid;
    }

    public void setShopUid(String shopUid) {
        this.shopUid = shopUid;
    }
}
