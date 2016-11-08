package cs4720.self_care_bear;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs4720.self_care_bear.TaskManagerListFragment.OnListFragmentInteractionListener;
import cs4720.self_care_bear.dummy.DummyContent;
import cs4720.self_care_bear.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TaskManagerListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private Context mContext;
    private List<TaskManagerItem> allTasks;
    private RecyclerView rv;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskManagerListFragment() {
    }

    @SuppressLint("ValidFragment")
    public TaskManagerListFragment(List<TaskManagerItem> items) {
        allTasks = items;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TaskManagerListFragment newInstance(List<TaskManagerItem> items) {
        TaskManagerListFragment frag= new TaskManagerListFragment(items);
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        setRetainInstance(true);
    }

    public void setData(List<TaskManagerItem> tasks) {
        this.allTasks = tasks;
    }

    public List<TaskManagerItem> getData() {
        return allTasks;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy", "onDestroy was called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taskmanagerlist_list, container, false);

        // Set the adapter

        mContext = view.getContext();
        rv = (RecyclerView) view.findViewById(R.id.task_manager_list);

        TaskManagerListRecyclerViewAdapter adapter = new TaskManagerListRecyclerViewAdapter(allTasks, mListener, mContext);
        rv.setAdapter(adapter);
        Log.i("TaskManagerListFragment", "Adapter was set");

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


    public void addT(TaskManagerItem addMe) {
        allTasks.add(addMe);
        rv.getAdapter().notifyDataSetChanged();
    }

    public void removeT(TaskManagerItem removeMe) {
        allTasks.remove(removeMe);
        rv.getAdapter().notifyDataSetChanged();
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(TaskManagerItem item);
    }
}
