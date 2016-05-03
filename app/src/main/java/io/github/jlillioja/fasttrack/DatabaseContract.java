package io.github.jlillioja.fasttrack;

import android.provider.BaseColumns;

/**
 * Created by Jacob on 4/1/2016.
 */
public interface DatabaseContract {

    String DATABASE_NAME = "FTDB";
    int DATABASE_VERSION = 5;

    interface Click extends BaseColumns {
        String TABLE_NAME = "clicks";
        /* Timestamp is in milliseconds from epoch. */
        String COLUMN_NAME_TIMESTAMP = "timestamp";
        String COLUMN_NAME_AGENT_ID = "agent_id";
    }

    interface Agent extends BaseColumns {
        String TABLE_NAME = "agents";
        String COLUMN_NAME_AGENT_NAME = "agent_name";
        String COLUMN_NAME_WIDGETID = "widgetId";
        String COLUMN_NAME_FILTERED = "filtered";
    }
}
