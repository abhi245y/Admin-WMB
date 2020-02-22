package com.example.adminwmb;

import com.google.firebase.firestore.GeoPoint;

public class Stop {
    private GeoPoint stop_location;
    private String stope_name;

    public Stop(){


    }

    public Stop(GeoPoint stop_location, String stope_name) {
        this.stop_location = stop_location;
        this.stope_name = stope_name;
    }

    public GeoPoint getStop_location() {
        return stop_location;
    }

    public void setStop_location(GeoPoint stop_location) {
        this.stop_location = stop_location;
    }

    public String getStope_name() {
        return stope_name;
    }

    public void setStope_name(String stope_name) {
        this.stope_name = stope_name;
    }
}
