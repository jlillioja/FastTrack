package io.github.jlillioja.fasttrack;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by jacob on 5/3/2016.
 */

// Utility Class
public class U {

    public static ContentValues formatAgent(int agentId, String field, String newValue) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.Agent._ID, agentId);
        contentValues.put(field, newValue);
        return contentValues;
    }

}