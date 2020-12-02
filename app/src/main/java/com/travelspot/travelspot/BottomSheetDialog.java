package com.travelspot.travelspot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cloudinary.android.MediaManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.travelspot.travelspot.Models.ServicesClient;
import com.travelspot.travelspot.Models.User;
import com.travelspot.travelspot.Models.UserServices;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private static final int pic_id = 123;
    private static final int img_id = 321;
    UserServices userServices = ServicesClient.getClient().create(UserServices.class);
    User user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout,container,false);

        user = UserSession.instance.getU();
        Button openCamera = v.findViewById(R.id.openCamera);
        Button chooseImage = v.findViewById(R.id.choosePhoto);

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Open Camera", Toast.LENGTH_SHORT).show();
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Start the activity with camera_intent,
                // and request pic id
                startActivityForResult(camera_intent, pic_id);

            }
        });

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Choose image from gallery", Toast.LENGTH_SHORT).show();
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, img_id);
            }
        });



        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Match the request 'pic id with requestCode
        if(data!=null){
            if (requestCode == pic_id) {

                // BitMap is data structure of image file
                // which stor the image in memory
                String fileName = randomFileName();
                Bitmap photo = (Bitmap)data.getExtras()
                        .get("data");
                try {
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(fileName)));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Uri imgUri = getImageUri(getContext(), photo);

                // Set the image in imageview for display

                user.setProfilePicture(fileName);
                MediaManager.get().upload(imgUri).option("use_filename","true").option("uniqe_filename","true").dispatch();
            }
            if(requestCode == img_id){
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    String fileName = randomFileName();
                    user.setProfilePicture(fileName);
                    MediaManager.get().upload(getImageUri(getContext(),selectedImage)).option("use_filename","true").option("uniqe_filename","true").dispatch();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
            userServices.updateUser(user).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {

                }
            });
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.e("pathlol",path);
        return Uri.parse(path);
    }


    public static BottomSheetDialog newInstance() {
        return new BottomSheetDialog();
    }

    public String randomFileName(){
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(20);

        for (int i = 0; i < 20; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
