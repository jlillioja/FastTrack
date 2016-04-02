package io.github.jlillioja.fasttrack;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ViewClicksActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_clicks);
        dbHelper = new DatabaseHelper(getApplicationContext());
        Cursor cursor = dbHelper.getAllClicks();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, new String[] {DatabaseContract.Click.COLUMN_NAME_TIMESTAMP}, new int[] {R.id.list_item});
        ListView listView = (ListView) findViewById(R.id.clickList);
        listView.setAdapter(adapter);
    }
}
