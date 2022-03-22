package com.example.testapp.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class MyLocation {
    double latitude;
    double longitude;
    double elevation;
    Date date;

    public MyLocation() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public JSONObject toJSON () {
        JSONObject obj = new JSONObject();
        try {
            obj.put("latitud", getLatitude());
            obj.put("longitud", getLongitude());
            obj.put("date", getDate());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
