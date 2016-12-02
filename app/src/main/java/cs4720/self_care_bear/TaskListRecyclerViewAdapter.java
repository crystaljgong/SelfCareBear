package cs4720.self_care_bear;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import cs4720.self_care_bear.TaskListFragment.OnListFragmentInteractionListener;
//import cs4720.self_care_bear.dummy.DummyContent.DummyItem;

import java.util.ArrayList;

import static cs4720.self_care_bear.R.id.checkbox;
import static cs4720.self_care_bear.R.id.taskItemName;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TaskItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TaskListRecyclerViewAdapter extends RecyclerView.Adapter<TaskListRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<TaskItem> taskList;
    private Context mContext;
    private final OnListFragmentInteractionListener mListener;

    public TaskListRecyclerViewAdapter(Context context, ArrayList<TaskItem> tasks, OnListFragmentInteractionListener listener) {
        taskList = tasks;
        mListener = listener;
        mContext = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_taskitem_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TaskItem item = taskList.get(position);
        holder.taskItemName.setText(taskList.get(position).getName());
        holder.completed.setChecked(taskList.get(position).getCompleted());
        if (taskList.get(position).getLocation() != "null") {
            holder.taskItemLocation.setText("At " + taskList.get(position).getLocation());
        }

        holder.completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (taskList == null) {
            //somehow get a stack trace here
        }
        return  taskList == null ? 0 : taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskItemName;
        public CheckBox completed;
        public TextView taskItemLocation;

        public ViewHolder(View view) {
            super(view);
            completed = (CheckBox) view.findViewById(R.id.checkBox);
            taskItemName = (TextView) view.findViewById(R.id.taskItemName);
            taskItemLocation = (TextView) view.findViewById(R.id.taskItemLocation);
                }

        @Override
        public String toString() {
            return super.toString() + " '" + taskItemName.getText() + "'";
        }
    }
}
