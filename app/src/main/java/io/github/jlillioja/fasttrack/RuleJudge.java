package io.github.jlillioja.fasttrack;

import android.content.Context;
import android.graphics.Color;

/**
 * Created by Jacob on 5/7/2016.
 */
public class RuleJudge {

    Context context;

    public RuleJudge(Context context) {
        this.context = context;
    }

    public boolean judge(int agentId) {
        int agentState = DatabaseHelper.getInstance(context).getAgentState(agentId);
        if (agentState%2 == 0) {
            return true;
        } else {
            return false;
        }
    }
}
