package com.travelspot.travelspot.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Follower {

    @SerializedName("createdAt")
    @Expose
    Date createdAt;
    @SerializedName("updatedAt")
    @Expose
    Date updatedAt;
    @SerializedName("followerId")
    @Expose
    int followerId;
    @SerializedName("followingId")
    @Expose
    int followingId;

    public Follower() {
    }

    public Follower(Date createdAt, Date updatedAt, int followerId, int followingId) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.followerId = followerId;
        this.followingId = followingId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public int getFollowerId() {
        return followerId;
    }

    public int getFollowingId() {
        return followingId;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setFollowerId(int followerId) {
        this.followerId = followerId;
    }

    public void setFollowingId(int followingId) {
        this.followingId = followingId;
    }

    @Override
    public String toString() {
        return "Follower{" +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", followerId=" + followerId +
                ", followingId=" + followingId +
                '}';
    }
}
