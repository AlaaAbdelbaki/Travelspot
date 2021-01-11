package com.travelspot.travelspot.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Trip {
    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("start")
    @Expose
    Date startDate;
    @SerializedName("end")
    @Expose
    Date endDate;
    @SerializedName("locations")
    @Expose
    String location;
    @SerializedName("createdAt")
    @Expose
    Date cratedAt;
    @SerializedName("updatedAt")
    @Expose
    Date updatedAt;
    @SerializedName("UserId")
    @Expose
    int userId;

    public Trip() {
    }

    public Trip(int id, String title, Date startDate, Date endDate, String location, Date cratedAt, Date updatedAt, int userId) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.cratedAt = cratedAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getCratedAt() {
        return cratedAt;
    }

    public void setCratedAt(Date cratedAt) {
        this.cratedAt = cratedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", location='" + location + '\'' +
                ", cratedAt=" + cratedAt +
                ", updatedAt=" + updatedAt +
                ", userId=" + userId +
                '}';
    }
}
