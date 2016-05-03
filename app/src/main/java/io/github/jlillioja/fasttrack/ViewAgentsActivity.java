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

public class ViewAgentsActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ViewAgentsActivity";
    private DatabaseHelper mDb;

    @InjectView(R.id.agentList) ListView mAgentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_agents);
        ButterKnife.inject(this);
        mDb = DatabaseHelper.getInstance(getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        Cursor cursor = mDb.getAgents();
        /* Adapter from agents to views.
        The String array lists data sources, the int array data destinations.
        The adapter assigns the data with respect to order in the array, and the formatting reflects this. */
        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(this, R.layout.agentlist_item, cursor,
                        new String[] {DatabaseContract.Agent.COLUMN_NAME_AGENT_NAME, DatabaseContract.Agent._ID, DatabaseContract.Agent.COLUMN_NAME_STATE},
                        new int[]    {R.id.agent_name,                               R.id.agent_id,              R.id.agent_state},
                        0);

        mAgentList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_agents_activity_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_view_agents:
                refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
