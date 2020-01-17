package com.example.outof;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String LIST_DATABASE_NAME = "expandablelist.db";
    public static final String LIST_TABLE_NAME = "expandable_data";
    public static final String LIST_COL1 = "EXPAND_ID";
    public static final String LIST_COL2 = "EXPAND_ITEM";
    public static final int LIST_COL3 = 0;

    public SQLiteDatabase database;

    public DatabaseHelper(@Nullable Context context) {
        super(context, LIST_DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createExpandTable = "CREATE TABLE " + LIST_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "EXPAND_ITEM TEXT, " + "EXPAND_CHECKED INTEGER)";
        db.execSQL(createExpandTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LIST_TABLE_NAME);
    }

    public boolean addData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LIST_COL2, name);
        long result = db.insert(LIST_TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + LIST_TABLE_NAME, null);
        return res;
    }

    public void clearData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + LIST_TABLE_NAME);
    }
}
