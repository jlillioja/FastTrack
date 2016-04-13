package io.github.jlillioja.fasttrack;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutCompat;
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
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        db.insertTimestamp(agentID);
    }

}
