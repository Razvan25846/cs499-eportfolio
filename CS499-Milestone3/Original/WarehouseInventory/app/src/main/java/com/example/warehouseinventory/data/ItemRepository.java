package com.example.warehouseinventory.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.warehouseinventory.model.InventoryItem;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {

    private final AppDatabaseHelper dbHelper;

    public ItemRepository(Context context) {
        dbHelper = new AppDatabaseHelper(context);
    }

    public long addItem(String name, String sku, int qty) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppDatabaseHelper.COL_ITEM_NAME, name);
        values.put(AppDatabaseHelper.COL_ITEM_SKU, sku);
        values.put(AppDatabaseHelper.COL_ITEM_QTY, qty);
        return db.insert(AppDatabaseHelper.TABLE_ITEMS, null, values);
    }

    public List<InventoryItem> getAllItems() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<InventoryItem> items = new ArrayList<>();

        Cursor c = db.query(
                AppDatabaseHelper.TABLE_ITEMS,
                null,
                null,
                null,
                null, null,
                AppDatabaseHelper.COL_ITEM_NAME + " COLLATE NOCASE ASC"
        );

        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_ITEM_ID));
            String name = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_ITEM_NAME));
            String sku = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_ITEM_SKU));
            int qty = c.getInt(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_ITEM_QTY));
            items.add(new InventoryItem(id, name, sku, qty));
        }

        c.close();
        return items;
    }

    public boolean updateQty(long itemId, int newQty) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppDatabaseHelper.COL_ITEM_QTY, newQty);

        int rows = db.update(
                AppDatabaseHelper.TABLE_ITEMS,
                values,
                AppDatabaseHelper.COL_ITEM_ID + "=?",
                new String[]{ String.valueOf(itemId) }
        );
        return rows > 0;
    }

    public boolean deleteItem(long itemId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(
                AppDatabaseHelper.TABLE_ITEMS,
                AppDatabaseHelper.COL_ITEM_ID + "=?",
                new String[]{ String.valueOf(itemId) }
        );
        return rows > 0;
    }
}