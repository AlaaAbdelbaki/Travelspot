package com.travelspot.travelspot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;


public class LoginFragment extends Fragment {

    TextInputEditText emailInput;
    TextInputEditText passwordInput;

    CheckBox rememberMe;

    Button login;

    MaterialTextView signup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        emailInput = v.findViewById(R.id.loginEmailInput);
        passwordInput = v.findViewById(R.id.loginPasswordInput);

        rememberMe = v.findViewById(R.id.rememberMe);

        login = v.findViewById(R.id.loginBtn);

        signup = v.findViewById(R.id.signUp);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                        R.anim.fade_out).replace(R.id.fragment_container,new SignupFragment()).commit();
            }
        });

        return v;
    }
}