package cs4720.self_care_bear;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.gcm.Task;

/**
 * Created by crystalgong on 11/1/16.
 */
public class TaskItem implements Parcelable {
    private String name;
    private int id;
    private boolean completed;
    private int pandaPoints;
    private String timeOfDay;
    private String location;

    public TaskItem(String name, boolean completed, int pandaPoints, String timeOfDay, String location) {
        this.name = name;
        this.completed = completed;
        this.pandaPoints = pandaPoints;
        this.timeOfDay = timeOfDay;
        this.location = location;
    }

    public TaskItem(int id, String name, boolean completed, int pandaPoints, String timeOfDay, String location) {
        this.id = id;
        this.name = name;
        this.completed = completed;
        this.pandaPoints = pandaPoints;
        this.timeOfDay = timeOfDay;
        this.location = location;
    }

    //necessary parcelable tasks added
    protected TaskItem(Parcel in) {
        name = in.readString();
        completed = in.readByte() != 0;
    }

    public static final Creator<TaskItem> CREATOR = new Creator<TaskItem>() {
        @Override
        public TaskItem createFromParcel(Parcel in) {
            return new TaskItem(in);
        }

        @Override
        public TaskItem[] newArray(int size) {
            return new TaskItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(name);
        dest.writeByte((byte) (completed ? 1 : 0));
    }

    @Override
    public boolean equals(Object o) { //name, location, points, and time are same
        boolean theySame = false;
        if (o instanceof TaskItem) {
            TaskItem item = (TaskItem) o;
            theySame = (item.getName().equals(this.getName()) &&
                    item.getLocation().equals(this.getLocation()) &&
                    item.getPandaPoints() == this.getPandaPoints()) &&
                    item.getTimeOfDay().equals(this.getTimeOfDay());

        }
        return theySame;

    }
}
