package cs4720.self_care_bear;


import java.util.List;

import cs4720.self_care_bear.TaskItem;

/**
 * Created by annie_000 on 11/2/2016.
 */

// code from expandable recyclerview tutorial: https://www.bignerdranch.com/blog/expand-a-recyclerview-in-four-steps/

public class TaskManagerItem extends TaskItem {

    private int pandaPoints;
    private String timeOfDay;
    private boolean completed;

    public TaskManagerItem(String name, boolean completed, int pandaPoints, String timeOfDay) {
        super(name, completed);
        this.pandaPoints = pandaPoints;
        this.timeOfDay = timeOfDay;
    }

    public String getTimeOfDay() {
        return this.timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public int getPandaPoints() {
        return this.pandaPoints;
    }

    public void setPandaPoints(int pandaPoints) {
        this.pandaPoints = pandaPoints;
    }

    public boolean getCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean comp) {
        this.completed = comp;
    }
}
