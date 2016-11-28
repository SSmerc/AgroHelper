package com.example.srecko.agrohelper;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.location.GpsSatellite;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

//import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.InputStream;
import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public MapsActivity() {
    }

    private class LocListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            lat = location.getLatitude();
            lon = location.getLongitude();
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
    }
    private ArrayList<LatLng> tocke;
    private GoogleMap mMap;
    private LocationManager lManager = null;
    private LocationListener lListener = null;
    private Intent parcel;
    private int index;
    private String intTyp;
    private double lon, lat;
    private AppAll all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //points on the map
        all=(AppAll) getApplication();
        if(parcel==null) {
            parcel = getIntent();
            intTyp=parcel.getStringExtra("Intent");
        }
        if(intTyp.equals("ShowDraw")) {
            index = parcel.getIntExtra("Index", 0);
            Parcela par = all.getParcela(index);
            tocke = new ArrayList<>();
            for (LatLng point : par.getParcelaLatLng()) {
                tocke.add(point);
            }
        }
        else
            tocke = new ArrayList<>();
        //layer = new KmlLayer(mMap, InputStream,getApplicationContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddPoint);
        FloatingActionButton fabConnect = (FloatingActionButton) findViewById(R.id.fabConnect);
        FloatingActionButton fabDelete = (FloatingActionButton) findViewById(R.id.fabRemoveLast);
        if(intTyp.equals("ShowDraw")) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    //updateMap();
                /*Snackbar.make(view, "Marker dodan", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                }
            });
            fabConnect.setEnabled(false);
            fabConnect.setVisibility(View.INVISIBLE);
            fabDelete.setEnabled(false);
            fabDelete.setVisibility(View.INVISIBLE);
            /*fabConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawOnMap();
              Snackbar.make(view, "Lokacija dodana dodan", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }
            });*/
        }
        else {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateMap();
                /*Snackbar.make(view, "Marker dodan", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                }
            });
            fabConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    all.addLLParcela(all.size()-1,tocke);
                    finish();
                }
            });
            fabDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tocke.size()>=1) {
                        tocke.remove(tocke.size() - 1);
                        drawOnMap();
                    }
                }
            });
        }
    }

    private void addPoint(LatLng latlng)
    {
        if(!intTyp.equals("ShowDraw")) {
            LatLng place = latlng;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 17));
            tocke.add(place);
            drawOnMap();
        }
    }

    private void drawOnMap()
    {
        mMap.clear();
        PolygonOptions polyOption= new PolygonOptions();
        for(int i=0;i<tocke.size();i++) {
            polyOption.add(tocke.get(i));
            mMap.addMarker(new MarkerOptions().position(tocke.get(i)).title(""+i));
        }
        Polygon poly = mMap.addPolygon(polyOption);

    }
    private void updateMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lListener = new LocListener();
        lManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, lListener);
        //original must revert back!!!!
        if(intTyp.equals("ShowDraw")) {
            LatLng place;
            if(tocke.size()>=1){
                 place = tocke.get(0);}
            else
                place = new LatLng(lat, lon);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 18));
            drawOnMap();
        }
        else
        {
            LatLng place = new LatLng(lat, lon);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 20));
        }
    }
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        updateMap();
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                addPoint(latLng);
            }
        });
        }
    }

