package com.travelspot.travelspot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;


public class SignupFragment extends Fragment {

    TextInputEditText nameInput;
    TextInputEditText lastNameInput;
    TextInputEditText emailInput;
    TextInputEditText passwordInput;

    Button signupBtn;

    MaterialTextView logIn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        nameInput = v.findViewById(R.id.nameinput);
        lastNameInput = v.findViewById(R.id.lastNameInput);
        emailInput = v.findViewById(R.id.emailInput);
        passwordInput = v.findViewById(R.id.paswordInput);

        signupBtn = v.findViewById(R.id.signUpBtn);

        logIn = v.findViewById(R.id.login);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                        R.anim.fade_out).replace(R.id.fragment_container,new LoginFragment()).commit();
            }
        });

        return v;


    }
}