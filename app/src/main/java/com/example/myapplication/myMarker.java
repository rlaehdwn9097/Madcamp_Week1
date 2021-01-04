package com.example.myapplication;

public class myMarker{

    public String title;
    public String snippet;
    public double lat;
    public double lng;

    public myMarker(){
    }

    public myMarker(String title, String snippet, double lat, double lng){
        this.title = title;
        this.snippet = snippet;
        this.lat = lat;
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }



}
