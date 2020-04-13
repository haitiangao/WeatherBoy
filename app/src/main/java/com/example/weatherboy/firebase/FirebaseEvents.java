package com.example.weatherboy.firebase;


import androidx.annotation.NonNull;


import com.example.weatherboy.util.DebugLogger;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import io.reactivex.Observable;


public class FirebaseEvents {
    private DatabaseReference locationReference;

    private static List<String> locations = new ArrayList<>();


    public FirebaseEvents(){
        locationReference = FirebaseDatabase.getInstance().getReference().child("Location/");
        setLocations();
    }

    public Observable<List<String>> getLocations(){
        return Observable.just(locations);
    }

    public void setLocations(){
        locationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                locations.clear();
                for(DataSnapshot currentSnap : dataSnapshot.getChildren()){
                    String currentLocation = currentSnap.getValue().toString();
                    locations.add(currentLocation);
                    DebugLogger.logDebug("childname:" +currentLocation.toString());
                }
                DebugLogger.logDebug("Observable room2:  "+locations.size());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //DebugLogger.logError(databaseError);
            }
        });

    }

    public void sendNewLocation(String location){
        String bookingKey = locationReference.push().getKey();
        if (bookingKey!=null)
            locationReference.child(bookingKey).setValue(location);

    }



}
