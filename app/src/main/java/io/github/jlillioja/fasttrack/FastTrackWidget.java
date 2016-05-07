package io.github.jlillioja.fasttrack;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link FastTrackWidgetConfigureActivity FastTrackWidgetConfigureActivity}
 */
public class FastTrackWidget extends AppWidgetProvider {

    private static final String mOnClick = "FastTrackOnClick";

    private static final int defaultAgentId = 2;



    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

