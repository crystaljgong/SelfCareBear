package cs4720.self_care_bear;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by annie_000 on 11/2/2016.
 */

public class TaskManagerRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_taskmanager_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i("p", "placeholder");
    }

    //@Override
    public void onBindViewHolder(TaskChildViewHolder holder, int position) {
        Log.i("p", "placeholder");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView changeThisToSomethingElse;
        public final ImageButton mContentView;
        public TaskItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            changeThisToSomethingElse = (TextView) view.findViewById(R.id.parent_list_item_task_title_text_view);
            //I just picked something and put it in this id, idk what actually goes here
            mContentView = (ImageButton) view.findViewById(R.id.parent_list_item_expand_arrow);
        }

    }
        // @Override
        public int getItemCount() {
            return 0;
        }
    }

