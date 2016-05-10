package io.github.jlillioja.fasttrack;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

/**
 * Created by Jacob on 5/7/2016.
 */
public class RuleJudge {

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

    public void setAlarm(int id) {
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        Intent intent = new Intent(context, FastTrackService.class);
        intent.setAction(FastTrackService.ACTION_ALARM);
        intent.putExtra(DatabaseContract.Agent._ID, db.getRuleAgent(id));
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        alarmManager.set(db.getRuleType(id), db.getRuleTime(id), pendingIntent);
    }

    public boolean judge(int agentId) {
        int agentState = DatabaseHelper.getInstance(context).getAgentState(agentId);
        return (agentState > 0);
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
