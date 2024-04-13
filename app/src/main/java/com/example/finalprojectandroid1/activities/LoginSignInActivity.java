package com.example.finalprojectandroid1.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.fragments.signInLogIn.LogIn;
import com.example.finalprojectandroid1.user.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//This is the activity that the starts when the application launches
public class LoginSignInActivity extends AppCompatActivity {

    private static final String TAG = "lLoginSignInActivity";
    private FirebaseAuth mAuth;

    ProgressBar progressBar;
    View fragmentContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_sign_in);
        progressBar = findViewById(R.id.progressBarLoginSigninActivity);
        fragmentContainer = findViewById(R.id.fragmentContainerLoginSIgninActivity);

        mAuth = FirebaseAuth.getInstance(); // Checking if a user is already logged in

        FirebaseUser currUser = mAuth.getCurrentUser(); // Saving the info in FirebaseUser


        if(currUser != null){ // If a user logged in
            String curUserUid = mAuth.getCurrentUser().getUid().toString(); // Saving the user uid
            Intent transferToMainActivity = new Intent(LoginSignInActivity.this, MainActivity.class);
            transferToMainActivity.putExtra("userUid",curUserUid); // Delivering the userUid to Main Activity for further uses
            startActivity(transferToMainActivity); // Starting Main Activity
            finish(); //Finish the login activity

        }else{// If a user is not logged in, ChooseLogInSignIn fragment will be visible
            progressBar.setVisibility(View.GONE);
            fragmentContainer.setVisibility(View.VISIBLE);
        }


    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }
}