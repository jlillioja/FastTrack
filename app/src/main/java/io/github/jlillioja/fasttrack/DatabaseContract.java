package io.github.jlillioja.fasttrack;

import android.app.AlarmManager;
import android.provider.BaseColumns;

/**
 * Created by Jacob on 4/1/2016.
 */
public interface DatabaseContract {

    String DATABASE_NAME = "FTDB";
    int DATABASE_VERSION = 7;

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
        String COLUMN_NAME_STATE = "state";
    }

    interface Rule extends BaseColumns {
        String TABLE_NAME = "rules";

        String COLUMN_NAME_TYPE = "rule_type";
        int TYPE_AT_TIME = AlarmManager.RTC;
        int TYPE_AFTER = AlarmManager.ELAPSED_REALTIME;

        /* For TYPE_AFTER rules */
        String COLUMN_NAME_TIME = "time_trigger";

        /* The ID of the agent to whom this rule belongs */
        String COLUMN_NAME_AGENT = "agent";
    }
}
