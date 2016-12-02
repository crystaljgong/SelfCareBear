package cs4720.self_care_bear;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p />
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TaskListFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ArrayList<TaskItem> tasks;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";

    private OnListFragmentInteractionListener mListener;
    private Context mContext;
    private RecyclerView rv;
    public TaskListRecyclerViewAdapter adapter;

    @SuppressLint("ValidFragment")
    public TaskListFragment(ArrayList<TaskItem> items) {
        tasks = items;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TaskListFragment newInstance(ArrayList<TaskItem> list) {
        TaskListFragment fragment = new TaskListFragment(list);
        Bundle args = new Bundle();
        args.putParcelableArrayList("taskList", list);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        setRetainInstance(true);

    }

    public void setData(ArrayList<TaskItem> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<TaskItem> getData() {
        return tasks;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy", "onDestroy was called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasklist_list, container, false);


     //   Log.i("tasks", tasks.toString());

        // Set the adapter
       // if (view instanceof RecyclerView) {
        mContext = view.getContext();
        rv = (RecyclerView)view.findViewById(R.id.taskListRV);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        rv.setItemAnimator(new DefaultItemAnimator());
        adapter = new TaskListRecyclerViewAdapter(mContext, tasks, mListener);
        rv.setAdapter(adapter);
      //  }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
super.onDetach();
        mListener = null;
        }

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * <p/>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 */
public interface OnListFragmentInteractionListener {
    // TODO: Update argument type and name
    void onListFragmentInteraction(TaskItem item);
}
}
