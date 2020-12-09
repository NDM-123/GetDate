package com.example.date1b;
// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    protected double Tlatitude, Tlongitude;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * <p>
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            int PERMISSION_ALL = 1;
            String[] PERMISSIONS = {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng myLoc;
        if(location != null) {
            Tlongitude = location.getLongitude();
            Tlatitude = location.getLatitude();
            myLoc = new LatLng(Tlatitude, Tlongitude);
        }else{
            //  Ariel Location
             myLoc = new LatLng(32.1046, 35.1745);
        }
        // Add a marker in persons location and move the camera
        mMap.addMarker(new MarkerOptions().position(myLoc).title("Marker in myLoc"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));

        addMarkers();
        googleMap.setOnMarkerClickListener(this);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                addMark(latLng);
                     //   .snippet("Your marker snippet"));
            }
        });
        // If we would want to set images for location instead of red marks:

        //setting the size of marker in map by using Bitmap Class
//        int height = 80;
//        int width = 80;
//        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.scooter);
//        Bitmap b=bitmapdraw.getBitmap();
//        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
//        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot s : dataSnapshot.getChildren()){
//                    Log.e("Count " ,""+s.getChildrenCount());
//                    Map<String, Object> td = (HashMap<String,Object>) s.getValue();
//                    MarkerInfo loc = s.getValue(MarkerInfo.class);
//                    LatLng location=new LatLng(Double.parseDouble(loc.latitude),Double.parseDouble(loc.longitude));
//                    mMap.addMarker(new MarkerOptions().position(location).title(loc.name));     //.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
//                   // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
//
//                }
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    private void addMark(final LatLng latLng) {
        // Init the dialog object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter description");

// Set up the input
        final EditText input = new EditText(this);

// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String description;
                description = input.getText().toString();

                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(description));

                // Add a new document with a generated id.
                Map<String, Object> data = new HashMap<>();
                data.put("latitude", Double.toString(latLng.latitude));
                data.put("longitude", Double.toString(latLng.longitude));
                data.put("nameplace", description);

                DocumentReference newMarker = fStore.collection("Locations").document();

                newMarker.set(data);
            }
        });
        builder.setNegativeButton("Cancel", new         DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    @Override
    public void onLocationChanged(Location location) {
        Tlongitude = location.getLongitude();
        Tlatitude = location.getLatitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private void addMarkers() {


        fStore.collection("Locations").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e != null) {

                }

                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {

                    double lat = Double.parseDouble(dc.getDocument().getData().get("latitude").toString());
                    double lon = Double.parseDouble(dc.getDocument().getData().get("longitude").toString());
                    String name = dc.getDocument().getData().get("name").toString();
                    LatLng location = new LatLng(lat, lon);

                    mMap.addMarker(new MarkerOptions().position(location).title(name).draggable(true));

                }
            }
        });
    }

//  Get text from user
  private String getText() {
        // Init the dialog object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter text");

// Set up the input
        final EditText input = new EditText(this);

// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("Cancel", new         DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

        return input.getText().toString();
    }

}





