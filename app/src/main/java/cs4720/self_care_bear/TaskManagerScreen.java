package cs4720.self_care_bear;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerScreen extends AppCompatActivity implements TaskManagerListFragment.OnListFragmentInteractionListener{

    public List<TaskManagerItem> mornTasks;
    public List<TaskManagerItem> aftTasks;
    public List<TaskManagerItem> evenTasks;
    static public TaskManagerListFragment morn;
    static public TaskManagerListFragment aft;
    static public TaskManagerListFragment even;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // make tasks
        mornTasks = new ArrayList<>();
        aftTasks = new ArrayList<>();
        evenTasks = new ArrayList<>();

        TaskManagerItem mornin = new TaskManagerItem("Morning task", false, 10, "Morning");
        TaskManagerItem evenin = new TaskManagerItem("Evening task", false, 40, "Evening");
        TaskManagerItem after = new TaskManagerItem("Afternoon task", false, 30, "Afternoon");
        TaskManagerItem evenin2 = new TaskManagerItem("Evening task 2", true, 20, "Evening");

        mornTasks.add(mornin);
        aftTasks.add(after);
        evenTasks.add(evenin);
        evenTasks.add(evenin2);

        morn = TaskManagerListFragment.newInstance(mornTasks);
        even = TaskManagerListFragment.newInstance(evenTasks);
        aft = TaskManagerListFragment.newInstance(aftTasks);

        Log.i("this is onCreate", "created the tasks");
//        morn.setData(mornTasks);
//        even.setData(evenTasks);
//        aft.setData(aftTasks);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Log.i("onCreate", "adapter was set");
        /**FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });**/

    }


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
                    return even;
                case 2:
                    return aft;

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

    public void onListFragmentInteraction(TaskManagerItem taskNum) {
        if (taskNum.getCompleted() == true) {
            Toast.makeText(TaskManagerScreen.this, "You didn't complete this task yet? :(", Toast.LENGTH_LONG).show();
            taskNum.setCompleted(false);
        } else {
            Toast.makeText(TaskManagerScreen.this, "You completed this task, good job! :3", Toast.LENGTH_LONG).show();
            taskNum.setCompleted(true);
        }
    }
}
