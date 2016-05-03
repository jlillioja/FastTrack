package io.github.jlillioja.fasttrack;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper mDbHelper;
    public static int agentID = 1;
    public static String agentName = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = DatabaseHelper.getInstance(getApplicationContext());
    }

    public void fastTrack(View view) {

        mDbHelper.insertTimestamp(agentID);
        mDbHelper.incrementState(agentID);
    }

    public void viewClicks(View view) {
        Intent intent = new Intent(this, ViewClicksActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_database:
                mDbHelper.recreate();
                return true;
            case R.id.view_agents:
                startActivity(new Intent(this, ViewAgentsActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
