package cs4720.self_care_bear;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static cs4720.self_care_bear.SettingsMenu.REQUEST_AUTHORIZATION;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TaskItem} and makes a call to the
 * specified {@link cs4720.self_care_bear.TaskManagerListFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TaskManagerListRecyclerViewAdapter extends RecyclerView.Adapter<TaskManagerListRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<TaskItem> tasks;
    private final TaskManagerListFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;

    public TaskManagerListRecyclerViewAdapter(ArrayList<TaskItem> items, TaskManagerListFragment.OnListFragmentInteractionListener listener, Context context) {
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
        final TaskItem item = tasks.get(position);
        holder.mTaskName.setText(tasks.get(position).getName());
        holder.mPandaPoints.setText("" + tasks.get(position).getPandaPoints() + " PandaPoints");
       // holder.mCompleted.setChecked(tasks.get(position).getCompleted());
        holder.mLocation.setText("Complete at " + tasks.get(position).getLocation());


        Log.d("onBindViewHolder", "" + tasks.get(position).getCompleted());

//        holder.mCompleted.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(item);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        //public final View mView;
        public final TextView mTaskName;
       // public final CheckBox mCompleted;
        public final TextView mPandaPoints;
        public ImageView deleteIcon;
        public final TextView mLocation;
        public ImageButton addToCal;
        //public TaskItem mItem;

        public ViewHolder(View view) {
            super(view);
            mTaskName = (TextView) view.findViewById(R.id.taskName);
            mPandaPoints = (TextView) view.findViewById(R.id.PandaPoints);
          //  mCompleted = (CheckBox) view.findViewById(R.id.completed);
            deleteIcon = (ImageView)itemView.findViewById(R.id.task_delete);
            mLocation = (TextView)view.findViewById(R.id.location);
            addToCal = (ImageButton) itemView.findViewById(R.id.addToCalendarButton);


            deleteIcon.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Task item has been deleted", Toast.LENGTH_LONG).show();
                    tasks.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

            //only show the add calendar option if calendar is on
            if (SettingsMenu.CALENDAR_ON) {
                addToCal.setVisibility(View.VISIBLE);
            }
            else addToCal.setVisibility(View.GONE);

            //when click on add to calendar, make a POST to calendar api to add this event to calendar.
            //this is only available when CALENDAR_ON = true so credential should always be there
            addToCal.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //notifyDataSetChanged();
                    //Make an Object array with context and task to pass into async task
//                    Object[] params = new Object[2];
//                    params[0] = tasks.get(getAdapterPosition());
//                    params[1] = v.getContext();
                    new MakeAddEventTask(SettingsMenu.MCREDENTIAL, v.getContext()).execute(tasks.get(getAdapterPosition()));

                }
            });
        }



        @Override
        public String toString() {
            return super.toString() + " '" + mPandaPoints.getText() + "'";
        }
    }
}




