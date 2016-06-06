package io.github.jlillioja.fasttrack;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

/**
 * Created by Jacob on 5/7/2016.
 */
public class WidgetBuilder {

    private Context context;
    private int agentId;

    public WidgetBuilder(Context context, int agentId) {
        this.context = context;
        this.agentId = agentId;
    }

    /* Construct a RemoteView for a widget. */
    public RemoteViews constructWidget() {
        DatabaseHelper db = DatabaseHelper.getInstance(context);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fast_track_widget);

        // Build the right state changes into the RemoteViews.

        int widgetColor;
        //TODO: there's gotta be a better way
        switch (db.getAgentState(agentId)) {
            case 0:
                widgetColor = Color.GRAY;
                break;
            case 1:
                widgetColor = Color.BLUE;
                break;
            case 2: case 3:
                widgetColor = Color.GREEN;
                break;
            case 4: case 5: case 6: case 7:
                widgetColor = Color.YELLOW;
                break;
            default:
                widgetColor = Color.RED;
                break;
        }

        views.setInt(R.id.widgetButton, "setBackgroundColor", widgetColor);

        views.setTextViewText(R.id.widgetButton, db.getAgentName(agentId)+"\n"+"+"+String.valueOf(db.getAgentState(agentId)));

        // The message it sends to the FastTrackService
        Intent intent = new Intent(context, FastTrackService.class);
        intent.setAction(FastTrackService.ACTION_CLICK);
        intent.putExtra(DatabaseContract.Agent._ID, agentId);
        PendingIntent pendingIntent = PendingIntent.getService(context, agentId, intent, 0);
        views.setOnClickPendingIntent(R.id.widgetButton, pendingIntent);

        return views;
    }

}
