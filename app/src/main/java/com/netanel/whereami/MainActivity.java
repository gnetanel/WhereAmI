package com.netanel.whereami;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final String TAG = MainActivity.class.getName().toString();
    TextView textView;
    LocationManager locationManager;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.editTextLocation);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        geocoder = new Geocoder(this);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            onLocationChanged(location);
        } else {
            textView.setText("Could not detect last location from GPS");
        }

        try {
            List<Address> addresses = geocoder.getFromLocation(
                    location.getLatitude(), location.getLongitude(), 10);
            for (Address address : addresses) {
                textView.append("\n" + address.getAddressLine(0));
            }
        } catch (IOException e) {
            Log.e(TAG, "Couldn't get Geocoder data", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,10, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    // LocationListener implements method...
    @Override
    public void onLocationChanged(Location location) {
        String text = String.format(
                "Lat:\t %f\nLong:\t %f\nAlt:\t %f\nBearing:\t %f", location
                        .getLatitude(), location.getLongitude(), location.getAltitude(),
                location.getBearing()); //
        textView.setText(text);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
