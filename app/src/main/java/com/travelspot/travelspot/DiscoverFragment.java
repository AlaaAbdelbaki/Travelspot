package com.travelspot.travelspot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.search.MapboxSearchSdk;
import com.mapbox.search.location.DefaultLocationProvider;
import com.mapbox.search.ui.view.SearchBottomSheetView;
import com.travelspot.travelspot.Adapters.TripAdapter;
import com.travelspot.travelspot.Adapters.UserAdapter;
import com.travelspot.travelspot.Models.ServicesClient;
import com.travelspot.travelspot.Models.Trip;
import com.travelspot.travelspot.Models.TripServices;
import com.travelspot.travelspot.Models.User;
import com.travelspot.travelspot.Models.UserServices;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DiscoverFragment extends Fragment {


    RecyclerView usersList;
    List<User> users;
    UserAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_discover, container, false);

        ;
        usersList = v.findViewById(R.id.usersList);

        usersList.setLayoutManager(new LinearLayoutManager(getContext()));
        loadUsersData();


        return v;
    }

    public void loadUsersData()
    {
        ServicesClient.getClient().create(UserServices.class).getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                users = response.body();
                mAdapter = new UserAdapter(getContext(),users);
                usersList.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }
}