package com.example.finalprojectandroid1.shop;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TimeRange implements Parcelable {
    private int startTime;
    private int endTime;

    public TimeRange() {
    }

    public TimeRange(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    protected TimeRange(Parcel in) {
        startTime = in.readInt();
        endTime = in.readInt();
    }

    public static final Creator<TimeRange> CREATOR = new Creator<TimeRange>() {
        @Override
        public TimeRange createFromParcel(Parcel in) {
            return new TimeRange(in);
        }

        @Override
        public TimeRange[] newArray(int size) {
            return new TimeRange[size];
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
