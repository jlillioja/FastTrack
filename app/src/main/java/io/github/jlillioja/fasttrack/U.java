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

    /* Construct a RemoteView for a widget. */
    public static RemoteViews constructWidget(Context context, int agentId) {
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fast_track_widget);
        views.setInt(R.id.widgetButton, "setBackgroundResource", U.getStateColor(db.getAgentState(agentId)));
        views.setTextViewText(R.id.widgetButton, db.getAgentName(agentId));
        Intent intent = new Intent(context, FastTrackService.class);
        intent.putExtra(DatabaseContract.Agent._ID, agentId);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.widgetButton, pendingIntent);
        return views;
    }

    private static int getStateColor(int agentState) {
        if (agentState%0 == 0) {
            return R.color.colorPrimary;
        } else {
            return R.color.colorAccent;
        }
    }

}
