package cs4720.self_care_bear;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

// code from this tutorial https://www.bignerdranch.com/blog/expand-a-recyclerview-in-four-steps/

/**
 * Created by annie_000 on 11/2/2016.
 */

public class TaskParentViewHolder {
    public TextView mTaskTitleTextView;
    public ImageButton mParentDropDownArrow;

    public TaskParentViewHolder(View itemView) {
        super(itemView);

        mTaskTitleTextView = (TextView) itemView.findViewById(R.id.parent_list_item_task_title_text_view);
        mParentDropDownArrow = (ImageButton) itemView.findViewById(R.id.parent_list_item_expand_arrow);
    }
}
