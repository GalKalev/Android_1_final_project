package com.example.finalprojectandroid1.fragments.signInLogIn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectandroid1.GlobalMembers;
import com.example.finalprojectandroid1.R;
import com.example.finalprojectandroid1.activities.LoginSignInActivity;
import com.example.finalprojectandroid1.user.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignIn extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignIn() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignIn.
     */
    // TODO: Rename and change types and number of parameters
    public static SignIn newInstance(String param1, String param2) {
        SignIn fragment = new SignIn();
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

    private String TAG = "Signin";

    private FirebaseAuth mAuth;
    String city;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        LoginSignInActivity loginSignInActivity = (LoginSignInActivity) getActivity();

        EditText emailInput = view.findViewById(R.id.emailSigninInput);
        EditText passwordInput = view.findViewById(R.id.passwordSigninInput);
        EditText nameInput = view.findViewById(R.id.nameSigninInput);
        EditText phoneInput = view.findViewById(R.id.phoneSigninInput);

        Button submit = (Button) view.findViewById(R.id.submitSignin);

        Spinner citiesSpinner = view.findViewById(R.id.citiesSpinnerSignin);
        String[] citiesList = GlobalMembers.citiesList;
        ArrayAdapter<String> citiesSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, citiesList);
        citiesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citiesSpinner.setAdapter(citiesSpinnerAdapter);

        TextView inputWarning = view.findViewById(R.id.signinInputWarning);

        mAuth = loginSignInActivity.getmAuth();

        citiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = (String) parent.getItemAtPosition(position);
                if(!selectedCity.equals("בחר עיר")){
                    city = selectedCity.trim();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String name = nameInput.getText().toString().trim();
                String phone = phoneInput.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || phone.isEmpty() || name.isEmpty() || city.isEmpty()) {
                    inputWarning.setVisibility(View.VISIBLE);
                    inputWarning.setText("נא למלא את כל השדות.");
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                inputWarning.setVisibility(View.GONE);
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(getContext(), "Signin successful", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                String uid = user.getUid();

                                UserInfo userInfo = new UserInfo(email, password, phone, name, city);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("users").child(uid).child("userAuth");

                                myRef.setValue(userInfo);

                                Bundle userInfoBundle = new Bundle();
                                userInfoBundle.putParcelable("user", userInfo);
                                userInfoBundle.putString("userUid", uid);
                                Navigation.findNavController(view).navigate(R.id.action_signIn_to_mainActivity, userInfoBundle);
                            } else {
                                Exception exception = task.getException();
                                if (exception instanceof FirebaseAuthUserCollisionException) {
                                    Log.w(TAG, "createUserWithEmailAndPassword:failure - Email already exists", exception);
                                    inputWarning.setVisibility(View.VISIBLE);
                                    inputWarning.setText("המייל שהזנת כבר קיים.");
                                } else {
                                    Log.w(TAG, "createUserWithEmailAndPassword:failure", exception);
                                    inputWarning.setVisibility(View.VISIBLE);
                                    inputWarning.setText("נא למלא את כל השדות.");
                                }
                            }
                        }
                    });
                }
            }
        });



        return view;
    }
}