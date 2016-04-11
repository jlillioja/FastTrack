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
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, new String[] {DatabaseContract.Click.COLUMN_NAME_TIMESTAMP}, new int[] {R.id.list_item});
        ListView listView = (ListView) findViewById(R.id.clickList);
        listView.setAdapter(adapter);
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
