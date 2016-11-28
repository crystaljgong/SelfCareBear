package cs4720.self_care_bear;


import com.google.android.gms.tasks.Task;

/**
 * Created by annie_000 on 11/2/2016.
 */

// code from expandable recyclerview tutorial: https://www.bignerdranch.com/blog/expand-a-recyclerview-in-four-steps/

public class TaskManagerItem extends TaskItem {

    private int id;
    private int pandaPoints;
    private String timeOfDay;
    private boolean completed;
    private String location;

    public TaskManagerItem(String name, boolean completed, int pandaPoints, String timeOfDay, String location) {
        super(name, completed);
        this.pandaPoints = pandaPoints;
        this.timeOfDay = timeOfDay;
        this.location = location;
    }
    public TaskManagerItem(int id, String name, boolean completed, int pandaPoints, String timeOfDay, String location) {
        super(name, completed);
        this.id = id;
        this.pandaPoints = pandaPoints;
        this.timeOfDay = timeOfDay;
        this.location = location;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
