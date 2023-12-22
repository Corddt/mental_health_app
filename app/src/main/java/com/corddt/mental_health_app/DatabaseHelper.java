package com.corddt.mental_health_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MotivationalDiary.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME_DIARY = "diary_entries";
    private static final String TABLE_NAME_PLANS = "plans";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ENTRY = "entry";
    private static final String COLUMN_PLAN = "plan";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_COMPLETED = "completed";
    private static final String TABLE_NAME_REWARDS = "rewards"; // 奖励表的名称
    private static final String COLUMN_REWARD = "reward";       // 奖励表的列名


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建日记表
        String createTableDiary = "CREATE TABLE " + TABLE_NAME_DIARY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ENTRY + " TEXT, " +
                COLUMN_COMPLETED + " INTEGER DEFAULT 0, " +
                COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTableDiary);

        // 创建计划表
        String createTablePlans = "CREATE TABLE " + TABLE_NAME_PLANS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLAN + " TEXT)";
        db.execSQL(createTablePlans);

        // 创建奖励表
        String createTableRewards = "CREATE TABLE " + TABLE_NAME_REWARDS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REWARD + " TEXT)";
        db.execSQL(createTableRewards);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 删除旧表并创建新表
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DIARY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PLANS);
        onCreate(db);
    }

    public void addDiaryEntry(String entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ENTRY, entry);
        db.insert(TABLE_NAME_DIARY, null, values);
        db.close();
    }

    public List<String> getAllDiaries() {
        List<String> diaries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_DIARY, null);
        int columnIndex = cursor.getColumnIndex(COLUMN_ENTRY);
        if (columnIndex != -1) { // 确保 columnIndex 是有效的
            while (cursor.moveToNext()) {
                diaries.add(cursor.getString(columnIndex));
            }
        }
        cursor.close();
        db.close();
        return diaries;
    }

    public void addPlan(String plan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAN, plan);
        db.insert(TABLE_NAME_PLANS, null, values);
        db.close();
    }

    public List<String> getAllPlans() {
        List<String> plans = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_PLANS, null);
        int columnIndex = cursor.getColumnIndex(COLUMN_PLAN);
        if (columnIndex != -1) { // 确保 columnIndex 是有效的
            while (cursor.moveToNext()) {
                plans.add(cursor.getString(columnIndex));
            }
        }
        cursor.close();
        db.close();
        return plans;
    }

    public int getCompletedTasksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME_DIARY + " WHERE " + COLUMN_COMPLETED + " = 1", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public void updateTaskCompleted(int taskId, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPLETED, isCompleted ? 1 : 0);
        db.update(TABLE_NAME_DIARY, values, COLUMN_ID + " = ?", new String[]{String.valueOf(taskId)});
        db.close();
    }

    public List<String> getAllRewards() {
        List<String> rewards = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_REWARDS, null);
        int columnIndex = cursor.getColumnIndex(COLUMN_REWARD);
        if (columnIndex != -1) {
            while (cursor.moveToNext()) {
                rewards.add(cursor.getString(columnIndex));
            }
        }
        cursor.close();
        db.close();
        return rewards;
    }
    // 新增检查用户不活动状态的方法
    public boolean checkUserInactivity(int days) {
        SQLiteDatabase db = this.getReadableDatabase();

        // 获取当前日期减去指定天数的日期
        String dateLimit = "datetime('now', '-" + days + " days')";

        // 构建查询来检查在这个日期之后是否有任务完成
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_DIARY +
                " WHERE " + COLUMN_TIMESTAMP + " > " + dateLimit;
        Cursor cursor = db.rawQuery(query, null);
        boolean isInactive = true;
        if (cursor.moveToFirst()) {
            isInactive = cursor.getInt(0) == 0; // 如果计数为0，则表示用户不活跃
        }
        cursor.close();
        db.close();
        return isInactive;
    }
}
