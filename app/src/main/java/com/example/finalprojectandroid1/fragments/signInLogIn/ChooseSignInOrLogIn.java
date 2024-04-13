package com.example.finalprojectandroid1.fragments.signInLogIn;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.finalprojectandroid1.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChooseSignInOrLogIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseSignInOrLogIn extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChooseSignInOrLogIn() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChooseSignInOrLogIn.
     */
    // TODO: Rename and change types and number of parameters
    public static ChooseSignInOrLogIn newInstance(String param1, String param2) {
        ChooseSignInOrLogIn fragment = new ChooseSignInOrLogIn();
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

    // Fragment containing two buttons "הרשמה" or "התחברות",
    // each navigate to the a different fragment

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_sign_in_or_log_in, container, false);

        Button loginBtn = (Button) view.findViewById(R.id.loginButton);
        Button signinBtn = (Button) view.findViewById(R.id.signinButton);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigate to LogIn fragment
                Navigation.findNavController(view).navigate(R.id.action_chooseSignInOrLogIn_to_logIn);
            }
        });

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigate to SignIn fragment
                Navigation.findNavController(view).navigate(R.id.action_chooseSignInOrLogIn_to_signIn);
            }
        });


        return view;
    }
}