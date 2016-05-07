package io.github.jlillioja.fasttrack;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;

/**
 * Created by jacob on 4/11/2016.
 */
public class FastTrackService extends IntentService {

    final static String COMPONENT_NAME = FastTrackService.class.getName();
    final static String LOG_TAG = FastTrackService.class.getSimpleName();
    final static String ACTION_TRACK = "action_track";

    public FastTrackService() {
        super(COMPONENT_NAME);
    }
    public void onCreate() { super.onCreate(); }

    @Override
    public void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "Handling intent");
        int agentID = intent.getIntExtra(DatabaseContract.Agent._ID, 0);

        // Update database
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        db.insertTimestamp(agentID);
        int state = db.incrementState(agentID);
        Log.d(LOG_TAG, "Agent "+String.valueOf(agentID)+" now has state "+String.valueOf(state));

        // Update widget
        WidgetBuilder builder = new WidgetBuilder(this, agentID);
        AppWidgetManager.getInstance(this).updateAppWidget(db.getAgentWidgetId(agentID), builder.constructWidget());
    }

}
