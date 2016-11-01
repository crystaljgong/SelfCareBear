package cs4720.self_care_bear;

/**
 * Created by crystalgong on 11/1/16.
 */
public class TaskItem {
    private String name;
    private boolean completed;

    public TaskItem(String name, boolean completed) {
        this.name = name;
        this.completed = completed;
    }

    public String getName() {
        return this.name;
    }
    public void changeName(String name) {
        this.name = name;
    }
    public boolean getCompleted() {
        return this.completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}
