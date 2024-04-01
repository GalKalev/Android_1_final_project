package com.example.finalprojectandroid1.shop;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class WeekdayWorkTime implements Parcelable {
    private int startTime;
    private int endTime;

    public WeekdayWorkTime() {
    }

    public WeekdayWorkTime(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    protected WeekdayWorkTime(Parcel in) {
        startTime = in.readInt();
        endTime = in.readInt();
    }

    public static final Creator<WeekdayWorkTime> CREATOR = new Creator<WeekdayWorkTime>() {
        @Override
        public WeekdayWorkTime createFromParcel(Parcel in) {
            return new WeekdayWorkTime(in);
        }

        @Override
        public WeekdayWorkTime[] newArray(int size) {
            return new WeekdayWorkTime[size];
        }
    };

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "WeekdayWorkTime{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(startTime);
        dest.writeInt(endTime);
    }
}
