package com.travelspot.travelspot;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mapbox.android.core.FileUtils;
import com.travelspot.travelspot.Models.ServicesClient;
import com.travelspot.travelspot.Models.User;
import com.travelspot.travelspot.Models.UserServices;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private static final int REQUEST_IMAGE_CAPTURE = 123;
    private static final int REQUEST_IMAGE_IMPORT = 321;
    private static final int PERMISSION_CODE = 1000 ;
    UserServices userServices = ServicesClient.getClient().create(UserServices.class);
    User user;
    Uri imageUri;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        user = UserSession.instance.getU();
        Button openCamera = v.findViewById(R.id.openCamera);
        Button chooseImage = v.findViewById(R.id.choosePhoto);

        //Open camera and take a picture
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If OS Version > Marshmallow
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED||
                            ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //Ask for permissions
                        requestPermissions(permission,PERMISSION_CODE);
                    }else{
                        //Permission already granted
                        openCameraFunc();
                    }
                }else{
                 //OS Version < Marshmallow
                }
            }
        });

        //Choose image from gallery
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return v;
    }

    private void openCameraFunc() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Image");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From camera");
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(cameraIntent,REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //Permission was granted
                    openCameraFunc();
                }else{
                    //Permission was denied
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Match the request 'pic id with requestCode
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                File image = new File(getPath(imageUri));
                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse(getContext().getContentResolver().getType(imageUri)),
                                image
                        );
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("profilePicture", image.getName(), requestFile);

                RequestBody userId =
                        RequestBody.create(
                                okhttp3.MultipartBody.FORM, String.valueOf(user.getId()));


                userServices.uploadProfilePic(userId,body).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.e( "onFailure: ",t.getMessage() );
                        Toast.makeText(getContext(), "Not Uploaded", Toast.LENGTH_SHORT).show();

                    }
                });
                userServices.getUser(user.getEmail()).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        UserSession.instance.cleanUserSession();
                        UserSession.getInstance(response.body());
                        user = UserSession.instance.getU();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });

            }
            if (requestCode == REQUEST_IMAGE_IMPORT) {
            }
    }


    private String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null,null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    public static BottomSheetDialog newInstance() {
        return new BottomSheetDialog();
    }

    String currentPhotoPath;




}



