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

    private final static String prefix = "io.github.jlillioja.FastTrack";
    public final static String ACTION_CLICK = prefix+".ACTION_CLICK";
    public static final String ACTION_ALARM = prefix+".ACTION_ALARM";

    public FastTrackService() {
        super(COMPONENT_NAME);
    }
    public void onCreate() { super.onCreate(); }

    @Override
    public void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "Handling intent");
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        RuleJudge judge = RuleJudge.getInstance(this);
        String action = intent.getAction();
        int agentID = intent.getIntExtra(DatabaseContract.Agent._ID, 0);
        int state=0;

        switch (action) {
            case ACTION_ALARM:
                int ruleId = intent.getIntExtra("RuleId", 0);
                Log.d(LOG_TAG, "Alarm "+String.valueOf(ruleId));
                state = db.resetState(agentID);
                break;
            case ACTION_CLICK:
                Log.d(LOG_TAG, "Click!");
                db.insertTimestamp(agentID);
                state = db.incrementState(agentID);
                judge.resetAgentAlarms(agentID);
                break;
        }

        Log.d(LOG_TAG, "Agent "+String.valueOf(agentID)+" now has state "+String.valueOf(state));

        // Update widget
        WidgetBuilder builder = new WidgetBuilder(this, agentID);
        AppWidgetManager.getInstance(this).updateAppWidget(db.getAgentWidgetId(agentID), builder.constructWidget());
    }

}
