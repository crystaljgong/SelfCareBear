package cs4720.self_care_bear;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static cs4720.self_care_bear.MainScreen.EVEN_START_TIME;
import static cs4720.self_care_bear.MainScreen.homeTaskList;
import static cs4720.self_care_bear.TaskManagerScreen.aft;
import static java.util.Calendar.HOUR_OF_DAY;


public class SettingsMenu extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        EasyPermissions.PermissionCallbacks {

    //for location stuff
    static final int REQUEST_LOCATION = 1004;
    public static boolean LOCATION_ON;

    private GoogleApiClient mGoogleApiClient;
    public static Location mLastLocation;
    public boolean mRequestingLocationUpdates;
    private LocationRequest mLocationRequest;

    Switch locationSwitch;

    //FOR CALENDAR STUFF
    Switch calendarSwitch;
    public static boolean CALENDAR_ON;

    static GoogleAccountCredential MCREDENTIAL;
    private TextView calendarText;
  //  ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR};

    private List<String> googCalTasks;

    boolean timerRunning; //don't need to instantiate bc boolean i think



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locationSwitch = (Switch) findViewById(R.id.locationSwitch);
        calendarSwitch = (Switch) findViewById(R.id.calendarSwitch);
        if (LOCATION_ON) {
            locationSwitch.setChecked(true);
        }
        else locationSwitch.setChecked(false);

        if (CALENDAR_ON) {
            calendarSwitch.setChecked(true);
        }
        else calendarSwitch.setChecked(false);


