package io.github.jlillioja.fasttrack;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.SystemClock;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Jacob on 5/7/2016.
 */
public class RuleJudge {

    private static final String LOG_TAG = "RuleJudge";
    private Context context;
    private static RuleJudge sInstance;
    private AlarmManager alarmManager;

    private RuleJudge(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.context = context;
    }

    public static synchronized RuleJudge getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RuleJudge(context);
        }
        return sInstance;
    }

    public void setAlarm(int ruleId) {
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        Intent intent = new Intent(context, FastTrackService.class);
        intent.setAction(FastTrackService.ACTION_ALARM);
        intent.putExtra(DatabaseContract.Agent._ID, db.getRuleAgent(ruleId));
        intent.putExtra(context.getString(R.string.key_rule_id), ruleId);
        PendingIntent pendingIntent = PendingIntent.getService(context, ruleId, intent, 0);
        int ruleType = db.getRuleType(ruleId);
        long ruleTime = db.getRuleTime(ruleId);

        if (ruleType == AlarmManager.ELAPSED_REALTIME) {
            alarmManager.setExact(ruleType, SystemClock.elapsedRealtime()+ruleTime, pendingIntent);
            Log.d(LOG_TAG, "Rule "+String.valueOf(ruleId)+": Type "+String.valueOf(ruleType)+", Time "+String.valueOf(ruleTime));
        }
    }

    public int resetAgentAlarms(int agentID) {
        Cursor rules = DatabaseHelper.getInstance(context).getAgentRules(agentID);
        int n =rules.getCount();
        for (int i=0;i<n;i++) {
            rules.moveToPosition(i);
            setAlarm(rules.getInt(rules.getColumnIndex(DatabaseContract.Rule._ID)));
        }
        return n;
    }
}
