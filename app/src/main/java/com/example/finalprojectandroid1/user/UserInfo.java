package com.example.finalprojectandroid1.user;


import java.util.HashMap;
import java.util.Map;

public class UserInfo {
    private static Map<String, Map.Entry<String,String>> usersInfo = new HashMap<>();

    private String userEmail;
    private String userPassword;
    private String userPhone;
    private String userName;

    public UserInfo(){

    }

    public UserInfo(String userEmail, String userPassword, String userPhone, String userName) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.userName = userName;

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


    @Override
    public String toString() {
        return "UserInfo{" +

                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userPhone='" + userPhone + '\'' +
                '}';
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserPhone() {
        return userPhone;
    }

}
