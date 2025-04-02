package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "orders.db";
    private static final String TABLE_NAME = "orders";
    private static final String COL_ID = "id";
    private static final String COL_CUSTOMER = "customer_name";
    private static final String COL_ITEMS = "items";
    private static final String COL_TOTAL_PRICE = "total_price";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CUSTOMER + " TEXT, " +
                COL_ITEMS + " TEXT, " +
                COL_TOTAL_PRICE + " REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertOrder(String customerName, String items, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CUSTOMER, customerName);
        contentValues.put(COL_ITEMS, items);
        contentValues.put(COL_TOTAL_PRICE, totalPrice);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }
}

