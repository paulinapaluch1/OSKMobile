package com.example.osk.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String nw, String ns, String time, Integer sent) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NW, nw);
        contentValue.put(DatabaseHelper.NS, ns);
        contentValue.put(DatabaseHelper.TIME, time);
        contentValue.put(DatabaseHelper.SENT, sent);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.NW, DatabaseHelper.NS,DatabaseHelper.TIME,DatabaseHelper.SENT};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String nw, String ns) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NW, nw);
        contentValues.put(DatabaseHelper.NS, ns);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues,
                DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

    public SQLiteDatabase getDatabase(){
        return database;

    }


    public SQLiteDatabase getReadableDatabase() {
        return dbHelper.getReadableDatabase();
    }
}