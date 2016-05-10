package io.github.jlillioja.fasttrack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by jacob on 4/29/2016.
 */
public class FilterDialog extends DialogFragment {

    private final String LOG_TAG = "FilterDialog";

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
                        Log.d(LOG_TAG, "Applying "+String.valueOf(changes.size())+" changes.");
                        /* Commit changes */
                        for (ContentValues change : changes) {
                            db.updateAgent(change);
                        }
                        mListener.onDialogPositiveClick(FilterDialog.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(LOG_TAG, "Discarding changes.");
                        changes.clear();
                        mListener.onDialogNegativeClick(FilterDialog.this);
                        /* Discard changes */
                    }
                });

        return dialogBuilder.create();
    }

    private class mMultiClickListener implements DialogInterface.OnMultiChoiceClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            agents.moveToPosition(which);
            Log.d(LOG_TAG, "Changing "+agents.getString(agents.getColumnIndex(DatabaseContract.Agent.COLUMN_NAME_AGENT_NAME)));
            int agentId = agents.getInt(agents.getColumnIndex(DatabaseContract.Agent._ID));
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.Agent._ID, agentId);
            values.put(DatabaseContract.Agent.COLUMN_NAME_FILTERED, isChecked?1:0);
            changes.add(values);
        }
    }

    public interface FilterDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    FilterDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the FilterDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the FilterDialogListener so we can send events to the host
            mListener = (FilterDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement FilterDialogListener");
        }
    }

}
