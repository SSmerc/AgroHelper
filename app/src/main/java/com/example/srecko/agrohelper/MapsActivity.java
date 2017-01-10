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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.srecko.agrohelper.R.id.map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    public MapsActivity() {
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        updateMap();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
    private GoogleApiClient googleApiClient;
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
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddPoint);
        FloatingActionButton fabConnect = (FloatingActionButton) findViewById(R.id.fabConnect);
        FloatingActionButton fabDelete = (FloatingActionButton) findViewById(R.id.fabRemoveLast);
        if(intTyp.equals("ShowDraw")) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(calculateAreaOfGPSPolygonOnEarthInSquareMeters(tocke)!=all.getParcela(index).getParcelInfo().getPovrsina())
                        all.getParcela(index).getParcelInfo().setPovrsina(calculateAreaOfGPSPolygonOnEarthInSquareMeters(tocke));
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
                    all.addSurfaceArea(all.size()-1,calculateAreaOfGPSPolygonOnEarthInSquareMeters(tocke));
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
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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
            mMap.addMarker(new MarkerOptions().position(tocke.get(i)).draggable(true).title(""+i));
        }
        Polygon poly = mMap.addPolygon(polyOption);

    }
    private void updateMap() {
        mMap.clear();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if(intTyp.equals("ShowDraw")) {
            LatLng place;
            if(tocke.size()>=1){
                 place = tocke.get(0);}
            else
                place = new LatLng(lat, lon);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 18));
            drawOnMap();
        }
        else {
            Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()), 18));
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
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

             @Override
             public void onMarkerDragEnd(Marker marker) {
                 // getting the Co-ordinates
                 tocke.set((Integer.parseInt(marker.getTitle())), new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
                 //move to current position
                 drawOnMap();
                 //updateMap();
             }
        });
        updateMap();
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                addPoint(latLng);
            }
        });
        }

    /*
    }*/


    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    private static final double EARTH_RADIUS = 6371000;// meters

    public static double calculateAreaOfGPSPolygonOnEarthInSquareMeters(final  ArrayList<LatLng> tocke) {
        return calculateAreaOfGPSPolygonOnSphereInSquareMeters(tocke, EARTH_RADIUS);
    }

    private static double calculateAreaOfGPSPolygonOnSphereInSquareMeters(final ArrayList<LatLng> tocke, final double radius) {
        if (tocke.size() < 3) {
            return 0;
        }

        final double diameter = radius * 2;
        final double circumference = diameter * Math.PI;
        final List<Double> listY = new ArrayList<Double>();
        final List<Double> listX = new ArrayList<Double>();
        final List<Double> listArea = new ArrayList<Double>();
        // calculate segment x and y in degrees for each point
        final double latitudeRef = tocke.get(0).latitude;
        final double longitudeRef = tocke.get(0).longitude;
        for (int i = 1; i < tocke.size(); i++) {
            final double latitude = tocke.get(i).latitude;
            final double longitude = tocke.get(i).longitude;
            listY.add(calculateYSegment(latitudeRef, latitude, circumference));
            listX.add(calculateXSegment(longitudeRef, longitude, latitude, circumference));
        }

        // calculate areas for each triangle segment
        for (int i = 1; i < listX.size(); i++) {
            final double x1 = listX.get(i - 1);
            final double y1 = listY.get(i - 1);
            final double x2 = listX.get(i);
            final double y2 = listY.get(i);
            listArea.add(calculateAreaInSquareMeters(x1, x2, y1, y2));
        }

        // sum areas of all triangle segments
        double areasSum = 0;
        for (final Double area : listArea) {
            areasSum = areasSum + area;
        }

        // get abolute value of area, it can't be negative
        return Math.abs(areasSum);// Math.sqrt(areasSum * areasSum);
    }

    private static Double calculateAreaInSquareMeters(final double x1, final double x2, final double y1, final double y2) {
        return (y1 * x2 - x1 * y2) / 2;
    }

    private static double calculateYSegment(final double latitudeRef, final double latitude, final double circumference) {
        return (latitude - latitudeRef) * circumference / 360.0;
    }

    private static double calculateXSegment(final double longitudeRef, final double longitude, final double latitude,
                                            final double circumference) {
        return (longitude - longitudeRef) * circumference * Math.cos(Math.toRadians(latitude)) / 360.0;
    }
    }

