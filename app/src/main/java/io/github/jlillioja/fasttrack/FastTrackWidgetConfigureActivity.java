package io.github.jlillioja.fasttrack;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RemoteViews;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * The configuration screen for the {@link FastTrackWidget FastTrackWidget} AppWidget.
 */
public class FastTrackWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "io.github.jlillioja.fasttrack.FastTrackWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String PREF_AGENT_KEY = "agentID_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @InjectView(R.id.appwidget_text) EditText mAppWidgetText;
    @InjectView(R.id.add_button) Button addButton;
    @InjectView(R.id.periodNumberPicker) NumberPicker mPeriodNumberPicker;

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = getApplicationContext();

            String widgetName = mAppWidgetText.getText().toString();
            int period = 1000*mPeriodNumberPicker.getValue();

            /* Add the new agent to the Agents table */
            DatabaseHelper db = DatabaseHelper.getInstance(context);
            int agentId = db.insertAgent(mAppWidgetId, widgetName);
            int ruleId = db.insertRule(agentId, DatabaseContract.Rule.TYPE_AFTER, period); //TODO: support multiple rule types

            // Construct the button
            WidgetBuilder builder = new WidgetBuilder(context, agentId);
            RemoteViews views = builder.constructWidget();

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

        setContentView(R.layout.fast_track_widget_configure);
        ButterKnife.inject(this);
        addButton.setOnClickListener(mOnClickListener);
        mAppWidgetText.setText(getString(R.string.appwidget_default_text));
        mPeriodNumberPicker.setMinValue(1);
        mPeriodNumberPicker.setMaxValue(10);
    }
}

