package cs4720.self_care_bear;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.Task;
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
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainScreen extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        TaskListFragment.OnListFragmentInteractionListener
{

    //for the recyclerview
    private ArrayList<TaskItem> tasks;
    static public TaskListFragment homeTaskList;

    //initialize views
    private View homeScreenPage;
    private TextView pointsStatus;
    private TextView timeOfDay;

    //fields
    static public ArrayList<TaskItem> MORN_TASKS;
    static public ArrayList<TaskItem> AFT_TASKS;
    static public ArrayList<TaskItem> EVEN_TASKS;

    //Fields for points and steps!
    public static int P_POINTS;
    public static int STEPS;

    public boolean initialStartup = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_screen);
        homeScreenPage = findViewById(R.id.homeScreen);

        timeOfDay = (TextView) findViewById(R.id.timeOfDayTextView);

        //make a list of tasks
        tasks = new ArrayList<>();
        TaskItem dummy = new TaskItem("dummyThing", false, 10, "morning", "home");
        tasks.add(dummy);

        //initialize all task lists
        addTasks();

        //initialize points, steps
        P_POINTS = 0;
        STEPS = 0;

        //check time of day
        Calendar c = Calendar.getInstance();
        if (c.HOUR_OF_DAY < 9 && MORN_TASKS != null) {
            tasks = MORN_TASKS;
            timeOfDay.setText("Morning Tasks");

        }
        else if (c.HOUR_OF_DAY >= 9 && c.HOUR_OF_DAY < 21 && AFT_TASKS != null) {
            tasks = AFT_TASKS;
            timeOfDay.setText("Afternoon Tasks");
        }
        else {
            if (EVEN_TASKS != null) {
                tasks = EVEN_TASKS;
                timeOfDay.setText("Evening Tasks");
            }
        }

        //make the recyclerview
        homeTaskList = TaskListFragment.newInstance(tasks);
        getSupportFragmentManager().beginTransaction().add(R.id.taskListFrag, homeTaskList).commit();

        //navigation drawer stuff

        createNavigationDrawer();

        //textview for panda points, steps
        pointsStatus = (TextView)findViewById(R.id.main_screen_status);

    }

    // preloaded tasks for first startup
    public void addTasks() {

        MORN_TASKS = new ArrayList<>();
        AFT_TASKS = new ArrayList<>();
        EVEN_TASKS = new ArrayList<>();

        TaskItem mornin = new TaskItem("Get out of bed", false, 10, "Morning", "home");
        TaskItem mornin2 = new TaskItem("Brush your teeth", false, 10, "Morning", "home");
        TaskItem mornin3 = new TaskItem("Eat breakfast", false, 10, "Morning", "home");
        TaskItem evenin = new TaskItem("Brush your teethteethteethteethteethteethteethteethteeth", false, 10, "Evening", "home");
        TaskItem after = new TaskItem("Eat lunchlunchlunchlunchlunchlunchlunchlunchlunchlunchlunchlunch ", false, 10, "Afternoon", "home");
        TaskItem evenin2 = new TaskItem("Go to sleep", false, 20, "Evening", "home");

        MORN_TASKS.add(mornin);
        MORN_TASKS.add(mornin2);
        MORN_TASKS.add(mornin3);
        AFT_TASKS.add(after);
        EVEN_TASKS.add(evenin);
        EVEN_TASKS.add(evenin2);
    }

    @Override
    public void onResume() {
        super.onResume();
//        pointsStatus.setText(
//                "Panda Points: " + P_POINTS + "\n" +
//                        "Steps today: " + STEPS
//        );


    }

    private void createNavigationDrawer() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    //*****
//Navigation Drawer things
//*****


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_bar, menu);
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_task_manager) {
            Intent startTaskManager = new Intent(MainScreen.this, TaskManagerScreen.class);
            //startTaskManager.putStringArrayListExtra("google calendar tasks", (ArrayList) googCalTasks);
            startActivity(startTaskManager);
        } else if (id == R.id.nav_shop) {
            Intent startGiftShop = new Intent(MainScreen.this, GiftShop.class);
            startActivity(startGiftShop);

        } else if (id == R.id.nav_settings) {
            Intent startSettings = new Intent(MainScreen.this, SettingsMenu.class);
            startActivity(startSettings);

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onListFragmentInteraction(TaskItem taskNum) {
        if (taskNum.getCompleted() == true) {
            P_POINTS -= taskNum.getPandaPoints();
            Toast.makeText(this, "You didn't complete this task yet? Panda Points = " + P_POINTS, Toast.LENGTH_SHORT).show();
            taskNum.setCompleted(false);
            pointsStatus.setText(
                "Panda Points: " + P_POINTS + "\n" +
                        "Steps today: " + STEPS
        );
        } else {
            P_POINTS += taskNum.getPandaPoints();
            Toast.makeText(this, "You completed this task, good job! Panda Points =  " + P_POINTS, Toast.LENGTH_SHORT).show();
            taskNum.setCompleted(true);
            pointsStatus.setText(
                    "Panda Points: " + P_POINTS + "\n" +
                            "Steps today: " + STEPS
            );

        }
    }

}
