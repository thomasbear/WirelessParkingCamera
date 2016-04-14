package com.wordpress.yujiaguo.parkingcamera4yp2;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Provider;
import java.security.PublicKey;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //call method to show gps location on homepage
        showGPS();

        //call method to show wifi hotspot status on homepage
        showHotspotStatus();
    }


    //method of launching the camera in Chrome Custom Tabs
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
        intentBuilder.setStartAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);

        //launch intent
        CustomTabsIntent intent = intentBuilder.build();
        intent.launchUrl(this, uri);
    }

    //Method of launching map activity
    public void onStartMap(View view) {
        Intent StartMapIntent=new Intent(this,MapsActivity.class);
        startActivity(StartMapIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //method of showing GPS location
    private void showGPS() {
        TextView GPSLatLng = (TextView)findViewById(R.id.textView_LocationContent);

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

        //show location if available
        if(myLocation == null) {
            Toast.makeText(getApplicationContext(),
                    "GPS location currently unavailable, check GPS settings",
                    Toast.LENGTH_LONG).show();
        }
        else{
            GPSLatLng.setText(""+ myLatitude+", "+myLongitude);
        }
    }

    //turn on and off wifi hotspot, access wifi hotspot status
    public void onSetWifiHotspot(View view){
        //this variable detectes the initial state of hotspot
        boolean isHotSpotOn = ApManager.isApOn(MainActivity.this);

        //toggle hotspot status
        boolean toggleResult = ApManager.configApState(MainActivity.this);

        //update hotspot status in textView_HotspotStatusContent
        TextView textviewWifi = (TextView)findViewById
                (R.id.textView_textView_HotspotStatusContent);
        //needed, as it take time for the "real" status to change
        isHotSpotOn = !isHotSpotOn;

        //update textview
        if(isHotSpotOn) {
            //update textview
            textviewWifi.setText(" on");
            //let user know
            Toast.makeText(getApplicationContext(),
                    "Wi-Fi hotspot has just been turned on",
                    Toast.LENGTH_SHORT).show();
        }else{
            //update textview
            textviewWifi.setText(" off");
            //let user know
            Toast.makeText(getApplicationContext(),
                    "Wi-Fi hotspot has just been turned off",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //show wifi hotspot status
    private void showHotspotStatus() {
        //get status of wifi hotspot
        boolean isApOn = ApManager.isApOn(MainActivity.this);

        //set textview to display the status
        TextView textviewWifi = (TextView)findViewById
                (R.id.textView_textView_HotspotStatusContent);
        if(isApOn) {
            textviewWifi.setText(" on");
        }else{
            textviewWifi.setText(" off");
        }
    }







    /*onClick method to show GPS location for button
    public void onClickGPS(View view){
        TextView GPSLatLng = (TextView)findViewById(R.id.textView_LocationContent);

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

        //show location if available
        if(myLocation == null) {
            Toast.makeText(getApplicationContext(),
                    "GPS location currently unavailable, check GPS settings",
                    Toast.LENGTH_LONG).show();
        }
        else{
        GPSLatLng.setText(""+ myLatitude+", "+myLongitude);
        }
    }*/


}
