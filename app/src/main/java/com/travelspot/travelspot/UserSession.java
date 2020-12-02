package com.travelspot.travelspot;

import com.travelspot.travelspot.Models.User;

public class UserSession {
    public static UserSession instance;

    private User u;

    public User getU() {
        return u;
    }



    @Override
    public String toString() {
        return "UserSession{" +
                "u=" + u +
                '}';
    }

    public UserSession(User u) {
        this.u = u;

    }

    public static UserSession getInstance(User u) {
        if(instance == null) {
            instance = new UserSession(u);
        }
        return instance;
    }



    public void cleanUserSession() {
        instance=null;
    }
}
