package io.github.jlillioja.fasttrack;

import android.app.Activity;
import android.app.AlarmManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * The configuration screen for the {@link FastTrackWidget FastTrackWidget} AppWidget.
 */
public class FastTrackWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "io.github.jlillioja.fasttrack.FastTrackWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String PREF_AGENT_KEY = "agentID_";
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private int ruleType;
    private Bundle icicle;

    @InjectView(R.id.appwidget_text) EditText mAppWidgetText;
    @InjectView(R.id.add_button) Button addButton;
    @InjectView(R.id.time_EditText) EditText mAfterTime;
    @InjectView(R.id.increment_picker) ListView mAfterIncrement;
    @InjectView(R.id.periodTimePicker) TimePicker mAtTimePicker;
    @InjectView(R.id.afterRadioButton) RadioButton afterRadioButton;
    @InjectView(R.id.atRadioButton) RadioButton atRadioButton;
    @InjectView(R.id.afterOptions) RelativeLayout afterOptions;
    @InjectView(R.id.atOptions) RelativeLayout atOptions;
    @InjectView(R.id.increment_list_item) TextView incrementListItem;

    public static class TimeIncrement {
        public static final int SECONDS = 1000;
        public static final int MINUTES = SECONDS*60;
        public static final int HOURS = MINUTES*60;
        public static final int DAYS = HOURS*24;
        public static final int WEEKS = DAYS*7;
        public static final int[] INCREMENTS = {SECONDS, MINUTES, HOURS, DAYS, WEEKS};
    }

    private TimeIncrement increment;

    public FastTrackWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.icicle = icicle; //So we can restart fresh

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

        mAppWidgetText.setText(getString(R.string.appwidget_default_text));
    }

    View.OnClickListener mRadioButtonOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.atRadioButton:
                    clear();
                    setRuleType(AlarmManager.RTC);
                    makeAtOptionsAvailable();
                    break;
                case R.id.afterRadioButton:
                    clear();
                    setRuleType(AlarmManager.ELAPSED_REALTIME);
                    makeAfterOptionsAvailable();
                    break;
                default:
                    return;
            }
        }
    };

    View.OnClickListener mListOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {

        }
    };

    View.OnClickListener mAddWidgetOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = getApplicationContext();

            String widgetName = mAppWidgetText.getText().toString();
            long period = 0;
            switch (ruleType) {
                case AlarmManager.ELAPSED_REALTIME:
                    period = Integer.valueOf(mAfterTime.getText().toString());
                    break;
                case AlarmManager.RTC:
                    period = TimeUnit.HOURS.toMillis(mAtTimePicker.getHour())
                            +TimeUnit.MINUTES.toMillis(mAtTimePicker.getMinute());
                    break;
            }

            /* Add the new agent to the Agents table */
            DatabaseHelper db = DatabaseHelper.getInstance(context);
            int agentId = db.insertAgent(mAppWidgetId, widgetName);
            int ruleId = db.insertRule(agentId, ruleType, period);

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

    private void setRuleType(int ruleType) {
        this.ruleType = ruleType;
    }


    //TODO: reconsider this
    private void clear() {
        String name = mAppWidgetText.getText().toString();
        onCreate(icicle);
        mAppWidgetText.setText(name);
    }

    private void makeAtOptionsAvailable() {
        mAtTimePicker.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.VISIBLE);
    }

    private void makeAfterOptionsAvailable() {
        mAfterTime.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.VISIBLE);
    }
}

