package io.github.jlillioja.fasttrack;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;import android.database.Cursor;


/**
 * Created by jacob on 4/29/2016.
 */
public class FilterDialog extends DialogFragment {

    DatabaseHelper db;
    Cursor agents;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        db = DatabaseHelper.getInstance(getContext());
        agents = db.getAgents();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder
                .setTitle(R.string.filter_by_agent)
                .setMultiChoiceItems(agents, DatabaseContract.Agent.COLUMN_NAME_FILTERED, DatabaseContract.Agent.COLUMN_NAME_AGENT_NAME, new mMultiClickListener())
                .setPositiveButton(R.string.filter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        /* Commit changes */
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        /* Discard changes */
                    }
                });

        return dialogBuilder.create();
    }

    private class mMultiClickListener implements DialogInterface.OnMultiChoiceClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            int pos = agents.getPosition(); //Not sure if I need to reset the cursor's position after moving around.
            agents.moveToPosition(which);
            int agentId = agents.getInt(agents.getColumnIndex(DatabaseContract.Agent._ID));
            db.toggleFilter(agentId, isChecked);
            agents.moveToPosition(pos);
        }
    }
}
