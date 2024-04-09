package com.example.finalprojectandroid1.appointment;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.finalprojectandroid1.shop.TimeRange;

import java.util.ArrayList;

public class AppointmentModel implements Parcelable {
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

    protected AppointmentModel(Parcel in) {
        shopName = in.readString();
        shopUid = in.readString();
        shopAddress = in.readString();
        userUid = in.readString();
        userName = in.readString();
        time = in.readParcelable(TimeRange.class.getClassLoader());
        date = in.readString();
        appointmentTypes = in.createStringArrayList();
        price = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shopName);
        dest.writeString(shopUid);
        dest.writeString(shopAddress);
        dest.writeString(userUid);
        dest.writeString(userName);
        dest.writeParcelable(time, flags);
        dest.writeString(date);
        dest.writeStringList(appointmentTypes);
        dest.writeString(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AppointmentModel> CREATOR = new Creator<AppointmentModel>() {
        @Override
        public AppointmentModel createFromParcel(Parcel in) {
            return new AppointmentModel(in);
        }

        @Override
        public AppointmentModel[] newArray(int size) {
            return new AppointmentModel[size];
        }
    };

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String userToString() {
        return "shopName='" + shopName + '\'' +
                ", shopUid='" + shopUid + '\'' +
                ", shopAddress='" + shopAddress + '\'' +
                ", time=" + time.toString() +
                ", date='" + date + '\'' +
                ", appointmentTypes=" + appointmentTypes +
                ", price='" + price + '\'';
    }

    public String shopToString(){
        return  "userUid='" + userUid + '\'' +
                ", userName='" + userName + '\'' +
                ", time='" + time.toString() + '\'' +
                ", date='" + date + '\'' +
                ", appointmentTypes=" + appointmentTypes + '\'';

    }
}
