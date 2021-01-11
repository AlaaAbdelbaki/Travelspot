package com.travelspot.travelspot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.travelspot.travelspot.Models.ServicesClient;
import com.travelspot.travelspot.Models.User;
import com.travelspot.travelspot.Models.UserServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignupFragment extends Fragment {

    TextInputEditText nameInput;
    TextInputEditText lastNameInput;
    TextInputEditText emailInput;
    TextInputEditText passwordInput;

    Button signupBtn;

    MaterialTextView logIn;

    UserServices userServices = ServicesClient.getClient().create(UserServices.class);

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

        emailInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    Call<User> getUserDetails = userServices.getUser(emailInput.getText().toString());
                    getUserDetails.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.isSuccessful()){
                                if(response.body() != null){
                                    Toast.makeText(getContext(), "Email already exits", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setEmail(emailInput.getText().toString());
                user.setFirstName(nameInput.getText().toString());
                user.setLastName(lastNameInput.getText().toString());
                userServices.signup(user).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.body()){
                            Toast.makeText(getContext(), "Account created ! please login", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                                    R.anim.fade_out).replace(R.id.fragment_container,new LoginFragment()).commit();
                        }else {
                            Toast.makeText(getContext(), "Error !", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });
            }
        });

        return v;


    }
}