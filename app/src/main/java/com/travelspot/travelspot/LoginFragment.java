package com.travelspot.travelspot;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.travelspot.travelspot.Models.ServicesClient;
import com.travelspot.travelspot.Models.UserServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;


public class LoginFragment extends Fragment {

    UserServices userServices;
    TextInputEditText emailInput;
    TextInputEditText passwordInput;

    CheckBox rememberMe;

    Button login;

    MaterialTextView signup;
    SharedPreferences userStates;
    SharedPreferences.Editor userStatesEdit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        userServices = ServicesClient.getClient().create(UserServices.class);
        emailInput = v.findViewById(R.id.loginEmailInput);
        passwordInput = v.findViewById(R.id.loginPasswordInput);

        rememberMe = v.findViewById(R.id.rememberMe);

        login = v.findViewById(R.id.loginBtn);

        signup = v.findViewById(R.id.signUp);

        userStates = v.getContext().getSharedPreferences("userStates", MODE_PRIVATE);
        userStatesEdit=userStates.edit();
        if(userStates.contains("isLogged") && userStates.contains("Remembered"))
        {
            Log.i("USER LOGGED", "Allready logged in");
        }else
        {
            userStatesEdit.putString("email",null);
            userStatesEdit.putBoolean("Remembered",false);
            userStatesEdit.apply();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailInput.getText().toString().equals("") || passwordInput.getText().toString().equals(""))
                {
                    Toast.makeText(v.getContext(),"Email or Password Empty", Toast.LENGTH_SHORT).show();
                }else
                {

                    Call<Boolean> call = userServices.checkLogin(emailInput.getText().toString(),passwordInput.getText().toString());
                    call.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            boolean res = response.body();
                            if(res)
                            {
                                userStatesEdit.putString("email",emailInput.getText().toString());
                                if(rememberMe.isChecked())
                                {
                                    userStatesEdit.putBoolean("Remembered",true);
                                    userStatesEdit.apply();
                                }
                                Toast.makeText(v.getContext(),"User Exist and password correct", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }else
                                Toast.makeText(v.getContext(),"Email or Password Incorrect", Toast.LENGTH_SHORT).show();
                            Log.i("Response","Response is here ");
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {

                            call.cancel();
                        }
                    });

                }
            }
        });


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