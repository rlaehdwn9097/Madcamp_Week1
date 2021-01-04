package com.example.myapplication;

public class myMarker implements Comparable<myMarker> {

    public String title;
    public String snippet;
    public double lat;
    public double lng;
    public String time;

    public myMarker(){
    }

    public myMarker(String title, String snippet, double lat, double lng, String time){
        this.title = title;
        this.snippet = snippet;
        this.lat = lat;
        this.lng = lng;
        this.time = time;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int compareTo(myMarker o) {
        if(Long.valueOf(this.time)  < Long.valueOf(o.time)){
            return 1;
        }
        return 0;
    }
}
