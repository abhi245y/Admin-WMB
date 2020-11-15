package com.abhi245y.adminwmb;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class BusStopListModel {

    private String bus_stop_name;
    private int bus_stop_number;
    private GeoPoint bus_stop_location;
    private Timestamp timestamp;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public BusStopListModel() {
    }

    public BusStopListModel(String bus_stop_name, int bus_stop_number, GeoPoint bus_stop_location) {
        this.bus_stop_name = bus_stop_name;
        this.bus_stop_number = bus_stop_number;
        this.bus_stop_location = bus_stop_location;
    }

    public String getBus_stop_name() {
        return bus_stop_name;
    }

    public void setBus_stop_name(String bus_stop_name) {
        this.bus_stop_name = bus_stop_name;
    }

    public int getBus_stop_number() {
        return bus_stop_number;
    }

    public void setBus_stop_number(int bus_stop_number) {
        this.bus_stop_number = bus_stop_number;
    }

    public GeoPoint getBus_stop_location() {
        return bus_stop_location;
    }

    public void setBus_stop_location(GeoPoint bus_stop_location) {
        this.bus_stop_location = bus_stop_location;
    }
}
