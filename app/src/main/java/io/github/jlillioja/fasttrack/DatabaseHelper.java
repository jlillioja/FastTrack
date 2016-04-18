package io.github.jlillioja.fasttrack;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

/**
 * Created by Jacob on 4/2/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements DatabaseContract {

    private static DatabaseHelper sInstance;
    private static String LOG_TAG = "DatabaseHelper";

    //TODO: Consider abstracting SQL_CREATE statements for DRYness

    private static final String SQL_CREATE_CLICKS =
            "CREATE TABLE " +
                    Click.TABLE_NAME +
                    " (" +
                    Click._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Click.COLUMN_NAME_TIMESTAMP + " TIMESTAMP, " +
                    Click.COLUMN_NAME_AGENT + " INTEGER)";

    private static final String SQL_CREATE_AGENTS =
            "CREATE TABLE " +
                    Agent.TABLE_NAME +
                    " (" +
                    Agent._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Agent.COLUMN_NAME_AGENT + " TEXT, " +
                    Agent.COLUMN_NAME_WIDGETID + " INTEGER)";

    private static final String SQL_DELETE_CLICKS =
            "DROP TABLE IF EXISTS " + Click.TABLE_NAME;
    private static final String SQL_DELETE_AGENTS =
            "DROP TABLE IF EXISTS " + Agent.TABLE_NAME;

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
        db.execSQL(SQL_CREATE_CLICKS);
        db.execSQL(SQL_CREATE_AGENTS);

    }

    public void recreate(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_CLICKS);
        db.execSQL(SQL_DELETE_AGENTS);
        onCreate(db);
    }

    public void recreate() {
        recreate(getWritableDatabase());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        recreate(db);
    }

    public void insertTimestamp(int agentID) {
        SQLiteDatabase db = getWritableDatabase();
        //INSERT INTO CLICKS (TIMESTAMP, AGENT) VALUES (datetime(), agentID)
        db.execSQL("INSERT INTO "+Click.TABLE_NAME+" ("+Click.COLUMN_NAME_TIMESTAMP+", "+Click.COLUMN_NAME_AGENT+") VALUES (datetime(), "+String.valueOf(agentID)+")");
    }

    public void insertTimestamp() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + Click.TABLE_NAME + " (" + Click.COLUMN_NAME_TIMESTAMP + ") VALUES (datetime())");
    }

    public void insertAgent(String agent, int widgetId) {
        SQLiteDatabase db = getWritableDatabase();
        //INSERT INTO agents (agent_name, widgetID) VALUES (agent, widgetId)
        db.execSQL("INSERT INTO "+Agent.TABLE_NAME+" ("+Agent.COLUMN_NAME_AGENT+", "+Agent.COLUMN_NAME_WIDGETID+") VALUES ("+agent+", "+widgetId+")");
    }

    //Map from widgetId that a widget has to the agentId it needs
    public int getAgentId(int widgetId) {
        SQLiteDatabase db = getReadableDatabase();
        //SELECT _id FROM agents WHERE WIDGETID = widgetId
        //Should return a result set with 1 row and 1 column
        Cursor cursor = db.rawQuery("SELECT ? FROM ? WHERE ? = ?", new String[]{Agent._ID, Agent.TABLE_NAME, Agent.COLUMN_NAME_WIDGETID, String.valueOf(widgetId)});
        return cursor.getInt(0);
    }

    public Cursor getAllClicks() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + Click.TABLE_NAME, null);
    }
}
