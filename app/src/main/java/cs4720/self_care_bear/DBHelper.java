package cs4720.self_care_bear;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import com.google.android.gms.tasks.Task;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by annie_000 on 11/7/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DataStorage.db";
    // table name
    public static final String TABLE_TASKS = "tasks";
    // table columns
    public static final String KEY_NAME = "name";
    public static final String KEY_COMPLETED = "completed";
    public static final String KEY_PANDA_POINTS = "panda_points";
    public static final String[] COLUMNS = {KEY_NAME, KEY_COMPLETED, KEY_PANDA_POINTS};
    //public static final String KEY_TIME_OF_DAY = "time_of_day";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASKS + "(" + KEY_NAME + " TEXT PRIMARY KEY " + KEY_COMPLETED + " INTEGER " + KEY_PANDA_POINTS + " INTEGER " + KEY_TIME_OF_DAY + " TEXT" + ")";
        String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASKS + "(" + KEY_NAME + " TEXT PRIMARY KEY, " + KEY_COMPLETED + " TEXT " + KEY_PANDA_POINTS + " INTEGER " + ");";
        //String CREATE_TASK_TABLE = "create table tasks (name varchar(25), panda_points varChar(25))";
        db.execSQL(CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("delete table tasks");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addTask(TaskManagerItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, item.getName());
        contentValues.put(KEY_COMPLETED, "" + item.getCompleted());
        contentValues.put(KEY_PANDA_POINTS, "" + item.getPandaPoints());
        db.insert(TABLE_TASKS,
            null,
            contentValues);
        db.close();
    }

}
