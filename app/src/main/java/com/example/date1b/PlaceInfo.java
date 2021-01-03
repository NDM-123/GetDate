package com.example.date1b;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceInfo extends AppCompatActivity {
    Button navigateGoogle;
    FirebaseStorage storage;
    Button Add_to_favorite;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeinfo);
        Add_to_favorite =   findViewById(R.id.addFavorite);
        setTitle("Place the date");
        storage = FirebaseStorage.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        final Bundle data = getIntent().getExtras();

        final ImageView image = findViewById(R.id.locPicture);
        TextView tvName = findViewById(R.id.locname);
        TextView tvDesc = findViewById(R.id.locdescription);
        navigateGoogle = findViewById(R.id.navwithgm);

        final String name = data.getString("name");
        String desc = data.getString("desc");
        if (name != null) tvName.setText(name);
        if (desc != null) tvDesc.setText(desc);

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        StorageReference islandRef;
        //Download file in Memory
        if (name!= null)
             islandRef = storageRef.child("images/" + name);
        else
            islandRef = storageRef.child("images/IMG_20180119_163127.jpg");
        final long ONE_MEGABYTE = 1024 * 1024 * 10;

        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
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
                exception.getStackTrace();
            }
        });
        Add_to_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = fAuth.getCurrentUser();
                Map<String, Object> placeFavorite = new HashMap<>();
                placeFavorite.put("1", name);
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
                        e.getStackTrace();
                    }
                });
//                db.collection("Hospedaje").document(documentGetID).update("myHashMapField", "key" to /value/);
                        //.collection("favorites").document("").set(placeFavorite, SetOptions.merge());
//                Map<String, Object> favor = new HashMap<>();
////                DatabaseReference databaseRef = FirebaseStorage.getInstance().getReference("Users").do;
//                databaseRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        // Error
//                    }
//                });

//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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


    }


}
