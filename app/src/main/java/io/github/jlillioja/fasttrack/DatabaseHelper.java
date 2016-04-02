package io.github.jlillioja.fasttrack;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jacob on 4/2/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements DatabaseContract {

    private SQLiteDatabase db;

    private static final String SQL_CREATE =
            "CREATE TABLE "+
            Click.TABLE_NAME+
            " (" +
            Click._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Click.COLUMN_NAME_TIMESTAMP + " TIMESTAMP)";

    private static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + Click.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE);
        onCreate(db);
    }

    public void insertTimestamp() {
        db.execSQL("INSERT INTO " + Click.TABLE_NAME + " (" + Click.COLUMN_NAME_TIMESTAMP + ") VALUES (datetime())");
    }

    public Cursor getAllClicks() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM "+Click.TABLE_NAME, null);
    }
}
