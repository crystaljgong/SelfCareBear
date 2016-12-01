// db stuff taken from class example

package cs4720.self_care_bear;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.ArrayList;

public class TaskManagerScreen extends AppCompatActivity
        implements TaskManagerListFragment.OnListFragmentInteractionListener,
        AddTaskFragment.OnFragmentInteractionListener,
        AddTaskFragment.DataListener {


    static public TaskManagerListFragment morn;
    static public TaskManagerListFragment aft;
    static public TaskManagerListFragment even;

    static ProgressDialog mProgress;
    Button button;
    public static final String PREFS_NAME = "PrefsFile";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);

//        // restore preferences
//        SharedPreferences defaultTasks = getSharedPreferences(PREFS_NAME, 0);
//        String taskValue = defaultTasks.getString("taskValue", "none");
//
//        TextView taskItemName = (TextView) findViewById(R.id.taskName);
//        //taskItemName.setText(taskValue);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        // restore file
//        String FILENAME = "a_file";
//        TextView editText2 = (TextView) findViewById(R.id.PandaPoints);
//
//        try {
//            FileInputStream fis = openFileInput(FILENAME);
//            StringBuilder builder = new StringBuilder();
//            int ch;
//            while ((ch = fis.read()) != -1) {
//                builder.append((char) ch);
//            }
//            editText2.setText(builder.toString());
//            fis.close();
//        } catch (Exception e) {
//            Log.e("Storage", e.getMessage());
//        }

        button = (Button) findViewById(R.id.addTaskButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaskFragment frag = new AddTaskFragment();
                frag.show(getSupportFragmentManager(), "Add Task");
            }
        });

        //get data from google calendar
//        ArrayList<String> googCalData = getIntent().getStringArrayListExtra("google calendar tasks");
//        for(String s : googCalData) {
//            TaskItem item = new TaskItem(s, false, 10, "Afternoon", "home");
//            MainScreen.AFT_TASKS.add(item);
//        }
        // make tasks
        //addTasks();

        morn = TaskManagerListFragment.newInstance(MainScreen.MORN_TASKS);
        even = TaskManagerListFragment.newInstance(MainScreen.EVEN_TASKS);
        aft = TaskManagerListFragment.newInstance(MainScreen.AFT_TASKS);

        Log.i("this is onCreate", "created the tasks");

        // db test
        DBHelper helper = new DBHelper(this);
        for (int i = 0; i < MainScreen.MORN_TASKS.size(); i++) {
            helper.addTask(MainScreen.MORN_TASKS.get(i));
        }
        for (int i = 0; i < MainScreen.AFT_TASKS.size(); i++) {
            helper.addTask(MainScreen.AFT_TASKS.get(i));
        }
        for (int i = 0; i < MainScreen.EVEN_TASKS.size(); i++) {
            helper.addTask(MainScreen.EVEN_TASKS.get(i));
        }

        //reading from database
        ArrayList<TaskItem> tasksss = helper.getAllTasks();
        for (TaskItem item : tasksss) {
            String log = "Name: " + item.getName() + ", completed: " + item.getCompleted() + ", points: " + item.getPandaPoints() + ", time: " + item.getTimeOfDay() + ", place: " + item.getLocation();
            Log.i("reading from database", log);
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Log.i("onCreate", "adapter was set");



    }


    public void onResume() {
        super.onResume();
        if (morn.adapter != null && aft.adapter != null && even.adapter != null) {
            morn.adapter.notifyDataSetChanged();
            aft.adapter.notifyDataSetChanged();
            even.adapter.notifyDataSetChanged();
        }
    }

    public void onDataRecieved(String name, String timeOfDay, int points, String location) {
        TaskItem newItem = new TaskItem(name, false, points, timeOfDay, location);
        if (timeOfDay.equals("Morning")) {
            MainScreen.MORN_TASKS.add(newItem);
        } else if (timeOfDay.equals("Evening")) {
            MainScreen.EVEN_TASKS.add(newItem);
        } else {
            MainScreen.AFT_TASKS.add(newItem);
        }
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        SharedPreferences defaultTasks = getSharedPreferences(PREFS_NAME, 0);
//        SharedPreferences.Editor editor = defaultTasks.edit();
//        TextView taskItemName = (TextView)findViewById(R.id.taskItemName);
//        editor.putString("taskValue", taskItemName.getText().toString());
//
//        editor.commit();
//
//        // Using a file
//        String FILENAME = "a_file";
//        TextView PandaPoints = (TextView)findViewById(R.id.PandaPoints);
//        String string = PandaPoints.getText().toString();
//        try {
//            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
//            fos.write(string.getBytes());
//            fos.close();
//        }catch(Exception e) {
//            Log.e("StorageExample", e.getMessage());
//        }
//    }
//
//    public void saveToDB(View view) {
//        DBHelper helper = new DBHelper(this);
//        SQLiteDatabase db = helper.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        TextView taskItemName = (TextView)findViewById(R.id.taskItemName);
//        TextView PandaPoints = (TextView)findViewById(R.id.PandaPoints);
//        String name = taskItemName.getText().toString();
//        String panda_points = PandaPoints.getText().toString();
//        values.put("name", name);
//        values.put("PandaPoints", panda_points);
//
//        long newRowId;
//        newRowId = db.insert(
//                "tasks",
//                null,
//                values);
//
//        String[] projection = {
//                "name",
//                "PandaPoints"
//        };
//
//        String sortOrder =
//                "name" + " DESC";
//
//        Cursor cursor = db.query("tasks", projection, null, null, null, null, sortOrder);
//
//        while(cursor.moveToNext()) {
//            String currTask = cursor.getString(
//                    cursor.getColumnIndexOrThrow("name")
//            );
//            Log.i("DBData", currTask);
//        }
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_manager, menu);
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
    public void onFragmentInteraction(Uri uri) {

    }

//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public PlaceholderFragment() {
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_task_manager, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return morn;
                case 1:
                    return aft;
                case 2:
                    return even;

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Morning";
                case 1:
                    return "Afternoon";
                case 2:
                    return "Evening";
            }
            return null;
        }
    }

    public void onListFragmentInteraction(TaskItem taskNum) {
        if (taskNum.getCompleted() == true) {
            Toast.makeText(TaskManagerScreen.this, "You didn't complete this task yet?", Toast.LENGTH_SHORT).show();
            taskNum.setCompleted(false);
        } else {
            Toast.makeText(TaskManagerScreen.this, "You completed this task, good job!", Toast.LENGTH_SHORT).show();
            taskNum.setCompleted(true);

        }
    }
}
