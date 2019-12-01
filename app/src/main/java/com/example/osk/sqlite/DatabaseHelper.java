package com.example.osk.sqlite;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "gpspoints";

    public static final String _ID = "id";
    public static final String NW = "nw";
    public static final String NS = "ns";
    public static final String TIME = "time";
    public static final String SENT = "sent";

    static final String DB_NAME = "OSK.DB";

    static final int DB_VERSION = 1;

  //  private static final String CREATE_TABLE = "create table if not exists " + TABLE_NAME + "(" + _ID
          //  + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NW + " TEXT NOT NULL, " + NS + " TEXT NOT NULL, "+TIME+" TEXT NOT NULL," +SENT +" INTEGER "+")";

    private static final String CREATE_TABLE ="CREATE TABLE IF NOT EXISTS gpspoints(id integer primary key autoincrement, nw varchar(255) not null, ns varchar(255) not null, time varchar(255) not null, sent integer not null);";




    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}