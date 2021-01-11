package com.travelspot.travelspot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.Style;
import com.squareup.picasso.Picasso;
import com.travelspot.travelspot.Adapters.PostAdapter;
import com.travelspot.travelspot.Adapters.TripAdapter;
import com.travelspot.travelspot.Models.Post;
import com.travelspot.travelspot.Models.PostsCallBack;
import com.travelspot.travelspot.Models.PostsServices;
import com.travelspot.travelspot.Models.ServicesClient;
import com.travelspot.travelspot.Models.Trip;
import com.travelspot.travelspot.Models.TripServices;
import com.travelspot.travelspot.Models.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class HomeFragment extends Fragment {

    private static final int PERMISSION_CODE = 1000;
    CircleImageView profilePic;
    TextView welcomeBackText;
    MaterialButton addPostButton;
    TextView cityName;
    TextView countryName;
    double longitude;
    double latitude;
    User user;
    RecyclerView tripList;
    TripAdapter mAdapter;
    List<Trip> trips;

    LocationManager lm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        profilePic = v.findViewById(R.id.profilePic);
        welcomeBackText = v.findViewById(R.id.welcomeBackText);
        addPostButton = v.findViewById(R.id.addPost);
        cityName = v.findViewById(R.id.cityName);
        countryName = v.findViewById(R.id.countryName);
        tripList = v.findViewById(R.id.placesRecyclerView);
        user = UserSession.instance.getU();

        tripList.setLayoutManager(new LinearLayoutManager(getContext()));
        loadTripsData();



        Picasso.get().load("http://192.168.1.17:3000/"+user.getProfilePicture()).into(profilePic);
        profilePic.setRotation(90);
        welcomeBackText.setText("Welcome back "+user.getFirstName());
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED||
                    ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED){
                String[] permission = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
                //Ask for permissions
                requestPermissions(permission,PERMISSION_CODE);
            }else{
                //Permission already granted
                getPos();
                makeGeocodeSearch(new LatLng(latitude,longitude));
            }
        }else{
            //OS Version < Marshmallow
        }

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddPostActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });






        return v;
    }


    public void loadTripsData()
    {
        ServicesClient.getClient().create(TripServices.class).getTrips(UserSession.instance.getU().getId()).enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                trips = response.body();
                Log.e( "TripsCount: ",String.valueOf(trips.size()) );
                mAdapter = new TripAdapter(getContext(),trips);
                tripList.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<Trip>> call, Throwable t) {
                Toast.makeText(getContext(), "Can't connect to server !", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void makeGeocodeSearch(final LatLng latLng) {
        try {
            // Build a Mapbox geocoding request
            MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken(getString(R.string.mapbox_access_token))
                    .query(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()))
                    .geocodingTypes(GeocodingCriteria.TYPE_REGION)
                    .mode(GeocodingCriteria.MODE_PLACES)
                    .build();
            client.enqueueCall(new Callback<GeocodingResponse>() {
                @Override
                public void onResponse(Call<GeocodingResponse> call,
                                       Response<GeocodingResponse> response) {
                    if (response.body() != null) {
                        List<CarmenFeature> results = response.body().features();
                        if (results.size() > 0) {

                            // Get the first Feature from the successful geocoding response
                            CarmenFeature feature = results.get(0);
                            Log.e( "onResponse: ",feature.placeName() );
                            String place = feature.placeName();
                            String city = place.substring(0,place.indexOf(","));
                            String country = place.substring(place.indexOf(",")+2);
                            cityName.setText(city);
                            countryName.setText(country);
                        } else {
                            Toast.makeText(getContext(), "Error getting Country",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                    Timber.e("Geocoding Failure: " + throwable.getMessage());
                }
            });
        } catch (ServicesException servicesException) {
            Timber.e("Error geocoding: " + servicesException.toString());
            servicesException.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //Permission was granted
                    getPos();

                }else{
                    //Permission was denied
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    //a mess walah dont look i dont even know
    // how this works but it does somehow
    private void getPos() {
        if(getActivity()!=null){
            Log.d("Activity","Not null");
        }
        LocationManager lm = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
        if(lm!=null){
            Log.d("Location Manager","Not null");
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(
                            getActivity(),
                            new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                            1);
            return ;
        }
        List<String> providers = lm.getProviders(true);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        for (String provider : providers) {
            Location l = lm.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (location == null || l.getAccuracy() < location.getAccuracy()) {
                // Found best last known location: %s", l);
                location = l;
            }
        }
        if(location!=null){
            Log.d("Location","Not null");
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }else{
            Log.d("Location","null");
            Toast
                    .makeText(getActivity(),
                            "Unknown error!",
                            Toast.LENGTH_SHORT)
                    .show();
        }
        Log.d("POS", "Lat= " + latitude + " Long= " + longitude);

    }
}