package com.example.finalprojectandroid1.fragments.signInLogIn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.LoginSignInActivity;
import com.example.finalprojectandroid1.shop.ShopModel;
import com.example.finalprojectandroid1.user.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogIn#newInstance} factory method to
 * create an instance of this fragment.
 */

public class LogIn extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LogIn() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogIn.
     */
    // TODO: Rename and change types and number of parameters
    public static LogIn newInstance(String param1, String param2) {
        LogIn fragment = new LogIn();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // User will log in to an account

    private String TAG = "Login";

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        LoginSignInActivity loginSignInActivity = (LoginSignInActivity) getActivity();

        EditText emailInput = view.findViewById(R.id.emailLoginInput);
        EditText passwordInput = view.findViewById(R.id.passwordLoginInput);

        Button submit = view.findViewById(R.id.submitLogin);

        // Warning text for error when user enter false information
        TextView inputWarning = view.findViewById(R.id.loginInputWarning);

        ProgressBar progressBar = view.findViewById(R.id.progressBarLogin);

        mAuth = loginSignInActivity.getmAuth();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                // Checking to see if one of the input is empty and notifying the user accordingly
                if(email.isEmpty() || password.isEmpty()){
                    progressBar.setVisibility(View.GONE);
                    inputWarning.setText("נא למלא את כל השדות.");
                    inputWarning.setVisibility(View.VISIBLE);
                }else{
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Log in successful
                                    if (task.isSuccessful()) {

                                        inputWarning.setVisibility(View.GONE);
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String uid = user.getUid();

                                        // Locating user in database
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference("users")
                                                .child(uid).child("userAuth");

                                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    UserInfo userInfo = snapshot.getValue(UserInfo.class);
                                                    if (userInfo != null) {
                                                        String name = userInfo.getUserName();
                                                        Log.d(TAG, "User name: " + name);
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("userUid", uid);
                                                        bundle.putParcelable("user",userInfo);
                                                        // Navigating user uid and user information to Main Activity
                                                        Navigation.findNavController(view).navigate(R.id.action_logIn_to_mainActivity, bundle);
                                                    }
                                                } else {
                                                    Log.d(TAG, "User data not found");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Log.w(TAG, "Failed to read value.", error.toException());
                                            }
                                        });
                                    }else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(requireContext(), "שגיאה התחברות למערכת", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });



        return view;
    }
}