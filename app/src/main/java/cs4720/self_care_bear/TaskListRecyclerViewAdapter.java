package cs4720.self_care_bear;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cs4720.self_care_bear.TaskListFragment.OnListFragmentInteractionListener;
//import cs4720.self_care_bear.dummy.DummyContent.DummyItem;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TaskItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TaskListRecyclerViewAdapter extends RecyclerView.Adapter<TaskListRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<TaskItem> taskList;
    private Context mContext;
    private final OnListFragmentInteractionListener mListener;

    public TaskListRecyclerViewAdapter(ArrayList<TaskItem> tasks, OnListFragmentInteractionListener listener) {
        taskList = tasks;
        mListener = listener;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_taskitem_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = taskList.get(position);
        holder.taskItemName.setText(taskList.get(position).getName());
        holder.mContentView.setText(taskList.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView taskItemName;
        public final TextView mContentView;
        public TaskItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            taskItemName = (TextView) view.findViewById(R.id.taskItemName);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
