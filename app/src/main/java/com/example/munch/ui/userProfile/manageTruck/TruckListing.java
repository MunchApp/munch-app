package com.example.munch.ui.userProfile.manageTruck;

public class TruckListing {
    private boolean onlineStatus;
    private String name;
    private String address;
    private String photo;
    private String id;
    private float[] latLng;
    public TruckListing(boolean onlineStatus, String name, String address, String photo){
        this.onlineStatus = onlineStatus;
        this.name = name;
        this.address = address;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getAddress() {
        return address;
    }

    public Boolean getOnlineStatus() {
        return  onlineStatus;
    }
}
