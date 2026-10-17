package com.example.warehouseinventory.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserRepository {

    private final AppDatabaseHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new AppDatabaseHelper(context);
    }

    public boolean createUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AppDatabaseHelper.COL_USERNAME, username);
        values.put(AppDatabaseHelper.COL_PASSWORD, password);

        long rowId = db.insert(AppDatabaseHelper.TABLE_USERS, null, values);
        return rowId != -1;
    }

    public boolean validateLogin(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = AppDatabaseHelper.COL_USERNAME + "=? AND " + AppDatabaseHelper.COL_PASSWORD + "=?";
        String[] args = { username, password };

        Cursor c = db.query(
                AppDatabaseHelper.TABLE_USERS,
                new String[]{ AppDatabaseHelper.COL_USER_ID },
                selection,
                args,
                null, null, null
        );

        boolean ok = c.moveToFirst();
        c.close();
        return ok;
    }

    public boolean userExists(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(
                AppDatabaseHelper.TABLE_USERS,
                new String[]{ AppDatabaseHelper.COL_USER_ID },
                AppDatabaseHelper.COL_USERNAME + "=?",
                new String[]{ username },
                null, null, null
        );

        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }
}