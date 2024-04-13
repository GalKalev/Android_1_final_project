package com.example.finalprojectandroid1.shop;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.finalprojectandroid1.shop.shopFragments.Address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopModel implements Parcelable {

    // Shop Model

    private String shopUid;
    private String shopName;
    private Address shopAddress;
    private String shopImage;
    private String shopDes;
    private String shopOwnerId;
    private HashMap<String, AppointmentsTimeAndPrice> shopSetAppointment;
    private ArrayList<String> shopTags;
    private List<String> shopLinks;
    HashMap<String, List<TimeRange>> shopDefaultAvailableTime;


    public ShopModel(){

    }
    public ShopModel(String shopUid, String shopName, Address shopAddress, String shopImage, String shopDes,
                     String shopOwnerId, HashMap<String, AppointmentsTimeAndPrice> shopSetAppointment,
                     ArrayList<String> shopTags, List<String> shopLinks,HashMap<String, List<TimeRange>> shopDefaultAvailableTime) {
        this.shopUid = shopUid;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopImage = shopImage;
        this.shopDes = shopDes;
        this.shopOwnerId = shopOwnerId;
        this.shopSetAppointment = shopSetAppointment;
        this.shopTags = shopTags;
        this.shopLinks = shopLinks;
        this.shopDefaultAvailableTime = shopDefaultAvailableTime;
    }


    protected ShopModel(Parcel in) {
        shopUid = in.readString();
        shopName = in.readString();
        shopAddress = in.readParcelable(Address.class.getClassLoader());
        shopImage = in.readString();
        shopDes = in.readString();
        shopOwnerId = in.readString();
        shopTags = in.createStringArrayList();
        shopLinks = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shopUid);
        dest.writeString(shopName);
        dest.writeParcelable(shopAddress, flags);
        dest.writeString(shopImage);
        dest.writeString(shopDes);
        dest.writeString(shopOwnerId);
        dest.writeStringList(shopTags);
        dest.writeStringList(shopLinks);
    }

    public static final Creator<ShopModel> CREATOR = new Creator<ShopModel>() {
        @Override
        public ShopModel createFromParcel(Parcel in) {
            return new ShopModel(in);
        }

        @Override
        public ShopModel[] newArray(int size) {
            return new ShopModel[size];
        }
    };

    public String getShopUid() {
        return shopUid;
    }

    public String getShopOwnerId() {
        return shopOwnerId;
    }

    public HashMap<String, List<TimeRange>> getShopDefaultAvailableTime() {
        return shopDefaultAvailableTime;
    }

    public void setShopDefaultAvailableTime(HashMap<String, List<TimeRange>> shopDefaultAvailableTime) {
        this.shopDefaultAvailableTime = shopDefaultAvailableTime;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Address getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(Address shopAddress) {
        this.shopAddress = shopAddress;
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

    public HashMap<String, AppointmentsTimeAndPrice> getShopSetAppointment() {
        return shopSetAppointment;
    }

    public void setShopSetAppointment(HashMap<String, AppointmentsTimeAndPrice> shopSetAppointment) {
        this.shopSetAppointment = shopSetAppointment;
    }

    public ArrayList<String> getShopTags() {
        return shopTags;
    }

    public void setShopTags(ArrayList<String> shopTags) {
        this.shopTags = shopTags;
    }

    public List<String> getShopLinks() {
        return shopLinks;
    }

    public void setShopLinks(List<String> shopLinks) {
        this.shopLinks = shopLinks;
    }

    @Override
    public String toString() {
        return "ShopModel{" +
                "_id='" + shopUid + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopAddress='" + shopAddress + '\'' +
                ", shopImage='" + shopImage + '\'' +
                ", shopDes='" + shopDes + '\'' +
                ", shopOwnerId='" + shopOwnerId + '\'' +
                ", shopSetAppointment=" + shopSetAppointment +
                ", shopTags=" + shopTags +
                ", shopLinks=" + shopLinks +
                ", shopDefaultAvailableTime=" + shopDefaultAvailableTime.toString() +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