        //LOCATION

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
                    LOCATION_ON = true;
                    Toast.makeText(getBaseContext(), "Location on", Toast.LENGTH_SHORT).show();
                    //connect
                    mGoogleApiClient.connect();
                    mRequestingLocationUpdates = true;
                } else {
                    LOCATION_ON = false;
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
                .setInterval(10 * 60 * 1000)        //every 10 min, in milliseconds
                .setFastestInterval(60 * 1000); // every min, in milliseconds

        //CALENDAR
        //initialize google calendar list
        googCalTasks = new ArrayList<>();

        //calendar Switch stuff
        calendarSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final Handler handler = new Handler();
                Timer timer = new Timer();

                if (isChecked) {
                    CALENDAR_ON = true;
                    Toast.makeText(getBaseContext(), "Working...", Toast.LENGTH_LONG).show();
                    callCalAPI();

                    //call every 30 minutes
                    if (!timerRunning) {
                        timerRunning = true;
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(new Runnable() {
                                    public void run() {
                                        callCalAPI();
                                    }
                                });
                            }
                        };
                        timer.schedule(task, 0, 15 * 60000); //every 15 minutes
                    }

                } else {
                    CALENDAR_ON = false;
                    if (timerRunning) {
                        timerRunning = false;
                        timer.cancel();
                        timer.purge();
                    }

                    //Toast.makeText(getBaseContext(), "Removed tasks", Toast.LENGTH_SHORT).show();
                    googCalTasks.clear();
                    calendarText.setText("");
                    //somehow disconnect google play calendar?
                }
            }
        });

        calendarText = (TextView) findViewById(R.id.textView);
       // mProgress = new ProgressDialog(this);
       // mProgress.setMessage("Calling Google Calendar API ...");

        MCREDENTIAL = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

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
            Toast.makeText(this, "" + mLastLocation.getLatitude() + ", "
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
        } catch (SecurityException e) {
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

    //Google Calendar API stuff

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void callCalAPI() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (MCREDENTIAL.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            Toast.makeText(this, "No network connection available.", Toast.LENGTH_SHORT).show();
            calendarSwitch.setChecked(false);
        } else {
            new MakeRequestTask(MCREDENTIAL).execute();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                MCREDENTIAL.setSelectedAccountName(accountName);
                callCalAPI();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        MCREDENTIAL.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode  code indicating the result of the incoming
     *                    activity result.
     * @param data        Intent (containing result data) returned by incoming
     *                    activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this,
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.",
                            Toast.LENGTH_LONG).show();
                    calendarSwitch.setChecked(false);
                } else {
                    callCalAPI();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        MCREDENTIAL.setSelectedAccountName(accountName);
                        callCalAPI();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    callCalAPI();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     *
     * @param requestCode  The request code passed in
     *                     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */

    //@Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    // @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     *
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     *
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     *
     * @param connectionStatusCode code describing the presence (or lack of)
     *                             Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private void addCalTasks() {
        boolean success = true;
        //for each task from google calendar, add it to afternoon tasks
        for (String task : googCalTasks) {
            Log.i("task", task);

            String[] nameLoc = task.split(",");
            if (nameLoc[1].equals("null")) {
                nameLoc[1] = "N/A";
            }

            TaskItem calTask = new TaskItem(nameLoc[0] + " (from Calendar)", false, 10, "afternoon", nameLoc[1]);
            if (!MainScreen.AFT_TASKS.contains(calTask)) {
                MainScreen.AFT_TASKS.add(calTask);
                if (aft != null) {
                    aft.adapter.notifyDataSetChanged();
                    homeTaskList.adapter.notifyDataSetChanged();
                }
                else {
                    success = false;
                    break;
                }
                System.out.println(task);
            }
            else Log.i("addTask", "didn't add " + calTask.getName() + " bc duplicate!");
        }
        if (success) {
            Toast.makeText(this, "Added tasks.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Visit the task manager screen before doing this.", Toast.LENGTH_SHORT).show();
            calendarSwitch.setChecked(false);
        }
        //notify the main screen recyclerview if it's afternoon as well as task manager recycler view
    }

    /**
     * An asynchronous task that handles the Google Calendar API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Self_Care_Bear")
                    .build();
        }

        /**
         * Background task to call Google Calendar API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                googCalTasks = getEventsFromCalApi();
                return getEventsFromCalApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of the next 10 events from the primary calendar.
         *
         * @return List of Strings describing returned events.
         * @throws IOException
         */
        private List<String> getEventsFromCalApi() throws IOException {
            DateTime now = new DateTime(System.currentTimeMillis());

            //get evening time
            Calendar c = Calendar.getInstance();
            int currentHour = c.get(Calendar.HOUR_OF_DAY);
            if (currentHour > EVEN_START_TIME) {  // if it's no longer afternoon, add tomorrow's afternoon tasks
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
            c.set(HOUR_OF_DAY, EVEN_START_TIME);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            long millis = c.getTimeInMillis();

            DateTime eveningTime = new DateTime(millis);

            //make a list of strings that holds the event names
            List<String> eventStrings = new ArrayList<String>();

// Iterate through entries in calendar list
            String pageToken = null;

            do {
                CalendarList calendarList = mService.calendarList().list().setPageToken(pageToken).execute();
                List<CalendarListEntry> gotItems = calendarList.getItems();

                for (CalendarListEntry calendarListEntry : gotItems) {
                    Log.i("cal", calendarListEntry.getId());
                    if (calendarListEntry.isSelected()) {
                        //a list of events makes a bunch of events from specific calendarList
                        Events events = mService.events().list(calendarListEntry.getId())
                                .setMaxResults(10)
                                .setTimeMin(now)
                                .setTimeMax(eveningTime)
                                // this should be 10pm tonight but truly idk how to do datetime
                                .setOrderBy("startTime")
                                .setSingleEvents(true)
                                .execute();
                        List<Event> items = events.getItems();

                        //for each item, extract the dateTime and turn it into a string
                        for (Event event : items) {
                            DateTime start = event.getStart().getDateTime();
                            if (start == null) {
                                // All-day events don't have start times, so just use
                                // the start date.
                                start = event.getStart().getDate();
                            }
                            //add that string to the list of events
                            if(!event.getSummary().contains("(from Self-Care-Bear)")) {
                                eventStrings.add(
                                        //String.format("%s (%s)", event.getSummary(), start)); This has time and date stamp
                                        String.format("%s,%s", event.getSummary(), event.getLocation())); //Just has name of the event
                            }
                        }
                    }
                }

                pageToken = calendarList.getNextPageToken();
            } while (pageToken != null);


            return eventStrings;
        }

        @Override
        protected void onPreExecute() {
            calendarText.setText("");
            //Toast.makeText(getBaseContext(), "Calling Google Calendar API...", Toast.LENGTH_SHORT).show();
           // mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
           // mProgress.hide();
            if (output == null || output.size() == 0) {
                Toast.makeText(getBaseContext(), "No results returned.", Toast.LENGTH_SHORT).show();
            } else {
                output.add(0, "Data retrieved using the Google Calendar API:");
                calendarText.setText(TextUtils.join("\n", output));
                Toast.makeText(getBaseContext(), "Retrieving events from Calendar...", Toast.LENGTH_SHORT).show();
                addCalTasks();
            }
        }

        @Override
        protected void onCancelled() {
           // mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            REQUEST_AUTHORIZATION);
                } else {
                    Toast.makeText(getBaseContext(), "The following error occurred:\n"
                            + mLastError.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getBaseContext(), "Request cancelled.", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
