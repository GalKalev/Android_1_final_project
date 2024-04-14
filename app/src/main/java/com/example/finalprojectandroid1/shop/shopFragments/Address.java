package com.example.finalprojectandroid1.shop.shopFragments;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

public class Address implements Parcelable {

    // Class for setting the shop's address

    String street;
    int houseNum;
    int floor;
    String city;

    public Address() {
    }

    public Address(String street, int houseNum, int floor, String city) {
        this.street = street;
        this.houseNum = houseNum;
        this.floor = floor;
        this.city = city;
    }

    protected Address(Parcel in) {
        street = in.readString();
        houseNum = in.readInt();
        floor = in.readInt();
        city = in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(int houseNum) {
        this.houseNum = houseNum;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(street);
        dest.writeInt(houseNum);
        dest.writeInt(floor);
        dest.writeString(city);
    }

    public String presentAddress(){
        return street + " דירה " + houseNum + " קומה " + floor + ", " + city;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", houseNum=" + houseNum +
                ", floor=" + floor +
                ", city='" + city + '\'' +
                '}';
    }


}
