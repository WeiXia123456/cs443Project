package edu.umb.cs443;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import edu.umb.cs443.parkingLot;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends FragmentActivity implements OnMyLocationButtonClickListener,
        OnMyLocationClickListener, OnMapReadyCallback {

    private GoogleMap mMap;

    private DBHelper db;
    private Marker marker ;
    private int i= 1;
    LinkedList<parkingLot> list;
    private String parkedAddress;
    private String parkedName;

    Button button;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    private  LatLng latLngPark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBHelper(this);
        db.insertData();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        button = findViewById(R.id.button2);
        button.setEnabled(false);
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> t = fusedLocationProviderClient.getLastLocation();
        t.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MainActivity.this);
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);




    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                }
                break;
        }


    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public boolean onMyLocationButtonClick() {

        return false;
    }

    public void showParkingLot(View v) {
        mMap.clear();
     //   TextView txt = (TextView) findViewById(R.id.textView);
        list = db.getParkingLot(currentLocation);
        String s = "";
        //   int i = 1;

        for (parkingLot a : list) {
       //     s += "& " + a.getName() + ". Charges: " + String.valueOf(a.getCharges() + "$/h \n");
            // txt.setText(String.valueOf(a.getLatitude()));

            LatLng latLng = new LatLng(a.getLatitude(), a.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(a.getName());
            marker = mMap.addMarker(markerOptions);


        }
     //   txt.setText(s);


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                String name = marker.getTitle();
                parkingLot park = db.getparkLot(name);
                TextView txt = (TextView) findViewById(R.id.textView);
                parkedName = name;
                parkedAddress = park.getAddress();
                latLngPark = new LatLng(park.getLatitude(),park.getLongitude());
                button.setEnabled(true);

             //   txt.setText(name);
               txt.setText("Name: "+ name + "\n" + "Address: "+park.getAddress()+ "\n"+ "Rates: "+ park.getCharges()+ "$/h");
            }
        });

    }


    public void parkHere(View v){
        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions().position(latLngPark).title("parkedhere").icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
        mMap.addMarker(markerOptions);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                String name = marker.getTitle();
                TextView txt = (TextView) findViewById(R.id.textView);

                //   txt.setText(name);
                txt.setText("parkedName: "+ parkedName + "\n"+"parkedAddress: "+ parkedAddress +"    Your car parked here");
            }
        });
    }





}


