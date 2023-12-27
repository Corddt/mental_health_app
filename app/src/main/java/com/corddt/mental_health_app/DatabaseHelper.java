package com.corddt.mental_health_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MotivationalDiary.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME_DIARY = "diary_entries";
    private static final String TABLE_NAME_PLANS = "plans";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ENTRY = "entry";
    private static final String COLUMN_PLAN = "plan";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_COMPLETED = "completed";
    private static final String TABLE_NAME_REWARDS = "rewards";
    private static final String COLUMN_REWARD = "reward";

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
                COLUMN_TIMESTAMP + " TEXT DEFAULT (strftime('%Y-%m-%d', 'now')))";
        db.execSQL(createTableDiary);

        // 创建计划表
        String createTablePlans = "CREATE TABLE " + TABLE_NAME_PLANS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLAN + " TEXT, " +
                COLUMN_COMPLETED + " INTEGER DEFAULT 0, " +
                COLUMN_TIMESTAMP + " TEXT DEFAULT (strftime('%Y-%m-%d', 'now')))";
        db.execSQL(createTablePlans);
        // 创建奖励表
        String createTableRewards = "CREATE TABLE " + TABLE_NAME_REWARDS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REWARD + " TEXT)";
        db.execSQL(createTableRewards);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DIARY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PLANS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_REWARDS);
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

    public void addPlan(Plan plan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAN, plan.getContent());
        values.put(COLUMN_COMPLETED, plan.isCompleted() ? 1 : 0);
        db.insert(TABLE_NAME_PLANS, null, values);
        db.close();
    }


    public List<Plan> getAllPlans() {
        List<Plan> plans = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_PLANS, null);

        int idIndex = cursor.getColumnIndex(COLUMN_ID);
        int planIndex = cursor.getColumnIndex(COLUMN_PLAN);
        int completedIndex = cursor.getColumnIndex(COLUMN_COMPLETED);

        while (cursor.moveToNext()) {
            if (idIndex != -1 && planIndex != -1 && completedIndex != -1) {
                int id = cursor.getInt(idIndex);
                String content = cursor.getString(planIndex);
                boolean completed = cursor.getInt(completedIndex) == 1;
                plans.add(new Plan(id, content, completed));
            }
        }
        cursor.close();
        db.close();
        return plans;
    }

    public void updatePlan(int planId, String newPlan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAN, newPlan);
        db.update(TABLE_NAME_PLANS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(planId)});
        db.close();
    }

    public void togglePlanStatus(int planId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME_PLANS, new String[]{COLUMN_COMPLETED}, COLUMN_ID + " = ?", new String[]{String.valueOf(planId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int completedIndex = cursor.getColumnIndex(COLUMN_COMPLETED);
            if (completedIndex != -1) {
                int currentStatus = cursor.getInt(completedIndex);
                ContentValues values = new ContentValues();
                values.put(COLUMN_COMPLETED, currentStatus == 0 ? 1 : 0);
                db.update(TABLE_NAME_PLANS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(planId)});
            }
            cursor.close();
        }
        db.close();
    }

    public int getCompletedPlansCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME_PLANS + " WHERE " + COLUMN_COMPLETED + " = 1", null);
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
    // 获取指定日期的日记和计划
    public List<String> getDiaryAndPlansByDate(String date) {
        List<String> diaryAndPlans = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        // 获取日记
        Cursor diaryCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_DIARY + " WHERE " + COLUMN_TIMESTAMP + " = ?", new String[]{date});
        int entryIndexDiary = diaryCursor.getColumnIndex(COLUMN_ENTRY);
        while (diaryCursor.moveToNext()) {
            if (entryIndexDiary != -1) {
                String entry = diaryCursor.getString(entryIndexDiary);
                diaryAndPlans.add("Diary: " + entry);
            }
        }
        diaryCursor.close();

        // 获取计划
        Cursor planCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_PLANS + " WHERE " + COLUMN_TIMESTAMP + " = ?", new String[]{date});
        int contentIndexPlan = planCursor.getColumnIndex(COLUMN_PLAN);
        int completedIndexPlan = planCursor.getColumnIndex(COLUMN_COMPLETED);
        while (planCursor.moveToNext()) {
            if (contentIndexPlan != -1 && completedIndexPlan != -1) {
                String content = planCursor.getString(contentIndexPlan);
                boolean completed = planCursor.getInt(completedIndexPlan) == 1;
                diaryAndPlans.add("Plan: " + content + " (Completed: " + (completed ? "Yes" : "No") + ")");
            }
        }
        planCursor.close();

        db.close();
        return diaryAndPlans;
    }

}
