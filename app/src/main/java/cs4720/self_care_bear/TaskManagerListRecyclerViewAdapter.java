package cs4720.self_care_bear;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import cs4720.self_care_bear.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TaskManagerItem} and makes a call to the
 * specified {@link cs4720.self_care_bear.TaskManagerListFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TaskManagerListRecyclerViewAdapter extends RecyclerView.Adapter<TaskManagerListRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<TaskManagerItem> tasks;
    private final TaskManagerListFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;

    public TaskManagerListRecyclerViewAdapter(ArrayList<TaskManagerItem> items, TaskManagerListFragment.OnListFragmentInteractionListener listener, Context context) {
        tasks = items;
        mListener = listener;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_taskmanagerlist_cell, parent, false);
        return new ViewHolder(view);
    }

    //TODO figure out why check isn't initalized as checked when completed is true...?
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TaskManagerItem item = tasks.get(position);
        holder.mTaskName.setText(tasks.get(position).getName());
        holder.mPandaPoints.setText("" + tasks.get(position).getPandaPoints() + " PandaPoints");
        holder.mCompleted.setChecked(tasks.get(position).getCompleted());
        holder.mLocation.setText("Complete at " + tasks.get(position).getLocation());


        Log.d("onBindViewHolder", "" + tasks.get(position).getCompleted());

        holder.mCompleted.setOnClickListener(new View.OnClickListener() {
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
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //public final View mView;
        public final TextView mTaskName;
        public final CheckBox mCompleted;
        public final TextView mPandaPoints;
        public ImageView deleteIcon;
        public final TextView mLocation;
        //public TaskManagerItem mItem;

        public ViewHolder(View view) {
            super(view);
            mTaskName = (TextView) view.findViewById(R.id.taskName);
            mPandaPoints = (TextView) view.findViewById(R.id.PandaPoints);
            mCompleted = (CheckBox) view.findViewById(R.id.completed);
            deleteIcon = (ImageView)itemView.findViewById(R.id.task_delete);
            mLocation = (TextView)view.findViewById(R.id.location);


            deleteIcon.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Task item has been deleted", Toast.LENGTH_LONG).show();
                    tasks.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPandaPoints.getText() + "'";
        }
    }
}
