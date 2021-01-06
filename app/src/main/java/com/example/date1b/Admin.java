package com.example.date1b;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Admin extends AppCompatActivity {
    StorageReference mStorageRef;
    Button logout, removePlace, searchT, advSearch;
    AutoCompleteTextView searchBar;
    ImageButton navigate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setTitle("Place the date");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        // Logout button with a listener
        logout = findViewById(R.id.logoutBtn);
        removePlace = findViewById(R.id.removeLocationBtn);
        searchBar = findViewById(R.id.SearchBar);
        searchT = findViewById(R.id.SearchTextBtn);
        advSearch = findViewById(R.id.AdvSearchBtn);
        navigate = findViewById(R.id.NavMap);

        searchBar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchBar.setText("");
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GoogleMapsActivity.class));
            }
        });
        searchT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fStore.collection("Locations").addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//
//                        if (e != null) {
//
//                        }
//                        ArrayList<MarkerInfo> al = new ArrayList<MarkerInfo>();
//                        for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
//
//                            String lat = (dc.getDocument().getData().get("latitude").toString());
//                            String lon = (dc.getDocument().getData().get("longitude").toString());
//                            String name = dc.getDocument().getData().get("name").toString();
//                            // LatLng location = new LatLng(lat, lon);
//                            MarkerInfo mi = new MarkerInfo(name, lat, lon);
//
//                            al.add(mi);
//                        }
                Intent intent = new Intent(getApplicationContext(), SearchText.class);

                startActivity(intent);
                //  Recieve string from search bar
                String name = "";
                intent.putExtra("name", name);
                //     finish();

            }


        });

        removePlace.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RemoveLocation.class));
            }
        });
        advSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RemoveLocation.class));
            }
        });
    }
}
