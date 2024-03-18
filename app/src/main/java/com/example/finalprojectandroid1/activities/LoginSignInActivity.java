package com.example.finalprojectandroid1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.fragments.signInLogIn.LogIn;
import com.google.firebase.auth.FirebaseAuth;

public class LoginSignInActivity extends AppCompatActivity {

    private static final String TAG = "lLoginSignInActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_sign_in);

        mAuth = FirebaseAuth.getInstance();
        String curUser = mAuth.getCurrentUser().getEmail().toString();
        if(!curUser.isEmpty()){
            startActivity(new Intent(LoginSignInActivity.this, MainActivity.class));
            finish(); // Finish the login activity
        }

        Log.d(TAG, "curUser: " + curUser);




    }




    public FirebaseAuth getmAuth() {
        return mAuth;
    }
//    public void getUser
}