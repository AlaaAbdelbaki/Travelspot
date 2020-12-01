package com.travelspot.travelspot;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.travelspot.travelspot.Models.Country;
import com.travelspot.travelspot.Models.ServicesClient;
import com.travelspot.travelspot.Models.User;
import com.travelspot.travelspot.Models.UserServices;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class EditProfileFragment extends Fragment {

    TextInputEditText emailInput;
    TextInputEditText firstNameInput;
    TextInputEditText lastNameInput;
    TextInputEditText passwordInput;
    TextInputEditText confirmPasswordInput;

    MaterialButton updateBtn;

    UserServices userServices = ServicesClient.getClient().create(UserServices.class);
    SharedPreferences userStates;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        userStates = v.getContext().getSharedPreferences("userStates", MODE_PRIVATE);

        emailInput = v.findViewById(R.id.emailInput);
        firstNameInput = v.findViewById(R.id.firstNameInput);
        lastNameInput = v.findViewById(R.id.LastNameInput);
        passwordInput = v.findViewById(R.id.passwordInput);
        confirmPasswordInput = v.findViewById(R.id.confirmPasswordInput);

        updateBtn = v.findViewById(R.id.update);


        Call<User> getUserDetails = userServices.getUser(userStates.getString("email",null));
        getUserDetails.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    //Response unsuccessful
                }else{
                    user = response.body();
                    emailInput.setText(user.getEmail());
                    firstNameInput.setText(user.getFirstName());
                    lastNameInput.setText(user.getLastName());

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setEmail(emailInput.getText().toString());
                user.setFirstName(firstNameInput.getText().toString());
                user.setLastName(lastNameInput.getText().toString());
                if(passwordInput.getText().toString().equals(confirmPasswordInput.getText().toString())){
                    if (!passwordInput.getText().toString().equals("")){
                        user.setPassword(passwordInput.getText().toString());
                    }
                    Log.e("UserBtn",user.toString());
                    Call<Boolean> updateUser = userServices.updateUser(user);
                    Log.e("Body", new Gson().toJson(updateUser.request().body()));
                    updateUser.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if(response.isSuccessful()){
                                Log.e("help pls",response.body().toString());
                                getActivity().getSupportFragmentManager().popBackStack();

                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {

                        }
                    });
                }else{
                    Toast.makeText(getContext(), "Please make sure your passwords are identical", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Inflate the layout for this fragment
        return v;
    }
}