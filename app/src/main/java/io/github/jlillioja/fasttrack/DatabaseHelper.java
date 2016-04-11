package io.github.jlillioja.fasttrack;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Jacob on 4/2/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements DatabaseContract {

    private static DatabaseHelper sInstance;
    private static String LOG_TAG = "DatabaseHelper";

    private static final String SQL_CREATE =
            "CREATE TABLE "+
            Click.TABLE_NAME+
            " (" +
            Click._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Click.COLUMN_NAME_TIMESTAMP + " TIMESTAMP)";

    private static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + Click.TABLE_NAME;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            Log.d(LOG_TAG, "Creating DatabaseHelper");
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    public void recreate() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DELETE);
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE);
        onCreate(db);
    }

    public void insertTimestamp() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + Click.TABLE_NAME + " (" + Click.COLUMN_NAME_TIMESTAMP + ") VALUES (datetime())");
    }

    public Cursor getAllClicks() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM "+Click.TABLE_NAME, null);
    }
}
