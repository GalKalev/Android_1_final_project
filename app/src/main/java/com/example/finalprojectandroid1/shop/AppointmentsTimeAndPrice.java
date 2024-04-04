package com.example.finalprojectandroid1.shop;

import android.os.Parcel;
import android.os.Parcelable;

public class AppointmentsTimeAndPrice implements Parcelable {
    int time;
    int price;

    public AppointmentsTimeAndPrice() {
    }

    public AppointmentsTimeAndPrice(int time, int price) {
        this.time = time;
        this.price = price;
    }

    protected AppointmentsTimeAndPrice(Parcel in) {
        time = in.readInt();
        price = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(time);
        dest.writeInt(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AppointmentsTimeAndPrice> CREATOR = new Creator<AppointmentsTimeAndPrice>() {
        @Override
        public AppointmentsTimeAndPrice createFromParcel(Parcel in) {
            return new AppointmentsTimeAndPrice(in);
        }

        @Override
        public AppointmentsTimeAndPrice[] newArray(int size) {
            return new AppointmentsTimeAndPrice[size];
        }
    };

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
