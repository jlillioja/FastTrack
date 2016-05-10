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
        if (RuleJudge.getInstance(context).judge(agentId)) {
            views.setInt(R.id.widgetButton, "setBackgroundColor", Color.RED);
        } else {
            views.setInt(R.id.widgetButton, "setBackgroundColor", Color.BLUE);
        }

        views.setTextViewText(R.id.widgetButton, db.getAgentName(agentId));

        // The message it sends to the FastTrackService
        Intent intent = new Intent(context, FastTrackService.class);
        intent.setAction(FastTrackService.ACTION_CLICK);
        intent.putExtra(DatabaseContract.Agent._ID, agentId);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.widgetButton, pendingIntent);

        return views;
    }

}
