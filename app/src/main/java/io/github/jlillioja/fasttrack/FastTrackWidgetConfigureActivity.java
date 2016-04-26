package io.github.jlillioja.fasttrack;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;

/**
 * The configuration screen for the {@link FastTrackWidget FastTrackWidget} AppWidget.
 */
public class FastTrackWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "io.github.jlillioja.fasttrack.FastTrackWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String PREF_AGENT_KEY = "agentID_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetText;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = FastTrackWidgetConfigureActivity.this;

            String widgetName = mAppWidgetText.getText().toString();

            /* Add the new agent to the Agents table */
            int agentId = DatabaseHelper.getInstance(context).insertAgent(mAppWidgetId, widgetName);

            /* Construct the button */
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fast_track_widget);
            views.setTextViewText(R.id.widgetButton, widgetName);
            Intent intent = new Intent(context, FastTrackService.class);
            intent.putExtra(DatabaseContract.Agent._ID, agentId);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.widgetButton, pendingIntent);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager.getInstance(context).updateAppWidget(mAppWidgetId, views);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public FastTrackWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.fast_track_widget_configure);
        mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mAppWidgetText.setText(getString(R.string.appwidget_default_text));
    }


}

