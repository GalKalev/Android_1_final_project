package com.example.finalprojectandroid1.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.fragments.signInLogIn.LogIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;

public class LoginSignInActivity extends AppCompatActivity {

    private static final String TAG = "lLoginSignInActivity";
    private FirebaseAuth mAuth;

    private UserInfo user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_sign_in);

        mAuth = FirebaseAuth.getInstance();
        String curUserEmail = mAuth.getCurrentUser().getEmail().toString();
        if(!curUserEmail.isEmpty()){
            Intent transferToMainActivity = new Intent(LoginSignInActivity.this, MainActivity.class);
            String curUserUid = mAuth.getCurrentUser().getUid().toString();
            transferToMainActivity.putExtra("curUserUid", curUserUid);

//            transferToMainActivity.putExtra("curUserName",curUserName);
//            startActivity(new Intent(LoginSignInActivity.this, MainActivity.class));
            startActivity(transferToMainActivity);
            finish(); // Finish the login activity
        }

        Log.d(TAG, "curUserEmail: " + curUserEmail);

    }


    public UserInfo getAuthUserInfo(){
        return user;
    }

    public void setAuthUserInfo(UserInfo user){
        this.user = user;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }
//    public void getUser
}