package com.travelspot.travelspot.Models;

import java.util.ArrayList;

public interface PostsCallBack {
    void onSuccess(ArrayList<Post> value);
    void onError();
}
