package com.example.warehouseinventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "warehouse_inventory.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "_id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    public static final String TABLE_ITEMS = "items";
    public static final String COL_ITEM_ID = "_id";
    public static final String COL_ITEM_NAME = "name";
    public static final String COL_ITEM_SKU = "sku";
    public static final String COL_ITEM_QTY = "qty";

    public AppDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_USERS + " (" +
                        COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_USERNAME + " TEXT UNIQUE NOT NULL, " +
                        COL_PASSWORD + " TEXT NOT NULL" +
                        ");"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_ITEMS + " (" +
                        COL_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_ITEM_NAME + " TEXT NOT NULL, " +
                        COL_ITEM_SKU + " TEXT NOT NULL, " +
                        COL_ITEM_QTY + " INTEGER NOT NULL DEFAULT 0" +
                        ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }
}