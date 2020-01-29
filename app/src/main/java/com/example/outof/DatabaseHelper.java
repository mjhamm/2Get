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
    private static final String LIST_DATABASE_NAME = "shoppingList.db";

    //Table Names
    private static final String TABLE_VIEW = "viewList";

    private static final String TABLE_GROUP = "groupList";
    private static final String TABLE_CHILDREN = "childrenList";

    //Common Columns
    private static final String KEY_ID = "id";
    private static final String KEY_ITEM = "name";
    private static final String KEY_CHECKED = "checked";
    private static final String KEY_GROUP_NAME = "groupName";
    private static final String KEY_GROUP_EXPANDED = "expanded";

    public DatabaseHelper(@Nullable Context context) {
        super(context, LIST_DATABASE_NAME, null, DB_VERSION);
    }

    //Create View List Table
    private static final String CREATE_TABLE_VIEW = "CREATE TABLE IF NOT EXISTS " + TABLE_VIEW + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_ITEM +
            " TEXT, " + KEY_CHECKED + " INTEGER)";

    private static final String CREATE_TABLE_GROUP = "CREATE TABLE IF NOT EXISTS " + TABLE_GROUP + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_GROUP_NAME + " TEXT, " +KEY_GROUP_EXPANDED + " INTEGER)";

    private static final String CREATE_TABLE_CHILDREN = "CREATE TABLE IF NOT EXISTS " + TABLE_CHILDREN + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_GROUP_NAME + " TEXT, " + KEY_ITEM + " TEXT, "
            + KEY_CHECKED + " INTEGER)";

    //------------------------------ ALL TABLES -------------------------------------------------------------------------------------------

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_VIEW);
        db.execSQL(CREATE_TABLE_GROUP);
        db.execSQL(CREATE_TABLE_CHILDREN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIEW);
    }

    //Deletes all data from both Tables
    public void clearData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_VIEW);
    }

    //------------------------------ VIEW TABLE -------------------------------------------------------------------------------------------

    //Update View List Table if row exists
    public void updateView(String name, boolean checked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ITEM, name);
        contentValues.put(KEY_CHECKED, checked);
        db.update(TABLE_VIEW, contentValues,KEY_ITEM + " =?", new String[]{name});
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

    //Retrieve data from View List Table
    public Cursor getListContents_View() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_VIEW, null);
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

    //Remove data from View List Table
    public boolean removeDataFromView(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_VIEW, KEY_ITEM + "=?", new String[]{name}) > 0;
    }

    //------------------------------ GROUP TABLE -------------------------------------------------------------------------------------------

    public Cursor getListContents_Group() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_GROUP,null);
    }

    //------------------------------ CHILDREN TABLE -------------------------------------------------------------------------------------------

    public void addChild(String groupName, String childName, int childChecked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_GROUP_NAME, groupName);
        contentValues.put(KEY_ITEM, childName);
        contentValues.put(KEY_CHECKED, childChecked);
        db.insert(TABLE_CHILDREN,null, contentValues);
    }


    public Cursor getChildren(String groupName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CHILDREN + " WHERE " + KEY_GROUP_NAME + " = " + groupName;
        return db.rawQuery(query,null);
    }

    public Cursor getListContents_Children() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CHILDREN, null);
    }
}
