package com.example.projekt_ptm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "points.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_POINTS = "points";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "nazwa";
    public static final String COL_DESC = "opis";
    public static final String COL_DATE = "data";
    public static final String COL_X = "x";
    public static final String COL_Y = "y";
    public static final String COL_IMAGE = "zdjecie";

    private static final String CREATE_TABLE_POINTS =
            "CREATE TABLE " + TABLE_POINTS + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_NAME + " TEXT, " +
                    COL_DESC + " TEXT, " +
                    COL_DATE + " TEXT, " +
                    COL_X + " REAL, " +
                    COL_Y + " REAL, " +
                    COL_IMAGE + " TEXT)";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_POINTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTS);
        onCreate(db);
    }

    public long insertPoint(String name, String desc, String date, double x, double y, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_DESC, desc);
        values.put(COL_DATE, date);
        values.put(COL_X, x);
        values.put(COL_Y, y);
        values.put(COL_IMAGE, image);

        long id = db.insert(TABLE_POINTS, null, values);
        db.close();
        return id;
    }

    public Cursor getAllPoints() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_POINTS, null);
    }
}
