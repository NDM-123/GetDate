package com.example.date1b;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class PlaceInfo extends AppCompatActivity{
    Button navigateGoogle;
    FirebaseStorage storage;
    Button Add_to_favorite;
    Button submit;
    Button back_home;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ImageButton wazeButton;
    RatingBar rt;
    String name ="";
    float firebaseRate = 0;
    ImageView image;
    TextView tvName;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeinfo);
        Add_to_favorite =   findViewById(R.id.addFavorite);
        wazeButton = findViewById(R.id.waze_button);
        back_home =  findViewById(R.id.backHome);
        storage = FirebaseStorage.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        final Bundle data = getIntent().getExtras();
        name = data.getString("name");

        image = findViewById(R.id.locPicture);
        //tvName = findViewById(R.id.locname);
        TextView tvDesc = findViewById(R.id.locdescription);
        navigateGoogle = findViewById(R.id.navwithgm);
        rt = (RatingBar) findViewById(R.id.ratingBar);
        submit = findViewById(R.id.submit_button);
        addRatingIfNull();
        fireRate();
        addImage();


        String desc = data.getString("desc");
        if (name != null) {
            setTitle(name);
        }else {
            setTitle("Place the date");
        }
        if (desc != null) tvDesc.setText(desc);

        //finding the specific RatingBar with its unique ID
        LayerDrawable stars=(LayerDrawable)rt.getProgressDrawable();

        //Use for changing the color of RatingBar
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        wazeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameWaze= name;
                nameWaze = nameWaze.replaceAll("\\s+","%20");
                Toast.makeText(PlaceInfo.this, "waze navigate", Toast.LENGTH_SHORT).show();
                try
                {
                    // Launch Waze to look for Hawaii:
                    String url = "https://waze.com/ul?q="+nameWaze ;
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
                    startActivity( intent );
                }
                catch ( ActivityNotFoundException ex  )
                {
                    // If Waze is not installed, open it in Google Play:
                    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
                    startActivity(intent);
                }

            }
        });
        Add_to_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = fAuth.getCurrentUser();
                String userId = user.getUid();
                //go to curr User in Database
                fStore.collection("Users").document(userId).update("favorites", FieldValue.arrayUnion(name))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PlaceInfo.this, "location added to favorites", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        e.getStackTrace();
                    }
                });
            }
        });
        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlaceInfo.this, "back Home", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        navigateGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double lat = data.getDouble("lat");
                Double lon = data.getDouble("lon");
                if (lat != null && lon != null) {
                    Uri navigation = Uri.parse("google.navigation:q=" + lat + "," + lon + "");
                    Intent navigationIntent = new Intent(Intent.ACTION_VIEW, navigation);
                    navigationIntent.setPackage("com.google.android.apps.maps");
                    startActivity(navigationIntent);
                }
            }
        });

    submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // This function is called when button is clicked.
            // Display ratings, which is required to be converted into string first.
            TextView t = (TextView)findViewById(R.id.ratingSum);
            updateRating(rt.getRating());
            t.setText("You Rated :"+String.valueOf(rt.getRating()));

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
                        Toast.makeText(PlaceInfo.this, "My Account",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        break;
                    case R.id.nav_settings:
                        Toast.makeText(PlaceInfo.this, "Settings",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        break;
                    case R.id.nav_search:
                        Toast.makeText(PlaceInfo.this, "Search",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        break;
                    case R.id.nav_map:
                        Toast.makeText(PlaceInfo.this, "Map",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), GoogleMapsActivity.class));
                        finish();
                        break;
                    case R.id.nav_logout:
                        Toast.makeText(PlaceInfo.this, "Logout",Toast.LENGTH_SHORT).show();
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

    private void addRatingIfNull() {
        fStore.collection("Locations").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for(DocumentSnapshot doc : task.getResult().getDocuments()){
                        if(name!=null) {
                            System.out.println(doc.getString("name"));
                            if (name.equals(doc.getString("name"))) {
                                System.out.println(doc.get("rating"));
                                if(doc.get("rating")==null) {
                                    //an average rate to start with
                                    System.out.println(doc.getId());
                                    // Update one field, creating the document if it does not already exist.
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("rating", 3.0);
                                    fStore.collection("Locations").document(doc.getId()).set(data, SetOptions.merge())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    System.out.println("DocumentSnapshot successfully written!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    System.out.println( "Error writing document");
                                                }
                                            });
                                    return;
                                }
                            }
                        }
                    }
                }
            }

        });
        return;
    }


    private void addImage() {
        // Create a storage reference from our app
        final StorageReference storageRef = storage.getReference();
        StorageReference islandRef = null;
        //Download file in Memory
        if (name!= null) islandRef = storageRef.child("images/" + name);
        final StorageReference finalIslandRef = islandRef;
        islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
//                islandRef = storageRef.child("images/IMG_20180119_163127.jpg");
                final long ONE_MEGABYTE = 1024 * 1024 * 10;

                finalIslandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Data for "images/pictureName.jpg" is returns, use this as needed
                        Bitmap img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        if (img != null) {
                            image.setImageBitmap(img);//Bitmap.createScaledBitmap(img, 120, 120, true));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
//                        exception.getStackTrace();
                        System.out.println("problem with image to image view");
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // File not found
                StorageReference example = storageRef.child("images/IMG_20180119_163127.jpg");
                final long ONE_MEGABYTE = 1024 * 1024 * 10;

                example.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Data for "images/pictureName.jpg" is returns, use this as needed
                        Bitmap img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        if (img != null) {
                            image.setImageBitmap(img);//Bitmap.createScaledBitmap(img, 120, 120, true));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
//                        exception.getStackTrace();
                        System.out.println("problem with image to image view");

                    }
                });


            }
        });

    }

    private void fireRate() {

        fStore.collection("Locations").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        if (name != null) {
                            if (name.equals(doc.getString("name"))) {
                                float r =  ((Double)doc.get("rating")).floatValue();
                                firebaseRate = r;
                                String ra = "The rating is: " + firebaseRate;
                                System.out.println(ra);
                                //tvName.setText(ra);
                                rt.setRating(r);
                                return;
                            }
                        }
                    }
                }
            }

        });
        return;
    }




    private void updateRating(final float rating) {
        FirebaseFirestore a = fStore.collection("Locations").getFirestore();
        fStore.collection("Locations").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                for(DocumentSnapshot doc : task.getResult().getDocuments()){
                    if(name!=null) {
                        if (name.equals(doc.getString("name"))) {
                            float r = firebaseRate;
                            if(rating>r && r<5)r+=0.1;
                            if(rating<r && r>0)r-=0.1;
                            fStore.collection("Locations").document(doc.getId()).update("rating",r);
                            return;
                        }
                    }
                    }
                }
            }

        });
        return;
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
