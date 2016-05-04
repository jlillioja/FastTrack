package io.github.jlillioja.fasttrack;

import android.app.DialogFragment;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.GregorianCalendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ViewClicksActivity extends AppCompatActivity
                                implements FilterDialog.FilterDialogListener {

    private static final String LOG_TAG = "ViewClicksActivity";
    private DatabaseHelper mDb;

    @InjectView(R.id.clickList) ListView mClickList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_clicks);
        ButterKnife.inject(this);
        mDb = DatabaseHelper.getInstance(getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        Cursor cursor = mDb.getClicks();
        /* Adapter from clicks to views.
        The String array lists data sources, the int array data destinations.
        The adapter assigns the data with respect to order in the array, and the formatting reflects this. */
        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(this, R.layout.clicklist_item, cursor,
                        new String[] {DatabaseContract.Click.COLUMN_NAME_TIMESTAMP, DatabaseContract.Agent.COLUMN_NAME_AGENT_NAME, DatabaseContract.Agent._ID},
                        new int[]    {R.id.timestamp,                               R.id.agent_name,                               R.id.agent_id},
                        0);

        /*
        Format the timestamp from epoch time to readable time.
        TODO: dates could look nicer.
         */
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == cursor.getColumnIndex(DatabaseContract.Click.COLUMN_NAME_TIMESTAMP)) {
                    Long epochTime = cursor.getLong(columnIndex);
                    TextView textview = (TextView) view;
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTimeInMillis(epochTime);
                    textview.setText(cal.getTime().toString());
                    return true;
                } else return false;
            }
        });

        mClickList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_clicks_activity_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_view_clicks:
                refresh();
                return true;
            case R.id.filter_view_clicks:
                Log.d(LOG_TAG, "Showing Filter Dialog");
                DialogFragment dialog = new FilterDialog();
                dialog.show(getFragmentManager(), "filter dialog");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // See the new changes
        refresh();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Nothing to do
    }
}
