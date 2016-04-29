package io.github.jlillioja.fasttrack;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jacob on 4/29/2016.
 */
public class FilterDialog extends DialogFragment {

    DatabaseHelper db;
    Cursor agents;
    ArrayList<ContentValues> changes;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        db = DatabaseHelper.getInstance(getContext());
        agents = db.getAgents();
        changes = new ArrayList<ContentValues>();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder
                .setTitle(R.string.filter_by_agent)
                .setMultiChoiceItems(agents, DatabaseContract.Agent.COLUMN_NAME_FILTERED, DatabaseContract.Agent.COLUMN_NAME_AGENT_NAME, new mMultiClickListener())
                .setPositiveButton(R.string.filter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        /* Commit changes */
                        for (ContentValues change : changes) {
                            db.toggleFilter(change);
                        }
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
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.Agent._ID, agentId);
            values.put(DatabaseContract.Agent.COLUMN_NAME_FILTERED, isChecked?1:0);
            changes.add(values);
            agents.moveToPosition(pos);
        }
    }
}
