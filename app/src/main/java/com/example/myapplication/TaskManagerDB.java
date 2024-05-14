package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerDB extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "tasks";
    private static final String DB_NAME = "taskManagerDatabase.db";
    private final Context mContext;

    public TaskManagerDB(Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlDB = "CREATE TABLE tasks (id TEXT PRIMARY KEY, title TEXT, description TEXT, duedate TEXT)";
        db.execSQL(sqlDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not used in this example
    }

    private boolean isValidDateFormat(String date) {
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        return date.matches(regex);
    }

    private boolean isValidDate(String date) {
        String[] parts = date.split("-");
        if (parts.length != 3) {
            return false;
        }
        for (int i = 0; i < 10; i++) {
            if (i == 4 || i == 7) continue;
            if (date.charAt(i) < '0' || date.charAt(i) > '9') return false;
        }

        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        if (year <= 0 || month < 1 || month > 12 || day < 1 || day > 31) {
            return false;
        }

        if (month == 2) {
            if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                return day <= 29;
            } else {
                return day <= 28;
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return day <= 30;
        }
        return true;
    }

    public Boolean addTask(Task task) {
        if (!isValidDateFormat(task.getDueDate())) {
            Toast.makeText(mContext, "Invalid due date format. Please use yyyy-MM-dd format.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidDate(task.getDueDate())) {
            Toast.makeText(mContext, "Invalid due date. Please make sure that the date is valid!", Toast.LENGTH_SHORT).show();
            return false;
        }
        SQLiteDatabase sql_DB = getWritableDatabase();
        ContentValues cal = new ContentValues();
        cal.put("id", task.getId());
        cal.put("title", task.getTitle());
        cal.put("description", task.getDescription());
        cal.put("dueDate", task.getDueDate());

        long rowId = sql_DB.insert(TABLE_NAME, null, cal);
        sql_DB.close();
        if (rowId > -1) {
            Toast.makeText(mContext, "Task added successfully!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public Task getTask(String id) {
        SQLiteDatabase sql_DB = this.getReadableDatabase();
        Cursor query = sql_DB.query(TABLE_NAME, new String[]{"id", "title", "description", "dueDate"}, "id=?", new String[]{id}, null, null, null, null);
        if (query != null) {
            query.moveToFirst();
        } else {
            query.close();
            sql_DB.close();
            return null;
        }
        Task task = new Task(query.getString(0), query.getString(1), query.getString(2), query.getString(3));
        query.close();
        sql_DB.close();
        return task;
    }

    public List<Task> getAllTask() {
        SQLiteDatabase sql_DB = getReadableDatabase();
        Cursor query = sql_DB.query(TABLE_NAME, null, null, null, null, null, "duedate");
        List<Task> result = new ArrayList<>();

        while (query.moveToNext()) {
            result.add(new Task(query.getString(0), query.getString(1), query.getString(2), query.getString(3)));
        }
        query.close();
        sql_DB.close();
        return result;
    }

    public Boolean deleteTask(String id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = {id};
        int numRowsDeleted = db.delete(TABLE_NAME, "id" + "= ?", whereArgs);
        db.close();
        return numRowsDeleted > 0;
    }

    public Boolean deleteAllTask() {
        List<Task> all = getAllTask();
        for (Task task : all) {
            if (!deleteTask(task.getId())) return false;
        }
        return true;
    }
}
