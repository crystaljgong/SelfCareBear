package cs4720.self_care_bear;

import android.*;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static android.widget.Toast.LENGTH_SHORT;

public class SettingsMenu extends AppCompatActivity
    implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{


    //for location stuff
    static final int REQUEST_LOCATION = 1004;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    public boolean mRequestingLocationUpdates;
    private LocationRequest mLocationRequest;

    Switch locationSwitch;
    Switch calendarSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locationSwitch = (Switch) findViewById(R.id.locationSwitch);
        calendarSwitch = (Switch) findViewById(R.id.calendarSwitch);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getBaseContext(), "Location on", Toast.LENGTH_SHORT).show();
                    //connect
                    mGoogleApiClient.connect();
                    mRequestingLocationUpdates = true;
                }
                else {
                    if (mGoogleApiClient.isConnected()) {
                       // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                        mGoogleApiClient.disconnect();
                        mRequestingLocationUpdates = false;

                    }
                }
            }
        });

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5 * 60 * 1000)        //every 5 min, in milliseconds
                .setFastestInterval(60 * 1000); // every min, in milliseconds

    }


    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    //Location services stuff
    @Override
    public void onConnected(Bundle connectionHint) {

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
//            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
//            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
           Toast.makeText(this,  "" + mLastLocation.getLatitude() + ", "
                   + mLastLocation.getLongitude(), Toast.LENGTH_LONG).show();

        }

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
            //idk
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        //compare this to current locations!! idk!!!
    }

    public void onConnectionSuspended(int arg0) {
        //apparently it automatically tries to reconnect
    }
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("E", "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }
    public void onConnectionFailedListener() {
        //truly i don't know what i'm doing
    }



}
