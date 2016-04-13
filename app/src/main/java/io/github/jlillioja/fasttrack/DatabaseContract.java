package io.github.jlillioja.fasttrack;

import android.provider.BaseColumns;

/**
 * Created by Jacob on 4/1/2016.
 */
public interface DatabaseContract {

    String DATABASE_NAME = "FTDB";
    int DATABASE_VERSION = 2;

    interface Click extends BaseColumns {
        String TABLE_NAME = "clicks";
        String COLUMN_NAME_TIMESTAMP = "timestamp";
        String COLUMN_NAME_AGENT = "agent";
    }

    interface Agent extends BaseColumns {
        String TABLE_NAME = "agents";
        String COLUMN_NAME_AGENT = "agent";
    }
}
