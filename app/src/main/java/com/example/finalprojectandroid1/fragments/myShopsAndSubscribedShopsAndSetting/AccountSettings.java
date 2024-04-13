package com.example.finalprojectandroid1.fragments.myShopsAndSubscribedShopsAndSetting;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectandroid1.GlobalMembers;
import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.LoginSignInActivity;
import com.example.finalprojectandroid1.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountSettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountSettings extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountSettings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountSettings.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountSettings newInstance(String param1, String param2) {
        AccountSettings fragment = new AccountSettings();
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


    // User can sign out here
    String TAG = "AccountSettings";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);

        Button logoutButton = view.findViewById(R.id.logoutButton);
        MainActivity mainActivity = (MainActivity)getActivity();
        TextView userName = view.findViewById(R.id.userNameAccountSetting);
        try{
            userName.setText(mainActivity.getUser().getUserName().toString());

        }catch(NullPointerException e){
            Log.e(TAG, "user null: " + e.getMessage());
        }catch(Exception e){
            Log.e(TAG, "user error: " + e.getMessage());
        }

        // Signing out button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(getContext());

                // Dialog to confirm signing out
                dialog.setContentView(R.layout.card_cancel_confirmation_dialog);
                TextView cancelCheck = dialog.findViewById(R.id.showCancelTextCancelConfirmDialog);
                Button backBtn = dialog.findViewById(R.id.backButtonCancelDialog);
                Button confirmBtn = dialog.findViewById(R.id.confirmCancellationButtonDialog);
                cancelCheck.setText("להתנתק?");
                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                confirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(mainActivity, LoginSignInActivity.class);
                            mainActivity.finish();
                            startActivity(intent);
                        }catch(Exception e){
                            Log.e(TAG,"Error signing out: " + e.getMessage());
                            Toast.makeText(mainActivity, GlobalMembers.errorToastMessage, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.show();


            }
        });

        return view;
    }
}