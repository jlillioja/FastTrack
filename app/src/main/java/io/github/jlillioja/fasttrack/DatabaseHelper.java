package io.github.jlillioja.fasttrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.GregorianCalendar;

/**
 * Created by Jacob on 4/2/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements DatabaseContract {

    private static DatabaseHelper sInstance;
    private static GregorianCalendar cal;
    protected static String LOG_TAG = "DatabaseHelper";

    //TODO: Consider abstracting SQL_CREATE statements for DRYness

    private static final String SQL_CREATE_CLICKS =
            "CREATE TABLE " +
                    Click.TABLE_NAME +
                    " (" +
                    Click._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Click.COLUMN_NAME_TIMESTAMP + " INTEGER, " +
                    Click.COLUMN_NAME_AGENT_ID + " INTEGER)";

    private static final String SQL_CREATE_AGENTS =
            "CREATE TABLE " +
                    Agent.TABLE_NAME +
                    " (" +
                    Agent._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Agent.COLUMN_NAME_AGENT_NAME + " TEXT, " +
                    Agent.COLUMN_NAME_WIDGETID + " INTEGER, " +
                    Agent.COLUMN_NAME_FILTERED + " INTEGER, " +
                    Agent.COLUMN_NAME_STATE + " INTEGER)";

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
        cal = new GregorianCalendar();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CLICKS);
        db.execSQL(SQL_CREATE_AGENTS);
        insertAgent(MainActivity.agentName, db);
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
        Log.d(LOG_TAG, "Inserting timestamp");
        SQLiteDatabase db = getWritableDatabase();
        //INSERT INTO CLICKS (TIMESTAMP, AGENT_ID) VALUES (cal.getTime(), agentID)
        //db.execSQL("INSERT INTO "+Click.TABLE_NAME+" ("+Click.COLUMN_NAME_TIMESTAMP+", "+Click.COLUMN_NAME_AGENT_ID+") VALUES ("+cal.getTime().toString()+", "+String.valueOf(agentID)+")");
        ContentValues values = new ContentValues();
        values.put(Click.COLUMN_NAME_TIMESTAMP, cal.getTimeInMillis());
        values.put(Click.COLUMN_NAME_AGENT_ID, agentID);
        db.insert(Click.TABLE_NAME, null, values);
    }

    /* TODO: How can it DRY this overloaded function? */

    // Returns the _ID of the newly inserted agent
    public int insertAgent(int widgetId, String agent) {
        SQLiteDatabase db = getWritableDatabase();
        //INSERT INTO agents (agent_name, widgetID) VALUES (agent, widgetId)
        ContentValues values = new ContentValues();
        values.put(Agent.COLUMN_NAME_AGENT_NAME, agent);
        values.put(Agent.COLUMN_NAME_WIDGETID, widgetId);
        values.put(Agent.COLUMN_NAME_FILTERED, 1); //No agent is filtered by default.
        values.put(Agent.COLUMN_NAME_STATE, 0); //Default state is 0, unclicked.
        int id = (int) db.insert(Agent.TABLE_NAME, null, values); //Casting the long the insert method returns. TODO: make ID long everywhere.
        return id;
    }

    // Special function just for main activity.
    public int insertAgent(String agent, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(Agent.COLUMN_NAME_AGENT_NAME, agent);
        values.put(Agent.COLUMN_NAME_FILTERED, 1);
        values.put(Agent.COLUMN_NAME_STATE, 0);
        int id = (int) db.insert(Agent.TABLE_NAME, null, values);
        return id;
    }

    /*
    Returns a cursor over unfiltered clicks with relevant information from multiple tables.
    Currently selects all columns in clicks and joins agent name from agents.
     */
    public Cursor getClicks() {
        //return this.getReadableDatabase().rawQuery("SELECT * FROM " + Click.TABLE_NAME, null);
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String fullAgentID = Agent.TABLE_NAME + "." + Agent._ID; //Otherwise it confuses Click._id with Agent._id
        queryBuilder.setTables(Click.TABLE_NAME + " JOIN " + Agent.TABLE_NAME + " ON " + Click.COLUMN_NAME_AGENT_ID + "=" + fullAgentID);
        String[] columns = {Click.COLUMN_NAME_TIMESTAMP, Agent.COLUMN_NAME_AGENT_NAME, Agent.COLUMN_NAME_FILTERED, fullAgentID};
        Cursor cursor = queryBuilder.query(db, columns, Agent.COLUMN_NAME_FILTERED + "=1", null, null, null, null);
        Log.d(LOG_TAG, "Cursor size: " + String.valueOf(cursor.getCount()));
        return cursor;
    }

    public String getAgentName(int agentID) {
        return getProperty(Agent.TABLE_NAME, agentID, Agent.COLUMN_NAME_AGENT_NAME);
    }

    public int getAgentWidgetId(int agentId) {
        return Integer.valueOf(getProperty(Agent.TABLE_NAME, agentId, Agent.COLUMN_NAME_WIDGETID));
    }

    public int getAgentState(int agentId) {
        return Integer.valueOf(getProperty(Agent.TABLE_NAME, agentId, Agent.COLUMN_NAME_STATE));
    }

    private String getProperty(String table, int id, String column) {
        SQLiteDatabase db = getReadableDatabase();

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(table);
        Cursor cursor = queryBuilder.query(db, new String[]{column}, Agent._ID + "=" + String.valueOf(id), null, null, null, null);
        //Should return a result set with 1 row and 1 column
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String result = cursor.getString(cursor.getColumnIndex(column));
            cursor.close();
            return result;
        } else {
            cursor.close();
            return null;
        }
    }

    public Cursor getAgents() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder query = new SQLiteQueryBuilder();
        query.setTables(Agent.TABLE_NAME);
        return query.query(db, new String[]{Agent._ID, Agent.COLUMN_NAME_AGENT_NAME, Agent.COLUMN_NAME_FILTERED, Agent.COLUMN_NAME_STATE}, null, null, null, null, null);
    }

    /* Update an agent. Update given as a ContentValues object with at least Agent._ID specified. */
    public void updateAgent(ContentValues change) {
        SQLiteDatabase db = getWritableDatabase();
        db.update(Agent.TABLE_NAME, change, Agent._ID + " = " + String.valueOf(change.get(Agent._ID)), null);
    }

    /* Debugging tool to print result set of a SQL query. */
    /* TODO: DOESN"T WORK */
    private void logResults(String query, String[] args) {
        Cursor cursor = getReadableDatabase().rawQuery(query, args);
        for (int i = 0; i < cursor.getCount(); i++) { //For each row
            String entry = "";
            cursor.moveToPosition(i);
            for (int j = 0; j < cursor.getColumnCount(); j++) { //And each column
                entry = entry + String.format("%-10s : %-20s \n", cursor.getColumnName(j), cursor.getString(j));
            }
            entry = entry + "\n";
            Log.d(LOG_TAG, entry);
        }
        cursor.close();
    }



    public int incrementState(int agentID) {
        int state = getAgentState(agentID)+1;
        updateAgent(U.formatAgent(agentID, Agent.COLUMN_NAME_STATE, String.valueOf(state)));
        return state;
    }

    public void resetState(int agentID) {
        updateAgent(U.formatAgent(agentID, Agent.COLUMN_NAME_STATE, String.valueOf(0)));
    }


}
