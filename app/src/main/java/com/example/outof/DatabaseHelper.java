package com.example.outof;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    //public static final String TAG = "LOG";

    private static DatabaseHelper sInstance;
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

    protected static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(@Nullable Context context) {
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHILDREN);
        onCreate(db);
    }

    //Deletes all data from both Tables
    public void clearData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_VIEW);
    }

    //Makes all groups not Expanded
    public void clearGroups() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_GROUP + " SET " + KEY_GROUP_EXPANDED + " = 0");
    }

    //Makes all children not checked
    public void clearChildren() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_CHILDREN + " SET " + KEY_CHECKED + " = 0");
    }

    //------------------------------ VIEW TABLE -------------------------------------------------------------------------------------------

    //Updates ViewList item
    public void updateView(String name, boolean checked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ITEM, name);
        contentValues.put(KEY_CHECKED, checked);
        db.update(TABLE_VIEW, contentValues,KEY_ITEM + " =?", new String[]{name});
    }

    //Add data to View List Table
    public void addDataToView(String name, int checked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ITEM, name);
        contentValues.put(KEY_CHECKED, checked);
        db.insert(TABLE_VIEW, null, contentValues);
    }

    //Retrieve data from View List Table
    public Cursor getListContents_View() {
        //changed to readable
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_VIEW, null);
    }

    //Remove item from View list when child item unchecked
    public boolean removeDataFromView(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_VIEW, KEY_ITEM + "=?", new String[]{name}) > 0;
    }

    //------------------------------ GROUP TABLE -------------------------------------------------------------------------------------------

    //Add group to DB
    public void addGroup(String groupName, int expanded) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_GROUP_NAME, groupName);
        contentValues.put(KEY_GROUP_EXPANDED, expanded);
        db.insert(TABLE_GROUP, null,contentValues);
    }

    //Update group expanded variable
    public void updateGroup(String groupName, int expanded) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_GROUP_EXPANDED, expanded);
        db.update(TABLE_GROUP, contentValues, KEY_GROUP_NAME + " = ?", new String[]{groupName});
    }

    //Query all group items in MakeList Group Table
    public Cursor getListContents_Group() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_GROUP,null);
    }

    //------------------------------ CHILDREN TABLE -------------------------------------------------------------------------------------------

    //Add child to DB with link to Group
    public void addChild(String groupName, String childName, int childChecked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_GROUP_NAME, groupName);
        contentValues.put(KEY_ITEM, childName);
        contentValues.put(KEY_CHECKED, childChecked);
        db.insert(TABLE_CHILDREN,null, contentValues);
    }

    //Update child checked variable
    public void updateChild(String name, int checked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CHECKED, checked);
        db.update(TABLE_CHILDREN, contentValues,KEY_ITEM + " = ?", new String[]{name});
    }

    //Query all child items in MakeList Child Table
    public Cursor getListContents_Children() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CHILDREN, null);
    }
}
