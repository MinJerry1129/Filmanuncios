package com.mobiledevteam.filmanuncios.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.mobiledevteam.filmanuncios.R;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.util.Locale.getDefault;

public class LocationMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap GMap;
    Geocoder geocoder;
    List<Address> addresses;
    EditText _selAddress;
    String sel_location = "";
    Button set_location;
    LatLng my_location;
    SupportMapFragment mapFragment;
    List<Place.Field> fields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
    );
    int first_start = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_map);
        geocoder = new Geocoder(this, getDefault());
//        if(!Places.isInitialized()){
//            Places.initialize(getBaseContext(),"AIzaSyApkOUDEBKVYSBdPv0QNT8kTbonpoK-g4o");
//        }

        _selAddress = (EditText) findViewById(R.id.current_address);
        set_location = (Button) findViewById(R.id.btn_set);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setReady();
    }
    private void setReady() {
        _selAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(getBaseContext());
                startActivityForResult(intent, 101);
            }
        });
        set_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                set_location();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 101){
            if(resultCode == Activity.RESULT_OK && data != null){
                Place place = Autocomplete.getPlaceFromIntent(data);
                _selAddress.setText(place.getAddress());
                my_location = place.getLatLng();
                first_start = 1;
                mapFragment.getMapAsync(this);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (first_start != 0){
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(my_location));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(my_location,14));
        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                String selected_address = "";
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    selected_address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    sel_location = "yes";
                } catch (IOException e) {
                    e.printStackTrace();
                }
                _selAddress.setText(selected_address);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(selected_address);
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.addMarker(markerOptions);
            }
        });
    }
}