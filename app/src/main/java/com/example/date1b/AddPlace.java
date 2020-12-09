package com.example.date1b;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddPlace extends AppCompatActivity {
    // views for button
    Button addPlace;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    EditText namePlace,description;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /*
        setContentView(R.layout.activity_addPlace);
//        /*
        namePlace = findViewById(R.id.namePlace);
        description = findViewById(R.id.description);
        addPlace = findViewById(R.id.addbutton);
        */

}
}
