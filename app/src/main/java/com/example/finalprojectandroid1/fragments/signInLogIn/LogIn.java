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
import android.widget.TextView;

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
        TextView inputWarning = view.findViewById(R.id.loginInputWarning);

        mAuth = loginSignInActivity.getmAuth();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                if(email.isEmpty() || password.isEmpty()){
                    inputWarning.setText("נא למלא את כל השדות.");
                    inputWarning.setVisibility(View.VISIBLE);
                }else{
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        inputWarning.setVisibility(View.GONE);
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String uid = user.getUid();

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference("users").child(uid);

                                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    UserInfo userInfo = snapshot.getValue(UserInfo.class);
                                                    if (userInfo != null) {
                                                        String name = userInfo.getUserName();
                                                        ArrayList<ShopModel> dataset = new ArrayList<ShopModel>();

                                                        for (DataSnapshot itemSnapshot : snapshot.child("shoppingList").getChildren()) {
                                                            // Iterate through each child node under "shoppingList"
                                                            String shopName = itemSnapshot.child("shopName").getValue(String.class);
                                                            String shopAdress = itemSnapshot.child("shopAdress").getValue(String.class);
                                                            int shopImage = itemSnapshot.child("shopImage").getValue(Integer.class);
                                                            String shopDes = itemSnapshot.child("shopDes").getValue(String.class);
                                                            String shopTags = itemSnapshot.child("shopTags").getValue(String.class);
                                                            String shopLinks = itemSnapshot.child("shopLinks").getValue(String.class);

                                                            dataset.add(new ShopModel( shopName, shopAdress, shopImage,shopDes, shopTags, shopLinks, dataset.size()));

                                                        }

                                                        Log.d(TAG, "User name: " + name);
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("name", name);
                                                        bundle.putString("uid", uid);
                                                        bundle.putSerializable("dataset",dataset);
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
//                                    Toast.makeText(requireContext(), "login fail", Toast.LENGTH_SHORT).show();
                                        inputWarning.setVisibility(View.VISIBLE);
                                        inputWarning.setText("נא למלא את השדות נכון.");
                                    }
                                }
                            });
                }
            }
        });



        return view;
    }
}