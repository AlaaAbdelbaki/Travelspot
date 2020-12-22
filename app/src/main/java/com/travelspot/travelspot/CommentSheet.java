package com.travelspot.travelspot;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.travelspot.travelspot.Models.PostsServices;
import com.travelspot.travelspot.Models.ServicesClient;


public class CommentSheet extends BottomSheetDialogFragment {

    PostsServices postsServices= ServicesClient.getClient().create(PostsServices.class);

    public static CommentSheet newInstance(){ return new CommentSheet();}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_postbody_sheet, container, false);
        TextView fullComment = v.findViewById(R.id.fullComment);
        assert this.getArguments() != null;
        fullComment.setText(this.getArguments().getString("Post"));


        return v;
    }
}