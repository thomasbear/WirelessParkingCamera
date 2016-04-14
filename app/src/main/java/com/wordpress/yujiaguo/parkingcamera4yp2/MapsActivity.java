package com.wordpress.yujiaguo.parkingcamera4yp2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    //standard map setup
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    //features of maps
    private void setUpMap() {
        //Enable my location
        mMap.setMyLocationEnabled(true);

        //Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //Create a criteria maneger to retrieve provider
        Criteria criteria = new Criteria();

        //Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        //Get current location
        Location myLocation = locationManager.getLastKnownLocation(provider);

        //Get latitude & longitude
        Double myLatitude = myLocation.getLatitude();
        Double myLongitude = myLocation.getLongitude();

        //Create LatLng object for the current location
        LatLng mylatLng = new LatLng(myLatitude,myLongitude);

        //Show my current location
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylatLng));

        //Zooming close (range 2-21)
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //Pop up a marker
        Marker myLocationMarker =
                mMap.addMarker(new MarkerOptions().position(mylatLng).title("You are here!"));
        myLocationMarker.showInfoWindow();

    }

    //return home button action
    public void onReturnHome(View view) {
        Intent ReturnHomeIntent =new Intent(this,MainActivity.class);
        startActivity(ReturnHomeIntent);

    }


    //launch camera button action
    public void launchCamera(View view) {
        //chrom tabs intent
        //final CustomTabsIntent intent = new CustomTabsIntent.Builder().build();
        final String url = "http://192.168.43.35:10/videostream.cgi?user=user&pwd=access1234";
        final Uri uri = Uri.parse(url);

        //chrome tabs intent builder
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        //url hiding ***need to work on this
        intentBuilder.enableUrlBarHiding();
        //custom close button ****need to work on this
        intentBuilder.setCloseButtonIcon(BitmapFactory.decodeResource(
                getResources(), android.R.drawable.ic_media_previous));

        //setting custom animation of tabs
        intentBuilder.setStartAnimations(this, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);

        //launch intent
        CustomTabsIntent intent = intentBuilder.build();
        intent.launchUrl(this, uri);
    }


    //change map type
    public void changeMapType(View view){
        if (mMap.getMapType()==GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
            else{
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }


    //search for location action
    public void onSearch(View view){
        EditText locationInput = (EditText)findViewById(R.id.EditText_search);
        //extract input text
        String locationInputByUser = locationInput.getText().toString();

        //if there is an actual input
        if(locationInputByUser!= null||!locationInputByUser.equals("")){
            //use geocoder to transfer location to coordinates
            Geocoder geocoder = new Geocoder(this);

            //initialize address storage
            List<Address> addressList = null;
            try{
                //store address
                 addressList = geocoder.getFromLocationName(locationInputByUser, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
                //retrieve address
                Address addressCustom = addressList.get(0);

                //retrieve lat lng information
                LatLng latLngCustom = new LatLng(addressCustom.getLatitude(),
                        addressCustom.getLongitude());

                //initiate a map marker, to be set to custom address
                Marker customLocationMarker =mMap.addMarker(new MarkerOptions().
                    position(latLngCustom).title("Your Destination!"));
                //show this marker title
                customLocationMarker.showInfoWindow();


            //shift to this position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLngCustom));
            }
        }
    }



