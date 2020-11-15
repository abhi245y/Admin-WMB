package com.abhi245y.adminwmb;

import com.google.firebase.firestore.GeoPoint;

public class SearchStopModel {

    private String bus_stop_name;
    private GeoPoint bus_stop_location;
    public SearchStopModel(){

    }

    public SearchStopModel(String bus_stop_name, GeoPoint bus_stop_location) {
        this.bus_stop_name = bus_stop_name;
        this.bus_stop_location = bus_stop_location;
    }

    public String getBus_stop_name() {
        return bus_stop_name;
    }

    public void setBus_stop_name(String bus_stop_name) {
        this.bus_stop_name = bus_stop_name;
    }

    public GeoPoint getBus_stop_location() {
        return bus_stop_location;
    }

    public void setBus_stop_location(GeoPoint bus_stop_location) {
        this.bus_stop_location = bus_stop_location;
    }
}
