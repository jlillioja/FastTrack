package io.github.jlillioja.fasttrack;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ViewClicksActivity extends AppCompatActivity {

    private DatabaseHelper mDb;
    @InjectView(R.id.clickList) ListView mClickList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_clicks);
        ButterKnife.inject(this);
        mDb = DatabaseHelper.getInstance(getApplicationContext());
        refresh();
    }

    private void refresh() {
        Cursor cursor = mDb.getAllClicks();
        /* Adapter from clicks to views.
        The String array lists data sources, the int array data destinations.
        The adapter assigns the data with respect to order in the array, and the formatting reflects this. */
        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(this, R.layout.list_item, cursor,
                        new String[] {DatabaseContract.Click.COLUMN_NAME_TIMESTAMP, DatabaseContract.Click.COLUMN_NAME_AGENT_ID},
                        new int[]    {R.id.timestamp,                               R.id.agent_id},
                        0);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
