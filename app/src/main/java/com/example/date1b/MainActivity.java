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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    StorageReference mStorageRef;
    Button logout, addPlace, searchT;
    AutoCompleteTextView searchBar;
    ImageButton navigate,searchpicT;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Place the date");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        // Logout button with a listener
        logout = findViewById(R.id.logoutBtn);
        addPlace = findViewById(R.id.addLocationBtn);
        addPlace = findViewById(R.id.addLocationBtn);
        searchBar = findViewById(R.id.SearchBarMain);
        searchT = findViewById(R.id.SearchTextBtn);
        searchpicT = findViewById(R.id.searchpic);
        navigate = findViewById(R.id.NavMap);



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setText("");
            }

        });
        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GoogleMapsActivity.class));
            }
        });

        searchpicT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SearchText.class);

                startActivity(intent);
                //  Recieve string from search bar
                String place = "";
                place = searchBar.getText().toString();
                intent.putExtra("place_name", place);
                //     finish();

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
                String place = "";
                place = searchBar.getText().toString();
                intent.putExtra("place_name", place);
                //     finish();

            }

        });
        addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddPlace.class));
                finish();
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
                Toast.makeText(MainActivity.this, "My Account",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
            case R.id.nav_settings:
                Toast.makeText(MainActivity.this, "Settings",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
            case R.id.nav_search:
                Toast.makeText(MainActivity.this, "Search",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
            case R.id.nav_map:
                Toast.makeText(MainActivity.this, "Map",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), GoogleMapsActivity.class));
                finish();
                break;
            case R.id.nav_logout:
                Toast.makeText(MainActivity.this, "Logout",Toast.LENGTH_SHORT).show();
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

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);


    }


}

