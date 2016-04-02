package io.github.jlillioja.fasttrack;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = new DatabaseHelper(this);
    }

    public void fastTrack(View view) {
        mDbHelper.insertTimestamp();
    }

    public void viewClicks(View view) {
        Intent intent = new Intent(this, ViewClicksActivity.class);
        startActivity(intent);
    }
}
