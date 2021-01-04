package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        mDatabase.child("Marker").child("Marker_" + marker.getId()).child("title").setValue(marker.getTitle());
        mDatabase.child("Marker").child("Marker_" + marker.getId()).child("snippet").setValue(marker.getSnippet());
        mDatabase.child("Marker").child("Marker_" + marker.getId()).child("lat").setValue(location.latitude);
        mDatabase.child("Marker").child("Marker_" + marker.getId()).child("lng").setValue(location.longitude);
        mDatabase.child("Marker").child("Marker_" + marker.getId()).child("time").setValue(String.valueOf(time));
    }

    public static void removeMarkerFromFirebase(Marker marker) {
        System.out.println("remove marker 들어옴");
        System.out.println(String.valueOf(marker.getId()));
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Marker").child("Marker_" + marker.getId());
        mDatabase.removeValue();

    }


}
