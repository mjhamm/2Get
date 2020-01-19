package com.example.outof;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Database Version
    private static final int DB_VERSION = 1;

    //Database Name
    private static final String LIST_DATABASE_NAME = "shoppingList";

    //Table Names
    private static final String TABLE_VIEW = "viewList";
    private static final String TABLE_MAKE = "makeList";

    //Common Columns
    private static final String KEY_ID = "id";
    private static final String KEY_ITEM = "name";
    private static final String KEY_CHECKED = "checked";

    public DatabaseHelper(@Nullable Context context) {
        super(context, LIST_DATABASE_NAME, null, DB_VERSION);
    }

    //Create View List Table
    private static final String CREATE_TABLE_VIEW = "CREATE TABLE " + TABLE_VIEW + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_ITEM +
            " TEXT, " + KEY_CHECKED + " INTEGER)";

    //Create Make List Table
    private static final String CREATE_TABLE_MAKE = "CREATE TABLE " + TABLE_MAKE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_ITEM +
            " TEXT, " + KEY_CHECKED + " INTEGER)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_VIEW);
        db.execSQL(CREATE_TABLE_MAKE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIEW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAKE);
    }

    //Update View List Table if row exists
    public void updateView(String name, boolean checked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ITEM, name);
        contentValues.put(KEY_CHECKED, checked);
        db.update(TABLE_VIEW, contentValues,KEY_ITEM + " =?", new String[]{name});
    }

    //Update Make List Table if row exists
    public void updateMake(String name, boolean checked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ITEM, name);
        contentValues.put(KEY_CHECKED, checked);
        db.update(TABLE_MAKE, contentValues, KEY_ITEM + "=?", new String[]{name});
    }

    //Add data to View List Table
    public boolean addDataToView(String name, int checked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ITEM, name);
        contentValues.put(KEY_CHECKED, checked);
        long result = db.insert(TABLE_VIEW, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //Add data to Make List Table
    public boolean addDataToMake(String name, int checked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ITEM, name);
        contentValues.put(KEY_CHECKED, checked);
        long result = db.insert(TABLE_MAKE, null, contentValues);
        return result != -1;
    }

    //Remove data from View List Table
    public boolean removeDataFromView(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_VIEW, KEY_ITEM + "=?", new String[]{name}) > 0;
    }

    //Remove data from View List Table
    public boolean removeDataFromMake(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MAKE, KEY_ITEM + "=?", new String[]{name}) > 0;
    }

    //Retrieve data from View List Table
    public Cursor getListContents_View() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_VIEW, null);
    }

    //Retrieve data from Make List Table
    public Cursor getListContents_Make() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MAKE, null);
    }

    //Check for duplicates in View Table
    public boolean dupCheckViewTable(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur;
        cur = db.query(TABLE_VIEW, null, KEY_ITEM + " =?"/* AND " + KEY_CHECKED + "=?"*/, new String[]{name}, null, null, null, null);
        if (cur != null && cur.getCount() > 0) {
            cur.close();
            return true;
        } else {
            return false;
        }
    }

    //Check for duplicates in Make Table
    public boolean dupCheckMakeTable(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur;
        cur = db.query(TABLE_MAKE, null, KEY_ITEM + "=?"/* AND " KEY_CHECKED + "=?"*/, new String[]{name}, null, null, null, null);
        if (cur != null && cur.getCount() > 0) {
            cur.close();
            return true;
        } else {
            return false;
        }
    }

    //Deletes all data from both Tables
    public void clearData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_VIEW);
        db.execSQL("DELETE FROM " + TABLE_MAKE);
    }
}
