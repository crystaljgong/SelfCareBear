package cs4720.self_care_bear;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

// code from this tutorial https://www.bignerdranch.com/blog/expand-a-recyclerview-in-four-steps/

/**
 * Created by annie_000 on 11/2/2016.
 */

public class TaskChildViewHolder {
    public TextView mCrimeDateText;
    public CheckBox mCrimeSolvedCheckBox;

    public TaskChildViewHolder(View itemView) {
        super(itemView);

        mCrimeDateText = (TextView) itemView.findViewById(R.id.child_list_item_task_panda_points_text_view);
        mCrimeSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.child_list_item_task_completed_check_box);
    }
}
