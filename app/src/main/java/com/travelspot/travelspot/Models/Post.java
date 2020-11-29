package com.travelspot.travelspot.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Post {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("medias")
    @Expose
    private String medias;
    @SerializedName("createdAt")
    @Expose
    private Date createdAt;
    @SerializedName("updatedAt")
    @Expose
    private Date updatedAt;
    @SerializedName("TripId")
    @Expose
    private int tripId;
    @SerializedName("UserId")
    @Expose
    private int userId;


    public Post(int id, String body, String medias, Date createdAt, Date updatedAt, int tripId, int userId) {
        this.id = id;
        this.body = body;
        this.medias = medias;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.tripId = tripId;
        this.userId = userId;
    }

    public Post(int id)
    {
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMedias() {
        return medias;
    }

    public void setMedias(String medias) {
        this.medias = medias;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
