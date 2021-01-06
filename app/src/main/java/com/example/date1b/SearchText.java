package com.example.date1b;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchText extends AppCompatActivity {
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    ArrayList<String> al = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    AutoCompleteTextView nameBar;
    Button SearchTextBtn;
    String namBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameBar = findViewById(R.id.SearchBarMain);
        SearchTextBtn =   findViewById(R.id.SearchTextBtn);

        Intent i = getIntent();
        //
        final String place = getIntent().getStringExtra("name_place");
        ArrayList<MarkerInfo> list = (ArrayList<MarkerInfo>) i
                .getSerializableExtra("Locations");
        Toast.makeText(SearchText.this, place, Toast.LENGTH_SHORT).show();
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
//        ListView lstView = (ListView) findViewById(R.id.locations_list);

        SearchTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                namBtn = nameBar.getText().toString().trim();
                fStore.collection("Locations").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentSnapshot nameSnapshot : value.getDocuments()) {
                            if (nameSnapshot.get("name").equals(namBtn)) {
//                                nameSnapshot.getReference().delete();
                                Toast.makeText(SearchText.this, "Location was found", Toast.LENGTH_SHORT).show();
                                nameBar.setText("");
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
        });
//        lstView.setAdapter(dataAdapter);
    }
}