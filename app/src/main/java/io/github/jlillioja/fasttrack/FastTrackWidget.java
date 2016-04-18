package io.github.jlillioja.fasttrack;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.RemoteViews;

import butterknife.InjectView;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link FastTrackWidgetConfigureActivity FastTrackWidgetConfigureActivity}
 */
public class FastTrackWidget extends AppWidgetProvider {

    private static final String mOnClick = "FastTrackOnClick";

    private static final int agentID = 2;

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        String widgetText = FastTrackWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        int agentID = DatabaseHelper.getInstance(context).getAgentId(appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fast_track_widget);
        views.setTextViewText(R.id.widgetButton, widgetText);
        Intent intent = new Intent(context, FastTrackService.class);
        intent.putExtra(DatabaseContract.Agent._ID, agentID);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.widgetButton, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

