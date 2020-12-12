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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PlaceInfo extends AppCompatActivity {
    Button navigateGoogle;
    FirebaseStorage storage = FirebaseStorage.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeinfo);
        setTitle("Place the date");

        final Bundle data = getIntent().getExtras();

        final ImageView image = findViewById(R.id.locPicture);
        TextView tvName = findViewById(R.id.locname);
        TextView tvDesc = findViewById(R.id.locdescription);
        navigateGoogle = findViewById(R.id.navwithgm);

        String name = data.getString("name");
        String desc = data.getString("desc");
        if(name!=null)tvName.setText(name);
        if(desc!=null)tvDesc.setText(desc);

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();


         //Download file in Memory
        StorageReference islandRef = storageRef.child("images/IMG_20180119_163127.jpg");

        final long ONE_MEGABYTE = 1024 * 1024 * 10;

        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/pictureName.jpg" is returns, use this as needed
                 Bitmap img = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                if(img!=null){
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

        navigateGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Double lat =  data.getDouble("lat");
              Double lon =  data.getDouble("lon");
              if(lat!=null && lon!=null) {
                  Uri navigation = Uri.parse("google.navigation:q=" + lat + "," + lon + "");
                  Intent navigationIntent = new Intent(Intent.ACTION_VIEW, navigation);
                  navigationIntent.setPackage("com.google.android.apps.maps");
                  startActivity(navigationIntent);
              }
            }
        });


    }


}
