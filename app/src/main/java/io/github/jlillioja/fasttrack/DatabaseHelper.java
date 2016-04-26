package io.github.jlillioja.fasttrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
                    Click.COLUMN_NAME_AGENT_ID + " INTEGER)";

    private static final String SQL_CREATE_AGENTS =
            "CREATE TABLE " +
                    Agent.TABLE_NAME +
                    " (" +
                    Agent._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Agent.COLUMN_NAME_AGENT_NAME + " TEXT, " +
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
        //INSERT INTO CLICKS (TIMESTAMP, AGENT_ID) VALUES (datetime(), agentID)
        db.execSQL("INSERT INTO "+Click.TABLE_NAME+" ("+Click.COLUMN_NAME_TIMESTAMP+", "+Click.COLUMN_NAME_AGENT_ID +") VALUES (datetime(), "+String.valueOf(agentID)+")");
    }

    public void insertTimestamp() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + Click.TABLE_NAME + " (" + Click.COLUMN_NAME_TIMESTAMP + ") VALUES (datetime())");
    }

    // Returns the _ID of the newly inserted agent
    public int insertAgent(int widgetId, String agent) {
        SQLiteDatabase db = getWritableDatabase();
        //INSERT INTO agents (agent_name, widgetID) VALUES (agent, widgetId)
        ContentValues values = new ContentValues();
        values.put(Agent.COLUMN_NAME_AGENT_NAME, agent);
        values.put(Agent.COLUMN_NAME_WIDGETID, widgetId);
        int id = (int) db.insert(Agent.TABLE_NAME, null, values); //Casting the long the insert method returns. TODO: make ID long everywhere.
        return id;
    }

    public Cursor getAllClicks() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + Click.TABLE_NAME, null);
    }

    public String getAgentName(int agentID) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Agent.TABLE_NAME);
        Cursor cursor = queryBuilder.query(db, new String[]{Agent.COLUMN_NAME_AGENT_NAME}, Agent._ID +"="+String.valueOf(agentID), null, null, null, null);
        //Should return a result set with 1 row and 1 column
        if (cursor.getCount()>0) {
            return cursor.getString(cursor.getColumnIndex(Agent.COLUMN_NAME_AGENT_NAME));
        } else {return null;}
    }

    //TODO: does not validate name, only existence.
    public int validateAgent(int widgetId, String name) {
        SQLiteDatabase db = getReadableDatabase();
        //SELECT agent_name FROM agents WHERE WIDGETID = widgetId
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Agent.TABLE_NAME);
        Cursor cursor = queryBuilder.query(db, new String[]{Agent._ID}, Agent.COLUMN_NAME_WIDGETID+"="+String.valueOf(widgetId), null, null, null, null);
        //Should return a result set with 1 row and 1 column
        int id;
        if (cursor.getCount()>0) {
            id = cursor.getInt(cursor.getColumnIndex(Agent._ID));
        } else {
            id = insertAgent(widgetId, name);
        }
        return id;
    }
}
