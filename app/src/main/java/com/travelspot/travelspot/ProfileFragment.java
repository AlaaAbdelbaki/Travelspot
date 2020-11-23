package com.travelspot.travelspot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    SharedPreferences userStates;
    SharedPreferences.Editor userStatesEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_profile, container, false);
        Button disconnect = v.findViewById(R.id.button_disconnect);

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userStates = v.getContext().getSharedPreferences("userStates", MODE_PRIVATE);
                userStatesEdit=userStates.edit();
                userStatesEdit.clear();
                userStatesEdit.apply();
                Intent intent = new Intent(getActivity(), FirstActivity.class);
                startActivity(intent);
            }
        });



        return v;
    }
}