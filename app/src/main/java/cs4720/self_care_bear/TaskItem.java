package cs4720.self_care_bear;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by crystalgong on 11/1/16.
 */
public class TaskItem implements Parcelable {
    private String name;
    private boolean completed;

    public TaskItem(String name, boolean completed) {
        this.name = name;
        this.completed = completed;
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



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(name);
        dest.writeByte((byte) (completed ? 1 : 0));
    }

}
