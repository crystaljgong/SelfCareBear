package cs4720.self_care_bear;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainScreen extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        TaskListFragment.OnListFragmentInteractionListener,
        SensorEventListener
{

    //for the recyclerview
    private ArrayList<TaskItem> tasks;
    static public TaskListFragment homeTaskList;
    public static ArrayList<GiftItem> ALL_GIFTS;

    //initialize views
    private View homeScreenPage;
    //Made this public static so that gift shop can change it
    public static TextView pointsStatus;
    private TextView timeOfDay;

    // static fields
    static public ArrayList<TaskItem> MORN_TASKS;
    static public ArrayList<TaskItem> AFT_TASKS;
    static public ArrayList<TaskItem> EVEN_TASKS;
    public static int MORN_END_TIME;
    public static int EVEN_START_TIME;
    public static int P_POINTS;


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
        addGifts();

        //initialize points
        P_POINTS = 100;
        MORN_END_TIME = 9;
        EVEN_START_TIME = 18;


        //check time of day
        Calendar c = Calendar.getInstance();
        if (c.HOUR_OF_DAY < 9 && c.HOUR_OF_DAY > 3 && MORN_TASKS != null) {
            tasks = MORN_TASKS;
            timeOfDay.setText("Morning Tasks");

        }
        else if (c.HOUR_OF_DAY >= 9 && c.HOUR_OF_DAY < 18 && AFT_TASKS != null) {
            tasks = AFT_TASKS;
            timeOfDay.setText("Afternoon Tasks");
        }
        else {
            if (EVEN_TASKS != null) { // between 18 and 3
                tasks = EVEN_TASKS;
                timeOfDay.setText("Evening Tasks");
            }
        }

        //make the recyclerview
        homeTaskList = TaskListFragment.newInstance(tasks);
        getSupportFragmentManager().beginTransaction().add(R.id.taskListFrag, homeTaskList).commit();

        //navigation drawer stuff
        createNavigationDrawer();

        //textview for panda points
        pointsStatus = (TextView)findViewById(R.id.main_screen_status);
        // FOR TESTING PURPOSES
        // are we still doing step counter
        pointsStatus.setText("Panda Points: " + P_POINTS);


    }

    public void addGifts() {
        ALL_GIFTS = new ArrayList<>();
//        Drawable img = getResources().getDrawable(R.mipmap.snack);
        GiftItem snack = new GiftItem(R.mipmap.snack, "Snack", 50, false);
//        Drawable img2 = getResources().getDrawable(R.mipmap.flowers);
        GiftItem flower = new GiftItem(R.mipmap.flowers, "Flowers", 100, false);
//        Drawable img3 = getResources().getDrawable(R.mipmap.umbrella);
        GiftItem umbrella = new GiftItem(R.mipmap.umbrella, "Umbrella", 250, false);
//        Drawable img4 = getResources().getDrawable(R.mipmap.drill);
        GiftItem drill = new GiftItem(R.mipmap.drill, "Drill", 500, false);
//        Drawable img5 = getResources().getDrawable(R.mipmap.fireworks);
        GiftItem fireworks = new GiftItem(R.mipmap.fireworks, "Fireworks", 1000, false);

        ALL_GIFTS.add(snack);
        ALL_GIFTS.add(flower);
        ALL_GIFTS.add(umbrella);
        ALL_GIFTS.add(drill);
        ALL_GIFTS.add(fireworks);

        Log.i("addGifts", "" + ALL_GIFTS.get(0).getGiftName());

    }

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
                "Panda Points: " + P_POINTS);
        } else {
            P_POINTS += taskNum.getPandaPoints();
            Toast.makeText(this, "You completed this task, good job! Panda Points =  " + P_POINTS, Toast.LENGTH_SHORT).show();
            taskNum.setCompleted(true);
            pointsStatus.setText(
                    "Panda Points: " + P_POINTS);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
