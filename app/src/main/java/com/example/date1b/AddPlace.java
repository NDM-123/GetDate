package com.example.date1b;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddPlace extends AppCompatActivity {
    // views for button
    Button addPlacebtn;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    EditText namePlace, description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_place);

        namePlace = findViewById(R.id.namePlace);
        description = findViewById(R.id.description);
        addPlacebtn = findViewById(R.id.addbutton);

        addPlacebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add a new document with a generated id.
                Map<String, Object> data = new HashMap<>();
                data.put("nameplace", namePlace.getText().toString());
                data.put("description", description.getText().toString());
                //data.put("name", description);

                DocumentReference newMarker = fStore.collection("Locations").document();

                newMarker.set(data);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }

        });


    }
}
