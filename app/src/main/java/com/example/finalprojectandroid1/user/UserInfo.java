package com.example.finalprojectandroid1.user;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserInfo implements Parcelable {


    private String userEmail;
    private String userPassword;
    private String userPhone;
    private String userName;
    private String userCity;

    public UserInfo(){

    }

    public UserInfo(String userEmail, String userPassword, String userPhone,
                    String userName, String userCity) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.userName = userName;
        this.userCity = userCity;

    }

    protected UserInfo(Parcel in) {
        userEmail = in.readString();
        userPassword = in.readString();
        userPhone = in.readString();
        userName = in.readString();
        userCity = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userName='" + userName + '\'' +
                ", userCity='" + userCity + '\'' +
                '}';
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserPhone() {
        return userPhone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(userEmail);
        dest.writeString(userPassword);
        dest.writeString(userPhone);
        dest.writeString(userName);
        dest.writeString(userCity);
    }
}
