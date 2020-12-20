package com.example.date1b;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.storage.FirebaseStorage;


public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public MarkerInfoWindowAdapter(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoContents(Marker arg0) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_map_marker_info_window, null);

        TextView tvLat = v.findViewById(R.id.title);
        TextView tvLng = v.findViewById(R.id.snippet);
        tvLat.setText(arg0.getTitle());
        tvLng.setText(arg0.getSnippet());


        return v;
    }
}
