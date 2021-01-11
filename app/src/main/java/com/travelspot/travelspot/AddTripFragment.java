package com.travelspot.travelspot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.travelspot.travelspot.Models.Post;
import com.travelspot.travelspot.Models.ServicesClient;
import com.travelspot.travelspot.Models.Trip;
import com.travelspot.travelspot.Models.TripServices;
import com.travelspot.travelspot.Models.UserServices;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class AddTripFragment extends Fragment {

    Date startDate;
    Date endDate;
    TextInputEditText destinationInput;
    TextInputEditText tripTitleInput;
    TextInputLayout tripDuration;
    TextInputEditText tripDurationInput;
    TextInputLayout destination;
    MaterialButton addTrip;
    


    DateFormat formatter = new SimpleDateFormat("yyy-MM-dd");


    LatLng position = new LatLng(0,0);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_trip, container, false);

        tripTitleInput = v.findViewById(R.id.tripTitleInput);
        tripDuration = v.findViewById(R.id.tripDuration);
        tripDurationInput = v.findViewById(R.id.tripDurationInput);
        destination = v.findViewById(R.id.destination);
        destinationInput  = v.findViewById(R.id.destinationInput);
        addTrip = v.findViewById(R.id.addTrip);


        //Setting up the date Picker
        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();


        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long,Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long,Long> selection) {
                startDate = new Date(selection.first);
                endDate = new Date(selection.second);
                tripDurationInput.setText(materialDatePicker.getHeaderText());

            }
        });

        //Listeners

        //Open date picker and select dates
        tripDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(),"MATERIAL_DATE_PICKER");
            }
        });
        tripDurationInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(),"MATERIAL_DATE_PICKER");
            }
        });

        //open map and select country
        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),MapActivity.class);
                startActivity(i);
            }
        });
        destinationInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),MapActivity.class);
                startActivityForResult(i,1);
            }
        });

        //Add trip
        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tripTitleInput.getText().toString().equals("") || tripDurationInput.toString().equals("") || destinationInput.toString().equals("")){
                    Toast.makeText(getContext(), "Some fields are empty !", Toast.LENGTH_SHORT).show();
                }else{
                    Trip trip = new Trip();
                    trip.setTitle(tripTitleInput.getText().toString());
                    try {
                        trip.setStartDate(formatter.parse(formatter.format(startDate)));
                        trip.setEndDate(formatter.parse(formatter.format(endDate)));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    trip.setLocation(destinationInput.getText().toString());
                    Log.e( "destination value",destinationInput.getText().toString() );
                    trip.setUserId(UserSession.instance.getU().getId());
                    //Insert into database
                    ServicesClient.getClient().create(TripServices.class).addTrip(trip).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            Toast.makeText(getContext(), "Trip Added successfully !", Toast.LENGTH_SHORT).show();

                            tripTitleInput.setText("");
                            tripDurationInput.setText("");
                            destinationInput.setText("");

                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            //Toast.makeText(getContext(), "Error adding your trip !", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(), "Can't connect to server !", Toast.LENGTH_SHORT).show();

                            tripTitleInput.setText("");
                            tripDurationInput.setText("");
                            destinationInput.setText("");

                        }
                    });
                }

            }
        });



        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                position =new LatLng(data.getDoubleExtra("lat",0),data.getDoubleExtra("long",0));
                Log.e("onActivityResult: ",position.toString() );
                makeGeocodeSearch(position);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void makeGeocodeSearch(final LatLng latLng) {
        try {
            // Build a Mapbox geocoding request
            MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken(getString(R.string.mapbox_access_token))
                    .query(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()))
                    .geocodingTypes(GeocodingCriteria.TYPE_COUNTRY)
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
                            destinationInput.setText(feature.placeName());
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


}