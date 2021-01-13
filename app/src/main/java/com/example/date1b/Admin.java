package com.example.date1b;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Admin extends AppCompatActivity {
    StorageReference mStorageRef;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    Button logout, removePlace, searchT, advSearch;
    AutoCompleteTextView searchBar;
    ImageButton navigate;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    String namBtn;

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
                fromSearchToLocation();
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
                        Toast.makeText(Admin.this, "My Account",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Admin.class));
                        finish();
                        break;
                    case R.id.nav_settings:
                        Toast.makeText(Admin.this, "Settings",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Admin.class));
                        finish();
                        break;
                    case R.id.nav_search:
                        Toast.makeText(Admin.this, "Search",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Admin.class));
                        finish();
                        break;
                    case R.id.nav_map:
                        Toast.makeText(Admin.this, "Map",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), GoogleMapsAdmin.class));
                        finish();
                        break;
                    case R.id.nav_logout:
                        Toast.makeText(Admin.this, "Logout",Toast.LENGTH_SHORT).show();
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

    public void  fromSearchToLocation(){
        //  Recieve string from search bar
        namBtn = "";
        namBtn = searchBar.getText().toString();
        namBtn = searchBar.getText().toString().trim();
        fStore.collection("Locations").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot nameSnapshot : value.getDocuments()) {
                    if (nameSnapshot.get("name").equals(namBtn)) {
//                                nameSnapshot.getReference().delete();
                        searchBar.setText("");
                        Intent i = new Intent(getApplicationContext(), PlaceInfo.class);
                        i.putExtra("name", (String)nameSnapshot.get("name"));
                        i.putExtra("desc", (String)nameSnapshot.get("snippet"));
                        i.putExtra("lat", (String)nameSnapshot.get("latitude"));
                        i.putExtra("lan", (String)nameSnapshot.get("longitude"));
                        startActivity(i);


                    }
                }
            }
        });
    }
}
