package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.ScrollView;

import com.example.myapplication.R;
import com.example.myapplication.database.Job;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ShowActivity extends AppCompatActivity implements OnMapReadyCallback {
    private LatLngWrapper loc = new LatLngWrapper();
    // da mozemo naknadno raditi sa mapom i menjati
    private GoogleMap gmap;
    private MapView mapView;

    /* mora omotac zbog prom ref tipa jer kada menjamo njen sadrzaj to je nov objekat i druga ref
    * ali posto menjamo na drugom mestu ta promena ref ovde nece dogoditi, zato prosl obj koji ce sadrzati ref */
    class LatLngWrapper {
        LatLng loc;
        public LatLng getLoc() {
            return loc;
        }
        public void setLoc(LatLng loc) {
            this.loc = loc;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        // Prosledjeni ID posla za prikazivanje
        Intent i = getIntent();
        Integer jobID = i.getIntExtra("jobID",1);

        // Mapa za prikaz lokacije posla
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Komunikacija sa bazom u posebnoj niti
        // Dobavljanje lokacije posla i prikaz tekstualnih podataka posla na ekran
        ShowTask st = new ShowTask(ShowActivity.this, jobID, loc);
        st.execute(); // lako se zaboravi
    }

    // Bez LifeCycle metoda mapa se nece iscrtavati
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    // Upravljanje prikazom mape
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;

        gmap.setMinZoomPreference(13);  // koliko moze najvise da se smanji pogled/unzoom
        UiSettings uiSettings = gmap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true); // prikaz kontrola za zoom

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(loc.getLoc().latitude, loc.getLoc().longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Prikaz i oznacavanje lokacije posla na mapi
        if(addresses != null && addresses.get(0) != null)
            gmap.addMarker(new MarkerOptions().position(loc.getLoc()).title(addresses.get(0).getAddressLine(0)));
        gmap.moveCamera(CameraUpdateFactory.newLatLng(loc.getLoc()));

    }
}
