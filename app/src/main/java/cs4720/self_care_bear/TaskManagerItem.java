package cs4720.self_care_bear;

import cs4720.self_care_bear.TaskItem;

/**
 * Created by annie_000 on 11/2/2016.
 */

public class TaskManagerItem extends TaskItem {
    private int pandaPoints;
    private String timeOfDay;

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
    public void setPandaPoints() {
        this.pandaPoints = pandaPoints;
    }

}
