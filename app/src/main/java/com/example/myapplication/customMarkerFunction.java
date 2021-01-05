package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class customMarkerFunction {

    public Marker marker;
    public DatabaseReference mdatabase;

    public customMarkerFunction(Marker marker){

    }

    public static void writeNewMarkerToFirebase(Marker marker) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        //Contact contact = new Contact(name, phone, email, image);
        LatLng location = marker.getPosition();

        long time = System.currentTimeMillis();

        mDatabase.child("Marker").child("Marker_" + String.valueOf(time)).child("title").setValue(marker.getTitle());
        mDatabase.child("Marker").child("Marker_" + String.valueOf(time)).child("snippet").setValue(marker.getSnippet());
        mDatabase.child("Marker").child("Marker_" + String.valueOf(time)).child("lat").setValue(location.latitude);
        mDatabase.child("Marker").child("Marker_" + String.valueOf(time)).child("lng").setValue(location.longitude);
        mDatabase.child("Marker").child("Marker_" + String.valueOf(time)).child("time").setValue(String.valueOf(time));

    }

    public static void removeMarkerFromFirebase(Marker marker, ArrayList<myMarker> arrayList) {
        System.out.println("remove marker 들어옴");
        String time = marker2myMarker(marker, arrayList);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Marker").child("Marker_" + time);
        mDatabase.removeValue();
    }

    public static String marker2myMarker(Marker marker, ArrayList<myMarker> arrayList){
        //marker 를 myMarker로 변환 후 time을 찾아서 firebase 삭제
        LatLng location = marker.getPosition();

        for(int i =0; i < arrayList.size(); i++){
            if(arrayList.get(i).getLat() == location.latitude && arrayList.get(i).getLng() == location.longitude){
                String time = arrayList.get(i).time;
                return time;
            }
        }

        return "0";
    }


}
