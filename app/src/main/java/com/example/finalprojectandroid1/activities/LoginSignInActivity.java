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

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.fragments.signInLogIn.LogIn;
import com.example.finalprojectandroid1.user.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginSignInActivity extends AppCompatActivity {

    private static final String TAG = "lLoginSignInActivity";
    private FirebaseAuth mAuth;
    UserInfo user;
    ProgressBar progressBar;
   View fragmentContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_sign_in);
        progressBar = findViewById(R.id.progressBarLoginSigninActivity);
        fragmentContainer = findViewById(R.id.fragmentContainerLoginSIgninActivity);


        user  = new UserInfo();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currUser = mAuth.getCurrentUser();


//        String curUserEmail = mAuth.getCurrentUser().getEmail().toString();
        if(currUser != null){
            String curUserUid = mAuth.getCurrentUser().getUid().toString();
            Intent transferToMainActivity = new Intent(LoginSignInActivity.this, MainActivity.class);
            transferToMainActivity.putExtra("userUid",curUserUid);
            startActivity(transferToMainActivity);
            finish(); // Finish the login activity

        }else{
            progressBar.setVisibility(View.GONE);
            fragmentContainer.setVisibility(View.VISIBLE);
        }

//        Log.d(TAG, "curUserEmail: " + curUserEmail);

    }

    public void getUserFromDatabase(String userUid){

        FirebaseDatabase.getInstance().getReference("users").child(userUid).child("userAuth").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    user = snapshot.getValue(UserInfo.class);
                }catch(Exception e){
                    Log.e(TAG, e.getMessage());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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