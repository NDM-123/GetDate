package com.example.date1b;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class RemoveLocation extends AppCompatActivity {
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    ArrayList<String> al = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Button removeLoc, submit;
    String lat, lon;
    String nam;
    AutoCompleteTextView nameBar;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_location);

        nameBar = findViewById(R.id.autoCompleteSearchBar);
        removeLoc = findViewById(R.id.removemylocation);
        submit = findViewById(R.id.submitRemove);

        fStore.collection("Locations").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {

                }
                for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                    String name = dc.getDocument().getData().get("name").toString();
                    al.add(name);
                }
            }
        });


        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, al);
        nameBar.setThreshold(1);
        nameBar.setAdapter(adapter);
        nameBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameBar.setText("");
            }
        });


        removeLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nam = nameBar.getText().toString().trim();
                fStore.collection("Locations").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentSnapshot nameSnapshot : value.getDocuments()) {
                            if (nameSnapshot.get("name").equals(nam)) {
                                nameSnapshot.getReference().delete();
                                Toast.makeText(RemoveLocation.this, "Location was Deleted", Toast.LENGTH_SHORT).show();
                                nameBar.setText("");


                            }
                        }
                    }
                });

            }
        });

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch(id)
                {
                    case R.id.nav_account:
                        Toast.makeText(RemoveLocation.this, "My Account",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Admin.class));
                        finish();
                        break;
                    case R.id.nav_settings:
                        Toast.makeText(RemoveLocation.this, "Settings",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Admin.class));
                        finish();
                        break;
                    case R.id.nav_search:
                        Toast.makeText(RemoveLocation.this, "Search",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Admin.class));
                        finish();
                        break;
                    case R.id.nav_map:
                        Toast.makeText(RemoveLocation.this, "Map",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), GoogleMapsActivity.class));
                        finish();
                        break;
                    case R.id.nav_logout:
                        Toast.makeText(RemoveLocation.this, "Logout",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                        break;
                    default:
                        return true;

                }
                return true;
            }
        });
    }

    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);


    }
}