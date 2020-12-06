package com.example.date1b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchText extends AppCompatActivity{
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    ArrayList<String> al = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Intent i = getIntent();
        ArrayList<MarkerInfo> list = (ArrayList<MarkerInfo>) i
                .getSerializableExtra("Locations");

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
        ArrayAdapter<String> dataAdapter =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);
        ListView lstView = (ListView) findViewById(R.id.locations_list);
        lstView.setAdapter(dataAdapter);
    }
}