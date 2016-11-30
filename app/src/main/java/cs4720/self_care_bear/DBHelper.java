// used this tutorial: http://mobilesiri.com/android-sqlite-database-tutorial-using-android-studio/

package cs4720.self_care_bear;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by annie_000 on 11/7/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DataStorage.db";
    //TODO: make primary key incrementing thingy
    // table name
    public static final String TABLE_TASKS = "tasks";
    // table columns
    public static final String KEY_ID = "idNum";
    public static final String KEY_NAME = "name";
    public static final String KEY_COMPLETED = "completed";
    public static final String KEY_PANDA_POINTS = "PandaPoints";
    public static final String KEY_TIME_OF_DAY = "time_of_day";
    public static final String KEY_LOCATION = "location";
    public static final String[] COLUMNS = {KEY_ID, KEY_NAME, KEY_COMPLETED, KEY_PANDA_POINTS, KEY_TIME_OF_DAY, KEY_LOCATION};

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i("dbhelper", "constructor was just called");
        this.onCreate(this.getWritableDatabase());
        Log.i("dbhelper constructor", "db was made");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String DROP_TABLE_TASK = "DROP TABLE " + TABLE_TASKS + ";";
//        db.execSQL(DROP_TABLE_TASK);
        String CREATE_TASK_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " varchar(50), " + KEY_COMPLETED + " varchar(50), " + KEY_PANDA_POINTS + " INTEGER, " + KEY_TIME_OF_DAY + " varchar(50), " + KEY_LOCATION + " varchar(50) " + ");";
        System.out.println(CREATE_TASK_TABLE);
        db.execSQL(CREATE_TASK_TABLE);
        //String result = null;
        //Object[] array = new Object[1];
        //array[0] = result;
        //db.execSQL("DESCRIBE " + TABLE_TASKS + ";", array);
        //System.out.println(result);
        //db.execSQL();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("delete table tasks");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addTask(TaskItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, item.getName());
        contentValues.put(KEY_COMPLETED, "" + item.getCompleted());
        contentValues.put(KEY_PANDA_POINTS, "" + item.getPandaPoints());
        contentValues.put(KEY_TIME_OF_DAY, "" + item.getTimeOfDay());
        contentValues.put(KEY_LOCATION, "" + item.getLocation());
        db.insert(TABLE_TASKS,
            null,
            contentValues);

        //Log.i("addTask", "task added to db");
        System.out.println("task added to db");
        printOutTasks();
        db.close();
    }

    public TaskItem getManageTask(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, new String[] {KEY_ID, KEY_NAME, KEY_COMPLETED, KEY_PANDA_POINTS, KEY_TIME_OF_DAY, KEY_LOCATION}, KEY_ID + "=?", new String[] {String.valueOf(name) }, null, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        Boolean comp = cursor.getInt(2) > 0;
        TaskItem item = new TaskItem(Integer.parseInt(cursor.getString(0)), cursor.getString(1), comp, Integer.parseInt(cursor.getString(3)), cursor.getString(4), cursor.getString(5));

        return item;
    }

    public ArrayList<TaskItem> getAllTasks() {
        ArrayList<TaskItem> taskList = new ArrayList<TaskItem>();

        String selectQuery = "SELECT * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Boolean comp = cursor.getInt(2) > 0;
                TaskItem item = new TaskItem(Integer.parseInt(cursor.getString(0)), cursor.getString(1), comp, Integer.parseInt(cursor.getString(3)), cursor.getString(4), cursor.getString(5));
                taskList.add(item);
            } while (cursor.moveToNext());
        }

        return taskList;
    }

    public int getTaskCount() {
        String countQuery = "SELECT * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updateTask (TaskItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, item.getName());
        contentValues.put(KEY_COMPLETED, "" + item.getCompleted());
        contentValues.put(KEY_PANDA_POINTS, "" + item.getPandaPoints());
        contentValues.put(KEY_TIME_OF_DAY, "" + item.getTimeOfDay());
        contentValues.put(KEY_LOCATION, "" + item.getLocation());

        return db.update(TABLE_TASKS, contentValues, KEY_ID + " = ?", new String[]{String.valueOf(item.getId())});

    }
    public void deleteTask(TaskItem item) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID + " = ?", new String[] {String.valueOf(item.getId())});
        db.close();
    }

    public void printOutTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + TABLE_TASKS, null);
        int count = cursor.getCount();
        // print count
        System.out.println("" + count);
        String[] colNames = cursor.getColumnNames();
        boolean move = cursor.moveToFirst();
        System.out.println("" + move);

        for (int j = 0; j < count; ++j)
        {
            for (int i = 0; i < colNames.length; ++i)
            {
                System.out.println(colNames[i] + ": " + cursor.getString(i));
                //Log.i("printOutTasks", colNames[i] + ": " + cursor.getString(i));
            }
            //Log.i("printOutTasks", "END OF ROW");
            System.out.println("END OF ROW");

            cursor.moveToNext();
        }

    }

}
