package com.travelspot.travelspot;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.common.SignInButton;

public class FirstFragment extends Fragment {

    Button signUpBtn;
    SignInButton googleSingIn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_first, container, false);
        signUpBtn = v.findViewById(R.id.signUpEmail);

        googleSingIn = v.findViewById(R.id.signIn);
        TextView textview = (TextView) googleSingIn.getChildAt(0);
        textview.setText("Sign in with Google");

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                        R.anim.fade_out).replace(R.id.fragment_container,new SignupFragment()).commit();
            }
        });

        return v;
    }
}