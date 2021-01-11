package com.travelspot.travelspot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.travelspot.travelspot.Models.MediaServices;
import com.travelspot.travelspot.Models.Post;
import com.travelspot.travelspot.Models.PostsServices;
import com.travelspot.travelspot.Models.ServicesClient;
import com.travelspot.travelspot.Models.Trip;
import com.travelspot.travelspot.Models.TripServices;
import com.travelspot.travelspot.Models.User;
import com.travelspot.travelspot.Models.UserServices;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class AddPostActivity extends AppCompatActivity {


    HorizontalScrollView scrollView;
    LinearLayout imagesContainer;
    TextInputLayout location;
    TextInputEditText locationInput;
    TextInputEditText captionInput;
    MaterialButton addPostBtn;
    MaterialButton openCameraBtn;
    MaterialButton pickImageBtn;
    AutoCompleteTextView tripInput;
    List<String> tripNames = new ArrayList<>();
    List<Trip> trips;
    LatLng position;
    Uri imageUri;
    Post latestPost;

    //For Image Upload
    RequestBody postId;
    RequestBody requestFile;
    MultipartBody.Part body;

    private static final int PERMISSION_CODE_CAMERA = 1001;
    private static final int REQUEST_IMAGE_CAPTURE = 123;
    private static final int REQUEST_IMAGE_IMPORT = 321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Log.e("onCreate: ", UserSession.instance.getU().toString());

        scrollView = findViewById(R.id.scrollView);
        imagesContainer = findViewById(R.id.imagesContainer);
        locationInput = findViewById(R.id.locationInput);
        location = findViewById(R.id.location);
        captionInput = findViewById(R.id.captionInput);
        addPostBtn = findViewById(R.id.addPostBtn);
        tripInput = findViewById(R.id.tripInput);
        openCameraBtn = findViewById(R.id.openCameraBtn);
        pickImageBtn = findViewById(R.id.pickImageBtn);

        init();

        openCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If OS Version > Marshmallow
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(AddPostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                            ActivityCompat.checkSelfPermission(AddPostActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //Ask for permissions
                        requestPermissions(permission, PERMISSION_CODE_CAMERA);
                    } else {
                        //Permission already granted
                        openCameraFunc();
                    }
                } else {
                    //OS Version < Marshmallow
                }
            }
        });

        pickImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddPostActivity.this, MapActivity.class);
                startActivity(i);
            }
        });
        locationInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddPostActivity.this, MapActivity.class);
                startActivityForResult(i, 1);
            }
        });

        //STILL NOT FINISHED !!!
        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postBody = captionInput.getText().toString();
                String location = locationInput.getText().toString();
                String trip = tripInput.getText().toString();
                if (!(postBody.equals("") && location.equals("") && trip.equals(""))) {
                    Post post = new Post();
                    post.setBody(postBody);
                    post.setPosition(location);
                    post.setTripId(getTripId(trip));
                    post.setUserId(UserSession.instance.getU().getId());
                    ProgressDialog progress = new ProgressDialog(AddPostActivity.this);
                    progress.setTitle("Loading");
                    progress.setMessage("Uploading in progress...");
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();

                    ServicesClient.getClient().create(PostsServices.class).addPost(post).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            ServicesClient.getClient().create(PostsServices.class).getPostsByUser(UserSession.instance.getU().getId()).enqueue(new Callback<List<Post>>() {
                                @Override
                                public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                                    Log.e("postsSize: ", String.valueOf(response.body().size()));
                                    latestPost = response.body().get(response.body().size() - 1);
                                    postId =
                                            RequestBody.create(
                                                    MultipartBody.FORM, String.valueOf(latestPost.getId()));
                                    if (body != null && postId != null) {

                                        ServicesClient.getClient().create(MediaServices.class).uploadMedia(postId, body).enqueue(new Callback<Boolean>() {
                                            @Override
                                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                                progress.dismiss();
                                                Toast.makeText(AddPostActivity.this, "Post added successfully !", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(AddPostActivity.this, MainActivity.class));
                                                finish();
                                            }

                                            @Override
                                            public void onFailure(Call<Boolean> call, Throwable t) {

                                            }
                                        });
                                    } else {
                                        progress.dismiss();
                                        Toast.makeText(AddPostActivity.this, "Post added without images", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AddPostActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Post>> call, Throwable t) {

                                }
                            });

                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Toast.makeText(AddPostActivity.this, "Post was not created !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddPostActivity.this, MainActivity.class));
                            finish();
                        }
                    });


                } else {
                    Toast.makeText(AddPostActivity.this, "Some fields are empty !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void openCameraFunc() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

    private String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }


    void init() {
        ServicesClient.getClient().create(TripServices.class).getTrips(UserSession.instance.getU().getId()).enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                trips = response.body();
                Log.e("onResponse: ", response.body().toString());
                if (trips.size() == 0) {
                    tripNames.add("No trips yet");
                } else {
                    for (Trip trip : trips) {
                        tripNames.add(trip.getTitle());
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Trip>> call, Throwable t) {
                Toast.makeText(AddPostActivity.this, "Could not get Trips", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, tripNames);
        tripInput.setAdapter(adapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                position = new LatLng(data.getDoubleExtra("lat", 0), data.getDoubleExtra("long", 0));
                Log.e("onActivityResult: ", position.toString());
                makeGeocodeSearch(position);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE) {


            File image = new File(getPath(imageUri));
            ImageView imageView = new ImageView(AddPostActivity.this);
            imageView.setImageBitmap(BitmapFactory.decodeFile(image.getAbsolutePath()));
            imageView.setRotation(90);
            imageView.setMaxHeight(scrollView.getHeight());
            scrollView.addView(imageView);
            requestFile =
                    RequestBody.create(
                            MediaType.parse(getContentResolver().getType(imageUri)),
                            image
                    );
            body =
                    MultipartBody.Part.createFormData("postMedia", image.getName(), requestFile);
            Log.e("onActivityResult: ", image.getName());


        }
        if (requestCode == REQUEST_IMAGE_IMPORT) {
        }
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
                            Log.e("onResponse: ", feature.placeName());
                            locationInput.setText(feature.placeName());
                        } else {
                            Toast.makeText(AddPostActivity.this, "Error getting Country",
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



    private int getTripId(String tripName){
        for(Trip trip : trips){
            if(trip.getTitle().equals(tripName)){
                return trip.getId();
            }
        }
        return -1;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddPostActivity.this, MainActivity.class));
        finish();
    }
}